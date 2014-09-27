package recode.appro.faculdade.departamento.view;

import java.util.List;

import recode.appro.faculdade.departamento.modelo.Professor;
import recode.appro.telas.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterItemProfessor extends BaseAdapter {


List<Professor> professores;
Context context;
	
	public AdapterItemProfessor(List<Professor> professores, Context context) {
		
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
            
            view = mInflater.inflate(R.layout.adapter_item_professores, null);
        }
        
        if(position%2 == 0){
        	
        	view.setBackgroundResource(R.color.White);
        }
        
        TextView nome = (TextView) view.findViewById(R.id.textView_professor_nome);
        TextView departamento = (TextView) view.findViewById(R.id.textView_professor_departamento);
        
        nome.setText(professores.get(position).getNome());
        departamento.setText(professores.get(position).getNomeDepartamento());
        
        return view;
	}

}
