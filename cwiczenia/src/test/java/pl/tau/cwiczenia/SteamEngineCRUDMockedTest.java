package pl.tau.cwiczenia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;

@RunWith(MockitoJUnitRunner.class)
public class SteamEngineCRUDMockedTest extends AbstractSteamEngineCRUDTest {
	
	@Mock
	Connection mockConnection;
	
	@Parameters
    public static Collection<SteamEngine> data() throws SQLException {
    	
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
    	
    	return sampl;
    }

	public SteamEngineCRUDMockedTest(Collection<SteamEngine> samples) {
		super.setSamples(samples);
	}
	
	@Override
	public void before() throws SQLException {
		
		super.before();
	}

}
