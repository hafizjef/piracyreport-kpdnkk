package utem.workshop.piracyreport.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.R;
import utem.workshop.piracyreport.models.Report;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private List<Report> mReports;
    private Context mContext;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.report_addr)
        TextView reportAddr;

        @BindView(R.id.report_state)
        TextView reportState;

        @BindView(R.id.report_mail)
        TextView reportMail;

        @BindView(R.id.report_time)
        TextView reportTime;

        @BindView(R.id.report_assign)
        TextView reportAssigned;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    public ReportsAdapter(Context context, List<Report> reports) {
        mReports = reports;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    // Inflating layout
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate custom layout
        View reportView = inflater.inflate(R.layout.item_report, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(reportView);
        return viewHolder;
    }

    // Populate data into item through holder
    @Override
    public void onBindViewHolder(ReportsAdapter.ViewHolder holder, int position) {
        Report report = mReports.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getDefault());

        TextView tv_address = holder.reportAddr;
        TextView tv_state = holder.reportState;
        TextView tv_assign = holder.reportAssigned;
        TextView tv_mail = holder.reportMail;
        TextView tv_time = holder.reportTime;

        tv_address.setText(report.getAddress());
        tv_state.setText(report.getState());
        tv_assign.setText(report.getAssigned());
        tv_mail.setText(report.getEmail());
        tv_time.setText(sdf.format(new Date(report.getTimeStamp())));
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }
}
