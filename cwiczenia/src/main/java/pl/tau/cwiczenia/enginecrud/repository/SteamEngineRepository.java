package pl.tau.cwiczenia.enginecrud.repository;

import java.util.List;
import java.util.Optional;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;

public interface SteamEngineRepository {
	
	public Boolean init();
	public List<SteamEngine> selectAll();
	public SteamEngine save(SteamEngine e);
	public Optional<SteamEngine> selectWithId(Long id);
	public Boolean delete(Long id);
	public SteamEngine update(SteamEngine e);
	

}
