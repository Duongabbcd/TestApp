package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.Model.User;
import com.example.whatsapp.databinding.ActivitySetupProfileBinding ;
import com.google.android.gms.common.internal.ApiExceptionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfileActivity extends AppCompatActivity {
    ActivitySetupProfileBinding binding ;
    FirebaseAuth auth ;
    Uri selectedImage ;
    FirebaseDatabase database ;
    FirebaseStorage storage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth= FirebaseAuth.getInstance() ;
        storage= FirebaseStorage.getInstance() ;
        database= FirebaseDatabase.getInstance() ;

        binding.setprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT) ;
                intent.setType("image/");
                startActivityForResult(intent ,45);
            }
        });

        binding.cotinueBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameBox.getText().toString() ;

                if(name.isEmpty()){
                    binding.nameBox.setError("Please type a name") ;
                    return;
                }
                if(selectedImage != null){
                    StorageReference reference = storage.getReference().child("Prfiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString() ;

                                        String uid = auth.getUid() ;
                                        String phone= auth.getCurrentUser().getPhoneNumber() ;
                                        String name = binding.nameBox.getText().toString() ;
                                        User user = new User(uid ,name ,phone ,imageUrl) ;

                                        database.getReference()
                                                .child("users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent intent = new Intent(SetupProfileActivity.this ,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                }) ;
                            }
                        }
                    }) ;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!= null){
            if(data.getData() != null) {
                binding.setprofileImage.setImageURI(data.getData());
                selectedImage = data.getData() ;
            }
        }
    }
}