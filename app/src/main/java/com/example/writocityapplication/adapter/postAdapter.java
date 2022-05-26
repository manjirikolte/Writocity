package com.example.writocityapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writocityapplication.R;
import com.example.writocityapplication.fragments.CommentFragment;
import com.example.writocityapplication.fragments.FollowUserFragment;
import com.example.writocityapplication.fragments.FullPostviewFragment;
import com.example.writocityapplication.models.User;
import com.example.writocityapplication.models.WritePost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class postAdapter extends FirebaseRecyclerAdapter<WritePost, postAdapter.myViewHolder> {

    public postAdapter(@NonNull FirebaseRecyclerOptions<WritePost> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull WritePost model) {
        holder.post_heading.setText(model.getTitle());

        String postSubstring = model.getArticleBody();

        holder.post_content.setText(postSubstring);
        Picasso.get().load(model.getPostImage()).into(holder.post_IV_image);
        String authorName = model.getPostedBy().toString();

        FirebaseUser userInfo;

        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = userInfo.getUid();

        dbReference.child(authorName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String fullName = userProfile.fullName;
                    String profilePicUrl = userProfile.profilePhoto;
                    holder.post_TV_authorName.setText(fullName);
                    Picasso.get().load(profilePicUrl).into(holder.post_IV_profilePic);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Follow Profile Activity
         holder.post_IV_profilePic.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AppCompatActivity activity = (AppCompatActivity)v.getContext();
                 activity.getSupportFragmentManager().beginTransaction()
                         .replace(R.id.home_FL_frame, new FollowUserFragment(model.getPostedBy())).addToBackStack(null).commit();
             }
         });

        // full post view
        holder.post_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_FL_frame, new FullPostviewFragment(model.getTitle(),model.getArticleBody(), model.getPostImage(),model.getPostedBy())).addToBackStack(null).commit();
            }
        });

        //Post share button code
        holder.post_share.setOnClickListener(v->{
            Intent sharePost_intent = new Intent(Intent.ACTION_SEND);
            sharePost_intent.setType("text/plane");
            sharePost_intent.putExtra(Intent.EXTRA_TEXT,model.getArticleBody());
            v.getContext().startActivity(sharePost_intent);

        });

        //post Like button code
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();
        String postKey = getRef(position).getKey();

        holder.getlikebuttonstatus(postKey,userid);

           holder.like_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   likeReference = FirebaseDatabase.getInstance().getReference("likes");

                   testclick = true;

                   likeReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (testclick == true){

                               if (snapshot.child(postKey).hasChild(userid)){
                                   likeReference.child(postKey).removeValue();
                                   testclick = false;
                               }else {
                                   likeReference.child(postKey).child(userid).setValue(true);
                                   testclick = false;
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });

               }
           });

           //comments fragment
            holder.commentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity)v.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_FL_frame, new CommentFragment(model.getPostedBy(), postKey)).addToBackStack(null).commit();
                }
            });


    }

    DatabaseReference likeReference;
    Boolean testclick = false;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_post, parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView post_heading, post_content, post_TV_authorName, likeText;
        ImageView post_IV_image, post_IV_profilePic, post_share, like_button, commentView;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            post_heading = itemView.findViewById(R.id.post_heading);
            post_content = itemView.findViewById(R.id.post_content);
            post_IV_image = itemView.findViewById(R.id.post_IV_image);
            post_TV_authorName = itemView.findViewById(R.id.post_TV_authorName);
            post_IV_profilePic = itemView.findViewById(R.id.post_IV_profilePic);
            post_share = itemView.findViewById(R.id.post_share);
            like_button = itemView.findViewById(R.id.likeButton);
            likeText = itemView.findViewById(R.id.likeText);
            commentView = itemView.findViewById(R.id.commentsView);

        }


//        Like Button Code
        public void getlikebuttonstatus(final String postkey, final String userid){
            likeReference = FirebaseDatabase.getInstance().getReference("likes");
            likeReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                   if (snapshot.child(postkey).hasChild(userid)){
                       int likeCount = (int)snapshot.child(postkey).getChildrenCount();
                       likeText.setText(likeCount + " likes");
                       like_button.setImageResource(R.drawable.ic_baseline_favorite_24);
                   }else {
                       int likeCount = (int)snapshot.child(postkey).getChildrenCount();
                       likeText.setText(likeCount + " likes");
                       like_button.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                   }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}

