package recode.appro.faculdade.curso.modelo;

public class Disciplina {
	private int codigo, codigodepartamento, codigocurso;
	private String nome, tipo, cargahoraria, periodo;

	public Disciplina(int codigo, String nome, String tipo,
			String cargahoraria, String periodo, int codigodepartamento,
			int codigocurso) {
		
		this.codigo = codigo;
		this.codigodepartamento = codigodepartamento;
		this.codigocurso = codigocurso;
		this.nome = nome;
		this.tipo = tipo;
		this.cargahoraria = cargahoraria;
		this.periodo = periodo;
	}

	public Disciplina(String nome, String tipo, String cargahoraria,
			String periodo) {
		this.nome = nome;
		this.tipo = tipo;
		this.cargahoraria = cargahoraria;
		this.periodo = periodo;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigodepartamento() {
		return codigodepartamento;
	}

	public void setCodigodepartamento(int codigodepartamento) {
		this.codigodepartamento = codigodepartamento;
	}

	public int getCodigocurso() {
		return codigocurso;
	}

	public void setCodigocurso(int codigocurso) {
		this.codigocurso = codigocurso;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCargahoraria() {
		return cargahoraria;
	}

	public void setCargahoraria(String cargahoraria) {
		this.cargahoraria = cargahoraria;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

}
