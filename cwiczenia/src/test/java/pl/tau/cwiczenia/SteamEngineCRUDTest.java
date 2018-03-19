package pl.tau.cwiczenia;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositoryFactory;

public class SteamEngineCRUDTest {

	
	private SteamEngineRepositoryFactory factory;
	private Collection<SteamEngine> samples;
	
	private SteamEngineRepository repository;
	
	public SteamEngineCRUDTest(SteamEngineRepositoryFactory factory,
			Collection<SteamEngine> samples) throws SQLException {
		this.factory = factory;
		this.samples = samples;
		repository = factory.createRepository();
		repository.init();
	}
	
	@Before
	void before() {
		
		
	}
	
	@After
	void after() {
		
	}
	
	@Test
	public void selectAllTest() {
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertEquals(samples.size(),got.size());
		
		assertTrue(got.containsAll(samples));

		
	}
	
	@Test
	public void saveTest() {
		
		SteamEngine e = new SteamEngine();
		
		assertNull(e.getId());
		
		SteamEngine persisted = repository.save(e);
		
		assertNotNull(persisted.getId());
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(persisted.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(persisted.getId(), selectedOpt.get().getId());
		
	}
	
	@Test
	public void deleteTest() {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		repository.delete(some.getId());
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertEquals(samples.size()-1,got.size());
		
		assertFalse(got.contains(some));
			
	}
	
	@Test
	public void selectWithIdTest() {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(some.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(some.getId(), selectedOpt.get().getId());
	}
	
	public void updateTest() {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(some.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(some.getId(), selectedOpt.get().getId());
		
		samples.remove(some);
		
		SteamEngine changed = selectedOpt.get();
		changed.setName("test");
		
		SteamEngine updated = repository.update(changed);
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertTrue(got.containsAll(samples));
		
		assertEquals(updated.getName(), changed.getName());
	}
}
