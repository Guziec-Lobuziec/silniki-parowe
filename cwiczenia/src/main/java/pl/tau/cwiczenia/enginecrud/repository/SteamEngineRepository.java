package pl.tau.cwiczenia.enginecrud.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;

public interface SteamEngineRepository {
	
	public Boolean init() throws SQLException;
	public List<SteamEngine> selectAll();
	public SteamEngine save(SteamEngine e);
	public Optional<SteamEngine> selectWithId(Long id);
	public Boolean delete(Long id);
	public SteamEngine update(SteamEngine e);
	public void drop();
	

}
