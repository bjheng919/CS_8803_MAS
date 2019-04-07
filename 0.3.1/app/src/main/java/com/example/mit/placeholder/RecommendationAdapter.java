package com.example.mit.placeholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> implements View.OnClickListener {

    List<GroupProfile> groupProfileList;
    Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    // define interface for OnItemClickListener
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecommendationAdapter(List<GroupProfile> groupProfileList, Context context) {
        this.groupProfileList = groupProfileList;
        this.mContext = context;
    }

    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecommendationAdapter.ViewHolder vh, int position) {
        setImage(groupProfileList.get(position), vh);
        setTexts(groupProfileList.get(position), vh);
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

    private void setImage(GroupProfile group, final ViewHolder vh) {
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

    private void setTexts(GroupProfile group, ViewHolder vh) {
        vh.tvs[0].setText(group.getGroupName());
        vh.tvs[1].setText(group.getMembers().size()+" / "+group.getTotalNum());
        String commitNum = group.getCommitNum();
        if (commitNum.equals("0")) {
            vh.tvs[2].setText("Nobody has commited.");
        } else if (commitNum.equals("1")) {
            vh.tvs[2].setText("1 member has commited.");
        } else {
            vh.tvs[2].setText(commitNum + " members has commited.");
        }
    }

    @Override
    public int getItemCount() {
        return groupProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView[] tvs;
        final CircleImageView civ;

        public ViewHolder(View itemView) {
            super(itemView);

            civ = (CircleImageView) itemView.findViewById(R.id.customconimage);
            tvs = new TextView[3];
            this.tvs[0] = (TextView) itemView.findViewById(R.id.customconfname);
            this.tvs[1] = (TextView) itemView.findViewById(R.id.customconlname);
            this.tvs[2] = (TextView) itemView.findViewById(R.id.recommendationItemCommitTV);
        }
    }
}

