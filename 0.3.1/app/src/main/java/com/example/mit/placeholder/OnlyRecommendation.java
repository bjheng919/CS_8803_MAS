package com.example.mit.placeholder;

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
import java.util.List;


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
    List currGroupUuidList;
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
//                getActivity().finish();
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
                final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("surveys").child(CreateProfile.myuuid);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currSurvey = dataSnapshot.getValue(UserSurvey.class);

                        // Find the current user's groupUuidList; return empty list if user does not belong to any group
                        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid);
                        mref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                currGroupUuidList = (List<String>) dataSnapshot.getValue();

                                // if current user is at least in one group
                                if (currGroupUuidList == null) {
                                    currGroupUuidList = new ArrayList<String>();
                                }

                                // Get all the groups surveys except those containing current user
                                final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("groupSurveys");
                                mref.addValueEventListener(new ValueEventListener() {
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
                                        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("groups");
                                        mref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                groupProfileList.removeAll(groupProfileList);
                                                groupSurveyList.removeAll(groupProfileList);
                                                filteredGroupProfileList.removeAll(filteredGroupProfileList);
                                                filteredGroupSurveyList.removeAll(filteredGroupSurveyList);
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
            UserSurvey gs = groupSurveyList.get(i);
            if ((!filterValues[1] ||
                            (currSurvey.getSameNation() != null && gs.getSameNation() != null &&
                            ((currSurvey.getSameNation().equals("Yes") && gs.getSameNation().equals("Yes") && gs.getNation().equals(currSurvey.getNation())) ||
                            (currSurvey.getSameNation().equals("No") && gs.getSameNation().equals("No") && !gs.getNation().equals(currSurvey.getNation())) ||
                            (currSurvey.getSameNation().equals("IDC") && gs.getSameNation().equals("IDC"))))) &&
                    (!filterValues[2] ||
                            (currSurvey.getGender() != null && currSurvey.getGender() != null && gs.getGender().equals(currSurvey.getGender()))) &&
                    (!filterValues[3] ||
                            (currSurvey.getSmoke() != null && gs.getSmoke() != null && gs.getSmoke().equals(currSurvey.getSmoke()) &&
                            currSurvey.getCook() != null && gs.getSmoke() != null && gs.getCook().equals(currSurvey.getCook()) &&
                            currSurvey.getParty() != null && gs.getParty() != null && gs.getParty().equals(currSurvey.getParty()) &&
                            currSurvey.getPet() != null && gs.getPet() != null && gs.getPet().equals(currSurvey.getPet()))) &&
                    (!filterValues[4] ||
                            (currSurvey.getRmType() != null && gs.getRmType() != null && gs.getRmType().equals(currSurvey.getRmType())))) {
                if (filterValues[0]) {
                    int commitNum = Integer.parseInt(gp.getCommitNum());
                    gp.setSimilarity(gp.getSimilarity() + commitNum * 100);
                    gs.setSimilarity(gs.getSimilarity() + commitNum * 100);
                }
                filteredGroupProfileList.add(gp);
                filteredGroupSurveyList.add(gs);
            }
        }

        if (filterValues[0]) {
            Collections.sort(filteredGroupProfileList, Collections.reverseOrder());
            Collections.sort(filteredGroupSurveyList, Collections.reverseOrder());
        }
    }

    private int calculateSimilarity(UserSurvey curr, UserSurvey other) {
        int count = 0;
        if (curr.getSameNation()!= null && other.getSameNation() != null) {
            if (curr.getSameNation().equals("Yes") && other.getSameNation().equals("Yes") &&
                    curr.getNation().equals(other.getNation())) count++;
            if (curr.getSameNation().equals("No") && other.getSameNation().equals("No") &&
                    !curr.getNation().equals(other.getNation()))
                count++;
            if (curr.getSameNation().equals("IDC") && other.getSameNation().equals("IDC")) count++;
        }
        if (curr.getCook() != null && other.getCook() != null)
            if (curr.getCook().equals(other.getCook())) count++;
        if (curr.getLsEndTime() != null && other.getLsEndTime() != null)
            if (curr.getLsEndTime().equals(other.getLsEndTime())) count++;
        if (curr.getLsStartTime() != null && other.getLsStartTime() != null)
            if (curr.getLsStartTime().equals(other.getLsStartTime())) count++;
        if (curr.getParty() != null && other.getParty() != null)
            if (curr.getParty().equals(other.getParty())) count++;
        if (curr.getPet() != null && other.getPet() != null)
            if (curr.getPet().equals(other.getPet())) count++;
        if (curr.getRentHigh() != null && other.getRentHigh() != null)
            if (curr.getRentHigh().equals(other.getRentHigh())) count++;
        if (curr.getRentLow() != null && other.getRentLow() != null)
            if (curr.getRentLow().equals(other.getRentLow())) count++;
        if (curr.getRmmtNum() != null && other.getRmmtNum() != null)
            if (curr.getRmmtNum().equals(other.getRmmtNum())) count++;
        if (curr.getRmType() != null && other.getRmType() != null)
            if (curr.getRmType().equals(other.getRmType())) count++;
        if (curr.getSmoke() != null && other.getSmoke() != null)
            if (curr.getSmoke().equals(other.getSmoke())) count++;
        return count;
    }

}
