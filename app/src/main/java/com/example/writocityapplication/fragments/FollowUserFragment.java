package com.example.writocityapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writocityapplication.R;
import com.example.writocityapplication.adapter.postAdapter;
import com.example.writocityapplication.models.User;
import com.example.writocityapplication.models.WritePost;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String authorName;

    RecyclerView followUserAllPost;
    postAdapter showPostAdapter;

    public FollowUserFragment() {
        // Required empty public constructor
    }

    public FollowUserFragment(String authorName){
         this.authorName = authorName;
    }

    public static FollowUserFragment newInstance(String param1, String param2) {
        FollowUserFragment fragment = new FollowUserFragment();
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

        View view = inflater.inflate(R.layout.fragment_follow_user, container, false);

        ImageView followProfileImage;
        TextView followProfileName, followProfileBio;


        followProfileImage = view.findViewById(R.id.followProfile_IV_profilePic);
        followProfileName = view.findViewById(R.id.followProfile_TV_Name);
        followProfileBio = view.findViewById(R.id.followProfile_TV_bio);


        FirebaseUser userInfo;
        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users").child(authorName);
        String userId = userInfo.getUid();

        Toast.makeText(getContext(), authorName, Toast.LENGTH_SHORT).show();

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String fullName = userProfile.fullName;
                    String profilePicUrl = userProfile.profilePhoto;
                    String profileBio = userProfile.bio;
                    followProfileName.setText(fullName);
                    Picasso.get().load(profilePicUrl).into(followProfileImage);
                    followProfileBio.setText(profileBio);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Setting recycler view
        followUserAllPost = view.findViewById(R.id.followProfile_RV_posts);
        followUserAllPost.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<WritePost> options =
                new FirebaseRecyclerOptions.Builder<WritePost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Posts").orderByChild("postedBy").equalTo(authorName), WritePost.class)
                        .build();


        showPostAdapter = new postAdapter(options);
        followUserAllPost.setAdapter(showPostAdapter);

        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        showPostAdapter.startListening();
    }
}