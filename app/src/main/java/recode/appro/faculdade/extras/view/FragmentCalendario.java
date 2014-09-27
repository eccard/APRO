package recode.appro.faculdade.extras.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import recode.appro.telas.R;

public class FragmentCalendario extends Fragment implements  View.OnClickListener{

	public FragmentCalendario() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendario, container,
				false);

		Button calendarioEscolar = (Button) view.findViewById(R.id.button1);
		Button calendarioAdministrativo = (Button) view.findViewById(R.id.button_evento_atualizarConfirmados);

		calendarioEscolar.setOnClickListener(this);
		calendarioAdministrativo.setOnClickListener(this);
		return view;
	}


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button1:
                Intent calendarioescolar =  new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.calendario_url_escolar)));
                startActivity(calendarioescolar);
                Log.i("teste", "botão 1");
                break;

            case R.id.button_evento_atualizarConfirmados:
                Intent calendarioadm =  new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.calendario_url_adminsitrativo)));
                startActivity(calendarioadm);
                Log.i("teste", "botão 2");
                break;

            default:
                break;

        }

    }
}
