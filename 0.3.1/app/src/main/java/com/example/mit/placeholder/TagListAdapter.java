package com.example.mit.placeholder;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagListViewHolder> {

    List<String> tagStrList;

    public TagListAdapter(List<String> tagStrList) {
        this.tagStrList = tagStrList;
    }

    @Override
    public TagListAdapter.TagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_tag, parent, false);
        TagListViewHolder vh = new TagListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(TagListAdapter.TagListViewHolder vh, int position) {
        vh.tv.setText(tagStrList.get(position));
    }

    @Override
    public int getItemCount() {
        if (tagStrList != null) {
            return tagStrList.size();
        } else {
            return 0;
        }
    }

    public class TagListViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public TagListViewHolder(View itemView) {
            super(itemView);

            this.tv = (TextView) itemView.findViewById(R.id.tagTV);
        }
    }
}

