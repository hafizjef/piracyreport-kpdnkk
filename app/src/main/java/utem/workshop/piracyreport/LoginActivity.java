package utem.workshop.piracyreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    Intent intent;

    @BindView(R.id.toolbarLogin)
    Toolbar loginToolbar;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etUsername)
    TextInputEditText etEmail;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(loginToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// ...
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etEmail.getText())
                        && !TextUtils.isEmpty(etPassword.getText())
                        && Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {

                    signIn(etEmail.getText().toString(), etPassword.getText().toString());
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            final DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users")
                                    .child(user.getUid());

                            mData.keepSynced(true);

                            mData.child("keep-sync").setValue("delete-this", new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mData.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Timber.i("Name: " + dataSnapshot.child("name").getValue());
                                            Timber.i("Is Admin: " + dataSnapshot.child("isAdmin").getValue());


                                            if ((boolean)dataSnapshot.child("isAdmin").getValue()) {
                                                intent = new Intent(getBaseContext(), AdminActivity.class);
                                            } else {
                                                intent = new Intent(getBaseContext(), MainActivity.class);
                                            }

                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });

                        } else {
                            Timber.i("ERROR:" + task.getException());
                        }
                    }
                });
    }
}
