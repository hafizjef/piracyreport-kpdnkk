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
    private static final String DATA = "data";

    @BindView(R.id.stepperLayout)
    StepperLayout mStepperLayout;

    private String mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;
        mData = savedInstanceState != null ? savedInstanceState.getString(DATA) : null;
        mStepperLayout.setAdapter(new FormFragmentAdapter(getSupportFragmentManager(), this), startingStepPosition);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        outState.putString(DATA, mData);
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
    public void saveData(String data) {
        mData = data;
    }

    @Override
    public String getData() {
        return mData;
    }

    static class FormFragmentAdapter extends AbstractFragmentStepAdapter {

        FormFragmentAdapter(@NonNull FragmentManager fm, @NonNull Context ctx) {
            super(fm, ctx);
        }

        @Override
        public Step createStep(@IntRange(from = 0L) int position) {
            switch (position) {
                case 0: return FormFragment.newInstance();
                default: return FormFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
