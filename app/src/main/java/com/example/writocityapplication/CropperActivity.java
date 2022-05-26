package com.example.writocityapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

    String result;
    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

        readIntent();

        String destinationUri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg")
                .toString();
        UCrop.Options options = new UCrop.Options();

        UCrop.of(fileUri,Uri.fromFile(new File(getCacheDir(), destinationUri)))
                .withOptions(options)
                .withAspectRatio(16,9)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000,2000)
                .start(CropperActivity.this);
    }

    private void readIntent() {

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            result = intent.getStringExtra("PostImageURI");
            fileUri= Uri.parse(result);

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            final Uri resultUri = UCrop.getOutput(data);
            Intent returnImageIntent = new Intent();
            returnImageIntent.putExtra("ResultCropURI", resultUri + "");
            returnImageIntent.putExtra("requestCode", "-1");
            setResult(-1,returnImageIntent);
            finish();
        }else if (resultCode == UCrop.RESULT_ERROR){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }


    }


}