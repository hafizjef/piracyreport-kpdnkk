package utem.workshop.piracyreport;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements DataManager{

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String NAME = "data_name";
    private static final String EMAIL = "data_email";


    @BindView(R.id.stepperLayout)
    StepperLayout mStepperLayout;

    private String name, email, category, type, desc = null;
    private int lat, lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int startingStepPosition = 0;

        if (savedInstanceState !=null) {
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
        if (id == R.id.action_settings) {
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

    }

    @Override
    public void saveCatType(String type) {

    }

    @Override
    public void saveDesc(String description) {

    }

    @Override
    public void saveLat(int latitude) {

    }

    @Override
    public void saveLon(int longitude) {

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
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public int getLat() {
        return 0;
    }

    @Override
    public int getLon() {
        return 0;
    }

    static class FormFragmentAdapter extends AbstractFragmentStepAdapter {

        FormFragmentAdapter(@NonNull FragmentManager fm, @NonNull Context ctx) {
            super(fm, ctx);
        }

        @Override
        public Step createStep(@IntRange(from = 0L) int position) {
            switch (position) {
                case 0: return FormFragment.newInstance();
                case 1: return ImageFragment.newInstance();
                case 2: return ViewFormFragment.newInstance();
                default: return FormFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
