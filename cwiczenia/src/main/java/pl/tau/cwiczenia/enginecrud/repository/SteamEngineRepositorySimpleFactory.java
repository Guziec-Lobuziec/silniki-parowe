package pl.tau.cwiczenia.enginecrud.repository;

import java.sql.Connection;
import java.sql.SQLException;

public class SteamEngineRepositorySimpleFactory implements SteamEngineRepositoryFactory {
	
	private Connection connection;
	
	public SteamEngineRepositorySimpleFactory(Connection connection) throws SQLException  {
		this.connection = connection;
	}
	
	@Override
	public SteamEngineRepository createRepository() {
		// TODO Auto-generated method stub
		return null;
	}

}
