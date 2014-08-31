package recode.appro.telas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentUniversidadeInfo extends Fragment implements View.OnClickListener{
	
	public FragmentUniversidadeInfo() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_universidade_informacoes, container,
				false);
        Button mapa1 = (Button) view.findViewById(R.id.button1);
        Button mapa2 = (Button) view.findViewById(R.id.button_evento_atualizarConfirmados);

        mapa1.setOnClickListener(this);
        mapa2.setOnClickListener(this);
		return view;
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button1:
                Intent mapa1 = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mapasalandar1)));
                startActivity(mapa1);
                Log.i("teste", "botão 1");
                break;

            case R.id.button_evento_atualizarConfirmados:
                Intent mapa2 =  new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mapasalandar2)));
                startActivity(mapa2);
                Log.i("teste", "botão 2");
                break;

            default:
                break;

        }

    }
}

