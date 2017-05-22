package utem.workshop.piracyreport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.fragments.FormFragment;
import utem.workshop.piracyreport.fragments.SubmitReportFragment;
import utem.workshop.piracyreport.utils.DataManager;

public class MainActivity extends AppCompatActivity implements DataManager {

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String NAME = "data_name";
    private static final String EMAIL = "data_email";


    @BindView(R.id.stepperLayout)
    StepperLayout mStepperLayout;

    private String
            name, email, category,
            brand, desc, state, addr = null;
    private double lat, lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int startingStepPosition = 0;

        if (savedInstanceState != null) {
            startingStepPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);
            name = savedInstanceState.getString(NAME);
            email = savedInstanceState.getString(EMAIL);
        }
        mStepperLayout.setAdapter(new FormFragmentAdapter(getSupportFragmentManager(), this), startingStepPosition);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        outState.putString(NAME, name);
        outState.putString(EMAIL, email);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveEmail(String email) {
        this.email = email;
    }

    @Override
    public void saveName(String name) {
        this.name = name;
    }

    @Override
    public void saveCategory(String category) {
        this.category = category;
    }

    @Override
    public void saveBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public void saveDesc(String description) {
        this.desc = description;
    }

    @Override
    public void saveAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public void saveState(String state) {
        this.state = state;
    }

    @Override
    public void saveLat(double latitude) {
        this.lat = latitude;
    }

    @Override
    public void saveLon(double longitude) {
        this.lon = longitude;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getAddr() {
        return addr;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    static class FormFragmentAdapter extends AbstractFragmentStepAdapter {

        FormFragmentAdapter(@NonNull FragmentManager fm, @NonNull Context ctx) {
            super(fm, ctx);
        }

        @Override
        public Step createStep(@IntRange(from = 0L) int position) {
            switch (position) {
                case 0:
                    return FormFragment.newInstance();
                case 1:
                    return SubmitReportFragment.newInstance();
                default:
                    return FormFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
