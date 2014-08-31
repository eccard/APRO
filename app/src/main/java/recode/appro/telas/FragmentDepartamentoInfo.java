package recode.appro.telas;

import recode.appro.controlador.ControladorDepartamento;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDepartamentoInfo extends Fragment {

	private ControladorDepartamento controladorDepartamento;
	private Context context;
	private int codigoDepartamento;

	public FragmentDepartamentoInfo() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_departamento_info,
				container, false);

		this.context = getActivity().getApplicationContext();
		this.codigoDepartamento = NavigationDrawer.codigoDepartamento;
		this.controladorDepartamento = new ControladorDepartamento(context,
				codigoDepartamento);
		
		TextView descricao = (TextView) view
				.findViewById(R.id.textView_departamento_descricao);
		TextView chefe = (TextView) view
				.findViewById(R.id.textView_departamento_chefe);

		descricao.setText(controladorDepartamento.getDepartamento()
				.getDescricao());
		chefe.setText(controladorDepartamento.getDepartamento()
				.getNomeProfessor());

		return view;
	}

}
