package utem.workshop.piracyreport;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import utem.workshop.piracyreport.fragmentsAdapter.ImagePagerAdapter;
import utem.workshop.piracyreport.models.Report;
import utem.workshop.piracyreport.utils.Constants;

public class AssignStaffActivity extends AppCompatActivity {

    Report report;

    @BindView(R.id.image_pager)
    ViewPager image_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_staff);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            report = Parcels.unwrap(getIntent().getParcelableExtra(Constants.REPORT_OBJ));
        } else {
            finish();
        }

        ArrayList<String> mURL = report.getImgURL();

        ImagePagerAdapter adapter = new ImagePagerAdapter(this, mURL);
        image_pager.setAdapter(adapter);

    }
}
