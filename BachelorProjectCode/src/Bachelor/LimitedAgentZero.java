package Bachelor;

import java.util.Random;

public class LimitedAgentZero {
	private double P_opp_D; 		//chance that opponent will defect next round
	private double P_opp_C; 		//chance that opponent will cooperate next round
	protected int[] memoryAgentActions = new int[100]; // index 0 is the most recent round
	protected int[] memoryOppActions = new int[100];
	private double discountFactor;
	private int steps; 		//how many steps into the future
	private int[][] policies;
	protected int move; 				//the action that will be taken in the next round
	private double agentGain;
	
	
	public LimitedAgentZero(int steps, double discountFactor, int[] memoryAgentActions, int[]  memoryOppActions) {
		super();
		this.memoryAgentActions = memoryAgentActions;
		this.memoryOppActions = memoryOppActions;
		setDiscountFactor(discountFactor);
		setSteps(steps);
		generatePolicies(steps);
		setAgentGain(0);
	}
	
	public void generatePolicies(int steps){
		int amount = (int)Math.pow(2, steps);
		this.policies = new int[amount][steps];
		int flip = amount;
		int action = 1;
		for(int i = 0; i<steps; i++){
			flip = flip/2;
			for(int j = 0; j<amount; j++){
				if(j%flip == 0){
					action = action *-1;
				}
				policies[j][i]=action;
				
			}
		}
//		for(int i = 0; i<amount; i++){
//			System.out.println(" COMBI: ");
//			for(int j = 0; j<steps; j++){
//				System.out.println(policies[i][j]);
//				
//			}
//		}
	}
	
	public int playGame() {
		if(memoryAgentActions[0] == 0){ //When there is no history the first move is random
			Random rand = new Random();
			int num = rand.nextInt(2)+1;
			if(num == 1){
				setMove(1);
			}else{
				setMove(-1);
			}
			return this.move;
		}
		int[] Policy = bestPolicy();
		this.move = Policy[0];
		return this.move;
	}
	
	//Find the best policy
	protected int[] bestPolicy() {
		double gain = 0;
		double max = 0;
		int PolicyNumber = 0;
		int[] HypoAgentActions = new int[20];
		int[] HypoOppActions = new int[20];
		for(int i = 0; i< HypoAgentActions.length; i++){
			HypoAgentActions[i] = memoryAgentActions[i];
			HypoOppActions[i] = memoryOppActions[i];
		}
		for(int i=0; i<policies.length; i++){ //max gain of all policies
			gain = CalculateGain(policies[i], 0, HypoAgentActions, HypoOppActions);
			if(gain > max){
				max = gain;
				PolicyNumber = i;
			}
		}
		int[] BestPolicy = policies[PolicyNumber];
	//	System.out.print("Policy: "); //PRINT STATEMENTS FOR POLICY
	//	for(int i=0; i < BestPolicy.length; i++){
	//		System.out.print(BestPolicy[i] + ", ");
	//	}
	//	System.out.print("\n");
		return BestPolicy; //policy with highest gain
	}

	//Calculate the gain of a policy
	private double CalculateGain(int[] policy, int index, int[] HypoAgentActions, int[] HypoOppActions){
		double result = 0;
		P_opp_C = CalculateChance(HypoAgentActions, HypoOppActions, index);
		P_opp_D = 1 - P_opp_C;

		if(index+1 == policy.length){
			result = P_opp_C * roundGain(policy[index], 1) + P_opp_D * roundGain(policy[index], -1); //base case
		}else{
			int[] NewHypoAgentActions =  new int[20];
			NewHypoAgentActions = CopyMemory(HypoAgentActions, NewHypoAgentActions, policy[index]);
			int[] C_HypoOppActions =  new int[20];
			C_HypoOppActions = CopyMemory(HypoOppActions, C_HypoOppActions, 1);
			int[] D_HypoOppActions =  new int[20];
			D_HypoOppActions = CopyMemory(HypoOppActions, D_HypoOppActions, -1);

			result = P_opp_C * roundGain(policy[index], 1) + getDiscountFactor()*CalculateGain(policy, index+1, NewHypoAgentActions, C_HypoOppActions) + 
					P_opp_D * roundGain(policy[index], -1) + getDiscountFactor()*CalculateGain(policy, index+1, NewHypoAgentActions, D_HypoOppActions);
		}
		return result;
	}
	
	//Calculate the chance an opponent will do a move
	private double CalculateChance(int[] HypoAgentActions, int[] HypoOppActions, int index){//should past be less important?
		//ZERO ORDER
		int counter = 0;
		int correct = 0;
		double chance = 0; //Chance opponent cooperated in this situation
		for(int i=0; i< HypoAgentActions.length; i++){
			if(HypoAgentActions[i] == 0){
				break;
			}
			if(i>0 && HypoAgentActions[i] == HypoAgentActions[0] && HypoOppActions[i] == HypoOppActions[0]){
				counter++;
				if(HypoOppActions[i-1] == 1){
					correct++;
				}
			}
		}
		if(counter == 0){
			chance = 0.5;
		} else{
			chance = (double)correct/counter;
		}
		
		return chance;
		
	}
	
	//Basically the prisoners dilemma matrix
	public int roundGain(int agentMove, int oppMove){
		int gain = 0;
		if(agentMove == 1 && oppMove == 1){
			gain = 100;
		} else if(agentMove ==1 && oppMove == -1){
			gain = 1;
		} else if(agentMove == -1 && oppMove == 1){
			gain = 101;
		} else {
			gain = 2;
		}
		return gain;	
	}
	
	//Process the results of the round just played
	public void processResult(int agentMove, int oppMove){
		int gain = roundGain(agentMove, oppMove); 
		setAgentGain(getAgentGain()+gain);

    	memoryAgentActions = UpdateMemory(memoryAgentActions, agentMove);
    	memoryOppActions = UpdateMemory(memoryOppActions, oppMove);
    	
	}
	
	private int[] UpdateMemory(int[] memory, int move){
		for(int i = memory.length - 2; i >= 0 ; i--){ //update memory agent
    		memory[i+1] = memory[i];
    	}
    	memory[0] = move;
    	return memory;
	}
	
	private int[] CopyMemory(int[] memory, int[] copy, int move){
		for(int i = memory.length - 2; i >= 0 ; i--){ //update memory agent
			copy[i+1] = memory[i];
		}
		copy[0] = move;
		return copy;
	}
	
	//GETTERS AND SETTERS
	public double getP_opp_D() {
		return P_opp_D;
	}
	public void setP_opp_D(double p_opp_D) {
		P_opp_D = p_opp_D;
	}
	public double getP_opp_C() {
		return P_opp_C;
	}
	public void setP_opp_C(double p_opp_C) {
		P_opp_C = p_opp_C;
	}

	private double getDiscountFactor() {
		return discountFactor;
	}

	private void setDiscountFactor(double discountFactor) {
		this.discountFactor = discountFactor;
	}

	public int getSteps() {
		return steps;
	}

	private void setSteps(int steps) {
		this.steps = steps;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public double getAgentGain() {
		return agentGain;
	}

	public void setAgentGain(double agentGain) {
		this.agentGain = agentGain;
	}
}
