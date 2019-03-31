package com.example.vinayak.hw07;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    ArrayList<GroupProfile> groupProfileList = new ArrayList<>();
    ArrayList<UserSurvey> groupSurveyList = new ArrayList<>();
    ArrayList<GroupProfile> filteredGroupProfileList = new ArrayList<>();
    ArrayList<UserSurvey> filteredGroupSurveyList = new ArrayList<>();
    String currGroupUuidList;
    UserProfile currProfile;
    UserSurvey currSurvey;
    CustomRecommendationAdapter adapter;
    boolean[] filterValues;

    public OnlyRecommendation() {
        // Required empty constructor
    }

    public void setFilterValues(boolean[] filterValues) {
        this.filterValues = filterValues;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getGroupProfileSurveyList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setListenersForLists();

        adapter = new CustomRecommendationAdapter(getContext(), R.layout.customcontacts, filteredGroupProfileList);

        return inflater.inflate(R.layout.fragment_only_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.recommendationListLV);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii = new Intent(getActivity(),ProfileDetails.class);
                ii.putExtra("groupProfile", filteredGroupProfileList.get(i));
                ii.putExtra("groupSurvey", filteredGroupSurveyList.get(i));
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

    private void setListenersForLists() {
        // Find the current user's UserProfile
        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(CreateProfile.myuuid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currProfile = dataSnapshot.getValue(UserProfile.class);

                // Find the current user's survey
                final DatabaseReference mref3 = FirebaseDatabase.getInstance().getReference().child("surveys").child(CreateProfile.myuuid);
                mref3.addValueEventListener(new ValueEventListener() {
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
                                                groupProfileList.removeAll(groupProfileList);
                                                groupSurveyList.removeAll(groupProfileList);
                                                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                                                    if (tempGroupUuidList.contains(groupSnapshot.getKey())) {
                                                        // System.out.println("Checking groupuuid: " + groupSnapshot.getKey() + " -------------------------");
                                                        GroupProfile tempGroupProfile = groupSnapshot.getValue(GroupProfile.class);
                                                        UserSurvey tempGroupSurvey = tempGroupSurveyList.get(tempGroupUuidList.indexOf(groupSnapshot.getKey()));
                                                        int similarity = calculateSimilarity(currSurvey, tempGroupSurvey);
                                                        tempGroupProfile.setSimilarity(similarity);
                                                        tempGroupSurvey.setSimilarity(similarity);
                                                        groupProfileList.add(tempGroupProfile);
                                                        groupSurveyList.add(tempGroupSurvey);
                                                    }
                                                }

                                                Collections.sort(groupProfileList, Collections.reverseOrder());
                                                Collections.sort(groupSurveyList, Collections.reverseOrder());
                                                copyToFilteredLists();
                                                // System.out.println(groupProfileList);
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
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void copyToFilteredLists() {
        for (int i = 0; i < groupProfileList.size(); i++) {
            GroupProfile gp = groupProfileList.get(i);
            UserSurvey us = groupSurveyList.get(i);
            if ((!filterValues[0] || (filterValues[0] && Integer.parseInt(gp.getCommitNum()) >= 1)) &&
                    (!filterValues[1] || (filterValues[1] && us.getNation().equals(currSurvey.getNation()))) &&
                    (!filterValues[2] || (filterValues[2] && gp.getGender().equals(currProfile.getGender()))) &&
                    (!filterValues[3] || (filterValues[3] && us.getSmoke().equals(currSurvey.getSmoke()) &&
                            us.getCook().equals(currSurvey.getCook()) && us.getParty().equals(currSurvey.getParty()) &&
                            us.getPet().equals(currSurvey.getPet()))) &&
                    (!filterValues[4] || (filterValues[4] && us.getRmType().equals(currSurvey.getRmType())))) {
                filteredGroupProfileList.add(gp);
                filteredGroupSurveyList.add(us);
            }
        }
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
