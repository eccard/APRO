/*******************************************************************************
 * Copyright 2010 Sam Steele 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.c99.SyncProviderDemo;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import java.util.List;

import recode.appro.conexao.JSONParser;
import recode.appro.controlador.ControladorUsuario;
import recode.appro.telas.R;

public class LoginActivity extends AccountAuthenticatorActivity implements AdapterView.OnItemSelectedListener {
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

//    final NumberPicker np;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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
        mLoginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String nick = mNick.getText().toString().trim().toLowerCase();

				if (nick.length() > 0) {
					LoginTask t = new LoginTask(LoginActivity.this);

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
                        Log.i("tipoUsuario",tipoUsuario.getText().toString());
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
        String url_criar_usuario = "http://10.0.0.103/aproWSt/criar-usuario.php";

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
                setAccountAuthenticatorResult(result);
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
