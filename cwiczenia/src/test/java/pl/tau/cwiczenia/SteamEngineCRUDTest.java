package pl.tau.cwiczenia;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositoryFactory;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositorySimpleFactory;


@RunWith(Parameterized.class)
public class SteamEngineCRUDTest {
	
	private SteamEngineRepositoryFactory factory;
	private Collection<SteamEngine> samples;
	private Collection<SteamEngine> restore;
	
	private SteamEngineRepository repository;
	
	@Parameters
    public static Collection<Object[]> data() throws SQLException {
    	String url = "jdbc:hsqldb:hsql://localhost/workdb";
    	
    	Collection<SteamEngine> sampl = Arrays.asList(new SteamEngine[] {
    		new SteamEngine(new Long(0), "t0"),
    		new SteamEngine(new Long(1), "t1"),
    		new SteamEngine(new Long(2), "t2"),
    		new SteamEngine(new Long(3), "t3"),
    		new SteamEngine(new Long(4), "t4"),
    		new SteamEngine(new Long(5), "t5"),
    		new SteamEngine(new Long(6), "t6"),
    		new SteamEngine(new Long(7), "t7"),
    	});
        return Arrays.asList(new Object[][] {     
                 { new SteamEngineRepositorySimpleFactory(DriverManager.getConnection(url)), sampl} 
           });
    }
	
	public SteamEngineCRUDTest(SteamEngineRepositoryFactory factory,
			Collection<SteamEngine> samples) throws SQLException {
		this.factory = factory;
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
