package recode.appro.main.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;

import recode.appro.telas.R;

/**
 * Created by eccard on 10/27/14.
 */
public class FaceFragment extends Fragment implements  AdapterView.OnItemSelectedListener {

    //private static final String TAG = "MainFragment";
    EditText mNick;
    Button mLoginButton;
    RadioGroup radioGroupTipoUsuario;
    RadioGroup radioGroupCurso;
    Spinner periodo;
    TextView textViewcurso;
    TextView textViewperiodo;
    int periodoAluno;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private TextView userInfoTextView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_face_login, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
//        authButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes","public_profile"));
        authButton.setReadPermissions(Arrays.asList("public_profile"));
//        userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);


        mNick = (EditText) view.findViewById(R.id.nick);
        textViewcurso = (TextView) view.findViewById(R.id.textViewCursoLoginActivity);
        textViewperiodo = (TextView) view.findViewById(R.id.textViewPeriodoLoginActivity);

        radioGroupTipoUsuario = (RadioGroup) view.findViewById(R.id.radiongroup_tipo_usuario);
        radioGroupCurso = (RadioGroup) view.findViewById(R.id.radiobutton_loggin_curso);
        periodo = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.cursos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodo.setAdapter(adapter);
        periodo.setOnItemSelectedListener(this);
        mLoginButton = (Button) view.findViewById(R.id.login);
        radioGroupTipoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("entrouuuu","-----------");

            }
        });



        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
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
                    }
                }
            });
        } else if (state.isClosed()) {
//            userInfoTextView.setVisibility(View.INVISIBLE);
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

//        new BaixarFotoPerfil().execute(user);

        return userInfo.toString();
    }



    private Boolean saveToInternalSorage(Bitmap image) {
        try {
//            String filename = getActivity().getApplication().getFilesDir() + "/profile.jpg" ;
//            FileOutputStream fos = getActivity().getBaseContext().openFileOutput(filename,Context.MODE_PRIVATE);
            FileOutputStream fos = getActivity().getBaseContext().openFileOutput("profile.jpg", Context.MODE_APPEND);
// Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            return true;
        } catch (FileNotFoundException e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public  Bitmap getFacebookProfilePicture(String userID)
            throws SocketException, SocketTimeoutException,
            MalformedURLException, IOException, Exception {
        String imageURL;
        Bitmap bitmap = null;
        imageURL = "http://graph.facebook.com/" + userID
                + "/picture?type=normal";
//                + "/picture?type=large";

        Log.i("faceFragment", "começar a baixar");
        Log.i("facefrag",imageURL);
        URL url1 = new URL(imageURL);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setDoInput(true);
        connection.connect();
        connection.getContent();
        InputStream input = connection.getInputStream();
        int totalSize = connection.getContentLength();
        Log.i("pa","tamaho total do arquivo  - "+String.valueOf(totalSize));
        FileOutputStream fileOutputStream = getActivity().getBaseContext().openFileOutput("profile3.jpg", Context.MODE_PRIVATE);

        byte[] buffer = new byte[1024];
        long total = 0;
        int count;

        while ((count = input.read(buffer)) != -1) {
            // allow canceling with back button
            total += count;
            // publishing the progress....
            fileOutputStream.write(buffer, 0, count);
        }
        fileOutputStream.flush();
        fileOutputStream.close();

//        Bitmap myBitmap = BitmapFactory.decodeStream(input);
//        FileOutputStream fileOutputStream = getActivity().getBaseContext().openFileOutput("profile.jpg", Context.MODE_PRIVATE);
//        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
//        byte[] byteArray = outstream.toByteArray();
//        fileOutputStream.write(byteArray);
//        fileOutputStream.close();


//        URL secondURL1 = new URL(ucon1.getHeaderField("Location"));
//        InputStream in = (InputStream) new URL(imageURL).getContent();

//        byte[] buffer =  new byte[1];
//        int len = in.read(buffer);
//        Log.i("apa", "tamanho = "+String.valueOf(len));
//        while (len != -1) {
//            fos.write(buffer, 0, len);
//            len = in.read(buffer);
//            if (Thread.interrupted()) {
//                throw new InterruptedException();
//            }
//        }
//        fos.close();
//        in.close();
////        bitmap = BitmapFactory.decodeStream(in);

        Log.i("faceFragment", "terminou de baixar e decodificar");
        return bitmap;
    }
    public Bitmap getImageBitmap(String name){
        Context context=getActivity().getBaseContext();
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        periodoAluno= Integer.valueOf(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClick(View v) {
        Log.i("paaaa","fedapuuudta");
    }


    // Private interface for GraphUser that includes
    // the languages field: Used in Option 3
    private interface MyGraphUser extends GraphUser {
        // Create a setter to enable easy extraction of the languages field
        GraphObjectList<MyGraphLanguage> getLanguages();
    }

    // Private interface for a language Graph Object
    // for a User: Used in Options 2 and 3
    private interface MyGraphLanguage extends GraphObject {
        // Getter for the ID field
        String getId();

        // Getter for the Name field
        String getName();
    }
    class BaixarFotoPerfil extends AsyncTask<GraphUser,Void,Void>{

        @Override
        protected Void doInBackground(GraphUser... params) {
            GraphUser user=params[0];
            try {
                Log.i("faceFragment", "preparando para baixar e salvar");
                getFacebookProfilePicture(user.getId());
                Log.i("faceFragment", "terminou de baixar e salvar ");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}