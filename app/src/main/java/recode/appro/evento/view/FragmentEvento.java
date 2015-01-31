package recode.appro.evento.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import recode.appro.main.controle.ControladorUsuario;
import recode.appro.evento.Evento;
import recode.appro.conexao.JSONParser;
import recode.appro.main.FragmentListener;
import recode.appro.telas.R;

import org.json.JSONException;
/**
 * Created by eccard on 7/25/14.
 */
public class FragmentEvento extends android.support.v4.app.Fragment implements View.OnClickListener {
    final String TAG="FragmentEvento";
    Evento evento;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    // url to create new product
//    private static String url_cadastrar_usuario_em_evento = "http://10.0.0.103/aproWSt/usuario-em-evento.php";
    private static String url_cadastrar_usuario_em_evento;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    FragmentListener fragmentListener;

    //public FragmentEvento(Evento evento) {
     //   this.evento = evento;
    //}

    public FragmentEvento(){}

    public static FragmentEvento newInstance(Evento evento){

        FragmentEvento fragmentEvento = new FragmentEvento();
        Bundle args = new Bundle();
        args.putSerializable("evento",evento);
        fragmentEvento.setArguments(args);
        return fragmentEvento;

    }
    private Evento getEvento(){
        return (Evento) getArguments().getSerializable("evento");
    }

    @Override // ligação dessa classe com a fragmentEventoConfirmados
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        url_cadastrar_usuario_em_evento = this.getResources().getString(R.string.url_usuario_em_evento);
        try{
            fragmentListener = (FragmentListener) activity;

        }catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " deve implementar FragmentListener");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Evento");
        getActivity().getActionBar().setSubtitle(null);

        View view = inflater.inflate(R.layout.fragment_evento,container,false);

        evento = getEvento();

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
        Button confirmadosemevento= (Button) view.findViewById(R.id.button_confirmados_em_evento);

        confirmarpresenca.setOnClickListener(this);
        confirmadosemevento.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_confirmar_presenca:

                new CadastrarUsuarioEmEvento(getActivity().getApplicationContext()).execute();
                break;

            case R.id.button_confirmados_em_evento:
                Log.i("botão evento confirmados","apertou o botao");

                fragmentListener.callbackEventoConfirmados(evento);
                /*
                Bundle args = new Bundle();
                args.putString("id_evento",String.valueOf(evento.getCodigo()));
                Fragment fragment = new FragmentEventoConfrimados();
                fragment.setArguments(args);

                FragmentTransaction frgManager = getFragmentManager().beginTransaction();
                frgManager.replace(R.id.content_frame,fragment);
                frgManager.addToBackStack(null);
                frgManager.commit();
                */


                break;
            default:
                break;

        }


    }

/**
 * Background Async Task to Create new product
 * */
class CadastrarUsuarioEmEvento extends AsyncTask<String, String, String> {
    int success=0;
    Context ctx;
    String nick;

    public CadastrarUsuarioEmEvento(Context ctx){
        this.ctx=ctx;
    }

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
//        ControladorUsuario controladorUsuario = new ControladorUsuario(getActivity().getApplicationContext());
//        nick = controladorUsuario.GetNomeUsuario();
         SharedPreferences prefs = ctx.getSharedPreferences("gcm.eccard.prefs",
                 Context.MODE_PRIVATE);
        nick = prefs.getString("nick","");
        Log.i("nick do usuario ",nick);
        if(!nick.isEmpty()) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nick", nick));
//        params.add(new BasicNameValuePair("id_evento", String.valueOf(getEvento().getCodigo()))); //alteração , teste de novo padrão
            params.add(new BasicNameValuePair("id_evento", String.valueOf(evento.getCodigo()))); //alteração , teste de novo padrão

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
            Log.i(TAG,"presença confirmada em evento");
            Toast toast =  Toast.makeText(getActivity(),"Presença Confirmada", Toast.LENGTH_LONG);
            toast.show();
        }
        else
            if(success==0){
                Log.i(TAG,"presença não confirmada em evento- talvez faltou o nick. pode ter sido limpado na cache, tratar isso");
                Toast.makeText(getActivity(),"erro ao confirmar presença", Toast.LENGTH_LONG).show();
            }
    }
}
}