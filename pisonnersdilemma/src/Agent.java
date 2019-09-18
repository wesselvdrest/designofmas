import java.util.*;

class Agent implements Comparable<Agent> {

    private Random gen = new Random();
    public Simulation scape;
    
    private int id;
    private int xPosition;
    private int yPosition;
    private int score;
    private int encounters;
    private double average;
    private String strategy;
    private int[][] memory; 
    private int[][] memoryOwnActions;
    private boolean[] grim;
    boolean hasMoved;
	
    public Agent (Simulation controller, int i, String s) {
		scape = controller;

		id = i;
		score = 0;
		encounters = 0;
		average = 0;
		strategy = s;
		hasMoved = false;

		//Agent.memory records the past 10 actions of the 
		//encountered agents against this agent. 
		//memory[agentID][0] contains the most recent encounter.
		memory = new int[scape.numAgents][10];
		for (int m = 0; m < memory.length; m++){
			for (int n = 0; n < memory[m].length; n++){
				memory[m][n] = 0;
			}
		}

		//Agent.memoryOwnActions records the past 10 actions of this agent 
		//against encountered agents. 
		//memoryOwnActions[agentID][0] contains the most recent encounter.
		memoryOwnActions = new int[scape.numAgents][10];
		for (int m = 0; m < memoryOwnActions.length; m++){
			for (int n = 0; n < memoryOwnActions[m].length; n++){
				memoryOwnActions[m][n] = 0;
			}
		}	
    
    
    
		//This function records whether the opponent had ever defected.
		//If the other agent defects once, grim will be set to TRUE and
		//this agent will always defect from now on. FALSE means the 
		//other agent hasn't defected (yet). See wikipedia 'Grim Trigger'.
		grim = new boolean[scape.numAgents];
		for(int m = 0; m < grim.length; m++){
			grim[m] = false;
		}
}
    
    // This method lets agents move and play against other agents.
    public void act() {   		
    	move();
    	Vector players = find("players");
    	if(players.size() > 0) {
        	for(int p = 0; p < players.size(); p++) {
        		Agent other = (Agent)players.elementAt(p);
        		//The agents only play when *both* have moved.
        		if(other.hasMoved) {
        			play(other);
        		}
        	}
    	}
    }
    
    // 1 equals cooperation, -1 equals defection. 0 means no encounter occurred.
    // When this agent encounters other agents, this method
    // defines the game they will play. 
    public void play(Agent other) {
    	int ownAction = getAction(other.getID());
    	int otherAction = other.getAction(id);
    	
    	// Both agents cooperate.
    	if(ownAction == 1 && otherAction == 1) {
    		this.score += 3;
    		other.addScore(3);
    	}
    	
    	// This agent cooperates, the other defects.
    	if(ownAction == 1 && otherAction == -1) {
    		other.addScore(5);
    	}
    	
    	// This agent defects, the other cooperates.
    	if(ownAction == -1 && otherAction == 1) {
    		this.score += 5;
    	}
    	
    	// Both agents defect.
    	if(ownAction == -1 && otherAction == -1) {
    		this.score += 1;
    		other.addScore(2);
    	}
    	
    	// Below the memories of both agents are updated.
    	this.updateMemory(other.getID(), otherAction);
    	this.updateMemoryOwnActions(other.getID(), ownAction);
    	
   		other.updateMemory(this.id, ownAction);
   		other.updateMemoryOwnActions(this.id, otherAction);
    }
    
    // 1 equals cooperation, -1 equals defection. 0 means no encounter occurred.
    // This function contains two strategies. If you wish to add or remove one,
    // change the strategies array in the Simulation class.
    //
    // You can use action, memory[playerID][n] and memoryOwnActions[playerID][n]
    // Add strategies to variable "static String[] strategies" in Simulation
    public int getAction(int playerID) {
    	//if no condition below eventually matches; cooperate
    	int action = 1;
    	encounters++;
    	
    	//ALL-C
    	if(strategy.equals("ALL-C")) {
    		action = 1;	//Always compromise, no matter what.
    	}
    	
    	//ALL-D
    	else if(strategy.equals("ALL-D")) {	
    		action = -1; //Always defect, no matter what
    	}
    	//TIT-4-TAT
    	else if(strategy.equals("TIT-4-TAT")) {
    		if (memory[playerID][0] != 0)
    		action = memory[playerID][0]; //always play the same move the other agent played last time
    	}

    	//TAT-4-TIT
    	else if(strategy.equals("TAT-4-TIT")) {
    		//always play the opposite of what the other agent played last time
    		if (memory[playerID][0] == 1)
    			action = -1;
    		else if (memory[playerID][0] == -1)
    			action = 1;
    	}
    	
    	//LOOKBACK
    	else if(strategy.equals("LOOKBACK")) {
    		int defects = 0;
    		//see how many times the other agent defected in the past
    		for (int i = 0; i < 10; ++i){
    			if (memory[playerID][i] == -1)
    				defects++;
    		}
    		//if the number of defected is greater than or equal to 5; defect
    		if (defects >= 5)
    			action = -1;
    		//else; defect
    		else if (defects < 5)
    			action = 1;    			
    	}
    	
    	//GRIM
    	else if(strategy.equals("GRIM")) {
	    	//check if the other agent is on the 'grim trigger' list. if true; defect
			if(grim[playerID] == true)
				action = -1;
			//if the other agent isn't on the 'grim trigger' list, check if he should be. 
			//If so; defect and make grim 'true'
			else if(memory[playerID][0] == -1){
				grim[playerID] = true;
				action = -1;
			} 
    	}
    	

    	
    	
    	return action;
    }
    	
    	
    // Generate a vector with the objects you need. Currently there
    // are two options available: players and free sites.
    public Vector find(String objects) {
    	Vector data = new Vector(); 			
  	
   		for(int m = -1; m <= 1; m++) {
    		for(int n = -1; n <= 1; n++) {
    			int x = xPosition + m;
    			int y = yPosition + n;
    			if(x < 0) {
    				x += 50;
    			}
    			if(x >= 50) {
    				x -= 50;
    			}
    			if(y < 0) {
    				y += 50;
    			}
    			if(y >= 50) {
    				y -= 50;
    			}
    			Agent agent = scape.grid[x][y].getAgent();
    			if(objects.equals("players") && agent != null && !this.equals(agent)) {
	    			data.add(agent);
	   			}
    	
    			if(objects.equals("free sites") && agent == null) {
	    			data.add(scape.grid[x][y]);
	    		}
    		}
    	}
		return data;
	}

    public void updateMemory(int agentID, int action) {
    	for(int m = memory[agentID].length - 2; m >= 0 ; m--){
    		memory[agentID][m+1] = memory[agentID][m];
    	}
    	memory[agentID][0] = action;
    }

    public void updateMemoryOwnActions(int agentID, int action) {
    	for(int m = memoryOwnActions[agentID].length - 2; m >= 0 ; m--){
    		memoryOwnActions[agentID][m+1] = memoryOwnActions[agentID][m];
    	}
    	memoryOwnActions[agentID][0] = action;
    }
    
    // The agent moves 1 site in a random direction.
    // When an Agent moves, tell its old Site it has left, its new Site it has arrived, and itself that it has moved.
    public void move() {
    	Vector sites = find("free sites");
    	if(sites.size() != 0) {
    		int choice = gen.nextInt(sites.size());
    		Site nextSite = (Site)sites.elementAt(choice);
    		scape.grid[xPosition][yPosition].setAgent(null);
    		nextSite.setAgent(this);
    		xPosition = nextSite.getXPosition();
        	yPosition = nextSite.getYPosition();
    	}
    	this.hasMoved = true;
    }
        
    public int getID() {
    		return id;
    }
    
    public void setPosition(int x, int y) {
    		xPosition = x;
    		yPosition = y;
    }
    
    public int getXPosition() {
    		return xPosition;
    }
    
    public int getYPosition()	{
    		return yPosition;
    }  
    
    public String getStrategy() {
		return strategy;
	}

	public int getScore() {
    	return score;
    }
    
    public void addScore(int s) {
    	score += s;
    }
    
    public int getEncounters() {
		return encounters;
	}
    
    public int compareTo(Agent a) {
    	return ((Agent)a).getScore() - score;
    }
    
    
    public String getRanking() {
    	average = (double)score / (double)encounters;
    	return id + "\t" + strategy + "\t" + score + " / " + encounters + "  (" + scape.buttonPanel.round(average) + ") "; 
    }
}