public class Agent {

	private String name;
	private int amountPlayed;
	private String strategy;

//	This class is made to easily access attributes of agents
//	An agent has a name, a strategy, and the amount of games the agent has played.
	public Agent(String name, int amountPlayed, String strategy){
		this.name = name;
		this.amountPlayed = amountPlayed;
		this.strategy = strategy;
    }

//	Returns the name of the agent as a string
	public String getName() {
		return name;
	}

//	Returns the amount of games the agent has already played
	public int getAmountPlayed() {
		return amountPlayed;
	}

//	Sets the amount of games the agent has already played
	public void setAmountPlayed(int amountPlayed) {
		this.amountPlayed = amountPlayed;
	}
	
//	Increments the amount of games the agent has played by one
	public void incrementAmountPlayed() {
		this.amountPlayed++;
	}	

//	returns the strategy of the agent as a string
	public String getStrategy() {
		return strategy;
	}

//	sets the strategy of the agent
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}