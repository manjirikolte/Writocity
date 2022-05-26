package com.example.writocityapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writocityapplication.R;
import com.example.writocityapplication.models.Comment;
import com.example.writocityapplication.models.User;
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

public class commentAdapter extends FirebaseRecyclerAdapter<Comment, commentAdapter.myViewHolder> {


    public commentAdapter(@NonNull FirebaseRecyclerOptions<Comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Comment model) {
        holder.comment_TV.setText(model.getComment());


        FirebaseUser userInfo;
        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
        //String userId = userInfo.getUid();

        dbReference.child(model.getCommentPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String fullName = userProfile.fullName;
                    String profilePicUrl = userProfile.profilePhoto;
                    holder.comment_TV_authorName.setText(fullName);
                    Picasso.get().load(profilePicUrl).into(holder.comment_IV_profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_view, parent, false);
        return new commentAdapter.myViewHolder(view);

    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView comment_TV, comment_TV_authorName;
        ImageView comment_IV_profilePic;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_TV = itemView.findViewById(R.id.comment_TV);
            comment_TV_authorName = itemView.findViewById(R.id.comment_TV_authorName);
            comment_IV_profilePic = itemView.findViewById(R.id.comment_IV_profilePic);
        }
    }
}
