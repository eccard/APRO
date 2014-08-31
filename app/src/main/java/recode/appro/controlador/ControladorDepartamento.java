package recode.appro.controlador;

import java.io.IOException;

import recode.appro.model.Departamento;
import recode.appro.persistencia.DataBaseHelper;
import recode.appro.telas.NavigationDrawer;
import android.content.Context;
import android.database.SQLException;

public class ControladorDepartamento {

	private Context context;
	private DataBaseHelper dataBaseHelper;
	private int codigoDepartamento;

	public ControladorDepartamento(Context context, int codigoDepartamento) {

		this.context = context;
		this.codigoDepartamento = NavigationDrawer.codigoDepartamento;
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

		} catch (SQLException sqle) {

			throw sqle;

		}

	}

	public Departamento getDepartamento(){
		
		Departamento departamento;
		
		abrirBanco();
		departamento = dataBaseHelper.getDepartamento(codigoDepartamento);
		
		return departamento;
	}
}
