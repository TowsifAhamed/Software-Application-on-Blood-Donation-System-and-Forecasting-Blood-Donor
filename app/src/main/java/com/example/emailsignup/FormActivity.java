package com.example.emailsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FormActivity extends AppCompatActivity {

    private EditText pname,donday,hosaddress,doncity,doncountry;
    private Spinner pbgroup;
    private Button donsubmit;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("donationforms").push();
        final DatabaseReference ureference = database.getReference("users");
        pname = findViewById(R.id.pname);
        donday = findViewById(R.id.donday);
        hosaddress = findViewById(R.id.hosaddress);
        doncity = findViewById(R.id.doncity);
        doncountry = findViewById(R.id.doncountry);
        pbgroup = findViewById(R.id.pbgroup);
        donsubmit = findViewById(R.id.donsubmit);

        final Calendar donationCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dondate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                donationCalendar.set(Calendar.YEAR, year);
                donationCalendar.set(Calendar.MONTH, monthOfYear);
                donationCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                donday.setText(sdf.format(donationCalendar.getTime()));
            }
        };
        donday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FormActivity.this, dondate, donationCalendar
                        .get(Calendar.YEAR), donationCalendar.get(Calendar.MONTH),
                        donationCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        String[] bgroupSpinner1 = new String[] {
                "Choose Blood Group", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"
        };
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FormActivity.this, android.R.layout.simple_spinner_item, bgroupSpinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgroupSpinner1[0]= "Choose Blood Group";
        pbgroup.setAdapter(adapter1);

        //UUID uuid = UUID. randomUUID();
        //final String formID = uuid. toString();

        donsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pname.getText().toString().trim().isEmpty()) {
                    pname.setError("Email is required!");
                    pname.requestFocus();
                    return;
                }
                if (pbgroup.getSelectedItem().toString().trim().equals("Choose Blood Group")) {
                    Toast.makeText(FormActivity.this,"Please choose required blood group!",Toast.LENGTH_SHORT).show();
                    pbgroup.requestFocus();
                    return;
                }
                if (donday.getText().toString().trim().isEmpty()) {
                    donday.setError("Email is required!");
                    donday.requestFocus();
                    return;
                }
                if (hosaddress.getText().toString().trim().isEmpty()) {
                    hosaddress.setError("Email is required!");
                    hosaddress.requestFocus();
                    return;
                }
                if (doncity.getText().toString().trim().isEmpty()) {
                    doncity.setError("Email is required!");
                    doncity.requestFocus();
                    return;
                }
                if (doncountry.getText().toString().trim().isEmpty()) {
                    doncountry.setError("Email is required!");
                    doncountry.requestFocus();
                    return;
                }
                user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                userID = user.getUid();
                //Map<String, DonationForm> donrequest = new HashMap<>();
                //donrequest.put(formID, new DonationForm(pname.getText().toString().trim(),userID,"",hosaddress.getText().toString().trim(),donday.getText().toString().trim(),doncity.getText().toString().trim(),doncountry.getText().toString().trim(),"",false));
                reference.setValue(new DonationForm(pname.getText().toString().trim(),pbgroup.getSelectedItem().toString().trim(),userID,"",hosaddress.getText().toString().trim(),donday.getText().toString().trim(),doncity.getText().toString().trim(),doncountry.getText().toString().trim(),"",false));

                ureference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userprofile = dataSnapshot.getValue(User.class);
                        int numberreq = userprofile.times_requested;
                        numberreq += 1;
                        ureference.child(userID).child("times_requested").setValue(numberreq);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(FormActivity.this,"Request for Blood Donation is successful! Please wait for a response from a Donor!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FormActivity.this, MainActivity.class));
            }
        });
    }
}