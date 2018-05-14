package pl.tau.cwiczenia.dbunit;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.DriverManager;
import java.util.Optional;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineSimpleRepository;

@RunWith(JUnit4.class)
public class SteamEngineDbUnitTest extends AbstractDbUnitTest {

	private SteamEngineRepository repository;

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		repository = new SteamEngineSimpleRepository(DriverManager.getConnection(url));
	}

	@Test
	public void selectAllTest() throws Exception {
		
		assertEquals(6, repository.selectAll().size());
		
		checkAgainstDataSet("setup-ds.xml");
	}

	@Test
	public void saveTest() throws Exception {

		SteamEngine engine = new SteamEngine();
		engine.setName("added");

		Long saved = repository.save(engine).getId();

		assertNotNull(saved);

		checkAgainstDataSet("save-ds.xml");
		
		repository.delete(saved);
	}

	@Test
	public void deleteTest() throws Exception {
		
		assertTrue(repository.delete(new Long(1)));
		
		checkAgainstDataSet("delete-ds.xml");
		
	}

	@Test
	public void selectWithIdTest() throws Exception {
		
		Optional<SteamEngine> selected = repository.selectWithId(new Long(1));
		assertTrue(selected.isPresent());
		assertEquals(1, selected.get().getId().longValue());
		assertEquals("t1", selected.get().getName());
		
		checkAgainstDataSet("setup-ds.xml");
		
	}

	@Test
	public void updateTest() throws Exception {
		
		Optional<SteamEngine> selected = repository.selectWithId(new Long(2));
		assertTrue(selected.isPresent());
		
		SteamEngine toUpdate = new SteamEngine(new Long(2), "updated");
		SteamEngine updated = repository.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		assertEquals(toUpdate.getName(), updated.getName());
		
		checkAgainstDataSet("update-ds.xml");
		
	}

	@Override
	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.INSERT;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE;
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return this.getDataSet("setup-ds.xml");
	}
	
	private void checkAgainstDataSet(String set) throws Exception {
		IDataSet dbDataSet = this.getConnection().createDataSet();
		ITable actualTable = dbDataSet.getTable("STEAMENGINE");
		ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actualTable, new String[] { "ID" });
		IDataSet expectedDataSet = getDataSet(set);
		ITable expectedTable = DefaultColumnFilter.excludedColumnsTable(expectedDataSet.getTable("STEAMENGINE"), new String[] { "ID" });

		Assertion.assertEquals(expectedTable, filteredTable);
	}

}
