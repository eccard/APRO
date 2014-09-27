package recode.appro.faculdade.departamento.view;

import java.util.ArrayList;
import java.util.List;

import recode.appro.faculdade.departamento.controle.ControladorProfessor;
import recode.appro.faculdade.departamento.modelo.Professor;
import recode.appro.telas.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentProfessores extends Fragment {

	List<Professor> professores = new ArrayList<Professor>();
	ListView listViewProfessores;
	AdapterItemProfessor adapterItemProfessor;
	Context context;
	ControladorProfessor controladorProfessor;

	public FragmentProfessores() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View view = inflater.inflate(R.layout.fragment_list_view_generica,
				container, false);
		
		this.context = getActivity().getApplicationContext();
		this.controladorProfessor = new ControladorProfessor(context);
		this.professores = controladorProfessor.getProfessores();
		
		getActivity().getActionBar().setSubtitle(null);
		
		listViewProfessores = (ListView) view.findViewById(R.id.listView_generica);
		adapterItemProfessor = new AdapterItemProfessor(professores, context);
		listViewProfessores.setAdapter(adapterItemProfessor);

		return view;
	}

}
