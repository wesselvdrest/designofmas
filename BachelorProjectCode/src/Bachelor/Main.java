package Bachelor;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Main {
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException{
		int runs = 1000;
		int rounds = 100;
		int moveA;
		int moveB;
		int[] memoryAgentA = new int[100]; // index 0 is the most recent round
		int[] memoryAgentB = new int[100]; 
		int steps = 3;
		double discountFactor = 0.2;
		double learningSpeed = 0.6;
		double confidenceFirst = 0;
		double confidenceSecond = 0;
		double confidenceThird = 0;
		

		//PrintWriter writer = new PrintWriter(file_name, "UTF-8");
		PrintWriter writer = new PrintWriter(new FileWriter("ResultsHannaResults_3_0.txt", true));
		
		String content = "";
//		content = "Agent, Opp, Counter, Succes";
//		writer.println(content);
		
		for(int j = 0; j<runs; j++){
			System.out.println("run: " + j);
		//Some history to work with
		memoryAgentA[0] = -1;
		memoryAgentB[0] = -1;
		memoryAgentA[1] = -1;
		memoryAgentB[1] = -1;
		memoryAgentA[2] = -1;
		memoryAgentB[2] = -1;
		Random rand = new Random();
		for(int i =3; i<100; i++){
			int numA = rand.nextInt(2)+1;
			if(numA == 1){
				memoryAgentA[i] = 1;
			}else{
				memoryAgentA[i] = -1;
			}
			int numB = rand.nextInt(2)+1;
			if(numB == 1){
				memoryAgentB[i] = 1;
			}else{
				memoryAgentB[i] = -1;
			}
		}
//		Agent AgentA = new Agent(steps, discountFactor, learningSpeed, memoryAgentA, memoryAgentB, confidenceFirst, confidenceSecond, confidenceThird); //parameters: (thinking-steps, discountfactor, learning speed, memory agent, memory opponent, C1)
//		String orderA = "Third";
		Agent AgentB = new Agent(steps, discountFactor, learningSpeed, memoryAgentB, memoryAgentA, confidenceFirst, confidenceSecond, confidenceThird); // thinking-steps must be set 1 or higher.
		String orderB = "Third";
		
		LimitedAgentZero AgentA = new LimitedAgentZero(steps, discountFactor, memoryAgentA, memoryAgentB);
		String orderA = "Zero";
//		LimitedAgentZero AgentB = new LimitedAgentZero(steps, discountFactor, memoryAgentB, memoryAgentA); 
//		String orderB = "Zero";
		
//		LimitedAgentFirst AgentA = new LimitedAgentFirst(steps, discountFactor, learningSpeed, memoryAgentA, memoryAgentB, confidenceFirst);
//		String orderA = "First";
//		LimitedAgentFirst AgentB = new LimitedAgentFirst(steps, discountFactor, learningSpeed, memoryAgentB, memoryAgentA, confidenceFirst); 
//		String orderB = "First";
		
//		LimitedAgentSecond AgentA = new LimitedAgentSecond(steps, discountFactor, learningSpeed, memoryAgentA, memoryAgentB, confidenceFirst, confidenceSecond);
//		String orderA = "Second";
//		LimitedAgentSecond AgentB = new LimitedAgentSecond(steps, discountFactor, learningSpeed, memoryAgentB, memoryAgentA, confidenceFirst, confidenceSecond); 
//		String orderB = "Second";
		
		int counter = 0;
		int Succes = 0;
		
		
		for(int i=0; i<rounds; i++){
			moveA = AgentA.playGame();
			moveB = AgentB.playGame();
			AgentA.processResult(moveA, moveB);
			AgentB.processResult(moveB, moveA);
			//System.out.println("Agent A: " + moveA + "\tAgent B: " + moveB + "\n");
			//System.out.println("round: " + i);
			if(moveA == 1 ){
				counter++;
			}
			if(moveB == 1){
				counter++;
			}
			if(moveA ==1 && moveB ==1){
				Succes++;
			}
			
		}
		content = orderA + ", " + orderB + ", " + counter + ", " + Succes;
		writer.println(content);
		//System.out.println(counter);
		
	}
		writer.close();
	
	}
	
	
}

