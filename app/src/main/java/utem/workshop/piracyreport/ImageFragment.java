package utem.workshop.piracyreport;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class ImageFragment extends Fragment implements BlockingStep {

    @BindView(R.id.imageView)
    ImageView iv1;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    private int totalImage = 0;
    private DataManager dataManager;

    public static ImageFragment newInstance() {
        return new ImageFragment();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle args = getArguments();
        //int tabPosition = args.getInt(TAB_POSITION);
        View view = inflater.inflate(R.layout.image_form, container, false);
        ButterKnife.bind(this, view);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(getActivity()).start(1);

                LinearLayout layout = linearLayout;
                Timber.i("CLICKED");
                final ImageView imv = new ImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 20, 0, 0);
                imv.setLayoutParams(params);
                imv.setImageResource(R.drawable.ic_launcher);
                imv.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imv_height);
                imv.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imv_width);
                imv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewGroup) imv.getParent()).removeView(imv);
                        totalImage -= 1;
                        iv1.setVisibility(View.VISIBLE);
                    }
                });
                layout.addView(imv, 0);
                totalImage += 1;
                if (totalImage == 4) {
                    iv1.setVisibility(View.GONE);
                }

            }
        });


        return view;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof DataManager) {
            dataManager = (DataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
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
