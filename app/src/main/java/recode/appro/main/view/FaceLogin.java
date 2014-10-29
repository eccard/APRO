package recode.appro.main.view;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.main.controle.ControladorUsuario;
import recode.appro.telas.R;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;


public class FaceLogin extends Activity implements AdapterView.OnItemSelectedListener{
    EditText mNick;
    Button mLoginButton;
    RadioGroup radioGroupTipoUsuario;
    RadioGroup radioGroupCurso;
    Spinner periodo;
    TextView textViewcurso;
    TextView textViewperiodo;
    private static final String TAG_SUCCESS = "success";

    int periodoAluno;
    ArrayList<String> s;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

//    final NumberPicker np;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face_login);
        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile"));

        mNick = (EditText) findViewById(R.id.nick);
        textViewcurso = (TextView) findViewById(R.id.textViewCursoLoginActivity);
        textViewperiodo = (TextView) findViewById(R.id.textViewPeriodoLoginActivity);

        radioGroupTipoUsuario = (RadioGroup) findViewById(R.id.radiongroup_tipo_usuario);
        radioGroupCurso = (RadioGroup) findViewById(R.id.radiobutton_loggin_curso);
        periodo = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cursos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodo.setAdapter(adapter);
        periodo.setOnItemSelectedListener(this);

        mLoginButton = (Button) findViewById(R.id.login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String nick = mNick.getText().toString().trim().toLowerCase();

                if (nick.length() > 0) {
                    LoginTask t = new LoginTask(FaceLogin.this);

                    int selectdTipoUsuario = radioGroupTipoUsuario.getCheckedRadioButtonId();
                    int selectdCursoUsuario = radioGroupCurso.getCheckedRadioButtonId();
                    RadioButton tipoUsuario = (RadioButton) findViewById(selectdTipoUsuario);
                    RadioButton cursoUsuario = (RadioButton) findViewById(selectdCursoUsuario);

//                    tipoUsuario.getText();
//                    cursoUsuario.getText();
//                    periodoAluno

//                    Log.i("mensagem", nick);
//                    Log.i("mensagem", cursoUsuario.getText().toString());
//                    Log.i("mensagem", String.valueOf(periodoAluno));

                    ControladorUsuario controladorUsuario = new ControladorUsuario(getApplicationContext());
                    if(tipoUsuario.getText().toString().equalsIgnoreCase("Aluno")){
                        Log.i("tipoUsuario", tipoUsuario.getText().toString());
                        //esse primeiro parametro 1 é pra informar a thread que é do tipo aluno, e a inserção é direfente
                        t.execute("1",nick,cursoUsuario.getText().toString(),String.valueOf(periodoAluno));
//                    controladorUsuario.criarUsuarioAluno(nick,cursoUsuario.getText().toString(),periodoAluno);
                    }
                    else{
                        t.execute("0",nick);
//                        controladorUsuario.criarUsuarioPT(nick);
                    }

//					 t.execute(nick);

                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
//            userInfoTextView.setVisibility(View.VISIBLE);

            // Request user data and show the results
            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Display the parsed user info
//                        userInfoTextView.setText(buildUserInfoDisplay(user));
//                        mNick.setText(buildUserInfoDisplay(user));
                    }
                }
            });
        } else if (state.isClosed()) {
//            userInfoTextView.setVisibility(View.INVISIBLE);
        }
    }
    private interface MyGraphUser extends GraphUser {
        // Create a setter to enable easy extraction of the languages field
        GraphObjectList<MyGraphLanguage> getLanguages();
    }
    private interface MyGraphLanguage extends GraphObject {
        // Getter for the ID field
        String getId();

        // Getter for the Name field
        String getName();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        periodoAluno= Integer.valueOf(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.check_button_Professor_tecnico:
                if (checked)
                    textViewcurso.setVisibility(View.INVISIBLE);
                radioGroupCurso.setVisibility(View.INVISIBLE);
                textViewperiodo.setVisibility(View.INVISIBLE);
                periodo.setVisibility(View.INVISIBLE);
                // Pirates are the best
                break;
            case R.id.check_button_Aluno:
                if(checked)
                    textViewcurso.setVisibility(View.VISIBLE);
                radioGroupCurso.setVisibility(View.VISIBLE);
                textViewperiodo.setVisibility(View.VISIBLE);
                periodo.setVisibility(View.VISIBLE);

                break;
        }
    }

    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        userInfo.append(String.format("Id: %s\n\n",
                user.getId()));

        userInfo.append(String.format("Name: %s\n\n",
                user.getFirstName()));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        userInfo.append(String.format("Birthday: %s\n\n",
                user.getBirthday()));

        // Example: partially typed access, to location field,
        // name key (location)
        // - requires user_location permission
//        userInfo.append(String.format("Location: %s\n\n",
//                user.getLocation().getProperty("name")));

        // Example: access via property name (locale)
        // - no special permissions required
//        userInfo.append(String.format("Locale: %s\n\n",
//                user.getProperty("locale")));


        // Example: access via key for array (languages)
        // - requires user_likes permission

        // Option 3: Get the language data from the typed interface and after
        // sub-classing GraphUser object to get at the languages.
//        GraphObjectList<MyGraphLanguage> languages = (user.cast(MyGraphUser.class)).getLanguages();
//        if (languages.size() > 0) {
//            ArrayList<String> languageNames = new ArrayList<String>();
//            for (MyGraphLanguage language : languages) {
//                // Add the language name to a list. Use the name
//                // getter method to get access to the name field.
//                languageNames.add(language.getName());
//            }
//            userInfo.append(String.format("Languages: %s\n\n",
//                    languageNames.toString()));
//        }

        // Option2: Get the data from creating a typed interface
        // for the language data
//        JSONArray languages = (JSONArray)user.getProperty("languages");
//        if (languages.length() > 0) {
//            ArrayList<String> languageNames = new ArrayList<String> ();
//
//            // Get the data from creating a typed interface
//            // for the language data.
//            GraphObjectList<MyGraphLanguage> graphObjectLanguages =
//            	GraphObject.Factory.createList(languages,
//            			MyGraphLanguage.class);
//
//            // Iterate through the list of languages
//            for (MyGraphLanguage language : graphObjectLanguages) {
//            	// Add the language name to a list. Use the name
//                // getter method to get access to the name field.
//            	languageNames.add(language.getName());
//            }
//
//            userInfo.append(String.format("Languages: %s\n\n",
//            languageNames.toString()));
//        }

        // Option 1: Get the data from parsing JSON
//        JSONArray languages = (JSONArray)user.getProperty("languages");
//        if (languages.length() > 0) {
//            ArrayList<String> languageNames = new ArrayList<String> ();
//
//            for (int i=0; i < languages.length(); i++) {
//                JSONObject language = languages.optJSONObject(i);
//                languageNames.add(language.optString("name"));
//            }
//
//            userInfo.append(String.format("Languages: %s\n\n",
//            languageNames.toString()));
//        }
Log.i("paa",userInfo.toString());
//        new BaixarFotoPerfil().execute(user);

        return userInfo.toString();
    }


    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        Context mContext;
        ProgressDialog mDialog;
        JSONParser jsonParser = new JSONParser();
        JSONObject json;
        private String nick;
        private int tipoAluno;
        private String cursoUsuario;
        private String periodoAluno;
        private int sucess;
        String url_criar_usuario = "http://10.0.0.104/aproWSt/criar-usuario.php";

        LoginTask(Context c) {
            mContext = c;
            mLoginButton.setEnabled(false);

            mDialog = ProgressDialog.show(c, "", getString(R.string.authenticating), true, false);
            mDialog.setCancelable(true);
        }

        @Override
        public Boolean doInBackground(String... params) {
            tipoAluno = Integer.valueOf(params[0]);
            nick = params[1];

            if (tipoAluno == 1) {
                cursoUsuario = params[2];
                periodoAluno = params[3];

                // Building Parameters
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("nick", nick));
                params2.add(new BasicNameValuePair("estudante", String.valueOf(tipoAluno)));
                params2.add(new BasicNameValuePair("curso", cursoUsuario));
                params2.add(new BasicNameValuePair("periodo", periodoAluno));

                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(url_criar_usuario,
                        "POST", params2);

                // check log cat fro response
                Log.d("Create Response", json.toString());
                try {
                    sucess = json.getInt(TAG_SUCCESS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (tipoAluno == 0) {
                // Building Parameters
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("nick", nick));
                params2.add(new BasicNameValuePair("estudante", String.valueOf(tipoAluno)));

                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(url_criar_usuario,
                        "POST", params2);

                // check log cat fro response
                Log.d("Create Response", json.toString());
                try {
                    sucess = json.getInt(TAG_SUCCESS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


//			String matricula = params[1];

            // Do something internetty
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Bundle result = null;
            Account account = new Account(nick, mContext.getString(R.string.ACCOUNT_TYPE));
            AccountManager am = AccountManager.get(mContext);
            if (am.addAccountExplicitly(account, null, null)) {
                result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
//                ContentResolver.setIsSyncable(account,"evetos",1);
//                setAccountAuthenticatorResult(result);
                return true;
            } else {
                return false;
            }

            // jogar no webservice o novo usuario

        }

        @Override
        public void onPostExecute(Boolean result) {
            mLoginButton.setEnabled(true);
            mDialog.dismiss();
            if (sucess == 1) {
                Toast.makeText(getApplicationContext(), "Usuario Inserido com sucesso", Toast.LENGTH_LONG).show();
                ControladorUsuario controladorUsuario = new ControladorUsuario(getApplicationContext());
                if (tipoAluno == 1) {
                    controladorUsuario.criarUsuarioAluno(nick, cursoUsuario, Integer.valueOf(periodoAluno));
                } else if (tipoAluno == 0) {
                    Toast.makeText(getApplicationContext(), "Usuario não inserido com sucesso", Toast.LENGTH_LONG).show();
                    controladorUsuario.criarUsuarioPT(nick);
                }
                if (result)
                    finish();
            }
        }
    }
}
