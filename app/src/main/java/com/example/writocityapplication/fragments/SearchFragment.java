package com.example.writocityapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.writocityapplication.R;
import com.example.writocityapplication.adapter.postAdapter;
import com.example.writocityapplication.models.WritePost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Declaring variable
    RecyclerView post_recyclerView;
    postAdapter showPostAdapter;
    DatabaseReference mDatabaseReference;

    EditText searchPost;
    ImageView searchButton;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //Search Implementation
        searchPost = view.findViewById(R.id.SearchEditText);
        searchButton = view.findViewById(R.id.SearchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchEditText = searchPost.getText().toString().toUpperCase();
                processSearch(searchEditText);
            }
        });

        //Setting recycler view
        post_recyclerView = view.findViewById(R.id.post_recyclerView);
        post_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<WritePost> options =
                new FirebaseRecyclerOptions.Builder<WritePost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Posts"), WritePost.class)
                        .build();

        showPostAdapter = new postAdapter(options);
        post_recyclerView.setAdapter(showPostAdapter);

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        showPostAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        showPostAdapter.stopListening();
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
                                .orderByChild("title").startAt(s).endAt(s +"\uf8ff"), WritePost.class)
                        .build();

        showPostAdapter = new postAdapter(options);
        showPostAdapter.startListening();
        post_recyclerView.setAdapter(showPostAdapter);
    }


}