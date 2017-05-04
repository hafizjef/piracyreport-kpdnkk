package utem.workshop.piracyreport;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewFormFragment extends Fragment implements BlockingStep {

    @BindView(R.id.vName)
    TextView vName;

    @BindView(R.id.vEmail)
    TextView vEmail;

    @BindView(R.id.vAddress)
    TextView vAddress;

    private DataManager dataManager;
    private DatabaseReference mDatabase;

    public static ViewFormFragment newInstance() {
        return new ViewFormFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataManager) {
            dataManager = (DataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Report rs = new Report(dataManager.getName(), dataManager.getEmail());
        rs.setAddress(dataManager.getAddr());
        rs.setBrand(dataManager.getBrand());
        rs.setCategory(dataManager.getCategory());
        rs.setDescription(dataManager.getDesc());
        rs.setLat(dataManager.getLat());
        rs.setLon(dataManager.getLon());
        rs.setState(dataManager.getState());

        mDatabase = FirebaseDatabase.getInstance().getReference("reports");
        String reportId = mDatabase.push().getKey();
        mDatabase.child(reportId).setValue(rs, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getContext(), "Error, " + databaseError.getMessage(), Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(getContext(), "Report successfully submitted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        Toast.makeText(getContext(), "Submitting Report...", Toast.LENGTH_LONG).show();
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        vName.setText(dataManager.getName());
        vEmail.setText(dataManager.getEmail());
        vAddress.setText(dataManager.getAddr());
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
