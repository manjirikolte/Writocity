package com.example.writocityapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writocityapplication.R;
import com.example.writocityapplication.EditProfileActivity;
import com.example.writocityapplication.SettingMenuActivity;
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

public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //Declaring variable
    RecyclerView profile_RV_post;
    postAdapter profile_showPostAdapter;
    LinearLayout profile_LL_editProfile;
    TextView myProfile_TV_name, myProfile_TV_userName, myProfile_TV_bio;
    ImageView myProfile_IV_Menu, editProfile_IV_profilePic;

    DatabaseReference dbReference;
    private String userId;
    FirebaseUser userInfo;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        //Move to next activity
        myProfile_IV_Menu = view.findViewById(R.id.myProfile_IV_Menu);
        myProfile_IV_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingMenuActivity.class));
            }
        });


        //userProfile fetch data
        myProfile_TV_name = view.findViewById(R.id.myProfile_TV_Name);
        myProfile_TV_userName = view.findViewById(R.id.myProfile_TV_userName);
        myProfile_TV_bio = view.findViewById(R.id.myProfile_TV_bio);
        editProfile_IV_profilePic = view.findViewById(R.id.followProfile_IV_profilePic);

        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = userInfo.getUid();

        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){

                    String fullName = userProfile.fullName;
                    String userName = userProfile.username;
                    String profileBio = userProfile.bio;
                    String profilePhoto = userProfile.profilePhoto;

                    myProfile_TV_name.setText(fullName);
                    myProfile_TV_userName.setText(userName);
                    myProfile_TV_bio.setText(profileBio);
                    Picasso.get().load(profilePhoto).into(editProfile_IV_profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        profile_LL_editProfile = view.findViewById(R.id.profile_LL_editProfile);
        profile_LL_editProfile.setOnClickListener(view1 -> startActivity(new Intent(getContext(), EditProfileActivity.class)));

        //Setting recycler view
        profile_RV_post = view.findViewById(R.id.fullowProfile_RV_posts);
        profile_RV_post.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<WritePost> options =
                new FirebaseRecyclerOptions.Builder<WritePost>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Posts").orderByChild("postedBy").equalTo(userId), WritePost.class)
                        .build();

        profile_showPostAdapter = new postAdapter(options);
        profile_RV_post.setAdapter(profile_showPostAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        profile_showPostAdapter.startListening();

    }
}