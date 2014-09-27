package recode.appro.faculdade.curso.view;

import recode.appro.faculdade.curso.controle.ControladorCurso;
import recode.appro.main.view.NavigationDrawer;
import recode.appro.telas.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentCursoInfo extends Fragment {
	
	private ControladorCurso controladorCurso;
	private Context context;
	private int codigo;
	
	public FragmentCursoInfo() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_curso_informacoes, container,
				false);
				
		this.context = getActivity().getApplicationContext();
		this.codigo = NavigationDrawer.codigoCurso;
		//		this.codigo = getArguments().getInt("codigo");
		this.controladorCurso = new ControladorCurso(context, codigo);
		
	
		getActivity().getActionBar().setSubtitle(null);
		
		TextView descricao = (TextView) view.findViewById(R.id.textView_curso_descricao);
		TextView titulacao = (TextView) view.findViewById(R.id.textView_curso_titulacao);
		TextView duracao = (TextView) view.findViewById(R.id.textView_curso_duracao);
		TextView mercado = (TextView) view.findViewById(R.id.textView_curso_mercado);
		
		descricao.setText(controladorCurso.getInformacoes().get(0));
		titulacao.setText(controladorCurso.getInformacoes().get(1));
		duracao.setText(controladorCurso.getInformacoes().get(2));
		mercado.setText(controladorCurso.getInformacoes().get(3));
		
		return view;
	}
	
}

