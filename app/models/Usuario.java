package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;

import com.google.common.base.Objects;

@Entity
public class Usuario {
	
	@Id
	@Required
	private String email;
	@Required
	private String nome;
	@Required
	private String senha;
	
	@OneToMany
	private List<Evento> eventos = new ArrayList<Evento>();
	
	public Usuario() {
		
	}

	public Usuario(String nome,String email, String senha) {
		super();
		this.email = email;
		this.nome = nome;
		this.senha = String.valueOf(Objects.hashCode(email, senha));
	}

	
	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public boolean addEvento(Evento evento){
		return eventos.contains(evento)? false : eventos.add(evento);
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = String.valueOf(Objects.hashCode(email, senha));
	}
	
	public boolean autenticar(String senha){
		String hash = String.valueOf(Objects.hashCode(email, senha));
		return this.senha.equals(hash);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(email);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Usuario)) {
			return false;
		}
		return this.hashCode() == obj.hashCode();
	}

	@Override
	public String toString() {
		return senha.replace("-", "");
	}
	
}