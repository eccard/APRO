package recode.appro.telas;

import java.util.List;

import recode.appro.model.Disciplina;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterItemDisciplina extends BaseAdapter {


List<Disciplina> disciplinas;
Context context;
	
	public AdapterItemDisciplina(List<Disciplina> disciplinas, Context context) {
		this.disciplinas = disciplinas;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return disciplinas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return disciplinas.get(position);
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
            
            view = mInflater.inflate(R.layout.adapter_item_disciplina, null);
        }
        
        if(position%2 == 0){
        	
        	view.setBackgroundResource(R.color.White);
        }
        
        TextView disciplinaNome = (TextView) view.findViewById(R.id.textView_disciplina_nome);
        TextView periodo = (TextView) view.findViewById(R.id.textView_disciplina_periodo);
        TextView tipo = (TextView) view.findViewById(R.id.textView_disciplina_obrigatoria);
        TextView cargahoraria = (TextView) view.findViewById(R.id.textView_disciplina_cargahoraria);
        
        disciplinaNome.setText(disciplinas.get(position).getNome());
        periodo.setText(disciplinas.get(position).getPeriodo());
        tipo.setText(disciplinas.get(position).getTipo());
        cargahoraria.setText(disciplinas.get(position).getCargahoraria());
        
        return view;
	}

}
