package utem.workshop.piracyreport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.schibstedspain.leku.LocationPickerActivity;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;


public class FormFragment extends Fragment implements BlockingStep {

    AlertDialog selectionDialog;

    private String[] items = {"Clothing","Printed Materials","Software"};

    @BindView(R.id.nameField)
    EditText mNameField;

    @BindView(R.id.emailField)
    EditText mEmailField;

    @BindView(R.id.typeField)
    EditText mTypeField;

    @BindView(R.id.locationField)
    EditText mLocationField;

    @BindView(R.id.brandField)
    EditText mBrandField;

    @BindView(R.id.descField)
    EditText mDescField;

    private DataManager dataManager;

    public static FormFragment newInstance() {
        return new FormFragment();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_form, container, false);
        ButterKnife.bind(this, view);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please select type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTypeField.setText(items[which]);
            }
        });

        builder.setCancelable(false);
        selectionDialog = builder.create();

        mTypeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionDialog.show();
            }
        });

        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNameField.getText().toString().trim().length() < 3) {
                    mNameField.setError("Name cannot be less than 3 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    mEmailField.setError("Invalid Email Address");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLocationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LocationPickerActivity.class);
                i.putExtra(LocationPickerActivity.LATITUDE, 2.313970);
                i.putExtra(LocationPickerActivity.LONGITUDE, 102.321237);
                startActivityForResult(i, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Timber.i("Result *********** OK");
            if (requestCode == 1) {
                mLocationField.setText(data.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS));
                Address pb = data.getParcelableExtra(LocationPickerActivity.ADDRESS);
                Timber.i(pb.getAddressLine(pb.getMaxAddressLineIndex() - 1));
                Timber.i(pb.getAdminArea());
                dataManager.saveLat(pb.getLatitude());
                dataManager.saveLon(pb.getLongitude());
                dataManager.saveState(pb.getAdminArea());
            }
        }
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
    public VerificationError verifyStep() {
        if (TextUtils.isEmpty(mNameField.getText().toString()) || mNameField.getError() != null) {
            mNameField.requestFocus();
            return new VerificationError("Name is invalid");
        }
        if (TextUtils.isEmpty(mEmailField.getText().toString()) || mEmailField.getError() != null) {
            mEmailField.requestFocus();
            return new VerificationError("Email address is invalid");
        }
        if (TextUtils.isEmpty(mTypeField.getText().toString())) {
            selectionDialog.show();
            return new VerificationError("Type cannot be empty, please select type");
        }
        if (TextUtils.isEmpty(mLocationField.getText().toString())) {
            return new VerificationError("Location cannot be empty, please set your location");
        }
        if (TextUtils.isEmpty(mBrandField.getText().toString())) {
            mBrandField.requestFocus();
            return new VerificationError("Brand name cannot be empty");
        }
        if (TextUtils.isEmpty(mDescField.getText().toString())) {
            mDescField.requestFocus();
            return new VerificationError("Product description cannot be empty");
        }
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {

        SpannableStringBuilder snackbarText = new SpannableStringBuilder();
        int boldStart = snackbarText.length();
        snackbarText.append("ERROR");
        snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart,
                snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int contentStart = snackbarText.length();
        Timber.i(String.valueOf(contentStart));
        snackbarText.append(" " + error.getErrorMessage().toUpperCase());
        snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.ms_black)),
                boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        snackbarText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), contentStart,
                snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        Snackbar sb = Snackbar.make(getActivity().findViewById(R.id.snackBar), snackbarText, Snackbar.LENGTH_LONG);

        View sbView = sb.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWarnYellow));
        sb.show();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        dataManager.saveName(mNameField.getText().toString());
        dataManager.saveEmail(mEmailField.getText().toString());
        dataManager.saveCategory(mTypeField.getText().toString());
        dataManager.saveAddr(mLocationField.getText().toString());
        dataManager.saveBrand(mBrandField.getText().toString());
        dataManager.saveDesc(mDescField.getText().toString());

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
