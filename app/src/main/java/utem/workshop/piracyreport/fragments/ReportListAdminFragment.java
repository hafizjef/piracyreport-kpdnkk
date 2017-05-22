package utem.workshop.piracyreport.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcel;
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

    @BindView(R.id.rvReportList)
    RecyclerView rvReportList;

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static ReportListAdminFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ReportListAdminFragment fragment = new ReportListAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_list_admin, container, false);
        ButterKnife.bind(this, view);

        final ArrayList<Report> reports = new ArrayList<>();
        final ReportsAdapter adapter = new ReportsAdapter(getContext(), reports);

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("reports");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Re-init
                reports.clear();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Report report = postSnap.getValue(Report.class);
                    reports.add(report);
                }
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
