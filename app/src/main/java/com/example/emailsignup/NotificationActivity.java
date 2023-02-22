package com.example.emailsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notificationrv;
    private NotificationRecyclerAdapter notificationra;
    ArrayList<String> notiitem=new ArrayList<>();
    ArrayList<String> notikeys=new ArrayList<>();
    //private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //final String[] test = {"test"};
        //test = findViewById(R.id.test);
        notificationrv = findViewById(R.id.notificationrv);
        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference notireference = database.getReference("notifications").push();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        String userID = user.getUid();

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User userprofile = dataSnapshot.getValue(User.class);
                if (userprofile != null) {
                    FirebaseDatabase.getInstance().getReference("donationforms").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                DonationForm noti = snapshot.getValue(DonationForm.class);
                                if(userprofile.city.equals(noti.don_city) && !noti.is_completed) {
                                    //Toast.makeText(NotificationActivity.this, test[0],Toast.LENGTH_SHORT).show();
                                    //test[0] = test[0] + " test";
                                    String description = noti.patient_bgroup +" Blood needed at "+noti.hospital_address+" in "+noti.don_city+" on "+noti.don_date+" , Please response if you can. ";
                                    notiitem.add(description);
                                    notikeys.add(snapshot.getKey());
                                    //test.setText(snapshot.getKey());
                                    notificationra.notifyDataSetChanged();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        notificationrv.setLayoutManager(new LinearLayoutManager(this));
        notificationra = new NotificationRecyclerAdapter(this,notiitem,notikeys);
        notificationrv.setAdapter(notificationra);
    }
}

