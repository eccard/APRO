package recode.appro.controlador;

import java.io.IOException;
import java.util.ArrayList;

import recode.appro.model.Professor;
import recode.appro.persistencia.DataBaseHelper;
import recode.appro.telas.NavigationDrawer;
import android.content.Context;
import android.database.SQLException;

public class ControladorProfessor {

	private Context context;
	private DataBaseHelper dataBaseHelper;
	private int codigoDepartamento;
	
	public ControladorProfessor(Context context) {

		this.context = context;
		this.dataBaseHelper = new DataBaseHelper(context);
		this.codigoDepartamento = NavigationDrawer.codigoDepartamento;
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

	public ArrayList<Professor> getProfessores() {

		abrirBanco();
		ArrayList<Professor> professores = dataBaseHelper.getProfessores();

		return professores;
	}
	
	public ArrayList<String> getProfessoresDepartamento() {

		abrirBanco();
		ArrayList<String> professoresDepartamento = dataBaseHelper.getProfessoresByDepartamento(codigoDepartamento);

		return professoresDepartamento;
	}

	
}
