package com.example.writocityapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.writocityapplication.EditProfileActivity;
import com.example.writocityapplication.R;
import com.example.writocityapplication.adapter.commentAdapter;
import com.example.writocityapplication.adapter.postAdapter;
import com.example.writocityapplication.models.Comment;
import com.example.writocityapplication.models.WritePost;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentFragment() {
        // Required empty public constructor
    }

    // Declaring variables
    EditText postComment_ET;
    ImageView postComment_button;
    RecyclerView postComment_RV;
    commentAdapter commentAdapt;

    //Firebase Variable
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseStorage storage;

    String postedBy, postKey;
    public CommentFragment(String postedBy,String postKey){
        this.postedBy = postedBy;
        this.postKey = postKey;
    }


    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        postComment_RV = view.findViewById(R.id.postedComment_RV);
        postComment_ET = view.findViewById(R.id.postComment_ET);
        postComment_button = view.findViewById(R.id.postComment_button);

        FirebaseUser userInfo;
        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        String uid = userInfo.getUid();
        //publish comment
        postComment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String comment =   postComment_ET.getText().toString().trim();

                if (comment.isEmpty()){
                    postComment_ET.setError("Enter Heading ");
                }else {
                    //posting comment
                    Comment comment1 = new Comment(comment,uid);
                    FirebaseDatabase.getInstance().getReference("comments").child(postKey).push()
                            .setValue(comment1).addOnCompleteListener(task -> {

                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Successfully Publish", Toast.LENGTH_SHORT).show();
                                    postComment_ET.setText("");
                                }else {
                                    Toast.makeText(getContext(), "Not publish", Toast.LENGTH_SHORT).show();
                                }
                    });
                }
            }
        });

        //Setting recycler view
        postComment_RV = view.findViewById(R.id.postedComment_RV);
        postComment_RV.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Comment> options1 =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("comments").child(postKey), Comment.class)
                        .build();

        commentAdapt = new commentAdapter(options1);
        postComment_RV.setAdapter(commentAdapt);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        commentAdapt.startListening();
    }
}