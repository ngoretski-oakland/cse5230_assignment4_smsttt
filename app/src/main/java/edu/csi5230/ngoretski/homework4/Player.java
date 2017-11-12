package edu.csi5230.ngoretski.homework4;

/**
 * Holds the player information
 * @author nathan
 *
 */
public class Player {

	private String name;
	private String symbol;
	
	public Player (String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public String getLabel() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
	
}
