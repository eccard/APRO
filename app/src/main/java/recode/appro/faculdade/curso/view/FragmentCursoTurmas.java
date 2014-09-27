package recode.appro.faculdade.curso.view;

import java.util.ArrayList;
import java.util.List;

import recode.appro.noticia.modelo.Noticia;
import recode.appro.telas.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class FragmentCursoTurmas extends Fragment {

	List<Noticia> noticias = new ArrayList<Noticia>();
	ExpandableListView expandableListView;
	AdapterExpListHorario adapterExpandableList;
	
	
	public FragmentCursoTurmas() {
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setSubtitle(null);
		
		View view = inflater.inflate(R.layout.fragment_curso_horarios, container,
				false);
		
		expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView_horarios);
		adapterExpandableList = new AdapterExpListHorario(getActivity().getApplicationContext());
		expandableListView.setAdapter(adapterExpandableList);
				
		return view;
	}
	
}

