import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import models.Evento;
import models.Local;
import models.Tema;
import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import views.html.usuarios;

public class Global extends GlobalSettings {

	private GenericDAO dao = new GenericDAOImpl();

	@Override
	public void onStart(Application arg0) {

		JPA.withTransaction(new play.libs.F.Callback0() {

			@Override
			public void invoke() throws Throwable {
				try {
					criarParticipantes();
					proprietarios();
					
				} catch (Exception e) {
					Logger.info("Erro Global: " + e.getMessage());
				}
			}
		});
	}

	private void criarParticipantes() throws Exception {
		for (Usuario usuario : criarUsuarios()) {
			for (Evento evento : criarEventos()) {
				evento.addParticipante(usuario);
				dao.merge(evento);
			}
		}
		dao.flush();
	}

	private void proprietarios() {
		List<Evento> e = criarEventos();
		List<Usuario> u = criarUsuarios();
		
		for (int i = 0; i < e.size(); i++) {
			Usuario usuario = u.get(i);
			usuario.addEvento(e.get(i));
			dao.merge(usuario);
		}
		dao.flush();
	}

	private List<Usuario> criarUsuarios() {
		List<Usuario> u = new ArrayList<Usuario>();
		try {
			u.add((Usuario) persist(new Usuario("Rayane", "rayane@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Janaina", "janaina@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Mateus", "mateus@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Marcos", "marcos@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Francisco",
					"francisco@mail.com", "123456")));
			u.add((Usuario) persist(new Usuario("Tiago", "tiago@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Barbosa", "barbosa@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Marques", "marques@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Fernanda",
					"fernanda@mail.com", "123456")));
			u.add((Usuario) persist(new Usuario("Jaqueline",
					"jaqueline@mail.com", "123456")));
			u.add((Usuario) persist(new Usuario("Joao", "joao@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Lucas", "lucas@mail.com",
					"123456")));
			u.add((Usuario) persist(new Usuario("Rodrigo", "rodrigo@mail.com",
					"123456")));
		} catch (Exception e) {
			Logger.info("Erro Global: " + e.getMessage());
		}
		return u;
	}

	private List<Evento> criarEventos() {
		List<Evento> u = new ArrayList<Evento>();

		Local local1 = (Local) persist(new Local("UFCG",
				"Centro de Extenção José Farias Nóbrega", 100));
		Local local2 = (Local) persist(new Local("FACISA", "Auditorio", 50));
		Local local3 = (Local) persist(new Local("Fiep",
				"Auditorio de Reuniões", 20));

		try {
			Calendar d = new GregorianCalendar(2014, 12, 28);
			u.add((Evento) persist(new Evento("App Mobile", "Evento destinado a desenvolvimento para dispositivos móveis", d.getTime(),local1)));
			
			Evento evento = u.get(0);
			evento.addTema(Tema.ANDROID);
			dao.merge(evento);
			
			d = new GregorianCalendar(2015, 04, 30);
			u.add((Evento) persist(new Evento("Web Semântica", "Novas tendencias e cooperação", d.getTime(),local1)));
			
			evento = u.get(1);
			evento.addTema(Tema.WEB);
			dao.merge(evento);
			
			d = new GregorianCalendar(2015, 02, 05);
			u.add((Evento) persist(new Evento("Bancos de Dados Distribuídos", "Primeiro contato com esta tecnologia", d
					.getTime(), local2)));
			
			evento = u.get(2);
			evento.addTema(Tema.BANCO_DE_DADOS);
			dao.merge(evento);
			
			d = new GregorianCalendar(2015, 03, 10);
			u.add((Evento) persist(new Evento("Cloud Computing","Desafios em segurança de dados", d.getTime(), local2)));
			
			evento = u.get(3);
			evento.addTema(Tema.COMPUTACAO_EM_NUVEM);
			dao.merge(evento);
			
			d = new GregorianCalendar(2015, 01, 13);
			u.add((Evento) persist(new Evento("Novas Linguagens", "Desafios com novas linguagens", d.getTime(), local3)));
			
			evento = u.get(4);
			evento.addTema(Tema.PROGRAMACAO);
			dao.merge(evento);
			
			d = new GregorianCalendar(2015, 04, 20);
			u.add((Evento) persist(new Evento("Entretenimento", "Stream de dados", d
					.getTime(), local3)));
			
			evento = u.get(5);
			evento.addTema(Tema.WEB);
			evento.addTema(Tema.ANDROID);
			dao.merge(evento);
			
			d = new GregorianCalendar(2015, 05, 27);
			u.add((Evento) persist(new Evento("Cascata", "Novas tendências em HTML5 e CSS3", d
					.getTime(), local1)));

			evento = u.get(6);
			evento.addTema(Tema.WEB);
			evento.addTema(Tema.PROGRAMACAO);
			dao.merge(evento);
		} catch (Exception e) {
			Logger.info("Erro Global: " + e.getMessage());
		}
		return u;
	}

	@Transactional
	private <T> Object persist(Object object) {
		List<T> result = dao.findAllByClassName(object.getClass()
				.getSimpleName());
		if (!result.contains(object)) {
			dao.persist(object);
			dao.flush();
		}

		return getObjectBD(object);
	}

	@Transactional
	private <T> Object getObjectBD(Object object) {
		List<T> result = dao.findAllByClassName(object.getClass()
				.getSimpleName());
		for (Object obj : result) {
			if (obj.equals(object)) {
				return obj;
			}
		}
		return null;
	}
}
