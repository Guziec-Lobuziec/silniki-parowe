package pl.tau.cwiczenia.enginecrud.domian;

public class Pipe {
	
	private Long id;
	private String name;
	private Double diameter;
	private SteamEngine engine;
	
	public Pipe() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Pipe(Long id, String name, Double diameter, SteamEngine engine) {
		super();
		this.id = id;
		this.name = name;
		this.diameter = diameter;
		this.engine = engine;
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

	public Double getDiameter() {
		return diameter;
	}

	public void setDiameter(Double diameter) {
		this.diameter = diameter;
	}

	public SteamEngine getEngine() {
		return engine;
	}

	public void setEngine(SteamEngine engine) {
		this.engine = engine;
	}

	@Override
	public int hashCode() {
		final int prime = 37;
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
		Pipe other = (Pipe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
