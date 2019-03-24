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

import java.lang.reflect.Array;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnlyRecommendation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class OnlyRecommendation extends Fragment {

    private OnlyMessages.OnFragmentInteractionListener mListener;

    ListView listView;
    ArrayList<String> tempGroupUuidList = new ArrayList<>();
    ArrayList<UserSurvey> tempGroupSurveyList = new ArrayList<>();
    ArrayList<GroupProfile> groupProfileList=new ArrayList<>();
    String currGroupUuidList;
    UserSurvey currSurvey;
    CustomRecommendationAdapter adapter;

    public OnlyRecommendation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Find the current user's survey
        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("surveys").child(CreateProfile.myuuid);
        groupProfileList.removeAll(groupProfileList);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currSurvey = dataSnapshot.getValue(UserSurvey.class);

                // Find the current user's groupUuidList
                final DatabaseReference mref0 = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid);
                mref0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currGroupUuidList = dataSnapshot.child("groupUuidList").getValue().toString();

                        // Get all the groups surveys except those containing current user
                        final DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference().child("groupSurveys");
                        mref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                                    String tempGroupUuid = groupSnapshot.getKey();
                                    if (!currGroupUuidList.contains(tempGroupUuid)) {
                                        tempGroupUuidList.add(tempGroupUuid);
                                        tempGroupSurveyList.add(groupSnapshot.getValue(UserSurvey.class));
                                    }
                                }

                                // Set similarity for all temp group profile and add them to groupProfileList
                                final DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference().child("groups");
                                mref2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                                            if (tempGroupUuidList.contains(groupSnapshot.getKey())) {
                                                System.out.println("Checking groupuuid: " + groupSnapshot.getKey() + " -------------------------");
                                                GroupProfile tempGroupProfile = groupSnapshot.getValue(GroupProfile.class);
                                                int similarity = calculateSimilarity(currSurvey, tempGroupSurveyList.get(tempGroupUuidList.indexOf(groupSnapshot.getKey())));
                                                tempGroupProfile.setSimilarity(similarity);
                                                groupProfileList.add(tempGroupProfile);
                                            }
                                        }

                                        Collections.sort(groupProfileList, Collections.reverseOrder());
                                        System.out.println(groupProfileList);
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        adapter = new CustomRecommendationAdapter(getContext(), R.layout.customcontacts, groupProfileList);

        return inflater.inflate(R.layout.fragment_only_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.listViewContacts);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii = new Intent(getActivity(),ProfileDetails.class);
                ii.putExtra("profiledetails",groupProfileList.get(i));
                getActivity().finish();
                startActivity(ii);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private int calculateSimilarity(UserSurvey curr, UserSurvey other) {
        int count = 0;
        if (curr.getSameNation().equals("Yes") && other.getSameNation().equals("Yes") && curr.getNation().equals(other.getNation())) count++;
        if (curr.getSameNation().equals("No") && other.getSameNation().equals("No") && !curr.getNation().equals(other.getNation())) count++;
        if (curr.getCook().equals(other.getCook())) count++;
        if (curr.getLsEndTime().equals(other.getLsEndTime())) count++;
        if (curr.getLsStartTime().equals(other.getLsStartTime())) count++;
        if (curr.getParty().equals(other.getParty())) count++;
        if (curr.getPet().equals(other.getPet())) count++;
        if (curr.getRentHigh().equals(other.getRentHigh())) count++;
        if (curr.getRentLow().equals(other.getRentLow())) count++;
        if (curr.getRmmtNum().equals(other.getRmmtNum())) count++;
        if (curr.getRmType().equals(other.getRmType())) count++;
        if (curr.getSmoke().equals(other.getSmoke())) count++;
        return count;
    }
}
