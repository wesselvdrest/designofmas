public class Agent {

	private String name;
	private int amountPlayed;
	private String strategy;

	public Agent(String name, int amountPlayed, String strategy){
		this.name = name;
		this.amountPlayed = amountPlayed;
		this.strategy = strategy;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmountPlayed() {
		return amountPlayed;
	}

	public void setAmountPlayed(int amountPlayed) {
		this.amountPlayed = amountPlayed;
	}
	
	public void incrementAmountPlayed() {
		this.amountPlayed++;
	}	

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}