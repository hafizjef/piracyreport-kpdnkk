package utem.workshop.piracyreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("VisibleForTests")
public class ViewFormFragment extends Fragment implements BlockingStep {

    ArrayList<Image> images;
    private StorageReference mStorageRef;

    @BindView(R.id.vName)
    TextView vName;

    @BindView(R.id.vEmail)
    TextView vEmail;

    @BindView(R.id.vAddress)
    TextView vAddress;

    @BindView(R.id.btnAddImg)
    Button btnAddImg;

    @BindView(R.id.imgView1)
    ImageView imgV1;

    @BindView(R.id.imgView2)
    ImageView imgV2;

    @BindView(R.id.imgView3)
    ImageView imgV3;

    @BindView(R.id.imgView4)
    ImageView imgV4;

    private DataManager dataManager;

    public static ViewFormFragment newInstance() {
        return new ViewFormFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mStorageRef = FirebaseStorage.getInstance().getReference();

        View view = inflater.inflate(R.layout.report_view, container, false);
        ButterKnife.bind(this, view);

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(ViewFormFragment.this)
                        .folderMode(true)
                        .limit(4)
                        .start(99);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 99 && resultCode == RESULT_OK && data != null) {
            images = null;
            images = (ArrayList<Image>) ImagePicker.getImages(data);

            Timber.i("Image size: " + String.valueOf(images.size()));

            ImageView[] viewsArray = new ImageView[4];
            viewsArray[0] = imgV1;
            viewsArray[1] = imgV2;
            viewsArray[2] = imgV3;
            viewsArray[3] = imgV4;

            for (ImageView im : viewsArray) {
                im.setVisibility(View.INVISIBLE);
            }

            for (int index = 0; index < images.size(); index++) {
                Timber.i(images.get(index).getPath());
                // OOM Fixes
                //viewsArray[index].setImageBitmap(BitmapFactory.decodeFile(images.get(index).getPath()));
                Glide.with(getContext())
                        .load(images.get(index).getPath())
                        .into(viewsArray[index]);

                viewsArray[index].setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Submitting report...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        final ArrayList<String> imgURL = new ArrayList<>();
        final Report rs = new Report(dataManager.getName(), dataManager.getEmail());

        rs.setAddress(dataManager.getAddr());
        rs.setBrand(dataManager.getBrand());
        rs.setCategory(dataManager.getCategory());
        rs.setDescription(dataManager.getDesc());
        rs.setLat(dataManager.getLat());
        rs.setLon(dataManager.getLon());
        rs.setState(dataManager.getState());

        Toast.makeText(getContext(), "Submitting Report...", Toast.LENGTH_LONG).show();

        for (int index = 0; index < images.size(); index++) {
            Uri file = Uri.fromFile(new File(images.get(index).getPath()));
            UploadTask uploadTask = mStorageRef.child("images/" + file.getLastPathSegment()).putFile(file);

            final int finalIndex = index;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Timber.i(taskSnapshot.getDownloadUrl().toString());
                    imgURL.add(taskSnapshot.getDownloadUrl().toString());

                    if (finalIndex == images.size() - 1) {
                        rs.setImgURL(imgURL);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("reports");
                        String reportId = mDatabase.push().getKey();

                        mDatabase.child(reportId).setValue(rs, new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getContext(), "Error, " + databaseError
                                            .getMessage(), Toast.LENGTH_LONG);
                                } else {
                                    try {
                                        Toast.makeText(getContext(),
                                                "Report successfully submitted!",
                                                Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {

                                    }
                                }
                                progress.dismiss();
                            }
                        });
                    }
                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progDone = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progress.setMessage("Uploading images... " + (finalIndex + 1) + "/" +
                            images.size() + " " + progDone + "%");
                }
            });
        }
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
        Utils.hideSoftKeyboard(getActivity());
        vName.setText(dataManager.getName());
        vEmail.setText(dataManager.getEmail());
        vAddress.setText(dataManager.getAddr());
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
