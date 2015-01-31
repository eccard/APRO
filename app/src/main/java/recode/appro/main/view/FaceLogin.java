package recode.appro.main.view;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


import java.util.ArrayList;
import java.util.Arrays;

import recode.appro.gcm.RegisterApp;
import recode.appro.gcm.SendToServer;
import recode.appro.main.FragmentListener;
import recode.appro.main.TaskCallback;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class FaceLogin extends Activity implements AdapterView.OnItemSelectedListener,TaskCallback{
    EditText mNick;
    Button mLoginButton;
    RadioGroup radioGroupTipoUsuario;
    RadioGroup radioGroupCurso;
    Spinner periodo;
    TextView textViewcurso;
    TextView textViewperiodo;

    int periodoAluno;
    ArrayList<String> s;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    //gcm
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "-FaceLogin-";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    GoogleCloudMessaging gcm;
    String regid;

    TaskCallback mcallback;
//    final NumberPicker np;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcallback=this;
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

                if (!verificarConexao(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "impossivel resitrar sem internet", Toast.LENGTH_SHORT).show();
                }else
                if(nick.length() < 1){
                    Toast.makeText(getApplicationContext(), "insira um nick", Toast.LENGTH_SHORT).show();
                }else
                if( radioGroupTipoUsuario.getCheckedRadioButtonId()==R.id.check_button_Aluno && radioGroupCurso.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getApplicationContext(), "selecione um curso", Toast.LENGTH_SHORT).show();
                }
                else{
//                if(radioGroupCurso.getCheckedRadioButtonId()==R.id.check_button_Professor_tecnico ){
//                    LoginTask t = new LoginTask(FaceLogin.this);

                    int selectdTipoUsuario = radioGroupTipoUsuario.getCheckedRadioButtonId();
                    int selectdCursoUsuario = radioGroupCurso.getCheckedRadioButtonId();
                    RadioButton tipoUsuario = (RadioButton) findViewById(selectdTipoUsuario);
                    RadioButton cursoUsuario = (RadioButton) findViewById(selectdCursoUsuario);

                    periodoAluno = Integer.valueOf(periodo.getSelectedItem().toString());

//                    Log.i(TAG,tipoUsuario.getText().toString() + " " + cursoUsuario.getText().toString() + " "+ periodoAluno);
//                    ControladorUsuario controladorUsuario = new ControladorUsuario(getApplicationContext());

                    // iniciar o thread para pegar o reg id do gcm, e colocar no  método
                    // on post execute o essas informaçoes para iniciar a thread de jogar para o nosso
                    // banco de dados.

                    if (checkPlayServices()) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                        regid = getRegistrationId(getApplicationContext());

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("gcm.eccard.prefs", Context.MODE_PRIVATE);
                        boolean registrado = sharedPreferences.getBoolean("registrado",false);

                        if (regid.isEmpty() && !registrado) {

                            if(tipoUsuario.getText().toString().equalsIgnoreCase("Aluno")){
                                Log.i("tipoUsuario", tipoUsuario.getText().toString());
                                //esse primeiro parametro 1 é pra informar a thread que é do tipo aluno, e a inserção é direfente
//                                t.execute("1",nick,cursoUsuario.getText().toString(),String.valueOf(periodoAluno));
                                new RegisterApp(mcallback,getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute("1",nick,cursoUsuario.getText().toString(),String.valueOf(periodoAluno));
//                    controladorUsuario.criarUsuarioAluno(nick,cursoUsuario.getText().toString(),periodoAluno);
                            }
                            else{
//                                t.execute("0",nick);
                                new RegisterApp(mcallback,getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute("0",nick);
//                        controladorUsuario.criarUsuarioPT(nick);
                            }


                        }
                        // caso que ja tem um regid mas por algum motivo não conseguiu envialo ao servidor
                        else if(!registrado){
                            Log.e(TAG,"ja tem um regid do gcm, mas não enviou ao servidor");
                            Toast.makeText(getApplicationContext(), "Ja tem um regid do gcm, mas não enviou ao servidor", Toast.LENGTH_SHORT).show();

                            if(tipoUsuario.getText().toString().equalsIgnoreCase("Aluno")){
                                Log.i("tipoUsuario", tipoUsuario.getText().toString());
                                //esse primeiro parametro 1 é pra informar a thread que é do tipo aluno, e a inserção é direfente
                                new SendToServer(mcallback,getApplicationContext(),regid).execute("1",nick,cursoUsuario.getText().toString(),String.valueOf(periodoAluno));
                            }
                            else{
                                new SendToServer(mcallback,getApplicationContext(),regid).execute("0",nick);
                            }
                        }
                    }
                    else{
                        Log.i(TAG, "No valid Google Play Services APK found.");
                    }
                }
            }

        });
    }

    private String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences("gcm.eccard.prefs",context.MODE_PRIVATE);
    }
    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
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
    private Boolean verificarConexao(Context ctx){
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                this.finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void done() {
    finish();
    }


}
