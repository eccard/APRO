package recode.appro.noticia.controle;

import java.io.IOException;
import java.util.ArrayList;

import recode.appro.noticia.modelo.Noticia;
import recode.appro.main.persistencia.DataBaseHelper;
import android.content.Context;
import android.database.SQLException;

public class ControladorNoticia {
	
	private Context context;
	private DataBaseHelper dataBaseHelper;
	
	public ControladorNoticia(Context context) {
		
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
	
	public ArrayList<Noticia> getNoticias(){
	
		abrirBanco();
		ArrayList<Noticia> listaNoticias = dataBaseHelper.getNoticias();
		dataBaseHelper.close();
		
		return listaNoticias;
	}
    public int getCodigoUltimaNoticia(){
        abrirBanco();
        return dataBaseHelper.getCodigoUltimaNoticia();

    }
    public int getVisualizarNoticia(int codigo){
        abrirBanco();
        return dataBaseHelper.getVisualizarNoticia(codigo);
    }

    public void setVisualizarNoticia1(int codigo){
        abrirBanco();
        dataBaseHelper.setVisualizarNoticia1(codigo);

    }
    public void criarNoticia(Noticia noticia){
        abrirBanco();
        dataBaseHelper.criatNoticia(noticia);
    }
}
