package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
       ActivityPhoneNumberBinding binding ;

       FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth =FirebaseAuth.getInstance() ;

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(StartActivity.this ,MainActivity.class));
            finish();
        }

        binding.phoneBox.requestFocus();

        binding.cotinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,OTPActivity.class) ;
                intent.putExtra("phoneNumber" , binding.phoneBox.getText().toString()) ;
                startActivity(intent);
            }
        });
    }
}