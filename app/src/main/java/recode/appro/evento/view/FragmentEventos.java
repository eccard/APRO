package recode.appro.evento.view;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.evento.controle.ControladorEvento;
import recode.appro.evento.Evento;
import recode.appro.main.FragmentListener;
import recode.appro.main.view.ActivityPesquisa;
import recode.appro.main.view.NavigationDrawer;
import recode.appro.telas.R;

import android.app.ProgressDialog;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by eccard on 09/07/14.
 */
public class FragmentEventos extends Fragment implements AdapterView.OnItemClickListener {
    //    ArrayList<Evento> eventos = new ArrayList<Evento>();
//    List<Evento> eventos = new ArrayList<Evento>();
    ListView listViewEventos;
    AdapterItemEventos listViewAdapter;

    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> eventosList;
//    private static String url_all_eventoss = "http://10.0.0.104/aproWS/eventos/listarultimoseventos.php";
    private static String url_all_eventoss;
    // JSON Node names
    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_EVENTOS = "eventos";
    private static final String TAG_CODIGO = "codigo";
    private static final String TAG_NOME = "nome";
    private static final String TAG_ORGANIZADORES = "organizadores";
    private static final String TAG_DESCRICAO = "descricao";
    private static final String TAG_LOCAL = "local";
    private static final String TAG_DATA = "data";
    private static final String TAG_HORA = "hora";

    // products JSONArray
    JSONArray jeventos = null;
    // eventos novos vindo do servidos
    ArrayList<Evento> listaNovosEventos;

    private FragmentListener fragmentListener;
    public FragmentEventos() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Eventos");
        getActivity().getActionBar().setSubtitle(null);
        View view = inflater.inflate(R.layout.fragment_list_view_generica,
                container, false);

        // Hashmap for ListView
//        eventosList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
//        new LoadAllProducts().execute();


        listViewAdapter = new AdapterItemEventos( getActivity()
                .getApplicationContext());
        listViewEventos = (ListView) view.findViewById(R.id.listView_generica);
        listViewEventos.setAdapter(listViewAdapter);
        listViewEventos.setOnItemClickListener(this);


    return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("akiii",String.valueOf(position));
        Evento evento = listViewAdapter.getEventos().get(position);
        /*
        Fragment fragmentEvento = new FragmentEvento(evento);
        FragmentTransaction frgManager = getFragmentManager().beginTransaction();
        frgManager.replace(R.id.content_frame,fragmentEvento);
        frgManager.addToBackStack(null);
        frgManager.commit();
        */
        fragmentListener.callbackEvento(evento);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        url_all_eventoss = this.getResources().getString(R.string.url_ultimos_eventos);
        try{
            fragmentListener = (FragmentListener) activity;

        }catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " deve implementar FragmentListener");

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.pesquisa, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_pesquisa:
                Intent intent = new Intent(getActivity(), ActivityPesquisa.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return false;
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
        private NotificationManager mNotificationManager;
        private int numMessages = 0;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading eventos. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();



        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {


            ControladorEvento controladorEvento = new ControladorEvento(getActivity().getApplicationContext());
            int codigoultimoevento = controladorEvento.getCodigoUltimoEvento();
            Log.i("pegar o ultimo evento",String.valueOf(codigoultimoevento));
            controladorEvento = null;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_eventoss + "?codigo=" + String.valueOf(codigoultimoevento), "GET",params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESSO);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
//                    Log.i("json de eventos array",json.toString());
                    jeventos = json.getJSONArray(TAG_EVENTOS);
                    listaNovosEventos = new ArrayList<Evento>();
                    // looping through All Products
                    for (int i = 0; i < jeventos.length(); i++) {
                        JSONObject c = jeventos.getJSONObject(i);

                        // Storing each json item in variable
                        int codigo = c.getInt(TAG_CODIGO);
                        String nome = c.getString(TAG_NOME);
                        String organizadores = c.getString(TAG_ORGANIZADORES);
                        String descricao = c.getString(TAG_DESCRICAO);
                        String local = c.getString(TAG_LOCAL);
                        String data = c.getString(TAG_DATA);
                        String hora = c.getString(TAG_HORA);

                        Evento evento = new Evento(codigo,nome,descricao,organizadores,local,data,hora);
                        //jogar pro banco de dados
                        // exibir notificação
                        controladorEvento = new ControladorEvento(getActivity().getApplicationContext());
                        controladorEvento.criarEvento(evento);
                        generateNotification(evento);

                        listaNovosEventos.add(evento);
                        // creating new HashMap
//                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
//                        map.put(TAG_CODIGO, codigo);
//                        map.put(TAG_NOME, nome);

                        // adding HashList to ArrayList
//                        eventosList.add(map);

//                        Log.i("passou aki",eventosList.get(i).toString());
                    }
                } else {
                        Log.i("passou aki zeroo ","teste");

                    /*
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

         protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
             if(listaNovosEventos!=null) {
                 listViewAdapter.concatenarArrayDeEventos(listaNovosEventos);
             }
             listViewEventos.setAdapter(listViewAdapter);
             //for(int i=0;i<eventosList.size();i++){
             //    Log.i("todos",eventosList.get(i).toString());
             //}

//            Log.i("array",eventosList.get(1).toString());
             /*
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * /
                    ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[] { TAG_PID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
             */


         }
        public void generateNotification(Evento evento) {

            Log.i("Start", "notification");

      /* Invoking the default notification service */
            NotificationCompat.Builder  mBuilder =
                    new NotificationCompat.Builder(getActivity().getApplicationContext());

            mBuilder.setContentTitle("Novo evento");
            mBuilder.setContentText(evento.getNome());
            mBuilder.setTicker("Evento !!!");
            mBuilder.setSmallIcon(R.drawable.logo);

      /* Increase notification number every time a new notification arrives */
            mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
            Intent resultIntent = new Intent(getActivity().getApplicationContext(), NavigationDrawer.class);
            resultIntent.setAction("EVENTO"); //tentando linkar
            Bundle bundle = new Bundle();
            bundle.putSerializable("evento",evento);
            resultIntent.putExtras(bundle);
            // fim arrumar a inteçao

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity().getApplicationContext());
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
                    (NotificationManager) getActivity().getApplication().
                            getSystemService(getActivity().getApplication().NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
            mNotificationManager.notify(evento.getCodigo(), mBuilder.build());
        }

    }

}

