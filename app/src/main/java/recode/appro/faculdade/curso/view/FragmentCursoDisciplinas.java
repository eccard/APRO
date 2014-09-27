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
import android.widget.ListView;

public class FragmentCursoDisciplinas extends Fragment {

	private ListView listView;
	private AdapterItemDisciplina adapterItemDisciplina;
	private Context context;
	private int codigo;
	private ControladorCurso controladorCurso;

	public FragmentCursoDisciplinas() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_curso_disciplinas,
				container, false);

		getActivity().getActionBar().setSubtitle(null);

		this.context = getActivity().getApplicationContext();
		this.codigo = NavigationDrawer.codigoCurso;
		this.controladorCurso = new ControladorCurso(context, codigo);

		listView = (ListView) view.findViewById(R.id.listView_disciplinas);
		adapterItemDisciplina = new AdapterItemDisciplina(
				controladorCurso.getDisciplinas(), context);
		listView.setAdapter(adapterItemDisciplina);

		return view;
	}

}
