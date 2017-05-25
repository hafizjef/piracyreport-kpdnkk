package utem.workshop.piracyreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
        mAuth = FirebaseAuth.getInstance();

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doLogin();
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        if (!TextUtils.isEmpty(etEmail.getText())
                && !TextUtils.isEmpty(etPassword.getText())
                && Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {

            signIn(etEmail.getText().toString(), etPassword.getText().toString());
        } else {
            new MaterialDialog.Builder(LoginActivity.this)
                    .title("Input Error")
                    .content("Invalid email/password combination, Please check your input")
                    .positiveText("OK")
                    .show();
            etEmail.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etEmail, InputMethodManager.SHOW_FORCED);
        }
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
        final ProgressDialog loginDialog = new ProgressDialog(this);
        loginDialog.setTitle("Loading");
        loginDialog.setMessage("Please wait, Logging In...");
        loginDialog.setCancelable(false);
        loginDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            final DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users")
                                    .child(user.getUid());

                            mData.keepSynced(true);

                            mData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Timber.i("Name: " + dataSnapshot.child("name").getValue());
                                    Timber.i("Is Admin: " + dataSnapshot.child("isAdmin").getValue());


                                    if ((boolean) dataSnapshot.child("isAdmin").getValue()) {
                                        intent = new Intent(getBaseContext(), AdminActivity.class);
                                    } else {
                                        intent = new Intent(getBaseContext(), StaffActivity.class);
                                    }

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    loginDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            loginDialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Timber.i("ERROR:" + task.getException());
                        }
                    }
                });
    }
}
