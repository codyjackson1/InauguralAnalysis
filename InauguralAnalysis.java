import java.util.*;
import com.opencsv.*;
import java.io.*;

//Inaugural Analysis
//Looks at unique words in each speech and calculates the amount of same words with all other Inaugural speeches
//Utilized opencsv library: opencsv.sourceforge.net
public class InauguralAnalysis {
	ArrayList<String> x = new ArrayList<String>();
	ArrayList<String> y = new ArrayList<String>();
	String file;
	String name;
	String year;
	public InauguralAnalysis(String file, String name, String year) {
		this.name = name;
		this.year = year;
		this.file = file;
	}
	public void uniqueWords(String file2, String name2, String year2) throws FileNotFoundException {
		//Two scanners to read both inputed files at the same time
		Scanner input1 = new Scanner(new File(this.file));
		Scanner input2 = new Scanner(new File(file2));
		//CSVReader reader = new CSVReader(new FileReader("metadata.csv"));
		
		//Cycles through both inputed files and counts unique words
		while(input1.hasNext() || input2.hasNext() ) {
			if (input1.hasNext() ) {
				String s = input1.next();
				s = s.toLowerCase();
				s = s.replaceAll("\\.", "");
				s = s.replaceAll("\\:", "");
				s = s.replaceAll("\\;", "");
				s = s.replaceAll("\\,", "");
				s = s.replaceAll("\\(", "");
				s = s.replaceAll("\\)", "");
				if( !x.contains(s) ){
					this.x.add(s);
				}
				
			}
			if (input2.hasNext() ) {
				String s = input2.next();
				s = s.toLowerCase();
				s = s.replaceAll("\\.", "");
				s = s.replaceAll("\\:", "");
				s = s.replaceAll("\\;", "");
				s = s.replaceAll("\\,", "");
				s = s.replaceAll("\\(", "");
				s = s.replaceAll("\\)", "");
				if(!y.contains(s) ){
					this.y.add(s);
				}
			}
			
		}
		//System.out.println(String.format("%s (%s): %d unique words", this.name, this.year, x.size()));
		//System.out.println(String.format("%s (%s): %d unique words", name2, year2, x.size()));
	}
	
	//compares two ArrayList and counts overlapping words
	public String overlap(String name2, String year2) {
		int matches = 0;
		for(int i = 0; i < x.size(); i++) {
			for(int j = 0; j < y.size(); j++) {
				if( (x.get(i)).equals(y.get(j)) ) {
					matches++;
				}
			}
		}
		//System.out.println(String.format("%s (%s) and %s (%s) have %d unique words in common.", this.name, this.year, name2, year2, matches));
		String result = String.valueOf(matches);
		return result;
	}

		

	public static void main(String[] args) throws IOException {
		String[][] array = new String[58][58];
		int row = 0;
		int column = 0;
		//library/bits of code from opencsv.sourceforge.net
		//create two readers to compare to each other
		CSVReader reader = new CSVReader(new FileReader("metadata.csv"),',' ,'\'' , 1);
		CSVReader reader2 = new CSVReader(new FileReader("metadata.csv"),',' ,'\'' , 1);
		String [] nextLine;
		String [] nextLine2;
		//writer that will write to a csv file
		CSVWriter writer = new CSVWriter(new FileWriter("InauguralAnalysis.csv"));
		String[] names = new String[58];
		
		//Loops through both readers inserting the compared speech data into a 2d array
		while ((nextLine = reader.readNext()) != null) {

	        while((nextLine2 = reader2.readNext()) != null) {
	        	names[row] = nextLine[1] + " (" + nextLine[2] + ")";
		        String file2 = nextLine2[0];
		        String name2 = nextLine2[1];
		        String year2 = nextLine2[2];
		        //create the InauguralAnalysis object with the first reader
		        InauguralAnalysis first = new InauguralAnalysis(nextLine[0], nextLine[1], nextLine[2]);
		        //call the method using second reader
		        //returns the number of unique words between first and (file2, name2, year2).
		        first.uniqueWords(file2, name2, year2);
		        if(row == column) {
		        	array[row][column] = "0";
		        	column++;
		        } else {
		        	array[row][column] = first.overlap(name2, year2);
		        	column++;
		        }
	        }
	        //reset the second reader to continue the loop
	        reader2 = new CSVReader(new FileReader("metadata.csv"),',' ,'\'' , 1);
	        //reset positioning for the elements
	        column = 0;
	        //go to next row
	        row++;
	        
	     }
		//writes the 2d array to a csv file
		
		//writer.writeNext(names);
		for(int i = 0; i < row; i++) {
			writer.writeNext( array[i] );
		}
		
		writer.close();
	}

}
