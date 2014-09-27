package recode.appro.faculdade.departamento.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import recode.appro.telas.R;

public class AdapterItemProfessorDepartamento extends BaseAdapter {


List<String> professores;
Context context;
	
	public AdapterItemProfessorDepartamento(List<String> professores, Context context) {
		
		this.professores = professores;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return professores.size();
	}

	@Override
	public Object getItem(int position) {
		
		return professores.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            
        	LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            view = mInflater.inflate(R.layout.adapter_item_professores_departamento, null);
        }
        
        if(position%2 == 0){
        	
        	view.setBackgroundResource(R.color.White);
        }
        
        TextView nome = (TextView) view.findViewById(R.id.textView_professor_nome);
        
        nome.setText(professores.get(position));
        
        return view;
	}

}
