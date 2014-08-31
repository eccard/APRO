package recode.appro.model;

/**
 * Created by eccard on 8/31/14.
 */
public class Usuario {
    private String nick,estudante,curso;
    private int periodo;

    public Usuario(String nick, String estudante, String curso, int periodo) {
        this.nick = nick;
        this.estudante = estudante;
        this.curso = curso;
        this.periodo = periodo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEstudante() {
        return estudante;
    }

    public void setEstudante(String estudante) {
        this.estudante = estudante;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }
}
