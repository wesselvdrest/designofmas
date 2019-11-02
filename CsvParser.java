import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


//This class is just a parser of .CSV files
//it reads in all rows and columns of the file and for each agents it creates a new Agent using the Agent class.
public class CsvParser {
	public static ArrayList<Agent> agents = new ArrayList<Agent>();
	
    public static void main() {
        String fileName= "agents.csv";
        File file= new File(fileName);

        // this gives you a 2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(";");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }

            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // the following code lets you iterate through the 2-dimensional array
        int lineNo = 0;
        for(List<String> line: lines) {
            if (lineNo > 0) {
                agents.add(new Agent(line.get(0), Integer.parseInt(line.get(1)), line.get(2)));
            }
            lineNo++;
        }
    }
    
//    Returns an ArrayList of all agents
    public ArrayList<Agent> getAgents() {
    	return agents;
    }
    
}