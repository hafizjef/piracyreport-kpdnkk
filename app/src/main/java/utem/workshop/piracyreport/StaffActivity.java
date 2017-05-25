package utem.workshop.piracyreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.adapters.ReportsAdapter;
import utem.workshop.piracyreport.adapters.decorators.SpaceItemDecoration;
import utem.workshop.piracyreport.models.Report;
import utem.workshop.piracyreport.utils.Constants;

public class StaffActivity extends AppCompatActivity {

    private String uuid = "";

    private boolean filterDisabled = false;

    ArrayList<Report> mFilteredReport = new ArrayList<>();
    ArrayList<Report> mReport = new ArrayList<>();
    ReportsAdapter mFilteredAdapter = new ReportsAdapter(this, mFilteredReport);
    ReportsAdapter mAdapter = new ReportsAdapter(this, mReport);

    @BindView(R.id.rvJobList)
    RecyclerView rvJobList;

    @BindView(R.id.jobListEmpty)
    TextView jobListEmpty;

    @BindView(R.id.jobToolbar)
    Toolbar jobToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        ButterKnife.bind(this);

        setSupportActionBar(jobToolbar);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uuid = mAuth.getCurrentUser().getUid();

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("reports");
        Query queryRef = mData.orderByChild("assignedID").equalTo(uuid);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Re-init
                mReport.clear();
                mFilteredReport.clear();

                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    Report report = postSnap.getValue(Report.class);
                    report.setReportID(postSnap.getKey());

                    mReport.add(report);

                    if (!report.getStatus().equals(Constants.ACTION_RESOLVED)) {
                        mFilteredReport.add(report);
                    }
                }
                if (mReport.isEmpty()) {
                    rvJobList.setVisibility(View.GONE);
                    jobListEmpty.setVisibility(View.VISIBLE);
                } else {
                    rvJobList.setVisibility(View.VISIBLE);
                    jobListEmpty.setVisibility(View.GONE);
                }
                mFilteredAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFilteredAdapter.setOnItemClickListener(new ReportsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Report selectReport = mFilteredReport.get(position);
                Intent intent = new Intent(StaffActivity.this, StaffActionActivity.class);
                intent.putExtra(Constants.REPORT_OBJ, Parcels.wrap(selectReport));
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new ReportsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Report selectReport = mReport.get(position);
                Intent intent = new Intent(StaffActivity.this, StaffActionActivity.class);
                intent.putExtra(Constants.REPORT_OBJ, Parcels.wrap(selectReport));
                startActivity(intent);
            }
        });

        rvJobList.addItemDecoration(new SpaceItemDecoration(16));
        rvJobList.setAdapter(mFilteredAdapter);
        rvJobList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_staff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.staff_logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.show_all_report:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    filterDisabled = true;
                    toggleFilter();
                } else {
                    filterDisabled = false;
                    toggleFilter();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFilter() {

        if (filterDisabled) {
            rvJobList.swapAdapter(mAdapter, false);
            rvJobList.setAdapter(mAdapter);
        } else {
            rvJobList.swapAdapter(mFilteredAdapter, false);
            rvJobList.setAdapter(mFilteredAdapter);
        }
    }
}
