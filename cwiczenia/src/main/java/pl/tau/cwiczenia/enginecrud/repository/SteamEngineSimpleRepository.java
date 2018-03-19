package pl.tau.cwiczenia.enginecrud.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import pl.tau.cwiczenia.enginecrud.domian.SteamEngine;

public class SteamEngineSimpleRepository implements SteamEngineRepository {
	
	private final String tableName = "SteamEngine";
	
	private Connection connection;
	private boolean initialised =false;
	
	private PreparedStatement selectAll;
	private PreparedStatement save;
	private PreparedStatement selectWithId;
	private PreparedStatement delete;
	private PreparedStatement update;
	
	public SteamEngineSimpleRepository(Connection connection) throws SQLException {
		this.connection = connection;
		this.init();
	}
	
	@Override
	public Boolean init() throws SQLException {
		
		
		if (!initialised) {
			
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			while (rs.next()) {
				if (tableName.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					break;
				}
			}
			
			connection.createStatement().executeUpdate(
		            "CREATE TABLE "
		                + tableName+"(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
						  "name varchar(20) NOT NULL");
			
			selectAll = connection.
					prepareStatement("SELECT id, name FROM "+tableName);
			save = connection.
					prepareStatement(
							"INSERT INTO "+tableName+" (name) VALUES (?)",
							Statement.RETURN_GENERATED_KEYS);
			selectWithId = connection.
					prepareStatement("SELECT id, name FROM "+tableName+" WHERE id = ?");
			delete = connection.
					prepareStatement("DELETE FROM "+tableName+" WHERE id = ?");
			update = connection.
					prepareStatement("UPDATE "+tableName+" SET name = ? WHERE id = ?");
			
		}
		
		return this.initialised;
	}

	@Override
	public List<SteamEngine> selectAll() {
		
		List<SteamEngine> resp = new LinkedList<>();
		
		 try {
	            ResultSet rs = selectAll.executeQuery();

	            while (rs.next()) {
	            	SteamEngine p = new SteamEngine();
	                p.setId(rs.getLong("id"));
	                p.setName(rs.getString("name"));
	                resp.add(p);
	            }

	        } catch (SQLException e) {
	            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
	        }
		
		return resp;
	}

	@Override
	public SteamEngine save(SteamEngine e) {
		
		try {
            save.setString(1, e.getName());
            save.executeUpdate();
            ResultSet rs = save.getGeneratedKeys();
            rs.next();
            SteamEngine st = new SteamEngine();
            st.setId(rs.getLong(1));
            st.setName(e.getName());
            return st;
            
        } catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
            }
	}

	@Override
	public Optional<SteamEngine> selectWithId(Long id) {
		
		ResultSet resp;
		
		try {
			selectWithId.setLong(1, id);
			resp = selectWithId.executeQuery();
			
			if(resp.next()) {
				SteamEngine en = new SteamEngine();
				en.setId(resp.getLong("id"));
				en.setName(resp.getString("name"));
				
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
	public SteamEngine update(SteamEngine e) {
		
		try {
			update.setLong(2, e.getId());
			update.setString(1, e.getName());
			update.executeUpdate();
		} catch (SQLException ex) {
            throw new IllegalStateException(ex.getMessage() + "\n" + ex.getStackTrace().toString());
         }
		
		return e;
	}

}
