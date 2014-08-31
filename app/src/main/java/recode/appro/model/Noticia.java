package recode.appro.model;

import java.io.Serializable;

public class Noticia implements Serializable {

	private int codigo, cursorelacionado,visualizar;
	private String assunto, dataexpedida, horaexpedida, descricao;

	public Noticia(int codigo, String assunto, String dataexpedida,
			String horaexpedida, int cursorelacionado, String descricao) {
		super();
		this.codigo = codigo;
		this.cursorelacionado = cursorelacionado;
		this.assunto = assunto;
		this.dataexpedida = dataexpedida;
		this.horaexpedida = horaexpedida;
		this.descricao = descricao;
	}

    public Noticia(int codigo, String assunto, String dataexpedida,
                   String horaexpedida, int cursorelacionado, String descricao,int visualizar) {
        super();
        this.codigo = codigo;
        this.cursorelacionado = cursorelacionado;
        this.assunto = assunto;
        this.dataexpedida = dataexpedida;
        this.horaexpedida = horaexpedida;
        this.descricao = descricao;
        this.visualizar = visualizar;
    }

    public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCursorelacionado() {
		return cursorelacionado;
	}

	public void setCursorelacionado(int cursorelacionado) {
		this.cursorelacionado = cursorelacionado;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getDataexpedida() {
		return dataexpedida;
	}

	public void setDataexpedida(String dataexpedida) {
		this.dataexpedida = dataexpedida;
	}

	public String getHoraexpedida() {
		return horaexpedida;
	}

	public void setHoraexpedida(String horaexpedida) {
		this.horaexpedida = horaexpedida;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

    public int getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(int visualizar) {
        this.visualizar = visualizar;
    }
}
