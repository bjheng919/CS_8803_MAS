package com.example.mit.placeholder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
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

//    ListView listView;
    private ArrayList<String> tempGroupUuidList = new ArrayList<>();
    private ArrayList<UserSurvey> tempGroupSurveyList = new ArrayList<>();
    private ArrayList<GroupProfile> groupProfileList = new ArrayList<>();
    private ArrayList<UserSurvey> groupSurveyList = new ArrayList<>();
    private ArrayList<GroupProfile> filteredGroupProfileList = new ArrayList<>();
    private ArrayList<UserSurvey> filteredGroupSurveyList = new ArrayList<>();
    private List currGroupUuidList;
    private UserProfile currProfile;
    private UserSurvey currSurvey;
//    private CustomRecommendationAdapter adapter;
    private boolean[] filterValues;

    private RecyclerView mRecyclerView;
    private RecommendationAdapter adapter;

    public OnlyRecommendation() {
        // Required empty constructor
    }

    public void setFilterValues(boolean[] filterValues) {
        this.filterValues = filterValues;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setListenersForLists();

        View view = inflater.inflate(R.layout.recommendation_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recommendationListRV);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecommendationAdapter(filteredGroupProfileList, getContext());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new RecommendationAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent ii = new Intent(getActivity(),ProfileDetails.class);
                ii.putExtra("groupProfile", filteredGroupProfileList.get(position));
                ii.putExtra("groupSurvey", filteredGroupSurveyList.get(position));
//                getActivity().finish();
                startActivity(ii);
            }
        });

        return view;

//        return inflater.inflate(R.layout.fragment_only_recommendation, container, false);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        listView = (ListView) getView().findViewById(R.id.recommendationListLV);
//
//        listView.setAdapter(adapter);
//        adapter.setNotifyOnChange(true);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent ii = new Intent(getActivity(),ProfileDetails.class);
//                ii.putExtra("groupProfile", filteredGroupProfileList.get(i));
//                ii.putExtra("groupSurvey", filteredGroupSurveyList.get(i));
//                getActivity().finish();
//                startActivity(ii);
//            }
//        });
//    }

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
                                                        if (tempGroupProfile.getMembers().size() < Integer.parseInt(tempGroupProfile.getTotalNum())) {
                                                            UserSurvey tempGroupSurvey = tempGroupSurveyList.get(tempGroupUuidList.indexOf(groupSnapshot.getKey()));
                                                            int similarity = calculateSimilarity(currSurvey, tempGroupSurvey);
                                                            tempGroupProfile.setSimilarity(similarity);
                                                            tempGroupSurvey.setSimilarity(similarity);
                                                            groupProfileList.add(tempGroupProfile);
                                                            groupSurveyList.add(tempGroupSurvey);
                                                        }
                                                    }
                                                }

                                                Collections.sort(groupProfileList, Collections.reverseOrder());
                                                Collections.sort(groupSurveyList, Collections.reverseOrder());
                                                copyToFilteredLists();
                                                // System.out.println(groupProfileList);
                                                mRecyclerView.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
//                                                listView.setAdapter(adapter);
//                                                adapter.setNotifyOnChange(true);
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
                            ((currSurvey.getSameNation().equals("Yes") && gs.getSameNation().equals("Yes") &&
                            currSurvey.getNation() != null && gs.getNation()!= null && gs.getNation().equals(currSurvey.getNation())) ||
                            (currSurvey.getSameNation().equals("No") && gs.getSameNation().equals("No") &&
                            currSurvey.getNation() != null && gs.getNation()!= null && !gs.getNation().equals(currSurvey.getNation())) ||
                            (currSurvey.getSameNation().equals("IDC") && gs.getSameNation().equals("IDC"))))) &&
                    (!filterValues[2] ||
                            (currSurvey.getPrefGender() != null && gs.getPrefGender() != null &&
                            ((currSurvey.getGender() != null && gs.getGender() != null &&
                            currSurvey.getPrefGender().equals(gs.getGender()) && currSurvey.getGender().equals(gs.getPrefGender())) ||
                            (currSurvey.getPrefGender().equals("IDC") && gs.getPrefGender().equals("IDC"))))) &&
                    (!filterValues[3] ||
                            (currSurvey.getRmType() != null && gs.getRmType() != null && gs.getRmType().equals(currSurvey.getRmType()))) &&
                    (!filterValues[4] ||
                            (currSurvey.getPet() != null && gs.getPet() != null && gs.getPet().equals(currSurvey.getPet()))) &&
                    (!filterValues[5] ||
                            (currSurvey.getSmoke() != null && gs.getSmoke() != null && gs.getSmoke().equals(currSurvey.getSmoke()))) &&
                    (!filterValues[6] ||
                            (currSurvey.getParty() != null && gs.getParty() != null && gs.getParty().equals(currSurvey.getParty())))) {
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
//        if (curr.getCook() != null && other.getCook() != null)
//            if (curr.getCook().equals(other.getCook())) count++;
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
