package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.base.Objects;

import play.data.validation.Constraints.Required;

@Entity
public class Evento implements Comparable<Evento>{

	@Id
	@GeneratedValue
	private Long id;

	@Required
	private String titulo;

	@Required
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Required
	private Date data;

	@ManyToMany
	private List<Usuario> participantes = new ArrayList<Usuario>();

	@ElementCollection
	private List<Tema> temas = new ArrayList<Tema>();

	@OneToOne
	private Local local;

	public Evento() {

	}

	public Evento(String titulo, String descricao, Date data, Local local) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
		this.data = data;
		this.local = local;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Date getData() {
		return data;
	}

	public Long getId() {
		return id;
	}

	public List<Tema> getTemas() {
		return temas;
	}

	public boolean addTema(Tema tema){
		return temas.contains(tema) ? false : temas.add(tema);
	}
	public Integer getTotalDeParticipantes() {
		return participantes.size();
	}

	public List<Usuario> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Usuario> participantes) {
		this.participantes = participantes;
	}

	public boolean addParticipante(Usuario participante) {

		if (participantes.contains(participante) || participantes.size() == getCapacidade()) {
			return false;
		} else {
			return participantes.add(participante);
		}
	}

	public int getCapacidade() {
		return local.getCapacidade();
	}
	
	public int getVagas() {
		return local.getCapacidade() - participantes.size();
	}
	
	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(titulo, data, local);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Evento)) {
			return false;
		}

		return this.hashCode() == obj.hashCode();
	}

	@Override
	public String toString() {
		return "Evento: " + titulo + ", Local: " + local.getNome();
	}

	@Override
	public int compareTo(Evento o) {
		return o.getTotalDeParticipantes().compareTo(getTotalDeParticipantes());
	}
}