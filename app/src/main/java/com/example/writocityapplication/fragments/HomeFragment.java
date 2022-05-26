package com.example.writocityapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.writocityapplication.R;
import com.example.writocityapplication.adapter.postAdapter;
import com.example.writocityapplication.models.WritePost;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView home_RV_posts;
    postAdapter showPostAdapter;

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //Setting recycler view
        home_RV_posts = view.findViewById(R.id.home_RV_posts);
        home_RV_posts.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<WritePost> options =
                new FirebaseRecyclerOptions.Builder<WritePost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Posts"), WritePost.class)
                        .build();

        showPostAdapter = new postAdapter(options);
        home_RV_posts.setAdapter(showPostAdapter);


        //select Category
        FrameLayout thriller_category, politics_category, entertainment_category,
                love_category, friends_category;

        thriller_category = view.findViewById(R.id.thriller_category);
        politics_category = view.findViewById(R.id.politics_category);
        entertainment_category = view.findViewById(R.id.entertainment_category);
        love_category = view.findViewById(R.id.love_category);
        friends_category = view.findViewById(R.id.friends_category);

        thriller_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "thriller";
                processSearch(s);
            }
        });

        politics_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "politics";
                processSearch(s);
            }
        });

        entertainment_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "entertainment";
                processSearch(s);
            }
        });

        love_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "love";
                processSearch(s);
            }
        });

        friends_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "friends";
                processSearch(s);
            }
        });

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        showPostAdapter.startListening();
    }


    /**
     * this method will check and display specific user
     * information of entered name in searchBar from firebase
     * @param s is entered alphabets in searchBar
     */
    private void processSearch(String s) {
        FirebaseRecyclerOptions<WritePost> options =
                new FirebaseRecyclerOptions.Builder<WritePost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts")
                                .orderByChild("tag").startAt(s).endAt(s +"\uf8ff"), WritePost.class)
                        .build();

        showPostAdapter = new postAdapter(options);
        showPostAdapter.startListening();
        home_RV_posts.setAdapter(showPostAdapter);
    }


}