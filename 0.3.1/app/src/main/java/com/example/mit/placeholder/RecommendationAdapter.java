package com.example.mit.placeholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    public void onBindViewHolder(RecommendationAdapter.RecommendationViewHolder vh, int position) {
        setImage(groupProfileList.get(position), vh);
        setTexts(groupProfileList.get(position), vh);
        setTags(groupSurveyList.get(position), vh);
        vh.itemView.setTag(position);
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
        vh.tvs[1].setText(gp.getMembers().size()+" / "+gp.getTotalNum());
        String commitNum = gp.getCommitNum();
        if (commitNum.equals("0")) {
            vh.tvs[2].setText("Nobody has committed.");
        } else if (commitNum.equals("1")) {
            vh.tvs[2].setText("1 member has committed.");
        } else {
            vh.tvs[2].setText(commitNum + " members has committed.");
        }
    }

    private void setTags(UserSurvey gs, RecommendationViewHolder vh) {
        // Initialize the list of tag strings
        ArrayList<String> tagStrList = new ArrayList<>();
        if (gs.getSameNation() != null && gs.getSameNation().equals("Yes") && gs.getNation() != null) {
            tagStrList.add(gs.getNation());
        } else if (gs.getSameNation() != null && gs.getSameNation().equals("No") && gs.getNation() != null) {
            tagStrList.add("Multiple Origins");
        }
        if (gs.getPrefGender() != null && !gs.getPrefGender().equals("IDC")) {
            tagStrList.add(gs.getPrefGender());
        }
        if (gs.getRmType() != null) {
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
        if (gs.getParty() != null) {
            if (gs.getParty().equals("Yes")) {
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
            this.tvs = new TextView[3];
            this.tvs[0] = (TextView) itemView.findViewById(R.id.customconfname);
            this.tvs[1] = (TextView) itemView.findViewById(R.id.customconlname);
            this.tvs[2] = (TextView) itemView.findViewById(R.id.recommendationItemCommitTV);
            this.tagListRV = (RecyclerView) itemView.findViewById(R.id.recommendationItemTagRV);
        }
    }
}

