package recode.appro.noticia.view;

import java.util.ArrayList;
import java.util.List;

import recode.appro.noticia.controle.ControladorNoticia;
import recode.appro.noticia.modelo.Noticia;
import recode.appro.telas.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterItemNoticias extends BaseAdapter {

	Context context;
	List<Noticia> noticias = new ArrayList<Noticia>();
	ControladorNoticia controladorNoticia;
	
	public AdapterItemNoticias( Context context) {
		
		this.context = context;
		this.controladorNoticia = new ControladorNoticia(context);
		if(noticias.size()==0){
            this.noticias = controladorNoticia.getNoticias();

        }
	}
	
	@Override
	public int getCount() {
		return noticias.size();
	}

	@Override
	public Object getItem(int position) {
		
		return noticias.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.adapter_item_noticias, null);
		
		// Altera a cor de fundo para cada item par da lista de notícias  
		if(position%2 == 0){
			view.setBackgroundResource(R.color.White);
		}

		
		TextView assunto = (TextView) view.findViewById(R.id.textview_noticia);
		TextView data = (TextView) view.findViewById(R.id.textview_noticia_data);
		TextView hora = (TextView) view.findViewById(R.id.textView_noticia_hora);
		
	    assunto.setText(noticias.get(position).getAssunto());
		data.setText(noticias.get(position).getDataexpedida());
		hora.setText(noticias.get(position).getHoraexpedida());

        if(noticias.get(position).getVisualizar()==0){
            assunto.setTextColor(Color.BLUE);
        }

        return view;
	}
    public void concatenarArrayDeNoticias(ArrayList<Noticia> lista2){

//        for(int i=0;i<lista2.size();i++){
//            this.eventos.add(lista2.get(i));
//        }

        for (int i=0;i<this.noticias.size();i++){
            lista2.add(this.noticias.get(i));
        }
        this.noticias=lista2;
    }

    public List<Noticia> getNoticias() {
        return noticias;
    }
}
