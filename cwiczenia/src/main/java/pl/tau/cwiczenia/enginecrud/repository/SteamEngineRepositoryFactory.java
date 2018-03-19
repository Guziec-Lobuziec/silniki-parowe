package pl.tau.cwiczenia.enginecrud.repository;

import java.sql.SQLException;

public interface SteamEngineRepositoryFactory {

	public SteamEngineRepository createRepository() throws SQLException;
	
}
