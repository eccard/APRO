package recode.appro.controlador;

import java.io.IOException;
import java.util.ArrayList;

import recode.appro.model.Disciplina;
import recode.appro.persistencia.DataBaseHelper;
import android.content.Context;
import android.database.SQLException;

public class ControladorCurso {

	private Context context;
	private DataBaseHelper dataBaseHelper;
	private int codigo;

	public ControladorCurso(Context context, int codigo) {

		this.context = context;
		this.codigo = codigo;
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

	public ArrayList<String> getInformacoes() {

		abrirBanco();
		ArrayList<String> informacoes = dataBaseHelper
				.getCursoInformacoes(codigo);

		return informacoes;
	}

	public ArrayList<Disciplina> getDisciplinas() {

		abrirBanco();
		ArrayList<Disciplina> disciplinas = dataBaseHelper
				.getDisciplina(codigo);

		return disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinaSeg() {

		abrirBanco();
		ArrayList<Disciplina> disciplinasSeg = dataBaseHelper
				.getDisciplinaSEG(codigo);

		return disciplinasSeg;
	}

	public ArrayList<Disciplina> getDisciplinaTer() {

		abrirBanco();
		ArrayList<Disciplina> disciplinasTer = dataBaseHelper
				.getDisciplinaTER(codigo);

		return disciplinasTer;
	}

	public ArrayList<Disciplina> getDisciplinaQua() {

		abrirBanco();
		ArrayList<Disciplina> disciplinasQua = dataBaseHelper
				.getDisciplinaQUA(codigo);

		return disciplinasQua;
	}

	public ArrayList<Disciplina> getDisciplinaQui() {

		abrirBanco();
		ArrayList<Disciplina> disciplinasQui = dataBaseHelper
				.getDisciplinaQUI(codigo);

		return disciplinasQui;
	}

	public ArrayList<Disciplina> getDisciplinaSex() {

		abrirBanco();
		ArrayList<Disciplina> disciplinasSex = dataBaseHelper
				.getDisciplinaSEX(codigo);

		return disciplinasSex;
	}
}
