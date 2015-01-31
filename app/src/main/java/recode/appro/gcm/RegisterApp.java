package recode.appro.gcm;

/**
 * Created by eccard on 10/7/14.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.io.IOException;

import recode.appro.main.TaskCallback;
import recode.appro.telas.R;


public class RegisterApp extends AsyncTask<String, Void, String> {

    private static final String TAG = "GCMRelated";
    Context ctx;
    GoogleCloudMessaging gcm;
    String SENDER_ID = "870007196455";
    String regid = null;
    private int appVersion;
    String url_criar_usuario;



    // parametros vindo do FaceLogin
    String repassa_params[];

    TaskCallback mcallback;

    public RegisterApp(TaskCallback callback,Context ctx, GoogleCloudMessaging gcm, int appVersion){
        this.ctx = ctx;
        this.gcm = gcm;
        this.appVersion = appVersion;
        this.url_criar_usuario = ctx.getResources().getString(R.string.url_criar_usuario);
        this.mcallback=callback;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        String msg = "";
        repassa_params = params;
//        tipoAluno = Integer.valueOf(params[0]);
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            //verificar se tem internet para registrar
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
//            sendRegistrationIdToBackend();

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(ctx, regid);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }



        return msg;
    }

    private void storeRegistrationId(Context ctx, String regid) {
        final SharedPreferences prefs = ctx.getSharedPreferences("gcm.eccard.prefs",
                Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registration_id", regid);
        editor.putInt("appVersion", appVersion);
        editor.commit();

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(TAG,"terminou de pegar o regid do gcm, agora vamos mandar essas informações para nosso server...");
        new SendToServer(mcallback,ctx,regid).execute(repassa_params);
    }
}