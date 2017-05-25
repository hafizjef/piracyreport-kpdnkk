package utem.workshop.piracyreport.utils;

import android.support.compat.BuildConfig;
import android.util.Base64;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;


public class MailSender {

    private static final String TAG = MailSender.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String ENDPOINT = "https://api.mailgun.net/v3/domain.com/";
    public static final String ACCEPT_JSON_HEADER = "Accept: application/json";
    public static final String BASIC = "Basic";

    private SendMailApi sendMailApi;

    public interface SendMailApi {

        @Headers({ACCEPT_JSON_HEADER})
        @FormUrlEncoded
        @POST("/messages")
        void authUser(
                @Header("Authorization") String authorizationHeader,
                @Field("from") String from,
                @Field("to") String to,
                @Field("subject") String subject,
                @Field("html") String text,
                Callback<ResponseBody> cb
        );
    }

    public void sendMail(String to, String subject, String msg, Callback<ResponseBody> cb){
        String from = "NAME <EMAIL@DOMAIN>";
        String clientIdAndSecret = "api" + ":" + "api-key";
        String authorizationHeader = BASIC + " " + Base64.encodeToString(clientIdAndSecret.getBytes(), Base64.NO_WRAP);
        sendMailApi.authUser(authorizationHeader,from, to, subject, msg, cb);
    }

    public MailSender() {
        RestAdapter restAdapter = getAuthAdapter();
        sendMailApi = restAdapter.create(SendMailApi.class);
    }

    private RestAdapter getAuthAdapter(){
        RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.NONE;
        if(DEBUG)logLevel = RestAdapter.LogLevel.FULL;
        return new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(new Gson()))
                .setLogLevel(logLevel)
                .build();
    }

}