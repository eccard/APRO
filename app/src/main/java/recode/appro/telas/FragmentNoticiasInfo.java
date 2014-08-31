package recode.appro.telas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import recode.appro.model.Noticia;

/**
 * Created by eccard on 12/07/14.
 */
public class FragmentNoticiasInfo extends Fragment {

    Noticia noticia;

    public FragmentNoticiasInfo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getActionBar().setTitle("Informações");
        getActivity().getActionBar().setSubtitle(null);

        View view = inflater.inflate(R.layout.fragment_noticia_info,
                container, false);

        TextView info= (TextView) view.findViewById(R.id.textView_noticia_texto);

//        return super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }
}
