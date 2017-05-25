package utem.workshop.piracyreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.R;
import utem.workshop.piracyreport.models.Report;
import utem.workshop.piracyreport.utils.Constants;

public class StatisticFragment extends Fragment {

    @BindView(R.id.barChart)
    BarChart barChart;

    public static StatisticFragment newInstance() {
        StatisticFragment fragment = new StatisticFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_view, container, false);
        ButterKnife.bind(this, view);

        // Bar Data
        final List<BarEntry> barEntries = new ArrayList<>();

        final ArrayList<Report> rInQueue = new ArrayList<>();
        final ArrayList<Report> rInProgress = new ArrayList<>();
        final ArrayList<Report> rResolved = new ArrayList<>();

        barChart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));


        Description description = new Description();
        description.setText("Report Status charts");

        barChart.setDescription(description);
        barChart.setDrawBorders(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        Legend legend = barChart.getLegend();
        legend.setExtra(ColorTemplate.PASTEL_COLORS, new String[] {"In Queue", "In Progress", "Resolved"});

        YAxis left = barChart.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(true); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        left.setDrawLimitLinesBehindData(false);
        barChart.getAxisRight().setEnabled(false); // no right axis


        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("reports");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                barEntries.clear();
                rInProgress.clear();
                rInQueue.clear();
                rResolved.clear();

                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    Report report = postSnap.getValue(Report.class);
                    //report.setReportID(postSnap.getKey());
                    if (report.getStatus().equals(Constants.ACTION_QUEUE)) {
                        rInQueue.add(report);
                    } else if (report.getStatus().equals(Constants.ACTION_IN_PROGRESS)) {
                        rInProgress.add(report);
                    } else if (report.getStatus().equals(Constants.ACTION_RESOLVED)) {
                        rResolved.add(report);
                    }
                }

                barEntries.add(new BarEntry(0f, rInQueue.size()));
                barEntries.add(new BarEntry(1f, rInProgress.size()));
                barEntries.add(new BarEntry(2f, rResolved.size()));

                BarDataSet barDataSet = new BarDataSet(barEntries, "Report Status");
                barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChart.setFitBars(true);
                barChart.notifyDataSetChanged();
                barChart.animateY(2000, Easing.EasingOption.Linear);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
