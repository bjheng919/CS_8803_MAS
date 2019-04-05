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
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnlyGroups.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class OnlyGroups extends Fragment {

    private OnFragmentInteractionListener mListener;

    ListView listView;
    List grouplist;
    ArrayList<GroupProfile> profilelistfinal = new ArrayList<GroupProfile>();
    CustomGroupsAdapter adapter;

    public OnlyGroups() {
        // Required empty public constructor
    }

    private int mPage;

    public static OnlyGroups newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        OnlyGroups fragment = new OnlyGroups();
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


        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid);

        profilelistfinal.removeAll(profilelistfinal);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                grouplist = (List<String>)dataSnapshot.getValue();


                final DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference().child("groups");

                mref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        profilelistfinal.removeAll(profilelistfinal);

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                            GroupProfile groupprofile = postSnapshot.getValue(GroupProfile.class);

                            if(grouplist.contains(groupprofile.getUuid())) {
                                profilelistfinal.add(groupprofile);
                            }
                        }

                        listView.setAdapter(adapter);
                        adapter.setNotifyOnChange(true);

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

        adapter = new CustomGroupsAdapter(getContext(), R.layout.custommsgusers,profilelistfinal);

        return inflater.inflate(R.layout.fragment_only_messages, container, false);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.listViewContactsMessages);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii = new Intent(getActivity(),GroupChat.class);
                ii.putExtra("chatwith",profilelistfinal.get(i));
                getActivity().finish();
                startActivity(ii);
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
