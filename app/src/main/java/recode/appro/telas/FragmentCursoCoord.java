package recode.appro.telas;

import java.util.ArrayList;
import java.util.List;

import recode.appro.model.Noticia;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentCursoCoord extends Fragment {

	List<Noticia> noticias = new ArrayList<Noticia>();
	ListView listViewNoticias;
	AdapterItemNoticias listViewAdapter;
	
	
	public FragmentCursoCoord() {
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setSubtitle(null);
		
		View view = inflater.inflate(R.layout.fragment_curso_coordenacao, container,
				false);
				
		TextView endereco = (TextView) view.findViewById(R.id.textView_curso_coordenacao_endereco);
		TextView email = (TextView) view.findViewById(R.id.textView_curso_coordenacao_email);
		TextView telefone = (TextView) view.findViewById(R.id.textView_curso_coordenacao_telefone);
		TextView fax = (TextView) view.findViewById(R.id.textView_curso_coordenacao_fax);
		TextView coordenador = (TextView) view.findViewById(R.id.textView_curso_coordenacao_coordenador);
		TextView coordenadorEmail = (TextView) view.findViewById(R.id.textView_curso_coordenacao_coordenador_email);
		
		
		return view;
	}
	
}

