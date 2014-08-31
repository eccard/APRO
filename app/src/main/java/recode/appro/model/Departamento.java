package recode.appro.model;

public class Departamento {

	private String nome,nomeProfessor,descricao;
	public Departamento(String nome, String descricao, String nomeProfessor) {
		super();
		
		this.nome = nome;
		this.descricao = descricao;
		this.nomeProfessor = nomeProfessor;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeProfessor() {
		return nomeProfessor;
	}
	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
}
