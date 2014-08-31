package recode.appro.persistencia;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import recode.appro.model.Departamento;
import recode.appro.model.Disciplina;
import recode.appro.model.Evento;
import recode.appro.model.Noticia;
import recode.appro.model.Professor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	// Declaração do diretório e nome do arquivo do banco de dados.
	private static String DB_PATH = "/data/data/recode.appro.telas/databases/";
//    private static String DB_NAME = "PUROapp_Updated.db";
//    private static String DB_NAME = "PUROapp_Updated-1.db";
//    private static String DB_NAME = "PUROapp_Updated-1.1.db";
//    private static String DB_NAME = "PUROapp_Updated-1.2.db";
//    private static String DB_NAME = "PUROapp_Updated-1.3.db";
//    private static String DB_NAME = "PUROapp_Updated-1.4.db";
//    private static String DB_NAME = "PUROapp_Updated-1.5.db";
//    private static String DB_NAME = "PUROapp_Updated-1.6.db";
    private static String DB_NAME = "PUROapp_Updated-1.7.db";
    public SQLiteDatabase dbQuery;
    private final Context dbContexto;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);

		this.dbContexto = context;
		// DataBaseHelper.DB_PATH = context.getFilesDir().getPath();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// Criação do banco
	public void CriarDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (!dbExist) {
			this.getReadableDatabase();

			try {
				this.copiarDataBase();
			} catch (IOException e) {
				throw new Error("Erro ao copiar o Banco de Dados!");
			}
		}
	}

	private void copiarDataBase() throws IOException {

		InputStream myInput = dbContexto.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public void abrirDataBase() throws SQLException {
        try {
            String myPath = DB_PATH + DB_NAME;
            dbQuery = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            // dbQuery.execSQL("PRAGMA foreign_keys = ON;");
        }catch (SQLiteException e){
            Log.e(e.toString(),e.toString());
        }
	}

	@Override
	public synchronized void close() {
		if (dbQuery != null)
			dbQuery.close();
		super.close();
	}

	public ArrayList<Noticia> getNoticias() {

		ArrayList<Noticia> noticias = new ArrayList<Noticia>();

		Cursor cursor = dbQuery.rawQuery("select * from noticias", null);
		// codigo,assunto,dataExpedida,horaExpedida,cursoRelacionado,descricao
		if (cursor.moveToFirst()) {
			do {
				noticias.add(new Noticia(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3), cursor
								.getInt(4), cursor.getString(5),cursor.getInt(6)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return noticias;
	}
    public ArrayList<Evento> getEventos(){
        ArrayList<Evento> eventos = new ArrayList<Evento>();

        Cursor cursor = dbQuery.rawQuery("select * from evento", null);
        // codigo,assunto,dataExpedida,horaExpedida,cursoRelacionado,descricao
        // cod,nome,descricao,organizadores,local,data,hora,confirmados
        if (cursor.moveToFirst()) {
            do {

                eventos.add(new Evento(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor
                        .getString(4), cursor.getString(5),cursor.getString(6),cursor.getInt(7)
                ));
            } while (cursor.moveToNext());
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

//        for(int i=0; i<eventos.size();i++){
//            Log.i("eventos",eventos.get(i).getNome());
//        }
        return eventos;
    }
	public ArrayList<String> getCursoInformacoes(int codigo) {

		ArrayList<String> infocurso = new ArrayList<String>();
		String[] whereArgs = new String[] { String.valueOf(codigo) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select descricao,titulacao,duracao,mercadoTrabalho from curso where codigo = ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				infocurso.add(cursor.getString(0));
				infocurso.add(cursor.getString(1));
				infocurso.add(cursor.getString(2));
				infocurso.add(cursor.getString(3));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return infocurso;
	}

	public ArrayList<Disciplina> getDisciplina(int codigoCurso) {

		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] whereArgs = new String[] { String.valueOf(codigoCurso) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select Nome,Tipo,CargaHoraria,Periodo from Disciplina where CodigoCurso = ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				disciplinas
						.add(new Disciplina(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinaSEG(int codigoCurso) {

		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] whereArgs = new String[] { String.valueOf(codigoCurso) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select disciplina.Nome,turma.Horario,turma.numeroSala,professor.Nome from disciplina,professor,turma where turma.CodigoDisciplina= disciplina.Codigo and turma.MatProfessor = professor.Matricula and turma.diaSemana = "
								+ "'SEG'" + " and Disciplina.CodigoCurso= ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				disciplinas
						.add(new Disciplina(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinaTER(int codigoCurso) {

		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] whereArgs = new String[] { String.valueOf(codigoCurso) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select disciplina.Nome,turma.Horario,turma.numeroSala,professor.Nome from disciplina,professor,turma where turma.CodigoDisciplina= disciplina.Codigo and turma.MatProfessor = professor.Matricula and turma.diaSemana = "
								+ "'TER'" + " and Disciplina.CodigoCurso= ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				disciplinas
						.add(new Disciplina(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinaQUA(int codigoCurso) {

		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] whereArgs = new String[] { String.valueOf(codigoCurso) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select disciplina.Nome,turma.Horario,turma.numeroSala,professor.Nome from disciplina,professor,turma where turma.CodigoDisciplina= disciplina.Codigo and turma.MatProfessor = professor.Matricula and turma.diaSemana = "
								+ "'QUA'" + " and Disciplina.CodigoCurso= ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				disciplinas
						.add(new Disciplina(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinaQUI(int codigoCurso) {

		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] whereArgs = new String[] { String.valueOf(codigoCurso) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select disciplina.Nome,turma.Horario,turma.numeroSala,professor.Nome from disciplina,professor,turma where turma.CodigoDisciplina= disciplina.Codigo and turma.MatProfessor = professor.Matricula and turma.diaSemana = "
								+ "'QUI'" + " and Disciplina.CodigoCurso= ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				disciplinas
						.add(new Disciplina(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return disciplinas;
	}

	public ArrayList<Disciplina> getDisciplinaSEX(int codigoCurso) {

		ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] whereArgs = new String[] { String.valueOf(codigoCurso) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select disciplina.Nome,turma.Horario,turma.numeroSala,professor.Nome from disciplina,professor,turma where turma.CodigoDisciplina= disciplina.Codigo and turma.MatProfessor = professor.Matricula and turma.diaSemana = "
								+ "'SEX'" + " and Disciplina.CodigoCurso= ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				disciplinas
						.add(new Disciplina(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return disciplinas;
	}

	public Departamento getDepartamento(int codigoDepartamento) {

		Departamento departamento = null;
		String[] whereArgs = new String[] { String.valueOf(codigoDepartamento) };
		Cursor cursor = dbQuery
				.rawQuery(
						"select departamento.Nome,departamento.Descricao,professor.Nome from professor,departamento where professor.Matricula = departamento.MatProfessorChefe and departamento.Codigo = ?",
						whereArgs);
		if (cursor.moveToFirst()) {
			do {
				departamento = new Departamento(cursor.getString(0),
						cursor.getString(1), cursor.getString(2));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return departamento;
	}
	
	public ArrayList<Professor> getProfessores(){
		  ArrayList<Professor> nomesProfessores = new ArrayList<Professor>();
		  Cursor cursor = dbQuery.rawQuery("select professor.Nome,departamento.Nome from professor,departamento where  professor.CodigoDepartamento = departamento.Codigo order by professor.Nome", null);
		   if (cursor.moveToFirst()) {
		            do {
		                nomesProfessores.add(new Professor(cursor.getString(0),cursor.getString(1)));
		            } while (cursor.moveToNext());
		            if (cursor != null && !cursor.isClosed()) {
		                cursor.close();
		            }
		        }
		    return nomesProfessores; 
		}

	public ArrayList<String> getProfessoresByDepartamento(int codigoDepartamento){
		  ArrayList<String> nomesProfessores = new ArrayList<String>();
		  String[] whereArgs = new String[] {String.valueOf(codigoDepartamento)}; 
		  Cursor cursor = dbQuery.rawQuery("select professor.Nome from professor,departamento where professor.CodigoDepartamento = departamento.Codigo and departamento.Codigo = ?", whereArgs);
		   if (cursor.moveToFirst()) {
		            do {
		                nomesProfessores.add(cursor.getString(0));
		            } while (cursor.moveToNext());
		            if (cursor != null && !cursor.isClosed()) {
		                cursor.close();
		            }
		        }
		    return nomesProfessores; 
		}

    public int getCodigoUltimoEvento(){
//        Cursor cursor = dbQuery.rawQuery("SELECT MAX(cod) FROM evento",null);
        Cursor cursor = dbQuery.rawQuery("SELECT MAX(_id) FROM evento",null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    public int getCodigoUltimaNoticia(){
        Cursor cursor = dbQuery.rawQuery("SELECT MAX(_id) FROM noticias",null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public int getVisualizarNoticia(int codigo){
        String[] whereArgs = new String[] { String.valueOf(codigo)};
        Cursor cursor = dbQuery.rawQuery("SELECT visualizar FROM noticias where _id = ?",whereArgs);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public void setVisualizarNoticia1(int codigo){
        dbQuery.execSQL("UPDATE noticias SET visualizar = '1' where _id =" + String.valueOf(codigo));
    }
    public void criatNoticia(Noticia noticia){
          Log.i("entrando no criarção da noticia","entrando criação noticiaaaaaaaaaaaaaaaa");
        this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_id",noticia.getCodigo());
        values.put("assunto",noticia.getAssunto());
        values.put("dataExpedida",noticia.getDataexpedida());
        values.put("horaExpedida",noticia.getHoraexpedida());
        values.put("cursoRelacionado",noticia.getCursorelacionado());
        values.put("descricao",noticia.getDescricao());
        values.put("visualizar",noticia.getVisualizar());
        try{
            dbQuery.insert("noticias",null,values);
        }catch (SQLException e){
            Log.i(e.toString(),e.toString());
        }
    }

    public void criarEvento(Evento evento){
        Log.i("entrando no criarção da noticia","entrando criação noticiaaaaaaaaaaaaaaaa");
        this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put("cod",evento.getCodigo());
        values.put("_id",evento.getCodigo());
        values.put("nome",evento.getNome());
        values.put("descricao",evento.getDescricao());
        values.put("organizadores",evento.getOrganizadores());
        values.put("local",evento.getLocal());
        values.put("data",evento.getData());
        values.put("hora",evento.getHora());
        values.put("presenca",evento.getPresenca());
        try{
            dbQuery.insert("evento",null,values);
        }catch (SQLException e){
            Log.i(e.toString(),e.toString());
        }
    }

    public int verificarSeExisteUsuario(){
        Cursor cursor = dbQuery.rawQuery("SELECT COUNT(*) FROM usuario ",null);
//        cursor.moveToFirst();
        cursor.moveToFirst();
        int cont = cursor.getInt(0);
        cursor.close();
        return cont;

    }
    public void criarUsuarioAluno(String nick,String curso,int periodo){
        this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nick",nick);
        values.put("estudante","1");
        values.put("curso",curso);
        values.put("periodo",periodo);

        try{
            dbQuery.insert("usuario",null,values);
        }catch (SQLException e){
            Log.i(e.toString(),e.toString());
        }
    }
    public void criarUsuarioPT(String nick){
        this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nick",nick);
        values.put("estudante","0");
        values.put("curso","");
        values.put("periodo","");

        try{
            dbQuery.insert("usuario",null,values);
        }catch (SQLException e){
            Log.i(e.toString(),e.toString());
        }
    }
    public String GetNomeUsuario(){
        Cursor cursor = dbQuery.rawQuery("SELECT nick FROM usuario ",null);
//        cursor.moveToFirst();
        cursor.moveToFirst();
        String nick = cursor.getString(0);
        cursor.close();
        return nick;
    }
	/*
	 * public ArrayList<Noticia> getSpecificNoticias(int codigoNoticia) {
	 * 
	 * ArrayList<Noticia> noticias = new ArrayList<Noticia>(); String[]
	 * whereArgs = new String[] {String.valueOf(codigoNoticia)} Cursor cursor =
	 * dbQuery.rawQuery("select * from noticias where codigo = ?",whereArgs); if
	 * (cursor.moveToFirst()) { do { noticias.add(new Noticia(cursor.getInt(0),
	 * cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor
	 * .getString(4),cursor.getInt(5))); } while (cursor.moveToNext()); if
	 * (cursor != null && !cursor.isClosed()) { cursor.close(); } } return
	 * noticias; }
	 * 
	 * public ArrayList<Noticia> getSearchNoticia(String token) {
	 * 
	 * ArrayList<Noticia> noticias = new ArrayList<Noticia>(); String[]
	 * whereArgs = new String[] {token}; Cursor cursor = dbQuery.rawQuery(
	 * "select distinct assunto,dataExpedida,horaExpedida from noticias,curso where  (cursoRelacionado = (select Codigo from curso where Nome like "
	 * "%?%"") or assunto like ""%?%"" or  noticias.descricao like ""%?%"");",
	 * whereArgs); if (cursor.moveToFirst()) { do { noticias.add(new
	 * Disciplina(cursor.getInt(0), cursor.getString(1), cursor.getInt(2))); }
	 * while (cursor.moveToNext()); if (cursor != null && !cursor.isClosed()) {
	 * cursor.close(); } } return noticias; }
	 * 
	 * public ArrayList<String> getAutoCompleteDisciplina(int codigoCurso){
	 * ArrayList<String> nomedisciplinas = new ArrayList<String>(); String[]
	 * whereArgs = new String[] {String.valueOf(codigoCurso)}; Cursor cursor =
	 * dbQuery.rawQuery("select Nome from Disciplina where CodigoCurso = ?",
	 * whereArgs); if (cursor.moveToFirst()) { do { disciplinas.add(new
	 * Disciplina(cursor.getInt(0))); } while (cursor.moveToNext()); if (cursor
	 * != null && !cursor.isClosed()) { cursor.close(); } } return
	 * nomedisciplinas; }
	 * 
	 * public ArrayList<String> getSearchProfessores(int codigodepartamento) {
	 * 
	 * ArrayList<String> nomeprofessor = new ArrayList<String>(); String[]
	 * whereArgs = new String[] {String.valueOf(codigodepartamento)}; Cursor
	 * cursor =
	 * dbQuery.rawQuery("select Nome from Professor where CodigoDepartamento = ? "
	 * , whereArgs); if (cursor.moveToFirst()) { do { nomeprofessor.add(new
	 * Professor(cursor.getInt(0)); } while (cursor.moveToNext()); if (cursor !=
	 * null && !cursor.isClosed()) { cursor.close(); } } return nomeprofessor; }
	 * 
	 * 
	 * public InfoCurso getInfoCurso(int codigoCurso) {
	 * 
	 * InfoCurso ifc = null; String[] whereArgs = new String[]
	 * {String.valueOf(codigoCurso)}; Cursor cursor = dbQuery.rawQuery(
	 * "select descricao,duracao,titulacao,mercadoTrabalho from curso where Codigo = ?"
	 * , whereArgs); if (cursor.moveToFirst()) { do { ifc = new
	 * InfoCurso(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
	 * cursor.getString(3)); } while (cursor.moveToNext()); if (cursor != null
	 * && !cursor.isClosed()) { cursor.close(); } } return ifc; }
	 * 
	 * public ArrayList<Disciplina> getDisciplina(int codigoCurso) {
	 * 
	 * ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>(); String[]
	 * whereArgs = new String[] {String.valueOf(codigoCurso)}; Cursor cursor =
	 * dbQuery.rawQuery(
	 * "select Nome,Tipo,CargaHoraria,Periodo from Disciplina where CodigoCurso = ?"
	 * , whereArgs); if (cursor.moveToFirst()) { do { disciplinas.add(new
	 * Disciplina(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
	 * cursor.getString(3))); } while (cursor.moveToNext()); if (cursor != null
	 * && !cursor.isClosed()) { cursor.close(); } } return disciplinas; }
	 * 
	 * public ArrayList<Disciplina> getSearchDisciplina(int codigoCurso,String
	 * token) {
	 * 
	 * ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>(); String[]
	 * whereArgs = new String[] {String.valueOf(codigoCurso),token}; Cursor
	 * cursor = dbQuery.rawQuery(
	 * "select Nome,Tipo,CargaHoraria,Periodo from Disciplina where CodigoCurso = ? and  (Nome like "
	 * "%?%"" or Periodo like ""%?%"");", whereArgs); if (cursor.moveToFirst())
	 * { do { disciplinas.add(new Disciplina(cursor.getInt(0),
	 * cursor.getString(1), cursor.getInt(2), cursor.getString(3))); } while
	 * (cursor.moveToNext()); if (cursor != null && !cursor.isClosed()) {
	 * cursor.close(); } } return disciplinas; }
	 */

}
