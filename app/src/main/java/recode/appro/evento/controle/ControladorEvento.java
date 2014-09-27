package recode.appro.evento.controle;

import android.content.Context;
import android.database.SQLException;

import java.io.IOException;
import java.util.ArrayList;

import recode.appro.evento.Evento;
import recode.appro.main.persistencia.DataBaseHelper;

/**
 * Created by eccard on 09/07/14.
 */
public class ControladorEvento {
    private Context context;
    private DataBaseHelper dataBaseHelper;

    public ControladorEvento(Context context) {
        this.context = context;
        this.dataBaseHelper = new DataBaseHelper(context);
    }
    public void abrirBanco() {

        try {

            dataBaseHelper.CriarDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dataBaseHelper.abrirDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

    }
    public ArrayList<Evento> getEventos(){

        abrirBanco();
        ArrayList<Evento> listaEventos = dataBaseHelper.getEventos();
        dataBaseHelper.close();

        return listaEventos;
    }
    public int getCodigoUltimoEvento(){
        abrirBanco();
        return dataBaseHelper.getCodigoUltimoEvento();

    }
    public void criarEvento(Evento evento){
        abrirBanco();
        dataBaseHelper.criarEvento(evento);
    }

}
