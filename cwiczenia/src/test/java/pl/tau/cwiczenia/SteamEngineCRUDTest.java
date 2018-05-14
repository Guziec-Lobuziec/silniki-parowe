package pl.tau.cwiczenia;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositoryFactory;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepositorySimpleFactory;

@Ignore
@RunWith(Parameterized.class)
public class SteamEngineCRUDTest extends AbstractSteamEngineCRUDTest {
	
	@Parameters
    public static Collection<Object[]> data() throws SQLException {
    	String url = "jdbc:hsqldb:hsql://localhost/workdb";
    	
    	Collection<SteamEngine> sampl = Arrays.asList(new SteamEngine[] {
    		new SteamEngine(new Long(0), "t0", new ArrayList<>()),
    		new SteamEngine(new Long(1), "t1", new ArrayList<>()),
    		new SteamEngine(new Long(2), "t2", new ArrayList<>()),
    		new SteamEngine(new Long(3), "t3", new ArrayList<>()),
    		new SteamEngine(new Long(4), "t4", new ArrayList<>()),
    		new SteamEngine(new Long(5), "t5", new ArrayList<>()),
    		new SteamEngine(new Long(6), "t6", new ArrayList<>()),
    		new SteamEngine(new Long(7), "t7", new ArrayList<>()),
    	});
        return Arrays.asList(new Object[][] {     
                 { new SteamEngineRepositorySimpleFactory(DriverManager.getConnection(url)), sampl} 
           });
    }
	
	public SteamEngineCRUDTest(SteamEngineRepositoryFactory factory,
			Collection<SteamEngine> samples) throws SQLException {
		super.setFactory(factory);
		super.setSamples(samples);
	}
	
}
