package recode.appro.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.main.TaskCallback;
import recode.appro.telas.R;

/**
 * Created by eccard on 1/30/15.
 */
public class SendToServer extends AsyncTask<String,Void,Void> {
    String TAG="SendToServer";
    String nick;
    String regid;
    private int tipoAluno;
    private String cursoUsuario;
    private String periodoAluno;
    Context ctx;
    String url_criar_usuario;
    HttpPost httpPost;
    HttpGet httpGet;
    HttpResponse httpResp;
    int code;
    JSONParser jsonParser = new JSONParser();
    JSONObject json;
    private int sucess;
    private static final String TAG_SUCCESS = "success";

    private TaskCallback mCallback;

    public SendToServer(TaskCallback callback,Context ctx,String regid){
        this.ctx=ctx;
        this.url_criar_usuario = ctx.getResources().getString(R.string.url_criar_usuario);
        this.regid=regid;
        this.mCallback=callback;
    }

    @Override
    protected Void doInBackground(String... params) {
        tipoAluno = Integer.valueOf(params[0]);

        httpPost = new HttpPost(url_criar_usuario);
        List<NameValuePair> parametros = new ArrayList<NameValuePair>();

        if (tipoAluno == 1) {
            nick = params[1];
            cursoUsuario = params[2];
            periodoAluno = params[3];

            parametros.add(new BasicNameValuePair("nick", nick));
            parametros.add(new BasicNameValuePair("estudante", String.valueOf(tipoAluno)));
            parametros.add(new BasicNameValuePair("curso", cursoUsuario));
            parametros.add(new BasicNameValuePair("periodo", periodoAluno));
            parametros.add(new BasicNameValuePair("regId", regid));


        }
        else if (tipoAluno == 0) {
            nick = params[1];
            parametros.add(new BasicNameValuePair("nick", nick));
            parametros.add(new BasicNameValuePair("estudante", String.valueOf(tipoAluno)));
            parametros.add(new BasicNameValuePair("regId", regid));

        }
// funcionando outra alternativa
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(parametros, HTTP.UTF_8));
//
//            HttpClient httpClient = new DefaultHttpClient();
//            Log.i(TAG, "executa post");
//            httpResp = httpClient.execute(httpPost);
//            code = httpResp.getStatusLine().getStatusCode();
//            Log.i(TAG,"código do http post " + String.valueOf(code));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        json = jsonParser.makeHttpRequest(url_criar_usuario,
                "POST", parametros);
        // check log cat fro response
        Log.d("Create Response", json.toString());
        try {
            sucess = json.getInt(TAG_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(sucess==1) {
            Toast.makeText(ctx, "Usuario Inserido com sucesso", Toast.LENGTH_LONG).show();
            Log.i(TAG, " terminou de mandar para o servidor");
            SharedPreferences prefs = ctx.getSharedPreferences("gcm.eccard.prefs",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("registrado", true);
            editor.putString("nick", nick);
            editor.commit();

        }
        if(sucess==0){
            Toast.makeText(ctx, "Erro no inserção do usuario", Toast.LENGTH_LONG).show();
        }
        mCallback.done();
    }
}
