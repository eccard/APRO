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

import java.util.ArrayList;

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
					t.execute(nick);

                    int selectdTipoUsuario = radioGroupTipoUsuario.getCheckedRadioButtonId();
                    int selectdCursoUsuario = radioGroupCurso.getCheckedRadioButtonId();
                    RadioButton tipoUsuario = (RadioButton) findViewById(selectdTipoUsuario);
                    RadioButton cursoUsuario = (RadioButton) findViewById(selectdCursoUsuario);

                    tipoUsuario.getText();
                    cursoUsuario.getText();
//                    periodoAluno

                    Log.i("mensagem",nick);
                    Log.i("mensagem",cursoUsuario.getText().toString());
                    Log.i("mensagem",String.valueOf(periodoAluno));

                    ControladorUsuario controladorUsuario = new ControladorUsuario(getApplicationContext());
                    if(tipoUsuario.toString().equalsIgnoreCase("Aluno")){
                    controladorUsuario.criarUsuarioAluno(nick,cursoUsuario.getText().toString(),periodoAluno);
                    }
                    else{
                        controladorUsuario.criarUsuarioPT(nick);
                    }

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

		LoginTask(Context c) {
			mContext = c;
			mLoginButton.setEnabled(false);

			mDialog = ProgressDialog.show(c, "", getString(R.string.authenticating), true, false);
			mDialog.setCancelable(true);
		}

		@Override
		public Boolean doInBackground(String... params) {
			String nick = params[0];
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
                ContentResolver.setIsSyncable(account,"evetos",1);
				setAccountAuthenticatorResult(result);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onPostExecute(Boolean result) {
			mLoginButton.setEnabled(true);
			mDialog.dismiss();

			if (result)
				finish();
		}
	}
}
