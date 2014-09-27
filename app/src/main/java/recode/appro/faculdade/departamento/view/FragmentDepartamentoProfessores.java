package recode.appro.faculdade.departamento.view;

import recode.appro.faculdade.departamento.controle.ControladorProfessor;
import recode.appro.telas.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentDepartamentoProfessores extends Fragment {

	private ListView listView;
	private Context context;
	private ControladorProfessor controladorProfessor;

	public FragmentDepartamentoProfessores() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_departamento_professores, container, false);

		this.context = getActivity().getApplicationContext();
		this.controladorProfessor = new ControladorProfessor(context);

		getActivity().getActionBar().setSubtitle(null);

		listView = (ListView) view
				.findViewById(R.id.listView_departamento_professores);

		AdapterItemProfessorDepartamento adapterItemProfessorDepartamento = 
				new AdapterItemProfessorDepartamento(controladorProfessor.getProfessoresDepartamento(), context);
		listView.setAdapter(adapterItemProfessorDepartamento);

		return view;
	}

}
