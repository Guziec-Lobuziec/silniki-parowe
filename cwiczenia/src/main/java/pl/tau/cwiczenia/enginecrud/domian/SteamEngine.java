package pl.tau.cwiczenia.enginecrud.domian;

import java.util.ArrayList;
import java.util.List;

public class SteamEngine {
	
	private Long id;
	private String name;
	private List<Pipe> pipes;
	
	public SteamEngine() {
		super();
		this.pipes = new ArrayList<>();
	}
	
	public SteamEngine(Long id, String name, List<Pipe> pipes) {
		super();
		this.id = id;
		this.name = name;
		this.pipes = pipes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pipe> getPipes() {
		return pipes;
	}

	public void setPipes(List<Pipe> pipes) {
		this.pipes = pipes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SteamEngine other = (SteamEngine) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
