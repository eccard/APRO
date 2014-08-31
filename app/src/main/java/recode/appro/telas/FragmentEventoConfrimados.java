package recode.appro.telas;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.controlador.ControladorUsuario;
import recode.appro.model.Usuario;
import recode.appro.telas.R;

public class FragmentEventoConfrimados extends Fragment {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> usuarioList;

    // url to get all products list
    private static String  url_usuarios_confirmados="http://10.0.0.103/aproWSt/listar-confirmados-em-evento.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USUARIOS = "usuarios";
    private static final String TAG_NICK = "nick";
    private static final String TAG_ID = "_id";
    private static final String TAG_ESTUDANTE = "estudante";
    private static final String TAG_CURSO = "curso";
    private static final String TAG_PERIODO = "periodo";

    // products JSONArray
    JSONArray usuarios = null;
    ListView listViewUsuarios;
    AdapterItemUsuarios listViewAdapter;
    int id_evento;
    public FragmentEventoConfrimados() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        id_evento = Integer.valueOf(bundle.getString("id_evento"));

        getActivity().getActionBar().setTitle("Usuarios");
        getActivity().getActionBar().setSubtitle(null);

        View view = inflater.inflate(R.layout.fragment_list_view_generica,
                container, false);

        new LoadAllUsuarioEmEvento().execute();

        listViewAdapter = new AdapterItemUsuarios(getActivity()
                .getApplicationContext());
        listViewUsuarios = (ListView) view.findViewById(R.id.listView_generica);
//        listViewUsuarios.setAdapter(listViewAdapter);

        return view;
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllUsuarioEmEvento extends AsyncTask<String, String, String> {
        List<Usuario> usuariosArray =new ArrayList<Usuario>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

//            ControladorUsuario controladorUsuario = new ControladorUsuario(getActivity().getApplicationContext());
            // getting JSON string from URL

            try {
                JSONObject json = jParser.makeHttpRequest(url_usuarios_confirmados + "?id_evento="+id_evento, "GET", params);

                // Check your log cat for JSON reponse
                Log.d("All Users: ", json.toString());


                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    usuarios = json.getJSONArray(TAG_USUARIOS);

                    // looping through All Products
                    for (int i = 0; i < usuarios.length(); i++) {
                        JSONObject c = usuarios.getJSONObject(i);

                        // Storing each json item in variable
//                        int _id = c.getInt(TAG_ID);
                        String nick = c.getString(TAG_NICK);
                        String estudante = c.getString(TAG_ESTUDANTE);
                        String curso = c.getString(TAG_CURSO);
                        int periodo = c.getInt(TAG_PERIODO);

                        Usuario usuario = new Usuario(nick,estudante,curso,periodo);
                        usuariosArray.add(usuario);

                    }
                } else {

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
            listViewAdapter.setUsuarios(usuariosArray);
            listViewUsuarios.setAdapter(listViewAdapter);

            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed JSON data into ListView
//                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            AllProductsActivity.this, productsList,
//                            R.layout.list_item, new String[] { TAG_PID,
//                            TAG_NAME},
//                            new int[] { R.id.pid, R.id.name });
//                    // updating listview
//                    setListAdapter(adapter);
//                }
//            });

        }

    }
}
