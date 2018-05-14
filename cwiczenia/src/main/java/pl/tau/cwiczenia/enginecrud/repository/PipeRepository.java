package pl.tau.cwiczenia.enginecrud.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import pl.tau.cwiczenia.enginecrud.domian.Pipe;

public interface PipeRepository {
	
	public Boolean init() throws SQLException;
	public List<Pipe> selectAll();
	public Pipe save(Pipe e);
	public Optional<Pipe> selectWithId(Long id);
	public List<Pipe> selectWithSteamEngine(Long id);
	public Boolean delete(Long id);
	public Pipe update(Pipe e);
	public void drop();
	
}
