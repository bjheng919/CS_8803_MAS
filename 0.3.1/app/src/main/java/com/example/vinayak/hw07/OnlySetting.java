package com.example.vinayak.hw07;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnlyGroups.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class OnlySetting extends Fragment {

    private OnFragmentInteractionListener mListener;
    UserProfile usertemp;


    public OnlySetting() {
        // Required empty public constructor
    }

    private int mPage;

    public static OnlySetting newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        OnlySetting fragment = new OnlySetting();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.my_profile_details, container, false);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ImageView ivimage = (ImageView) getView().findViewById(R.id.profdisimage);

        usertemp = new UserProfile();

        ///usertemp = (UserProfile) getView().getIntent().getExtras().getSerializable("profiledetails");

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(CreateProfile.myuuid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView tvname = (TextView) getView().findViewById(R.id.profdisname);
                TextView tvemail =(TextView) getView().findViewById(R.id.profdisemail);
                TextView tvgender =(TextView) getView().findViewById(R.id.profdisgender);
                usertemp = dataSnapshot.getValue(UserProfile.class);
                tvname.setText(usertemp.getFname() + " " + usertemp.getLname());
                tvgender.setText(usertemp.getGender());
                tvemail.setText(usertemp.getEmail());

                if(usertemp.getImage()!=null)
                {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");
                    StorageReference storageref = storageReference.child(usertemp.getImage());

                    storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(getView().getContext()).load(uri).into(ivimage);
                        }
                    });
                }
                else {

                    ivimage.setImageResource(R.mipmap.noimage);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });





    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
