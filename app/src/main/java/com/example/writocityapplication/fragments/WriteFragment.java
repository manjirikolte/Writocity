package com.example.writocityapplication.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.writocityapplication.R;
import com.example.writocityapplication.models.WritePost;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class WriteFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    // Declaring variables
    EditText write_ET_heading, write_ET_post, write_ET_tags;
    LinearLayout write_LL_publishPost;
    ImageView write_IV_addPostImage, speechToText, postLike;
    ProgressBar write_progressBar;

    //Firebase Variable
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseStorage storage;
    Uri imageUrl;

    public WriteFragment() {
        // Required empty public constructor
    }

    public static WriteFragment newInstance(String param1, String param2) {
        WriteFragment fragment = new WriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        //Initializing variable
        write_ET_heading = view.findViewById(R.id.write_ET_heading);
        write_ET_heading.requestFocus();
        write_ET_post = view.findViewById(R.id.write_ET_post);
        write_ET_tags = view.findViewById(R.id.write_ET_tags);
        write_LL_publishPost = view.findViewById(R.id.write_LL_publishPost);
        write_IV_addPostImage = view.findViewById(R.id.write_IV_addPostImage);
        write_progressBar = view.findViewById(R.id.write_progressBar);
        speechToText = view.findViewById(R.id.speechToText);
        postLike = view.findViewById(R.id.likeText);
        //Firebase initializing
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();


        write_IV_addPostImage.setOnClickListener(view1 -> {
            // Image is selected successfully
            mGetContent.launch("image/*");
        });
        speechToText.setOnClickListener(view2 -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Start Speaking");
            startActivityForResult(intent,100);
        });

        //Publish post
        write_LL_publishPost.setOnClickListener(view12 -> {
            Toast.makeText(getContext(), "posting...", Toast.LENGTH_SHORT).show();
            String heading = write_ET_heading.getText().toString().toUpperCase();
            String postContent = write_ET_post.getText().toString();
            String tags = write_ET_tags.getText().toString();

            if (heading.isEmpty()){
                write_ET_heading.setError("Enter Heading ");
            }else if (postContent.isEmpty() ){
                write_ET_post.setError("Write something ");
            }else if(tags.isEmpty()){
                write_ET_tags.setError("add tags");
            }else if(imageUrl == null){
                Toast.makeText(getContext(), "Upload Image", Toast.LENGTH_SHORT).show();
            }else {

                //First upload Image and then upload content
//                progressDialog.show();
                StorageReference storageReference = storage.getReference().child("PostImages/" + UUID.randomUUID().toString());
                storageReference.putFile(imageUrl)
                        .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                //Upload post to firebase
                                WritePost uploadPost = new WritePost(heading, postContent, tags, uri.toString(),userId);
                                FirebaseDatabase.getInstance().getReference("Posts").push()
                                        .setValue(uploadPost).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){
                                        write_ET_heading.setText("");
                                        write_ET_post.setText("");
                                        write_ET_tags.setText("");
                                        Toast.makeText(getContext(), "Successfully publish", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
//                                            progressDialog.dismiss();
                                });
                            }
                        }))
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                float percent = (100 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                // Set properties to progress dialog box
                                //   progressDialog.setMessage("Loading..." + (int)percent + "%");
                                Toast.makeText(getContext(), "progress...", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        return view;

    }

    // Method to select an image from gallery
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            result -> {

//                Intent intent = new Intent(getContext(), CropperActivity.class);
//                intent.putExtra("PostImageURI", result.toString());
//                startActivity(intent);

                if (result != null){
                    write_IV_addPostImage.setImageURI(result);
                    imageUrl = result;
                }
            });


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK){
            write_ET_post.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }
}