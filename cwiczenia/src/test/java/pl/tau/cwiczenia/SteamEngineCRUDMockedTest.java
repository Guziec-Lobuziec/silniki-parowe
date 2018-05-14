package pl.tau.cwiczenia;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositoryFactory;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositorySimpleFactory;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineSimpleRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SteamEngineCRUDMockedTest {
	
	private abstract static class MockResultSet implements ResultSet {
		Collection<SteamEngine> samples;
		Iterator<SteamEngine> iter;
		SteamEngine item;
		
		@Override
		public boolean next() throws SQLException {
			if(!iter.hasNext())
				return false;
			item = iter.next();
			return true;
		}
		
		@Override
		public long getLong(String columnLabel) throws SQLException {
			return item.getId();
		}
		
		@Override
		public String getString(String columnLabel) throws SQLException {
			return item.getName();
		}
	}
	
	private final String tableName = "SteamEngine";
	
	private Collection<SteamEngine> samples;
	private SteamEngineRepository repository;
	
	@Mock
	private Connection mockConnection;
	
	@Mock
	private PreparedStatement mockSelectAll;
	
	@Mock
	private PreparedStatement mockSave;
	
	@Mock
	private PreparedStatement mockSelectWithId;
	
	@Mock
	private PreparedStatement mockDelete;
	
	@Mock
	private PreparedStatement mockUpdate;
	
	@Mock
	private Statement mockDeleteAll;
	
	@Mock
	private ResultSet mockMetadata;
	
	public SteamEngineCRUDMockedTest() {
		
		this.samples = Arrays.asList(new SteamEngine[] {
	    		new SteamEngine(new Long(0), "t0", new ArrayList<>()),
	    		new SteamEngine(new Long(1), "t1", new ArrayList<>()),
	    		new SteamEngine(new Long(2), "t2", new ArrayList<>()),
	    		new SteamEngine(new Long(3), "t3", new ArrayList<>()),
	    		new SteamEngine(new Long(4), "t4", new ArrayList<>()),
	    		new SteamEngine(new Long(5), "t5", new ArrayList<>()),
	    		new SteamEngine(new Long(6), "t6", new ArrayList<>()),
	    		new SteamEngine(new Long(7), "t7", new ArrayList<>()),
	    	});
		
	}
	
	@Before
	public void before() throws SQLException {
		
		when(mockConnection.prepareStatement("SELECT id, name FROM "+tableName))
			.thenReturn(mockSelectAll);
		
		when(mockConnection.prepareStatement(
				"INSERT INTO "+tableName+" (name) VALUES (?)",
				Statement.RETURN_GENERATED_KEYS))
			.thenReturn(mockSave);
		
		when(mockConnection.prepareStatement("SELECT id, name FROM "+tableName+" WHERE id = ?"))
			.thenReturn(mockSelectWithId);
		
		when(mockConnection.prepareStatement("DELETE FROM "+tableName+" WHERE id = ?"))
			.thenReturn(mockDelete);
		
		when(mockConnection.prepareStatement("UPDATE "+tableName+" SET name = ? WHERE id = ?"))
			.thenReturn(mockUpdate);
		
		
		Statement statement = mock(Statement.class);
		when(mockConnection.createStatement()).thenReturn(statement);
		
		
		DatabaseMetaData mockDatabaseMetaData = mock(DatabaseMetaData.class);
		when(mockDatabaseMetaData.getTables(null, null, null, null)).thenReturn(mockMetadata);
		
		when(mockConnection.getMetaData()).thenReturn(mockDatabaseMetaData);
		
		when(mockMetadata.next()).thenReturn(true).thenReturn(false);
		
		when(mockMetadata.getString("TABLE_NAME")).thenReturn(tableName);
		
		
	}
	
	@Test
	public void selectAllTest() throws SQLException {
		
		MockResultSet mockSelectAllResult = mock(MockResultSet.class);
		mockSelectAllResult.samples = new ArrayList<SteamEngine>(this.samples);
		mockSelectAllResult.iter = mockSelectAllResult.samples.iterator();
		
		when(mockSelectAll.executeQuery()).thenReturn(mockSelectAllResult);
		
		when(mockSelectAllResult.next()).thenCallRealMethod();
		when(mockSelectAllResult.getLong("id")).thenCallRealMethod();
		when(mockSelectAllResult.getString("name")).thenCallRealMethod();
		
		this.repository = new SteamEngineSimpleRepository(mockConnection);
		
		assertNotNull(this.repository);
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertEquals(samples.size(),got.size());
		
		assertTrue(got.containsAll(samples));

		
	}
	
	@Test
	public void saveTest() throws SQLException {
		
		SteamEngine e = new SteamEngine(null,"saveTest1", new ArrayList<>());
		
		ResultSet mockSaveResult = mock(ResultSet.class);
		ResultSet mockSelectWithIdResult = mock(ResultSet.class);
		
		when(mockSave.getGeneratedKeys()).thenReturn(mockSaveResult);
		
		when(mockSaveResult.getLong(1)).thenReturn((long) (samples.size()+1));
		
		when(mockSelectWithId.executeQuery()).thenReturn(mockSelectWithIdResult);
		
		when(mockSelectWithIdResult.next()).thenReturn(true);
		
		when(mockSelectWithIdResult.getLong("id")).thenReturn((long) (samples.size()+1));
		
		when(mockSelectWithIdResult.getString("name")).thenReturn("saveTest1");
		
		
		
		this.repository = new SteamEngineSimpleRepository(mockConnection);
		
		assertNotNull(this.repository);
		
		assertNull(e.getId());
		
		SteamEngine persisted = repository.save(e);
		
		assertNotNull(persisted.getId());
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(persisted.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(persisted.getId(), selectedOpt.get().getId());
		
	}
	
	@Test
	public void deleteTest() throws SQLException {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		MockResultSet mockSelectAllResult = mock(MockResultSet.class);
		mockSelectAllResult.samples = new ArrayList<SteamEngine>(this.samples);
		mockSelectAllResult.samples.remove(some);
		mockSelectAllResult.iter = mockSelectAllResult.samples.iterator();
		
		when(mockSelectAll.executeQuery()).thenReturn(mockSelectAllResult);
		
		when(mockSelectAllResult.next()).thenCallRealMethod();
		when(mockSelectAllResult.getLong("id")).thenCallRealMethod();
		when(mockSelectAllResult.getString("name")).thenCallRealMethod();
		
		when(mockDelete.executeUpdate()).thenReturn(new Integer(1));
		
		this.repository = new SteamEngineSimpleRepository(mockConnection);
		
		repository.delete(some.getId());
		
		Collection<SteamEngine> got = repository.selectAll();
		
		assertEquals(samples.size()-1,got.size());
		
		assertFalse(got.contains(some));
		
		verify(mockDelete).executeUpdate();
			
	}
	
	@Test
	public void selectWithIdTest() throws SQLException {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		ResultSet mockSelectWithIdResult = mock(ResultSet.class);
		
		when(mockSelectWithId.executeQuery()).thenReturn(mockSelectWithIdResult);
		
		when(mockSelectWithIdResult.next()).thenReturn(true);
		
		when(mockSelectWithIdResult.getLong("id")).thenReturn(some.getId());
		
		when(mockSelectWithIdResult.getString("name")).thenReturn(some.getName());
		
		this.repository = new SteamEngineSimpleRepository(mockConnection);
		
		
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(some.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(some.getId(), selectedOpt.get().getId());
	}
	
	@Test
	public void updateTest() throws SQLException {
		
		assertTrue(samples.iterator().hasNext());
		SteamEngine some = samples.iterator().next();
		
		ResultSet mockSelectWithIdResult = mock(ResultSet.class);
		
		when(mockSelectWithId.executeQuery()).thenReturn(mockSelectWithIdResult);
		
		when(mockSelectWithIdResult.next()).thenReturn(true);
		
		when(mockSelectWithIdResult.getLong("id")).thenReturn(some.getId());
		
		when(mockSelectWithIdResult.getString("name")).thenReturn(some.getName());
		
		this.repository = new SteamEngineSimpleRepository(mockConnection);
		
		
		
		Optional<SteamEngine> selectedOpt = repository.selectWithId(some.getId());
		
		assertTrue(selectedOpt.isPresent());
		
		assertEquals(some.getId(), selectedOpt.get().getId());
		
		SteamEngine changed = selectedOpt.get();
		changed.setName("test");
		
		repository.update(changed);
		
		
		when(mockSelectWithId.executeQuery()).thenReturn(mockSelectWithIdResult);
		
		when(mockSelectWithIdResult.next()).thenReturn(true);
		
		when(mockSelectWithIdResult.getLong("id")).thenReturn(changed.getId());
		
		when(mockSelectWithIdResult.getString("name")).thenReturn(changed.getName());
		
		
		Optional<SteamEngine> fromDb = repository.selectWithId(changed.getId());
		
		assertTrue(fromDb.isPresent());
		
		
		MockResultSet mockSelectAllResult = mock(MockResultSet.class);
		mockSelectAllResult.samples = new ArrayList<SteamEngine>(this.samples);
		mockSelectAllResult.samples.remove(some);
		mockSelectAllResult.samples.add(changed);
		mockSelectAllResult.iter = mockSelectAllResult.samples.iterator();
		
		when(mockSelectAll.executeQuery()).thenReturn(mockSelectAllResult);
		
		when(mockSelectAllResult.next()).thenCallRealMethod();
		when(mockSelectAllResult.getLong("id")).thenCallRealMethod();
		when(mockSelectAllResult.getString("name")).thenCallRealMethod();
		
		
		Collection<SteamEngine> got = repository.selectAll();
		
		ArrayList<SteamEngine> testSamples = new ArrayList<SteamEngine>(this.samples);
		testSamples.remove(some);
		
		assertTrue(got.containsAll(testSamples));
		
		assertEquals(fromDb.get(), changed);
	}

}
