package pl.tau.cwiczenia.dbunit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Optional;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pl.tau.cwiczenia.enginecrud.domian.Pipe;
import pl.tau.cwiczenia.enginecrud.repository.PipeRepository;
import pl.tau.cwiczenia.enginecrud.repository.PipeSimpleRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineSimpleRepository;



@RunWith(JUnit4.class)
public class PipeDbUnitTest extends AbstractDbUnitTest {

	private SteamEngineRepository engineRepository;
	private PipeRepository pipeRepository;

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		Connection connection = DriverManager.getConnection(url);
		pipeRepository = new PipeSimpleRepository(connection);
		engineRepository = new SteamEngineSimpleRepository(connection, pipeRepository);
	}

	@Test
	public void selectAllTest() throws Exception {
		
		assertEquals(4, pipeRepository.selectAll().size());
		
		checkAgainstDataSet("setup-ds.xml");
	}

	@Test
	public void saveTest() throws Exception {

		Pipe pipe = new Pipe();
		pipe.setName("added");
		pipe.setDiameter(new Double(0.3));
		pipe.setEngine(engineRepository.selectWithId(new Long(4)).get());

		Long saved = pipeRepository.save(pipe).getId();

		assertNotNull(saved);

		checkAgainstDataSet("save-pipe-ds.xml");
		
		pipeRepository.delete(saved);
	}

	@Test
	public void deleteTest() throws Exception {
		
		assertTrue(pipeRepository.delete(new Long(1)));
		
		checkAgainstDataSet("delete-pipe-ds.xml");
		
	}

	@Test
	public void selectWithIdTest() throws Exception {
		
		Optional<Pipe> selected = pipeRepository.selectWithId(new Long(1));
		assertTrue(selected.isPresent());
		assertEquals(1, selected.get().getId().longValue());
		assertEquals("t1", selected.get().getName());
		
		checkAgainstDataSet("setup-ds.xml");
		
	}

	@Test
	public void updateTest() throws Exception {
		
		Optional<Pipe> selected = pipeRepository.selectWithId(new Long(2));
		assertTrue(selected.isPresent());
		
		Pipe toUpdate = new Pipe(selected.get().getId(), "updated", selected.get().getDiameter(), selected.get().getEngine());
		Pipe updated = pipeRepository.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		assertEquals(toUpdate.getName(), updated.getName());
		assertEquals(toUpdate.getDiameter(), updated.getDiameter());
		assertEquals(toUpdate.getEngine(), updated.getEngine());
		
		checkAgainstDataSet("update-pipe-ds.xml");
		
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
		ITable actualTable = dbDataSet.getTable("PIPE");
		ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actualTable, new String[] { "ID" });
		IDataSet expectedDataSet = getDataSet(set);
		ITable expectedTable = DefaultColumnFilter.excludedColumnsTable(expectedDataSet.getTable("PIPE"), new String[] { "ID" });

		Assertion.assertEquals(expectedTable, filteredTable);
	}


}
