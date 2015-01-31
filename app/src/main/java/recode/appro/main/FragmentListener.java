package recode.appro.main;

import recode.appro.evento.Evento;

/**
 * Created by eccard on 9/2/14.
 */
public interface FragmentListener {

    public void callbackEvento(Evento evento);
    public void callbackEventoConfirmados(Evento evento);

    public void callbackNoticias();
}
