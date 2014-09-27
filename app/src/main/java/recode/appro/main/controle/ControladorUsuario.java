package recode.appro.main.controle;

import android.content.Context;
import android.database.SQLException;

import java.io.IOException;

import recode.appro.main.persistencia.DataBaseHelper;

/**
 * Created by eccard on 7/29/14.
 */
public class ControladorUsuario {
    private Context context;
    private DataBaseHelper dataBaseHelper;

    public ControladorUsuario(Context context) {
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

    public int verificarSeExisteUsuario(){
        abrirBanco();
        return dataBaseHelper.verificarSeExisteUsuario();
    }
    public void criarUsuarioAluno(String nick,String curso,int periodo){
        abrirBanco();
        dataBaseHelper.criarUsuarioAluno(nick,curso,periodo);
    }
    public void criarUsuarioPT(String nick){
        abrirBanco();
        dataBaseHelper.criarUsuarioPT(nick);
    }

    public String GetNomeUsuario(){
        abrirBanco();
        return dataBaseHelper.GetNomeUsuario();
    }

//    public void criarUsuario2(String nick,String tipoUsuario,String cursoUsuario,int periodoAluno){
//        int codUsuario=0;
//        int codCurso=0;
//        if (tipoUsuario.equalsIgnoreCase("Aluno")){
//            codUsuario=1;
//        }
//        else
//        if(tipoUsuario.equalsIgnoreCase("Professor,tecnico")){
//            codUsuario=0;
//        }
//
//        if(cursoUsuario.equalsIgnoreCase("Ciência da Computação")){
//
//        }
//        else
//        if(cursoUsuario.equalsIgnoreCase("Enfermagem")){
//
//        }
//        else
//        if(cursoUsuario.equalsIgnoreCase("Engenharia de Produção")){
//
//        }
//        else
//        if(cursoUsuario.equalsIgnoreCase("Produção Cultural")){
//
//        }
//        else
//        if(cursoUsuario.equalsIgnoreCase("Psicologia")){
//
//        }
//        else
//        if(cursoUsuario.equalsIgnoreCase("Serviço Social")){
//
//        }

//        abrirBanco();
//        dataBaseHelper.criarUsuario(nick,codUsuario,cursoUsuario,periodoAluno);
//    }
}
