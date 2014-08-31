package recode.appro.telas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import recode.appro.controlador.ControladorEvento;
import recode.appro.controlador.ControladorUsuario;
import recode.appro.model.Evento;
import recode.appro.conexao.JSONParser;
import org.json.JSONException;
/**
 * Created by eccard on 7/25/14.
 */
public class FragmentEvento extends android.support.v4.app.Fragment implements View.OnClickListener {
    Evento evento;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    // url to create new product
    private static String url_cadastrar_usuario_em_evento = "http://10.0.0.113/aproWSt/usuario-em-evento.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public FragmentEvento(Evento evento) {
        this.evento = evento;
    }

    public FragmentEvento(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Evento");
        getActivity().getActionBar().setSubtitle(null);

        View view = inflater.inflate(R.layout.fragment_evento,container,false);

        TextView nome = (TextView) view.findViewById(R.id.textView_evento_nome);
        TextView datahora = (TextView) view.findViewById(R.id.textView_evento_data_hora);
        TextView descricao = (TextView) view.findViewById(R.id.textView_evento_descricao);
        TextView local = (TextView) view.findViewById(R.id.textView_evento_local);

        TextView organizadores = (TextView) view.findViewById(R.id.textView_evento_organizadores);
        TextView confirmados = (TextView) view.findViewById(R.id.textView_evento_confimados);

        nome.setText(evento.getNome());
        datahora.setText("Dia: " + evento.getData() + " às " + evento.getHora());
        descricao.setText(evento.getDescricao());
        local.setText("Local: "+evento.getLocal());
        organizadores.setText("Organizadores: " + evento.getOrganizadores());
//        organizadores.setGravity(Gravity.CENTER_HORIZONTAL);
        confirmados.setText("Confirmados: 0");
        Button confirmarpresenca= (Button) view.findViewById(R.id.button_confirmar_presenca);

        confirmarpresenca.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_confirmar_presenca:

                new CadastrarUsuarioEmEvento().execute();
                break;

            default:
                break;

        }


    }

/**
 * Background Async Task to Create new product
 * */
class CadastrarUsuarioEmEvento extends AsyncTask<String, String, String> {
    int success;
    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cadastrando usuario em evento..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {
        ControladorUsuario controladorUsuario = new ControladorUsuario(getActivity().getApplicationContext());

        String nick = controladorUsuario.GetNomeUsuario();


        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nick", nick));
        params.add(new BasicNameValuePair("id_evento", String.valueOf(evento.getCodigo())));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_cadastrar_usuario_em_evento,
                "POST", params);

        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
//                Log.i("fagmere","fagmerviviiadooo");

//                pDialog.setMessage("cadastrado com sucesso");
                //Toast toast =  Toast.makeText(getActivity(),"Presença Confirmada", Toast.LENGTH_LONG);
                //toast.show();

                // successfully created product
//                Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
//                startActivity(i);

                // closing this screen
//                finish();
            } else {
                // failed to create product
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
        // dismiss the dialog once done
        pDialog.dismiss();
        if (success == 1) {
            Log.i("Sucesso","onPost- Sucesso");
            Toast toast =  Toast.makeText(getActivity(),"Presença Confirmada", Toast.LENGTH_LONG);
            toast.show();



        }


    }

}
}