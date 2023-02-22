package com.example.emailsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private EditText name,phone,country,city,bday,lastdonday;
    private TextView donor,in_need,email,bgroup_text,need_bgroup_text;
    private Spinner bgroup,need_bgroup;
    private Button submit,company;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        bday = findViewById(R.id.bday);
        donor = findViewById(R.id.donor);
        in_need = findViewById(R.id.in_need);
        bgroup = findViewById(R.id.bgroup);
        need_bgroup = findViewById(R.id.need_bgroup);
        submit = findViewById(R.id.submit);
        company = findViewById(R.id.company);
        lastdonday = findViewById(R.id.lastdondate);
        bgroup_text = findViewById(R.id.bgroup_text);
        need_bgroup_text = findViewById(R.id.need_bgroup_text);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                bday.setText(sdf.format(myCalendar.getTime()));
            }
        };
        bday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                lastdonday.setText(sdf.format(donationCalendar.getTime()));
            }
        };
        lastdonday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProfileActivity.this, dondate, donationCalendar
                        .get(Calendar.YEAR), donationCalendar.get(Calendar.MONTH),
                        donationCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userprofile = dataSnapshot.getValue(User.class);
                        String mail = userprofile.email;
                        mail = mail.substring(mail.indexOf("@") + 1);
                        if(mail.equals("gmail.com")) startActivity(new Intent(ProfileActivity.this, CompanyActivity.class));
                        else Toast.makeText(ProfileActivity.this,"User doesn't have the privilege!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        // Read from the database
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userprofile = dataSnapshot.getValue(User.class);
                if(userprofile!=null){
                    if(!userprofile.email.substring(userprofile.email.indexOf("@") + 1).equals("gmail.com")) {
                        company.setVisibility(View.GONE);
                    }
                    if(!userprofile.name.equals("")) {
                        name.setText(userprofile.name);
                    }
                    email.setText(userprofile.email);
                    if(!userprofile.phone.equals("")) {
                        phone.setText(userprofile.phone);
                    }
                    if(!userprofile.bday.equals("")) {
                        bday.setText(userprofile.bday);
                    }
                    if(!userprofile.lastdonday.equals("")) {
                        lastdonday.setText(userprofile.lastdonday);
                    }
                    if(userprofile.country.equals("")){
                        List<Address> addresses=null;
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(userprofile.latitude, userprofile.longitude, 1);
                            System.out.println(addresses.get(0).getLocality());
                            reference.child(userID).child("country").setValue(addresses.get(0).getCountryName());
                            reference.child(userID).child("city").setValue(addresses.get(0).getLocality());
                            country.setText(addresses.get(0).getCountryName());
                            city.setText(addresses.get(0).getLocality());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        country.setText(userprofile.country);
                        city.setText(userprofile.city);
                    }
                    String[] bgroupSpinner1 = new String[] {
                            "Choose Blood Group", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"
                    };
                    String[] bgroupSpinner2 = new String[] {
                            "Choose Blood Group", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"
                    };
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_item, bgroupSpinner1);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_item, bgroupSpinner2);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    if(userprofile.donor) {
                        donor.setText("Donor");
                        if(!userprofile.bgroup.equals("")) {
                            bgroupSpinner1[0]= userprofile.bgroup;
                            bgroup.setAdapter(adapter1);
                        }
                        else {
                            bgroupSpinner1[0]= "Choose Blood Group";
                            bgroup.setAdapter(adapter1);
                        }
                    }
                    else {
                        bgroup.setVisibility(View.GONE);
                        bgroup_text.setVisibility(View.GONE);
                    }
                    if(userprofile.in_need) {
                        in_need.setText("In Need");
                        if(!userprofile.need_bgroup.equals("")) {
                            bgroupSpinner2[0]=userprofile.need_bgroup;
                            need_bgroup.setAdapter(adapter2);
                        }
                        else {
                            bgroupSpinner2[0]= "Choose Needed Blood Group";
                            need_bgroup.setAdapter(adapter2);
                        }
                    }
                    else {
                        need_bgroup.setVisibility(View.GONE);
                        need_bgroup_text.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Failed to Read!",Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("users");
                userID = user.getUid();
                reference.child(userID).child("name").setValue(name.getText().toString().trim());
                reference.child(userID).child("phone").setValue(phone.getText().toString().trim());
                reference.child(userID).child("country").setValue(country.getText().toString().trim());
                reference.child(userID).child("city").setValue(city.getText().toString().trim());
                reference.child(userID).child("bgroup").setValue(bgroup.getSelectedItem().toString().trim());
                reference.child(userID).child("need_bgroup").setValue(need_bgroup.getSelectedItem().toString().trim());
                reference.child(userID).child("bday").setValue(bday.getText().toString().trim());
                reference.child(userID).child("lastdonday").setValue(lastdonday.getText().toString().trim());
            }
        });
    }
}