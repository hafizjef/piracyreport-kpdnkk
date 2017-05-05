package utem.workshop.piracyreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Timber.i("Currently Signed in: " + mAuth.getCurrentUser().getUid());

            DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users")
                    .child(mAuth.getCurrentUser().getUid());
            ValueEventListener adminListener = mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Timber.i("Name: " + dataSnapshot.child("name").getValue());
                    Timber.i("Is Admin: " + dataSnapshot.child("isAdmin").getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
