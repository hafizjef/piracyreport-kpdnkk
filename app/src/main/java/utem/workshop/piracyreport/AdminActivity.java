package utem.workshop.piracyreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.fragments.ReportListAdminFragment;
import utem.workshop.piracyreport.fragmentsAdapter.AdminFragmentAdapter;

public class AdminActivity extends AppCompatActivity {

    private AdminFragmentAdapter adapter;

    @BindView(R.id.admin_viewpager)
    ViewPager adminViewpager;

    @BindView(R.id.admin_tablayout)
    TabLayout adminTabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        adapter = new AdminFragmentAdapter(getSupportFragmentManager(), this);

        adminViewpager.setAdapter(adapter);

        adminViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((ReportListAdminFragment)adapter.getItem(position)).refresh();
                    case 1:
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        adminTabLayout.setupWithViewPager(adminViewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.admin_logout) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
