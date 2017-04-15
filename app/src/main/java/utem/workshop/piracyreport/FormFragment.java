package utem.workshop.piracyreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by max on 15/4/17.
 */

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

        View view = inflater.inflate(R.layout.content_main, container, false);

        return view;
    }
}
