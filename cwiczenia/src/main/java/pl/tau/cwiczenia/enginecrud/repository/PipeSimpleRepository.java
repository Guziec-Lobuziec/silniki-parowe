package pl.tau.cwiczenia.enginecrud.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import pl.tau.cwiczenia.enginecrud.domian.Pipe;

public class PipeSimpleRepository implements PipeRepository {

	private final String tableName = "Pipe";
	
	private SteamEngineRepository steamEngineRepository;
	
	private Connection connection;
	private boolean initialised =false;
	
	private PreparedStatement selectAll;
	private PreparedStatement save;
	private PreparedStatement selectWithId;
	private PreparedStatement delete;
	private PreparedStatement update;
	private PreparedStatement selectWithSteamEngine;
	
	public PipeSimpleRepository(Connection connection) throws SQLException {
		this.connection = connection;
		this.steamEngineRepository = new SteamEngineSimpleRepository(connection, this);
		this.init();
	}
	
	@Override
	public Boolean init() throws SQLException {
		
		
		if (!initialised) {
			
			boolean created = false;
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			while (rs.next()) {
				if (tableName.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					created = true;
					break;
				}
			}
			
			if(!created)
				connection.createStatement().executeUpdate(
			            "CREATE TABLE "
			                + tableName+"(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
							  "name varchar(20) NOT NULL, " +
							  "diameter float(53), " +
							  "steamEngineId bigint, "+
							  "FOREIGN KEY (steamEngineId) REFERENCES SteamEngine(id), " +
			                  "PRIMARY KEY (id))");
			
			selectAll = connection.
					prepareStatement("SELECT id, name, diameter, steamEngineId FROM "+tableName);
			save = connection.
					prepareStatement(
							"INSERT INTO "+tableName+" (name,diameter,steamEngineId) VALUES (?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			selectWithId = connection.
					prepareStatement("SELECT id, name, diameter, steamEngineId FROM "+tableName+" WHERE id = ?");
			delete = connection.
					prepareStatement("DELETE FROM "+tableName+" WHERE id = ?");
			update = connection.
					prepareStatement("UPDATE "+tableName+" SET name = ?, diameter = ?, steamEngineId = ? WHERE id = ?");
			selectWithSteamEngine = connection.
					prepareStatement("SELECT id, name, diameter, steamEngineId FROM "+tableName+" WHERE steamEngineId = ?");
			
		}
		
		return this.initialised;
	}

	@Override
	public List<Pipe> selectAll() {
		
		List<Pipe> resp = new LinkedList<>();
		
		 try {
	            ResultSet rs = selectAll.executeQuery();

	            while (rs.next()) {
	            	Pipe p = new Pipe();
	                p.setId(rs.getLong("id"));
	                p.setName(rs.getString("name"));
	                p.setDiameter(rs.getDouble("diameter"));
	                p.setEngine(steamEngineRepository.selectWithId(rs.getLong("steamEngineId")).get());
	                resp.add(p);
	            }

	        } catch (SQLException e) {
	            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
	        }
		
		return resp;
	}

	@Override
	public Pipe save(Pipe e) {
		
		try {
            save.setString(1, e.getName());
            save.setDouble(2, e.getDiameter());
            save.setLong(3, e.getEngine().getId());
            save.executeUpdate();
            ResultSet rs = save.getGeneratedKeys();
            rs.next();
            Pipe st = new Pipe();
            st.setId(rs.getLong(1));
            st.setName(e.getName());
            st.setDiameter(e.getDiameter());
            st.setEngine(e.getEngine());
            return st;
            
        } catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
            }
	}

	@Override
	public Optional<Pipe> selectWithId(Long id) {
		
		ResultSet resp;
		
		try {
			selectWithId.setLong(1, id);
			resp = selectWithId.executeQuery();
			
			if(resp.next()) {
				Pipe en = new Pipe();
				en.setId(resp.getLong("id"));
				en.setName(resp.getString("name"));
				en.setDiameter(resp.getDouble("diameter"));
				en.setEngine(steamEngineRepository.selectWithId(resp.getLong("steamEngineId")).get());
				
				return Optional.of(en);
			}
			
		}  catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
         }
		
		return Optional.empty();
	}

	@Override
	public Boolean delete(Long id) {
		
		try {
			delete.setLong(1, id);
			
			if(delete.executeUpdate() != 1)
				return false;
			else
				return true;
			
		} catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
         }
	}

	@Override
	public Pipe update(Pipe e) {
		
		try {
			update.setLong(4, e.getId());
			update.setString(1, e.getName());
			update.setDouble(2, e.getDiameter());
			update.setLong(3, e.getEngine().getId());
			update.executeUpdate();
		} catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
         }
		
		return e;
	}

	@Override
	public void drop() {
		
		try {
			Statement deleteAll = connection.createStatement();
			deleteAll.executeUpdate("DROP TABLE "+tableName);
		} catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
         }
		
	}

	@Override
	public List<Pipe> selectWithSteamEngine(Long id) {
		List<Pipe> resp = new LinkedList<>();
		
		 try {
			 	selectWithSteamEngine.setLong(1, id);
	            ResultSet rs = selectWithSteamEngine.executeQuery();

	            while (rs.next()) {
	            	Pipe p = new Pipe();
	                p.setId(rs.getLong("id"));
	                p.setName(rs.getString("name"));
	                p.setDiameter(rs.getDouble("diameter"));
	                p.setEngine(steamEngineRepository.selectWithId(rs.getLong("steamEngineId")).get());
	                resp.add(p);
	            }

	        } catch (SQLException e) {
	            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
	        }
		
		return resp;
	}

}
