package com.example.emailsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.AutofillService;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,pass1,pass2;
    private CheckBox donor,in_need;
    private Button registration_btn,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email);
        pass1=findViewById(R.id.pass1);
        pass2=findViewById(R.id.pass2);
        donor=findViewById(R.id.donor);
        in_need=findViewById(R.id.in_need);
        registration_btn=findViewById(R.id.registration_btn);
        login = findViewById(R.id.login);

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
    public void RegisterUser(){
        final String  stremail=email.getText().toString().trim(),
                strpass1=pass1.getText().toString().trim(),
                strpass2=pass2.getText().toString().trim();
        final Boolean booldonor=donor.isChecked(),
                boolin_need=in_need.isChecked();
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        final String formattedLocalDate = localDate.format(formatter);
        if (stremail.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(stremail).matches()) {
            email.setError("Email is not valid!");
            email.requestFocus();
            return;
        }
        if (strpass1.isEmpty()) {
            pass1.setError("Password is required!");
            pass1.requestFocus();
            return;
        }
        if (strpass2.isEmpty()) {
            pass2.setError("Confirm Password is required!");
            pass2.requestFocus();
            return;
        }


        if (!strpass1.equals(strpass2)){
            Toast.makeText(RegisterActivity.this,"Passwords didn't match",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(strpass1.length()<=6){
                Toast.makeText(RegisterActivity.this,"Password must be greater then 6 character",Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(stremail, strpass1)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(stremail, booldonor, boolin_need, "", "", "", "", "", 0.0, 0.0, "", "", "", formattedLocalDate, formattedLocalDate, 0, 0, 0);

                                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this,"User registration is successful! Please Verify your mail and Login!",Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            }
                                            else {
                                                Toast.makeText(RegisterActivity.this,"Failed to register, try again!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this,"Failed to register, try again!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}