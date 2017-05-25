package utem.workshop.piracyreport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import utem.workshop.piracyreport.fragmentsAdapter.ImagePagerAdapter;
import utem.workshop.piracyreport.models.Report;
import utem.workshop.piracyreport.models.User;
import utem.workshop.piracyreport.utils.Constants;
import utem.workshop.piracyreport.utils.Utils;

public class StaffActionActivity extends AppCompatActivity {

    Report report;

    @BindView(R.id.staff_image_pager)
    ViewPager image_pager;

    @BindView(R.id.staffTxAddressLine)
    TextView txAddressLine;

    @BindView(R.id.staffTxBrand)
    TextView txBrand;

    @BindView(R.id.staffTxCategory)
    TextView txCategory;

    @BindView(R.id.staffTxDescription)
    TextView txDescription;

    @BindView(R.id.staffTxStaffName)
    TextView txStaffName;

    @BindView(R.id.staffTxStatus)
    TextView txStatus;

    @BindView(R.id.btnUpdate)
    Button btnUpdate;

    @BindView(R.id.btnMap)
    Button btnMap;

    @BindView(R.id.staffActionToolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_action);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            report = Parcels.unwrap(getIntent().getParcelableExtra(Constants.REPORT_OBJ));
        } else {
            finish();
        }

        if (report.getStatus().equals(Constants.ACTION_RESOLVED)) {
            btnUpdate.setEnabled(false);
        } else {
            btnUpdate.setEnabled(true);
        }

        ArrayList<String> mURL = report.getImgURL();

        ImagePagerAdapter adapter = new ImagePagerAdapter(this, mURL);
        image_pager.setAdapter(adapter);

        txAddressLine.setText(report.getAddress());
        txBrand.setText(report.getBrand());
        txCategory.setText(report.getCategory());
        txDescription.setText(report.getDescription());
        txStaffName.setText(report.getAssigned());
        txStatus.setText(report.getStatus());

        final FirebaseDatabase mDataRef = FirebaseDatabase.getInstance();
        final DatabaseReference mDBUpdate = mDataRef.getReference("reports")
                .child(report.getReportID());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(StaffActionActivity.this)
                        .title("Update Report")
                        .content("Do you really want to update this report to Resolved?")
                        .inputRangeRes(0, 50, R.color.ms_errorColor)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .input("Remarks", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, final CharSequence input) {

                                Map mergeUpdate = new HashMap();

                                mergeUpdate.put("status", Constants.ACTION_RESOLVED);
                                mergeUpdate.put("remark", input.toString());

                                mDBUpdate.updateChildren(mergeUpdate, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Utils.mailStatusUpdate(report, input.toString());
                                            Toast.makeText(StaffActionActivity.this, "Report status updated", Toast.LENGTH_LONG)
                                                    .show();
                                            finish();
                                        }
                                    }
                                });

                            }
                        })
                        .positiveText("Yes")
                        .negativeText("Cancel")
                        .cancelable(false)
                        .show();

            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + report.getLat()
                        + "," + report.getLon());

                Intent intent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
