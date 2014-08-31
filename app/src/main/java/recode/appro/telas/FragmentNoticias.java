package recode.appro.telas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.controlador.ControladorNoticia;
import recode.appro.model.Noticia;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentNoticias extends Fragment implements AdapterView.OnItemClickListener {

//	List<Noticia> noticias = new ArrayList<Noticia>();
	ListView listViewNoticias;
	AdapterItemNoticias listViewAdapter;
    ControladorNoticia controladorNoticia;
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> eventosList;
    private static String url_all_noticias = "http://10.0.0.104/aproWS/noticias/listarultimasnoticias.php";

    // JSON Node names
    private static final String TAG_SUCCESSO = "sucesso";
    private static final String TAG_NOTICIAS = "noticias";
    private static final String TAG_CODIGO = "codigo";
    private static final String TAG_ASSUNTO = "assunto";
    private static final String TAG_DESCRICAO = "descricao";
    private static final String TAG_DATA = "data";
    private static final String TAG_HORA = "hora";
    private static final String TAG_CURSORELACIONADO = "cursoRelacionado";

    // products JSONArray
    JSONArray jnoticias = null;
    // eventos novos vindo do servidos
    ArrayList<Noticia> listaNovasNoticias;

	public FragmentNoticias() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setTitle("Notícias");
		getActivity().getActionBar().setSubtitle(null);
		
		View view = inflater.inflate(R.layout.fragment_list_view_generica,
				container, false);

        // Hashmap for ListView
//        noticiasList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
//        new LoadAllProducts().execute();


        listViewAdapter = new AdapterItemNoticias(getActivity()
				.getApplicationContext());
		listViewNoticias = (ListView) view.findViewById(R.id.listView_generica);
		listViewNoticias.setAdapter(listViewAdapter);

        listViewNoticias.setOnItemClickListener(this);
		return view;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        controladorNoticia = new ControladorNoticia(getActivity().getApplicationContext());

        listViewAdapter.getNoticias().get(position).setVisualizar(1);
        controladorNoticia.setVisualizarNoticia1(listViewAdapter.getNoticias().get(position).getCodigo());

        Noticia noticia = listViewAdapter.getNoticias().get(position);

        listViewAdapter.getNoticias().get(position).setVisualizar(1);


        Fragment fragmentnoticia = new FragmentNoticia(noticia);
        FragmentTransaction frgManager = getFragmentManager().beginTransaction();
        frgManager.replace(R.id.content_frame,fragmentnoticia);
        frgManager.addToBackStack(null);
        frgManager.commit();

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
            pDialog.setMessage("Loading noticias. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * getting All noticias from url
         * */
        protected String doInBackground(String... args) {


            ControladorNoticia controladorNoticia = new ControladorNoticia(getActivity().getApplicationContext());
            int codigoultimoNoticia = controladorNoticia.getCodigoUltimaNoticia();
//            Log.i("pegar o ultimo evento",String.valueOf(codigoultimoNoticia));
            controladorNoticia = null;

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

                        Noticia noticia = new Noticia(codigo,assunto,data,hora,curoRelacionado,descricao);
                        //jogar pro banco de dados
                        // exibir notificação

                        controladorNoticia = new ControladorNoticia(getActivity().getApplicationContext());
                        controladorNoticia.criarNoticia(noticia);

                        generateNotification(noticia);


                        listaNovasNoticias.add(noticia);

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
            if(listaNovasNoticias!=null) {
                listViewAdapter.concatenarArrayDeNoticias(listaNovasNoticias);
            }
//            listaNovasNoticias.clear();
            listViewNoticias.setAdapter(listViewAdapter);
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

        public void generateNotification(Noticia noticia) {

            Log.i("Start", "notification");

      /* Invoking the default notification service */
            NotificationCompat.Builder  mBuilder =
                    new NotificationCompat.Builder(getActivity().getApplicationContext());

            mBuilder.setContentTitle("Nova noticia");
            mBuilder.setContentText(noticia.getAssunto());
            mBuilder.setTicker("Noticia !!!");
            mBuilder.setSmallIcon(R.drawable.logo);

      /* Increase notification number every time a new notification arrives */
            mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
            Intent resultIntent = new Intent(getActivity().getApplicationContext(), NavigationDrawer.class);
            resultIntent.setAction("NOTICIA"); //tentando linkar
            Bundle bundle = new Bundle();
            bundle.putSerializable("noticia",noticia);
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
            mNotificationManager.notify(noticia.getCodigo(), mBuilder.build());
        }
    }


}
