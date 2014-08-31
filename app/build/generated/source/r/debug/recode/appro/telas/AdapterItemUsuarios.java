package recode.appro.telas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import recode.appro.controlador.ControladorEvento;
import recode.appro.model.Evento;
import recode.appro.model.Usuario;

/**
 * Created by eccard on 8/31/14.
 */
public class AdapterItemUsuarios extends BaseAdapter {
    Context context;
    //    ArrayList<Evento> eventos;
    List<Usuario> usuarios = new ArrayList<Usuario>();

    public AdapterItemUsuarios(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() { return   usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.adapter_item_evento_confirmados, null);

        // Altera a cor de fundo para cada item par da lista de notícias
        if(position%2 == 0){
            view.setBackgroundResource(R.color.White);
        }

        TextView nick = (TextView) view.findViewById(R.id.textView_nick);
        TextView informacoes = (TextView) view.findViewById(R.id.textView_periodo_curso);

        if(usuarios.get(position).getEstudante().equalsIgnoreCase("Aluno")){
            nick.setText(usuarios.get(position).getNick());
            informacoes.setText(usuarios.get(position).getPeriodo() + " p. " + usuarios.get(position).getCurso());
        }

        return view;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}

