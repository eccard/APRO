package recode.appro.model;

public class Professor {

	String nome, nomeDepartamento;

	public Professor(String nome, String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeDepartamento() {
		return nomeDepartamento;
	}

	public void setNomeDepartamento(String nomeDepartamento) {
		this.nomeDepartamento = nomeDepartamento;
	}

}
