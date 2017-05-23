package utem.workshop.piracyreport.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.AssignStaffActivity;
import utem.workshop.piracyreport.R;
import utem.workshop.piracyreport.adapters.ReportsAdapter;
import utem.workshop.piracyreport.adapters.decorators.SpaceItemDecoration;
import utem.workshop.piracyreport.models.Report;
import utem.workshop.piracyreport.utils.Constants;


public class ReportListAdminFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    ArrayList<Report> reports = new ArrayList<>();
    ArrayList<Report> noAssignReport = new ArrayList<>();
    ReportsAdapter adapter = new ReportsAdapter(getContext(), reports);
    ReportsAdapter noAssignAdapter = new ReportsAdapter(getContext(), noAssignReport);
    @BindView(R.id.rvReportList)
    RecyclerView rvReportList;
    @BindView(R.id.emptyView)
    TextView emptyView;
    private int mPage;

    public static ReportListAdminFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ReportListAdminFragment fragment = new ReportListAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_unassigned:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    refreshView(true);
                } else {
                    refreshView(false);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshView(boolean show) {

        if (show) {
            rvReportList.swapAdapter(noAssignAdapter, false);
        } else {
            rvReportList.swapAdapter(adapter, false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_list_admin, container, false);
        ButterKnife.bind(this, view);


        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("reports");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Re-init
                reports.clear();
                noAssignReport.clear();

                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    Report report = postSnap.getValue(Report.class);
                    report.setReportID(postSnap.getKey());

                    reports.add(report);

                    if (report.getAssigned().equals(Constants.NOT_ASSIGNED)) {
                        noAssignReport.add(report);
                    }
                }
                if (reports.isEmpty()) {
                    rvReportList.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    rvReportList.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                noAssignAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter.setOnItemClickListener(new ReportsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Report selectReport = reports.get(position);
                Intent intent = new Intent(getContext(), AssignStaffActivity.class);
                intent.putExtra(Constants.REPORT_OBJ, Parcels.wrap(selectReport));
                startActivity(intent);
            }
        });

        rvReportList.addItemDecoration(new SpaceItemDecoration(16));
        rvReportList.setAdapter(adapter);
        rvReportList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
