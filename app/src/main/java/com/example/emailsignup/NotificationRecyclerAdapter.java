package com.example.emailsignup;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<String> notiitem;
    ArrayList<String> notikeys;
    public NotificationRecyclerAdapter(Context context, ArrayList<String> notiitem, ArrayList<String> notikeys) {
        this.context = context;
        this.notiitem = notiitem;
        this.notikeys = notikeys;
    }

    @NonNull
    @Override
    public NotificationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notification_recyclerview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.ViewHolder holder, int position) {
        holder.notification.setText(notiitem.get(position));
    }

    @Override
    public int getItemCount() {
        return notiitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notification;
        Button donateblood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notification);
            donateblood = itemView.findViewById(R.id.donateblood);
            //String test = String.valueOf(notiitem.size());
            //Toast.makeText(context, test,Toast.LENGTH_SHORT).show();
            donateblood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DatabaseReference ureference = FirebaseDatabase.getInstance().getReference("users");
                    final String cuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String formid = notikeys.get(getAdapterPosition());
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("donationforms").child(formid);
                    reference.child("donor_user_id").setValue(cuserID);
                    reference.child("is_completed").setValue(true);

                    ureference.child(cuserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User userprofile = dataSnapshot.getValue(User.class);
                            int numberdon = userprofile.times_donated;
                            numberdon += 1;
                            ureference.child(cuserID).child("times_donated").setValue(numberdon);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Intent restart = new Intent(context,MainActivity.class);
                    context.startActivity(restart);
                }
            });
        }
    }
}
