package controllers;

import static play.data.Form.form;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.Evento;
import models.Local;
import models.Tema;
import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	private static GenericDAO dao = new GenericDAOImpl();
	private static final Form<Application.Temas> FORM_TEMAS = Form
			.form(Application.Temas.class);

	// INICIO METODOS QUE RETORNAM PAGINAS
	@Transactional
	public static Result index() {
		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(index.render("Inicio", getUser(session("email"))));
		}
	}

	@Transactional
	public static Result login() {
		return ok(login.render());
	}

	@Transactional
	public static Result eventosPage() {

		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(eventos.render("Eventos", getUser(session("email"))));
		}

	}

	@Transactional
	public static Result cadastroUsuarioPage() {
		return ok(cadastro.render());

	}

	@Transactional
	public static Result cadastrarEventoPage() {

		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(cadastrar_evento.render("Cadastro de Evento", FORM_TEMAS,
					getUser(session("email"))));
		}

	}

	@Transactional
	public static Result locaisPage() {
		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(locais.render("Locais", getUser(session("email"))));
		}

	}

	@Transactional
	public static Result cadastrarLocalPage() {

		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(cadastrar_local.render("Cadastro de Local",
					getUser(session("email"))));
		}

	}
	
	@Transactional
	public static Result meusEventosPage() {

		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(meus_eventos.render("Meus Eventos",
					getUser(session("email"))));
		}

	}

	@Transactional
	public static Result usuariosPage() {

		if (session("email") == null) {
			return redirect("/login");
		} else {
			return ok(usuarios.render("Usuarios", getUser(session("email"))));
		}
	}

	@Transactional
	public static List<Usuario> getUsuarios() {
		return dao.findAllByClassName("Usuario");
	}

	@Transactional
	public static List<Evento> getEventos() {
		List<Evento> eventos = dao.findAllByClassName("Evento");
		//ORGANIZANDO LISTA DE EVENTOS POR NUMEROS DE INSCRITOS
		//METODO COMPARABLE DA CLASSE EVENTO
		Collections.sort(eventos);
		return eventos;
	}

	@Transactional
	public static List<Local> getLocais() {
		return dao.findAllByClassName("Local");
	}

	// FIM METODOS GET'S

	// INICIO METODOS DE CADASTRO
	@Transactional
	public static Result cadastrarEvento() {

		String titulo = form().bindFromRequest().get("TITULO");
		Date data = formatarData(form().bindFromRequest().get("DATA"));
		String descricao = form().bindFromRequest().get("DESCRICAO");
		String id = form().bindFromRequest().get("LOCALE");
		Local local = dao.findByEntityId(Local.class, Long.parseLong(id));

		Evento evento = new Evento(titulo, descricao, data, local);

		Form<Temas> temasForm = FORM_TEMAS.bindFromRequest();
		Temas temas = temasForm.get();

		for (int i = 0; i < 5; i++) {
			if (temas.getTemas()[i]) {
				evento.addTema(getTema(i));
			}
		}

		persist(evento);

		Usuario usuario = getUser(session("email"));
		usuario.addEvento(evento);
		dao.merge(usuario);
		dao.flush();
		return redirect("/eventos");
	}

	@Transactional
	public static Result participarDeEvento(String email, Long id) {

		Evento evento = dao.findByEntityId(Evento.class, id);
		evento.addParticipante(getUser(email));

		dao.merge(evento);
		dao.flush();

		return redirect("/eventos");
	}
	
	@Transactional
	public static Result excluirEvento(Long id) {

		Evento evento = dao.findByEntityId(Evento.class, id);
		
		Usuario usuario = getUser(session("email"));
		usuario.getEventos().remove(evento);
		
		dao.merge(usuario);
		
		dao.remove(evento);
		dao.flush();

		return redirect("/meus-eventos");
	}

	@Transactional
	public static Result cadastrarLocal() {
		try {
			String nome = form().bindFromRequest().get("NOME");
			String descricao = form().bindFromRequest().get("DESCRICAO");
			Integer capacidade = Integer.parseInt(form().bindFromRequest().get(
					"CAPACIDADE"));

			Local local = new Local(nome, descricao, capacidade);
			persist(local);

		} catch (Exception e) {
			return redirect("/cadastrar/local");
		}

		return redirect("/locais");
	}

	@Transactional
	public static Result cadastrarUsuario() {
		try {
			String nome = form().bindFromRequest().get("NOME");
			String email = form().bindFromRequest().get("EMAIL");
			String senha = form().bindFromRequest().get("SENHA");

			Usuario usuario = new Usuario( nome, email, senha);

			persist(usuario);

		} catch (Exception e) {
			return redirect("/cadastrar/usuario");
		}

		return redirect("/login");
	}

	@Transactional
	public static Result autenticar() {

		String email = form().bindFromRequest().get("EMAIL");
		String senha = form().bindFromRequest().get("SENHA");

		Usuario user = getUser(email);

		if (user == null) {
			flash("erro", "Email Inválido");
			return redirect("/login");
		}

		if (!user.autenticar(senha)) {
			flash("erro", "Senha Incorreta");
			return redirect("/login");
		}

		session().clear();
		session("email", email);

		return redirect("/");
	}

	@Transactional
	public static Result sair() {
		session().clear();

		return redirect("/login");
	}

	@Transactional
	private static Tema getTema(Integer tipo) {
		for (Tema tema : Tema.values()) {
			if (tema.getTipo() == tipo) {
				return tema;
			}
		}
		return null;
	}

	// FIM METODOS DE CADASTRO

	@Transactional
	private static Date formatarData(String data) {

		String[] splitData = data.split("-");
		Integer dia = Integer.parseInt(splitData[2]);
		Integer mes = Integer.parseInt(splitData[1]);
		Integer ano = Integer.parseInt(splitData[0]);
		return new GregorianCalendar(ano, mes - 1, dia).getTime();

	}

	@Transactional
	private static Usuario getUser(String email) {
		return dao.findByEntityId(Usuario.class, email);
	}

	@Transactional
	private static <T> Object persist(Object object) {

		if (!getObjectsBD(object).contains(object)) {
			dao.persist(object);
			dao.flush();
		}
		return getObjectBD(object);
	}

	@Transactional
	private static <T> List<T> getObjectsBD(Object object) {
		return dao.findAllByClassName(object.getClass().getSimpleName());
	}

	@Transactional
	private static <T> Object getObjectBD(Object object) {

		for (Object o : getObjectsBD(object)) {
			if (o.equals(object)) {
				return o;
			}
		}
		return null;
	}

	public static class Temas {

		boolean bd = false;
		boolean web = false;
		boolean android = false;
		boolean nuvem = false;
		boolean programacao = false;

		public void setBd(boolean bd) {
			this.bd = bd;
		}

		public void setWeb(boolean web) {
			this.web = web;
		}

		public void setAndroid(boolean android) {
			this.android = android;
		}

		public void setNuvem(boolean nuvem) {
			this.nuvem = nuvem;
		}

		public void setProgramacao(boolean programacao) {
			this.programacao = programacao;
		}

		public Boolean[] getTemas() {
			return new Boolean[] { bd, web, android, nuvem, programacao };
		}
	}
}
