package com.example.mit.placeholder;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.JustifyContent;
import com.google.android.flexbox.FlexboxLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> implements View.OnClickListener {

    List<GroupProfile> groupProfileList;
    List<UserSurvey> groupSurveyList;
    Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    // define interface for OnItemClickListener
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecommendationAdapter(List<GroupProfile> groupProfileList, List<UserSurvey> groupSurveyList, Context context) {
        this.groupProfileList = groupProfileList;
        this.groupSurveyList = groupSurveyList;
        this.mContext = context;
    }

    @Override
    public RecommendationAdapter.RecommendationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_item, parent, false);
        RecommendationViewHolder vh = new RecommendationViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendationAdapter.RecommendationViewHolder vh, int position) {
        setImage(groupProfileList.get(position), vh);
        setTexts(groupProfileList.get(position), vh);
        setTags(groupProfileList.get(position), groupSurveyList.get(position), vh);
        vh.itemView.setTag(position);

        vh.tagListRV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //模拟父控件的点击
                    vh.getItemView().performClick();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //use getTag to get position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private void setImage(GroupProfile group, final RecommendationViewHolder vh) {
        if(group.getImage()==null) {
            vh.civ.setImageResource(R.mipmap.noimage);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");
            StorageReference storageref = storageReference.child(group.getImage());

            storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mContext).load(uri).into(vh.civ);
                }
            });
        }
    }

    private void setTexts(GroupProfile gp, RecommendationViewHolder vh) {
        vh.tvs[0].setText(gp.getGroupName());
        String totalNumStr = gp.getTotalNum();
        if (totalNumStr.contains("+")) {
            int wantedNum = Integer.parseInt(totalNumStr.substring(0, 1)) - gp.getMembers().size();
            vh.tvs[1].setText("" + wantedNum + "+ more roommates wanted");
        } else {
            int wantedNum = Integer.parseInt(totalNumStr) - gp.getMembers().size();
            if (wantedNum == 1) {
                vh.tvs[1].setText("1 more roommate wanted");
            } else {
                vh.tvs[1].setText("" + wantedNum + " more roommates wanted");
            }
        }
    }

    private void setTags(GroupProfile gp, UserSurvey gs, RecommendationViewHolder vh) {
        // Initialize the list of tag strings
        ArrayList<String> tagStrList = new ArrayList<>();
        tagStrList.add(gp.getTotalNum() + " People");
        if (gs.getPrefNation() != null && gs.getPrefNation().equals("Yes") && gs.getNation() != null) {
            tagStrList.add(gs.getNation());
        } else if (gs.getPrefNation() != null && gs.getPrefNation().equals("No") && gs.getNation() != null) {
            tagStrList.add("Multiple Origins");
        }
        if (gs.getPrefGender() != null && !gs.getPrefGender().equals("Don't mind")) {
            tagStrList.add(gs.getPrefGender());
        }
        if (gs.getRmType() != null && !gs.getRmType().equals("Don't mind")) {
            tagStrList.add(gs.getRmType());
        }
        if (gs.getPet() != null) {
            if (gs.getPet().equals("Yes")) {
                tagStrList.add("Pets");
            } else {
                tagStrList.add("No Pets");
            }
        }
        if (gs.getSmoke() != null) {
            if (gs.getSmoke().equals("Yes")) {
                tagStrList.add("Smoking");
            } else {
                tagStrList.add("No Smoking");
            }
        }
        if (gs.getUseRoom() != null) {
            if (gs.getUseRoom().equals("Yes")) {
                tagStrList.add("For Social");
            } else {
                tagStrList.add("For Study");
            }
        }

        // Find the tag list RecyclerView
        RecyclerView tagListRV = vh.tagListRV;

        // Create the FlexboxLayoutMananger, only flexbox library version 0.3.0 or higher support.
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        // Set flex layout styles.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        tagListRV.setLayoutManager(flexboxLayoutManager);

        // Set adapter object.
        TagListAdapter tagListAdapter = new TagListAdapter(tagStrList);
        tagListRV.setAdapter(tagListAdapter);
        tagListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (groupProfileList != null) {
            return groupProfileList.size();
        } else {
            return 0;
        }
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {
        TextView[] tvs;
        final CircleImageView civ;
        RecyclerView tagListRV;

        public RecommendationViewHolder(View itemView) {
            super(itemView);

            this.civ = (CircleImageView) itemView.findViewById(R.id.customconimage);
            this.tvs = new TextView[2];
            this.tvs[0] = (TextView) itemView.findViewById(R.id.recommendationItemGroupNameTV);
            this.tvs[1] = (TextView) itemView.findViewById(R.id.recommendationItemWantedTV);
            this.tagListRV = (RecyclerView) itemView.findViewById(R.id.recommendationItemTagRV);
        }

        public View getItemView() {
            return itemView;
        }
    }
}

