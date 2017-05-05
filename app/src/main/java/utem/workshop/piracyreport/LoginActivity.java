package utem.workshop.piracyreport;

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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

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
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference mdb = FirebaseDatabase.getInstance().getReference("users");
                            Map<String, Object> userEntry = new HashMap<>();
                            userEntry.put("email", user.getEmail());
                            userEntry.put("isAdmin", false);
                            mdb.child(user.getUid()).setValue(userEntry, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Timber.e(databaseError.getMessage());
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }
}
