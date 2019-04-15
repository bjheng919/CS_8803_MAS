package com.example.mit.placeholder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnlyGroups.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class OnlyMe extends Fragment {

    private OnFragmentInteractionListener mListener;
    UserProfile up;
    UserSurvey us;

    private Button meProfileBtn;
    private Button meSurveyBtn;
    private Button meLogoutBtn;

    public OnlyMe() {
        // Required empty public constructor
    }

    public static OnlyMe newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        OnlyMe fragment = new OnlyMe();
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

        View view = inflater.inflate(R.layout.activity_me, container, false);

        meProfileBtn = (Button) view.findViewById(R.id.meProfileBtn);
        meSurveyBtn = (Button) view.findViewById(R.id.meSurveyBtn);
        meLogoutBtn = (Button) view.findViewById(R.id.meLogoutBtn);

        meProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfile.class);
                startActivity(i);
            }
        });

        meSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FillSurvey.class);
                startActivity(i);
            }
        });

        meLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CircleImageView meCIV = (CircleImageView) getView().findViewById(R.id.meCIV);

        up = new UserProfile();

        ///up = (UserProfile) getView().getIntent().getExtras().getSerializable("profiledetails");

        // Get current UserProfile
        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(CreateProfile.myuuid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                up = dataSnapshot.getValue(UserProfile.class);

                TextView meFNameTV = (TextView) getView().findViewById(R.id.meFNameTV);
                TextView meEmailTV =(TextView) getView().findViewById(R.id.meEmailTV);

                meFNameTV.setText("Hi, " + up.getFname());
                meEmailTV.setText(up.getEmail());

                if(up.getImage() != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");
                    StorageReference storageref = storageReference.child(up.getImage());

                    storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(getView().getContext()).load(uri).into(meCIV);
                        }
                    });
                } else {
                    meCIV.setImageResource(R.mipmap.noimage);
                }

                // Get current UserSurvey
                final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("surveys").child(CreateProfile.myuuid);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        us = dataSnapshot.getValue(UserSurvey.class);

                        TextView meNationTV = (TextView) getView().findViewById(R.id.meNationTV);

                        meNationTV.setText(us.getNation());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
