package pl.tau.cwiczenia.rest;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineRepository;
import pl.tau.cwiczenia.enginecrud.repository.SteamEngineSimpleRepository;

@RestController
public class SteamEngineController {
	
	SteamEngineRepository repository;
	String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
	public SteamEngineController() throws SQLException {
		repository = new SteamEngineSimpleRepository(DriverManager.getConnection(url));
	}
	
	@RequestMapping(path= "/steamengine", method = RequestMethod.GET)
	public List<SteamEngine> getAll() {
		
		return repository.selectAll();
		
	}
	
	@RequestMapping(path= "/steamengine/{id}", method = RequestMethod.GET)
	public Object getWithId(@PathVariable("id") String id) {
		
		Long parsedId;
		
		try {
			parsedId = Long.parseLong(id);
		}
		catch (NumberFormatException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		Optional<SteamEngine> maybe = repository.selectWithId(parsedId);
		
		if(maybe.isPresent())
			return maybe.get();
		else
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
	}
	
	@RequestMapping(path= "/steamengine", method = RequestMethod.POST)
	public Object addNew(@RequestBody SteamEngine engine) {
		
		if(!repository.selectWithId(engine.getId()).isPresent())
			return repository.save(engine);
		else
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(path= "/steamengine", method = RequestMethod.PUT)
	public Object update(@RequestBody SteamEngine engine) {
		
		if(repository.selectWithId(engine.getId()).isPresent())
			return repository.update(engine);
		else
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
	}
	
	@RequestMapping(path= "/steamengine/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> removeOne(@PathVariable("id") String id) {
		
		Long parsedId;
		
		try {
			parsedId = Long.parseLong(id);
		}
		catch (NumberFormatException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		if(repository.delete(parsedId))
			return new ResponseEntity<String>(HttpStatus.OK);
		else
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
	}
	
}
