package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.Objects;
import play.data.validation.Constraints.Required;

@Entity
public class Local {

	@Id
	@GeneratedValue
	private Long id;

	@Required
	private String nome;

	@Required
	private String descricao;

	@Required
	private Integer capacidade;

	public Local() {
	
	}

	public Local(String nome, String descricao, Integer capacidade) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.capacidade = capacidade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nome, capacidade);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof Local)) {
			return false;
		}
		return this.hashCode() == obj.hashCode();
	}

	@Override
	public String toString() {
		return "Local: " + nome + ", " + descricao + ", Capacidade: "
				+ capacidade;
	}
}