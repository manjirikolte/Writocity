package com.example.writocityapplication.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writocityapplication.R;
import com.example.writocityapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullPostviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullPostviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String postTitle, postBodyContent, postImage , postedByAuthorName;
    TextToSpeech tts;


    public FullPostviewFragment() {
        // Required empty public constructor
    }
    public  FullPostviewFragment(String title, String bodyContent, String postImage, String postedByAuthorName){
        this.postTitle = title;
        this.postBodyContent = bodyContent;
        this.postImage = postImage;
        this.postedByAuthorName = postedByAuthorName;
    }

    public static FullPostviewFragment newInstance(String param1, String param2) {
        FullPostviewFragment fragment = new FullPostviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_full_postview, container, false);

        ImageView textToSpeechButton = view.findViewById(R.id.TextToSPeech_Button);
        TextView fullPost_content = view.findViewById(R.id.fullPost_content);
        TextView fullPost_heading = view.findViewById(R.id.fulllPost_heading);
        ImageView fullPost_IV_postImage = view.findViewById(R.id.fullPost_IV_postImage);
        TextView fullPost_TV_authorName = view.findViewById(R.id.fullPost_TV_authorName);
        ImageView fullPost_IV_authorPic = view.findViewById(R.id.fullPost_IV_profilePic);
        ImageView fullPostView_backButton = view.findViewById(R.id.fullPostView_backButton);

        //TextToSpeechButton code
        textToSpeechButton.setOnClickListener(v -> {

                    if (tts != null && tts.isSpeaking()){
                        tts.stop();
                        textToSpeechButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

                    }else {
                        textToSpeechButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

                        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS){
                                    tts.setLanguage(Locale.US);
                                    tts.setSpeechRate(1.0f);
                                    tts.speak(fullPost_content.getText().toString(),TextToSpeech.QUEUE_ADD,null);

                                }
                            }
                        });

                    }


        });




//        Back button code
        fullPostView_backButton.setOnClickListener(view2 -> {
            OnBackPressed();
        });

        fullPost_heading.setText(postTitle);
        fullPost_content.setText(postBodyContent);
        Picasso.get().load(postImage).resize(100,50).into(fullPost_IV_postImage);


        FirebaseUser userInfo;
        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

        dbReference.child(postedByAuthorName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String fullName = userProfile.fullName;
                    String profilePicUrl = userProfile.profilePhoto;
                    fullPost_TV_authorName.setText(fullName);
                    Picasso.get().load(profilePicUrl).into(fullPost_IV_authorPic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    public void OnBackPressed(){

    }
}