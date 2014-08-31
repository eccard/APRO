package recode.appro.telas;

import java.util.ArrayList;
import java.util.List;

import recode.appro.controlador.ControladorCurso;
import recode.appro.model.Disciplina;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AdapterExpListHorario extends BaseExpandableListAdapter {

	String[] listaPai = { "Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
	ControladorCurso controladorCurso;
	int codigoCurso;
	List<List<Disciplina>> matriz = new ArrayList<List<Disciplina>>();
	List<Disciplina> disciplinaSeg = new ArrayList<Disciplina>();
	List<Disciplina> disciplinaTer = new ArrayList<Disciplina>();
	List<Disciplina> disciplinaQua = new ArrayList<Disciplina>();
	List<Disciplina> disciplinaQui = new ArrayList<Disciplina>();
	List<Disciplina> disciplinaSex = new ArrayList<Disciplina>();

	Context context;

	public AdapterExpListHorario(Context context) {

		this.context = context;
		this.codigoCurso = NavigationDrawer.codigoCurso;
		this.controladorCurso = new ControladorCurso(context, codigoCurso);

		this.disciplinaSeg = controladorCurso.getDisciplinaSeg();
		this.disciplinaTer = controladorCurso.getDisciplinaTer();
		this.disciplinaQua = controladorCurso.getDisciplinaQua();
		this.disciplinaQui = controladorCurso.getDisciplinaQui();
		this.disciplinaSex = controladorCurso.getDisciplinaSex();

		this.matriz.add(disciplinaSeg);
		this.matriz.add(disciplinaTer);
		this.matriz.add(disciplinaQua);
		this.matriz.add(disciplinaQui);
		this.matriz.add(disciplinaSex);

	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listaPai.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return matriz.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return listaPai[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return matriz.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.expandable_list_item_pai, null);
		}

		TextView diaSemana = (TextView) convertView
				.findViewById(R.id.textView_turma_dia_semana);
		diaSemana.setText(listaPai[groupPosition]);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = mInflater.inflate(R.layout.adapter_item_turma, null);
		}

		if(childPosition%2 == 0){
			
			convertView.setBackgroundResource(R.color.White);
		}
		
		Disciplina disciplina = matriz.get(groupPosition).get(childPosition);

		TextView disciplinaTurma = (TextView) convertView
				.findViewById(R.id.textView_turma_disciplina);
		TextView salaTurma = (TextView) convertView
				.findViewById(R.id.textView_turma_sala);
		TextView professorTurma = (TextView) convertView
				.findViewById(R.id.textView_turma_professor);
		TextView horarioTurma = (TextView) convertView
				.findViewById(R.id.textView_turma_hora);

		disciplinaTurma.setText(disciplina.getNome());
		salaTurma.setText(disciplina.getCargahoraria());
		professorTurma.setText(disciplina.getPeriodo());
		horarioTurma.setText(disciplina.getTipo());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
