import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import models.Evento;
import models.Local;
import models.Usuario;
import models.dao.GenericDAOImpl;

import org.junit.Before;
import org.junit.Test;

public class HackfestTest {

	private GenericDAOImpl dao;
	private Usuario user;
	private Local locale;
	private Evento event;

	@Before
	public void setup() {
		dao = new GenericDAOImpl();
		user = new Usuario("User", "user@mail.com", "123");
		locale = new Local("LocalTeste", "LocalTeste", 20);
		event = new Evento("EventoTeste", "EventoTeste", Calendar.getInstance()
				.getTime(), locale);
	}

	@Test
	public void adicionarEvento() {

		// EVENTO NAO DEVE PERTENCER A LISTA DE EVENTOS DO USUARIO
		assertFalse(user.getEventos().contains(event));

		// DEVE ADICIONAR EVENTO A LISTA DE EVENTOS DO USUARIO
		assertTrue(user.addEvento(event));

		// NAO DEVE ADICIONAR EVENTO IGUAIS
		assertFalse(user.addEvento(event));

		// VERIFICANDO SE EVENTO PERTENCE AO USUARIO
		assertTrue(user.getEventos().contains(event));
	}

	@Test
	public void removerEvento() {

		// EVENTO NAO DEVE PERTENCER A LISTA DE EVENTOS DO USUARIO
		assertFalse(user.getEventos().contains(event));

		// ADICIONANDO EVENTO
		assertTrue(user.addEvento(event));

		// VERIFICANDO SE EVENTO FOI ADICIONADO
		assertTrue(user.getEventos().contains(event));

		// DEVE REMOVER EVENTO
		assertTrue(user.removerEvento(event));

		// NAO DEVE REMOVER EVENTO, POIS JA FOI REMOVIDO
		assertFalse(user.removerEvento(event));

		// EVENTO NAO DEVE PERTENCER A LISTA DE EVENTOS DO USUARIO
		assertFalse(user.getEventos().contains(event));
	}

	@Test
	public void adicionarParticipante() {

		Local local = new Local("Teste", "Test", 2);

		Usuario user2 = new Usuario("User2", "user2@mail.com", "123");

		// MUDANDO LOCAL DO EVENTO
		event.setLocal(local);

		// DEVE ADICIONAR PARTICIPANTE
		assertTrue(event.addParticipante(user2));

		// NAO DEVE ADICIONAR PARTICIPANTE JA INSCRITO
		assertFalse(event.addParticipante(user2));

		// MUDANDO CAPACIDADE DO LOCAL PARA 1
		local = new Local("Teste", "Test", 1);
		event.setLocal(local);

		assertTrue(event.getTotalDeParticipantes() == 1);
		assertTrue(event.getVagas() == 0);

		// NAO DEVE PERMITIR CADASTRAR NOVO PARTICIPANTE POIS ESTA LOTADO
		assertFalse(event.addParticipante(user));

		// MUDANDO CAPACIDADE DO LOCAL PARA 2
		local = new Local("Teste", "Test", 2);
		event.setLocal(local);

		assertTrue(event.getTotalDeParticipantes() == 1);
		assertTrue(event.getVagas() == 1);

		// DEVE PERMITIR CADASTRAR NOVO PARTICIPANTE POIS HA VAGAS
		assertTrue(event.addParticipante(user));

		// VERIFICANDO SE EVENTO PERTENCE AO USUARIO
		assertTrue(event.getParticipantes().contains(user));
		assertTrue(event.getParticipantes().contains(user2));
	}

	@Test
	public void eventosOrdenadosPorParticipantes() {

		Usuario user2 = new Usuario("User2", "user2@mail.com", "123");

		Evento event2 = new Evento("EventoTeste2", "EventoTeste2", Calendar
				.getInstance().getTime(), locale);

		Evento event3 = new Evento("EventoTeste3", "EventoTeste3", Calendar
				.getInstance().getTime(), locale);

		// event tem 2 participantes
		assertTrue(event.addParticipante(user));
		assertTrue(event.addParticipante(user2));

		// event2 tem 1participante
		assertTrue(event2.addParticipante(user));

		assertTrue(event.getTotalDeParticipantes() == 2);
		assertTrue(event2.getTotalDeParticipantes() == 1);
		assertTrue(event3.getTotalDeParticipantes() == 0);

		assertTrue(event.getVagas() == 18);
		assertTrue(event2.getVagas() == 19);
		assertTrue(event3.getVagas() == 20);

		// VERIFICANDO PARTICIPANTES
		assertTrue(event.getParticipantes().contains(user));
		assertTrue(event.getParticipantes().contains(user2));

		assertTrue(event2.getParticipantes().contains(user));
		assertFalse(event2.getParticipantes().contains(user2));

		assertFalse(event3.getParticipantes().contains(user));
		assertFalse(event3.getParticipantes().contains(user2));

		List<Evento> eventos = new ArrayList<Evento>();

		// ADICIONANDO EM ORDEM CONTRARIA
		assertTrue(eventos.add(event3));
		assertTrue(eventos.add(event2));
		assertTrue(eventos.add(event));

		// FAZENDO CHECAGEM ORDEM DE POSICOES
		assertTrue(eventos.get(0).equals(event3));
		assertTrue(eventos.get(1).equals(event2));
		assertTrue(eventos.get(2).equals(event));

		// ORDENANDO LISTA POR QUEM POSSUI MAIO NUMERO DE PARTICIPANTES
		Collections.sort(eventos);

		// FAZENDO CHECAGEM ORDEM DE POSICOES APOS ORDENACAO
		assertTrue(eventos.get(0).equals(event));
		assertTrue(eventos.get(1).equals(event2));
		assertTrue(eventos.get(2).equals(event3));
	}

	@Test
	public void autenticarUsuario() {

		Usuario user2 = new Usuario("User2", "user2@mail.com", "123");

		// A SENHA NAO PODE SER OBTIDA PELO MEDOTO GET, POIS A SENHA E COMPOSTA
		// PELO HASH DO EMAIL COM A SENHA
		assertFalse(user2.getSenha().equals("123"));
		// AUTENTICANDO COM SENHA INCORRETA
		assertFalse(user2.autenticar("abc"));

		// AUTENTICANDO COM SENHA INCORRETA
		assertTrue(user2.autenticar("123"));
		
		// ALTERANDO A SENHA
		user2.setSenha("ABC");
		
		// TESTANDO CASE SENSITIVE
		assertFalse(user2.autenticar("abc"));
				
		//VERIFICANDO COM A SENHA ANTERIOR
		assertFalse(user2.autenticar("123"));
		
		// AUTENTICANDO COM A NOVA SENHA
		assertTrue(user2.autenticar("ABC"));
	}
}
