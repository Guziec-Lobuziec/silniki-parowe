package pl.tau.cwiczenia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositoryFactory;

public abstract class AbstractSteamEngineCRUDTest {
	
	private SteamEngineRepositoryFactory factory;
	private Collection<SteamEngine> samples;
	private Collection<SteamEngine> restore;
	
	private SteamEngineRepository repository;
	
	public AbstractSteamEngineCRUDTest() {
	}
	
	public final SteamEngineRepositoryFactory getFactory() {
		return factory;
	}

	public final void setFactory(SteamEngineRepositoryFactory factory) {
		this.factory = factory;
	}

	public final Collection<SteamEngine> getSamples() {
		return restore;
	}

	public final void setSamples(Collection<SteamEngine> samples) {
		this.restore = samples;
	}


	@Before
	public void before() throws SQLException {
		repository = factory.createRepository();
		samples = new ArrayList<>(restore.size());
		restore.forEach(e -> {samples.add(e); repository.save(e);});
	}
	
	@After
	public void after() {
		repository.drop();
	}
	
	@Test
	public void selectAllTest() {
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertEquals(samples.size(),got.size());
		
		assertTrue(got.containsAll(samples));

		
	}
	
	@Test
	public void saveTest() {
		
		SteamEngine e = new SteamEngine(null,"saveTest1");
		
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
	
	@Test
	public void updateTest() {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(some.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(some.getId(), selectedOpt.get().getId());
		
		samples.remove(some);
		
		SteamEngine changed = selectedOpt.get();
		changed.setName("test");
		
		repository.update(changed);
		
		Optional<SteamEngine> fromDb = repository.selectWithId(changed.getId());
		
		assertTrue(fromDb.isPresent());
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertTrue(got.containsAll(samples));
		
		assertEquals(fromDb.get(), changed);
	}
	
}
