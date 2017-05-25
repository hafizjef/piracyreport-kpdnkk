package utem.workshop.piracyreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

/**
 * This activity are used to initialize the application state,
 * 1. Implement splash screen
 * 2. Check for login-state
 * 3. Redirect to defined view
 * 4. Database initialization
 **/

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            Timber.i("Currently Signed in: " + mAuth.getCurrentUser().getUid());

            final DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users")
                    .child(mAuth.getCurrentUser().getUid());

            mData.keepSynced(true);

            mData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Timber.i("Name: " + dataSnapshot.child("name").getValue());
                    Timber.i("Is Admin: " + dataSnapshot.child("isAdmin").getValue());
                    isAdmin = (boolean) dataSnapshot.child("isAdmin").getValue();

                    if (isAdmin) {
                        intent = new Intent(getBaseContext(), AdminActivity.class);
                    } else {
                        intent = new Intent(getBaseContext(), StaffActivity.class);
                    }

                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
