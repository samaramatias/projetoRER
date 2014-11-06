package models;

public enum Tema {

	BANCO_DE_DADOS(0, "Banco de Dados"),
	ANDROID(2, "Android"),
	COMPUTACAO_EM_NUVEM(3, "Computação em Nuvem"),
	PROGRAMACAO(4, "Programação"),
	WEB(1, "Web");

	private final String descricao;

	private final Integer tipo;

	private Tema(Integer tipo, String descricao) {
		this.tipo = tipo;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return descricao;
	}
}