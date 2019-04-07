package com.example.mit.placeholder;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
public class CustomRecommendationAdapter extends ArrayAdapter<GroupProfile> {

    List<GroupProfile> mData;
    Context mContext;
    int mResource;

    public CustomRecommendationAdapter(Context context, int resource, List<GroupProfile> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        GroupProfile group = mData.get(position);
        setImage(group, convertView);
        setTexts(group, convertView);

        return convertView;
    }

    private void setImage(GroupProfile group, View view) {
        final CircleImageView ivimage = (CircleImageView) view.findViewById(R.id.customconimage);

        if(group.getImage()==null) {
            ivimage.setImageResource(R.mipmap.noimage);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");
            StorageReference storageref = storageReference.child(group.getImage());

            storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mContext).load(uri).into(ivimage);
                }
            });
        }
    }

    private void setTexts(GroupProfile group, View view) {
        TextView[] tvs = new TextView[3];

        tvs[0] = (TextView) view.findViewById(R.id.customconfname);
        tvs[1] = (TextView) view.findViewById(R.id.customconlname);
        tvs[2] = (TextView) view.findViewById(R.id.recommendationItemCommitTV);

        tvs[0].setText(group.getGroupName());
        tvs[1].setText(group.getMembers().size()+" / "+group.getTotalNum());
        String commitNum = group.getCommitNum();
        if (commitNum.equals("0")) {
            tvs[2].setText("Nobody has commited.");
        } else if (commitNum.equals("1")) {
            tvs[2].setText("1 member has commited.");
        } else {
            tvs[2].setText(commitNum + " members has commited.");
        }
    }
}

