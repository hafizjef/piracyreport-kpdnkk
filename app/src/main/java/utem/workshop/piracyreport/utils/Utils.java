package utem.workshop.piracyreport.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import utem.workshop.piracyreport.models.Report;

public final class Utils {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isLoggedIn() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDate(long unixEpoch) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getDefault());

        return(sdf.format(new Date(unixEpoch)));
    }

    public static void mailReport(Report report) {

        Map<String, String> valueMap = new HashMap<>();

        valueMap.put("name", report.getName());
        valueMap.put("brand", report.getBrand());
        valueMap.put("location", report.getAddress());
        valueMap.put("date", Utils.getDate(report.getTimeStamp()));

        String templateString = "<h1>Hello, ${name}</h1>" +
                "<p style='font-size:24px'>Your report for <b>${brand}</b> have been successfully " +
                "submitted into our system and will be processed " +
                "shortly, thank you!</p>" +
                "<br><br>" +
                "<div style='font-size:20px'><div>Location: ${location}</div>" +
                "<div>Date: ${date}</div></div>" +
                "<div style='padding-top:30px'><i>Regards, Administrator</i></div>";

        StrSubstitutor sub = new StrSubstitutor(valueMap);
        String msgLine = sub.replace(templateString);

        String subjectLine = "Report Recorded";


        MailSender mailSender = new MailSender();
        mailSender.sendMail(report.getEmail(), subjectLine, msgLine, new Callback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Response response) {}
            @Override
            public void failure(RetrofitError error) {}
        });
    }

    public static void mailStatusUpdate(Report report, String remark) {

        Map<String, String> valueMap = new HashMap<>();

        valueMap.put("name", report.getName());
        valueMap.put("brand", report.getBrand());
        valueMap.put("assigned", report.getAssigned());
        valueMap.put("date", Utils.getDate(report.getTimeStamp()));
        valueMap.put("remark", remark);
        valueMap.put("status", Constants.ACTION_RESOLVED);

        String templateString = "<h1>Hello, ${name}</h1>" +
                "<p style='font-size:24px'>Your report for <b>${brand}</b> have been resolved " +
                "accordingly by our staff</p>" +
                "<br><br>" +
                "<div style='font-size:20px'><div>Report Handler: ${assigned}</div>" +
                "<div>Status: ${status}</div>" +
                "<div>Remarks: ${remark}</div>" +
                "<div>Date: ${date}</div></div>" +
                "<div style='padding-top:30px'><i>Regards, Administrator</i></div>";

        StrSubstitutor sub = new StrSubstitutor(valueMap);
        String msgLine = sub.replace(templateString);

        String subjectLine = "Report Status Update";


        MailSender mailSender = new MailSender();
        mailSender.sendMail(report.getEmail(), subjectLine, msgLine, new Callback<ResponseBody>() {
            @Override
            public void success(ResponseBody responseBody, Response response) {}
            @Override
            public void failure(RetrofitError error) {}
        });
    }
}
