package utem.workshop.piracyreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FormFragment extends Fragment {
    public static final String TAB_POSITION = "tab_position";

    public FormFragment() {

    }

    public static FormFragment newInstance(int tabPosition) {
        FormFragment fragment = new FormFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle args = getArguments();
        //int tabPosition = args.getInt(TAB_POSITION);

        return inflater.inflate(R.layout.report_form, container, false);
    }
}
