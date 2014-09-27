package recode.appro.main.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import recode.appro.telas.R;

public class AdapterDrawerItem extends BaseExpandableListAdapter {

	String[] listaPai = {"O Pólo","Calendários","Cursos","Departamentos",
							"Notícias","Professores","Eventos"};
	String[][] listaFilho = {{},{}, 
							{"Ciência da Computação","Enfermagem","Engenharia de Produção", 
    						"Produção Cultural","Psicologia","Serviço Social"},
    						{"ICT","IHS","RAE","RCT", "RFM", "RIR"},{},{},{}};
	Context context;
	
	public AdapterDrawerItem(Context context) {
		
		this.context = context;
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listaPai.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return listaFilho[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return listaPai[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return listaFilho[groupPosition][childPosition];
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

		if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_drawer_item_pai,parent,false);
        }

        TextView pai = (TextView) convertView.findViewById(R.id.textView_drawer_item_pai);
        pai.setText(listaPai[groupPosition]);
        convertView.setTag(listaPai[groupPosition]);
        
        return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

	        if (convertView == null)
	        {
	            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = layoutInflater.inflate(R.layout.adapter_drawer_item_filho,parent,false);
	        }

	        TextView filho = (TextView) convertView.findViewById(R.id.textView_drawer_item_filho);
	        filho.setText(listaFilho[groupPosition][childPosition]);
	        convertView.setTag(listaFilho[groupPosition][childPosition]);
	        
	        return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
