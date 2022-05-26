package com.example.writocityapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writocityapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText editProfile_ET_fullName,editProfile_ET_bio, editProfile_ET_userName;
    LinearLayout editProfile_LL_saveChanges;
    TextView editProfile_TV_ChangeProfilePic;
    ImageView editProfile_IV_profilePic;
    Uri imageUrl;


    private FirebaseAuth mAuth;
    FirebaseUser userInfo;
    String userId;
    DatabaseReference dbReference;
    FirebaseStorage storage;


    HashMap<String, Object> changeUser = new HashMap<String, Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfile_ET_fullName = findViewById(R.id.editProfile_ET_fullName);
        editProfile_ET_userName = findViewById(R.id.editProfile_ET_userName);
        editProfile_ET_bio = findViewById(R.id.editProfile_ET_bio);
        editProfile_LL_saveChanges = findViewById(R.id.editProfile_LL_saveChanges);
        editProfile_TV_ChangeProfilePic = findViewById(R.id.editProfile_TV_ChangeProfilePic);
        editProfile_IV_profilePic = findViewById(R.id.followProfile_IV_profilePic);


        userInfo = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        assert userInfo != null;
        userId = userInfo.getUid();
        storage = FirebaseStorage.getInstance();

        dbReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){

                    String fullName = userProfile.fullName;
                    String userName = userProfile.username;
                    String profileBio = userProfile.bio;
                    String profilePicUrl = userProfile.profilePhoto;
                    if (profilePicUrl != null){
                        Picasso.get().load(profilePicUrl).into(editProfile_IV_profilePic);
                    }

                    editProfile_ET_fullName.setText(fullName);
                    editProfile_ET_userName.setText(userName);
                    editProfile_ET_bio.setText(profileBio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        // change image
        editProfile_TV_ChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Image is selected successfully
                mGetContent.launch("image/*");
            }
        });

        //Save changes
        editProfile_LL_saveChanges.setOnClickListener(view -> saveProfileChanges());


    }


//  Update profile info
    private void saveProfileChanges() {


        String username = editProfile_ET_userName.getText().toString().trim();
        String bio = editProfile_ET_bio.getText().toString().trim();
        String fullName = editProfile_ET_fullName.getText().toString().trim();

        if (username.isEmpty()){
            editProfile_ET_userName.setError("Username is required!");
            editProfile_ET_userName.requestFocus();
            return;
        }

        if (fullName.isEmpty()){
            editProfile_ET_fullName.setError("Username is required!");
            editProfile_ET_fullName.requestFocus();
            return;
        }

        if (imageUrl != null){
            StorageReference SReference = storage.getReference().child("ProfilePhoto/"+userId);
            SReference.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getApplicationContext(), "storage upload", Toast.LENGTH_SHORT).show();

                    SReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            changeUser.put("profilePhoto",uri.toString());
                            dbReference.child(userId).updateChildren(changeUser);

                        }
                    });
                }
            });
        }

        changeUser.put("fullName",fullName);
        changeUser.put("username", username);
        changeUser.put("bio", bio);

        dbReference.child(userId).updateChildren(changeUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Method to select an image from gallery
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            result -> {

                if (result != null){
                    editProfile_IV_profilePic.setImageURI(result);
                    imageUrl = result;
                }
            });
}