import java.util.GregorianCalendar;

import models.Evento;
import models.Local;
import models.Tema;
import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Hackfest {

	private GenericDAO dao;
	private Usuario user;
	private Local local;
	private Evento evento;

	@Before
	public void setUp() throws Exception {
		dao = new GenericDAOImpl();
		user = new Usuario("Rayane", "rayane@mail.com", "123456");
		local = new Local("UFCG","Centro de Extenção José Farias Nóbrega", 100);
		evento = new Evento("Teste", "Colocar", new GregorianCalendar(2014, 12, 28).getTime(), local);
		evento.addTema(Tema.ANDROID);
		evento.addTema(Tema.WEB);
	}
	@Test
	public void permitirCadastrarUsuario() {

	}
	

	@Test
	public void permitirCadastrarEvento() {
		Assert.assertTrue(user.addEvento(evento));
		Assert.assertTrue(user.getEventos().contains(evento));
	}

	@Test
	public void permitirCadastrarParticipante() {
		Assert.assertTrue(user.addEvento(evento));
		
		Assert.assertTrue(evento.addParticipante(user));
		Assert.assertFalse(evento.addParticipante(user));
	}
}
