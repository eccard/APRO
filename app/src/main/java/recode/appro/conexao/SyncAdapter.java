package recode.appro.conexao;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import recode.appro.controlador.ControladorNoticia;
import recode.appro.model.Noticia;
import recode.appro.telas.NavigationDrawer;
import recode.appro.telas.R;

/**
 * Created by eccard on 7/28/14.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private NotificationManager mNotificationManager;
    JSONParser jParser = new JSONParser();
    // products JSONArray
    JSONArray jnoticias = null;
    // eventos novos vindo do servidos
    ArrayList<Noticia> listaNovasNoticias;
    private int numMessages = 0;

    // JSON Node names
    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_NOTICIAS = "noticias";
    private static final String TAG_CODIGO = "codigo";
    private static final String TAG_ASSUNTO = "assunto";
    private static final String TAG_DESCRICAO = "descricao";
    private static final String TAG_DATA = "data";
    private static final String TAG_HORA = "hora";
    private static final String TAG_CURSORELACIONADO = "cursoRelacionado";
    private static String url_all_noticias = "http://10.0.0.102/aproWS/noticias/listarultimasnoticias.php";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        try{
            syncNoticias(provider);


        }catch (Exception e){

        }
    }

    private void syncNoticias(ContentProviderClient contentProviderClient){

        ControladorNoticia controladorNoticia = new ControladorNoticia(getContext());
        int codigoultimoNoticia = controladorNoticia.getCodigoUltimaNoticia();
//            Log.i("pegar o ultimo evento",String.valueOf(codigoultimoNoticia));
//        controladorNoticia = null;

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_noticias + "?codigo=" + String.valueOf(codigoultimoNoticia), "GET",params);

        // Check your log cat for JSON reponse
        Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESSO);

            if (success == 1) {
                // products found
                // Getting Array of Products
//                    Log.i("json de eventos array",json.toString());
                jnoticias = json.getJSONArray(TAG_NOTICIAS);
                listaNovasNoticias = new ArrayList<Noticia>();
                // looping through All Products
                for (int i = 0; i < jnoticias.length(); i++) {
                    JSONObject c = jnoticias.getJSONObject(i);

                    // Storing each json item in variable
                    int codigo = c.getInt(TAG_CODIGO);
                    String assunto = c.getString(TAG_ASSUNTO);
                    String descricao = c.getString(TAG_DESCRICAO);
                    String data = c.getString(TAG_DATA);
                    String hora = c.getString(TAG_HORA);
                    int curoRelacionado = c.getInt(TAG_CURSORELACIONADO);

                    Noticia noticia = new Noticia(codigo, assunto, data, hora, curoRelacionado, descricao);
                    //jogar pro banco de dados
                    // exibir notificação

                    controladorNoticia.criarNoticia(noticia);

//                    generateNotification(noticia);

                    listaNovasNoticias.add(noticia);
                }
                if(listaNovasNoticias!=null) {
//                    eu acho q não precisa disso pos vai atualizar e gravar no banco
//                    quando entrar na tela noticia atualiza..
//                    listViewAdapter.concatenarArrayDeNoticias(listaNovasNoticias);
                }
                listaNovasNoticias.clear();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    public void generateNotification(Noticia noticia) {

        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =
                new NotificationCompat.Builder(getContext());

        mBuilder.setContentTitle("Nova noticia");
        mBuilder.setContentText(noticia.getAssunto());
        mBuilder.setTicker("Noticia !!!");
        mBuilder.setSmallIcon(R.drawable.logo);

      /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(getContext(), NavigationDrawer.class);

        resultIntent.setAction("NOTICIA"); //tentando linkar
        Bundle bundle = new Bundle();
        bundle.putSerializable("noticia",noticia);
        resultIntent.putExtras(bundle);
        // fim arrumar a inteçao

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addParentStack(NavigationDrawer.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                (NotificationManager) getActivity().getApplication().
//                        getSystemService(getActivity().getApplication().NOTIFICATION_SERVICE);
                (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
      /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(noticia.getCodigo(), mBuilder.build());
    }
}
