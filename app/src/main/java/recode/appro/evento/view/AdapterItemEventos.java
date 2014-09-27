package recode.appro.evento.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import recode.appro.evento.controle.ControladorEvento;
import recode.appro.evento.Evento;
import recode.appro.telas.R;

/**
 * Created by eccard on 09/07/14.
 */
public class AdapterItemEventos extends BaseAdapter {
    Context context;
//    ArrayList<Evento> eventos;
    List<Evento> eventos = new ArrayList<Evento>();
    ControladorEvento controladorEvento ;

    public AdapterItemEventos(Context context) {
        this.context = context;
        this.controladorEvento= new ControladorEvento(context);

        if(eventos.size()==0){
        this.eventos= controladorEvento.getEventos();
        }

    }

    @Override
    public int getCount() { return   eventos.size();
    }

    @Override
    public Object getItem(int position) {
        return eventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.adapter_item_eventos, null);

        // Altera a cor de fundo para cada item par da lista de notícias
        if(position%2 == 0){
            view.setBackgroundResource(R.color.White);
        }
        TextView nome = (TextView) view.findViewById(R.id.textview_evento);
        TextView local = (TextView) view.findViewById(R.id.textView_evento_local);
        TextView data = (TextView) view.findViewById(R.id.textview_evento_data);
        TextView hora = (TextView) view.findViewById(R.id.textView_evento_hora);

        nome.setText(eventos.get(position).getNome());
        local.setText(eventos.get(position).getLocal());
        data.setText(eventos.get(position).getData());
        hora.setText(eventos.get(position).getHora());

        return view;
    }
    public void concatenarArrayDeEventos(ArrayList<Evento> lista2){

//        for(int i=0;i<lista2.size();i++){
//            this.eventos.add(lista2.get(i));
//        }

        for (int i=0;i<this.eventos.size();i++){
            lista2.add(this.eventos.get(i));
        }
        this.eventos=lista2;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}
