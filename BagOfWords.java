// import java.io.File;
// import java.io.FileReader;
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;

import java.util.*;
import java.io.*;


public class BagOfWords{
	Map<String, Integer> bagOfWords;
	int totalfreq;
	int totalwords;
	int numofmsg;

	String fileName;
	String line;


	public BagOfWords(String fileName){
		bagOfWords = new HashMap<String, Integer>();

		this.fileName = fileName;
		this.line = "";	
	}

	public void readFile(){
			File folder = new File(this.fileName);
			File[] files = folder.listFiles();
			this.numofmsg = files.length;
			System.out.println(this.fileName);
			//reads all files in the directory
			for(File f: files){
				BufferedReader br;
				try{
					String temp;
			 		br = new BufferedReader(new FileReader(f));
			 		while((temp = br.readLine()) != null ){
			 			this.line += temp + " ";
			 		}
				} catch (FileNotFoundException fileNotFound) {
		            System.err.println("Unable to find the file: fileName");
		        } catch (IOException ioExcept) {
		            System.err.println("Unable to read the file: fileName");
		        }
			}
	}

	public void cleanLine(){
		this.line = this.line.toLowerCase();
		this.line = this.line.replaceAll("[^A-Za-z0-9\\s]", "");
	}

	public void createDictionary(){
		String[] arr = this.line.trim().split("\\s+");
		for ( String ss : arr) {
			//if key does not exists, adds new word to dictionary with value of 1 
			if (this.bagOfWords.get(ss) == null){
				this.bagOfWords.put(ss,1);
				// System.out.println("first found");
			}
			//else, increments 1 to the value of the founded key
  			else{
  				this.bagOfWords.put(ss,1+this.bagOfWords.get(ss));
  				// System.out.println(this.bagOfWords.get(ss));
  			}
  		}
  		//after creating dictionary, counts total number of words and total frequencies of all words
  		computeTotal();
	}

	public void computeTotal(){
  			this.totalwords = this.bagOfWords.size();
			for (Map.Entry<String, Integer> entry : this.bagOfWords.entrySet()){
			this.totalfreq += entry.getValue();
		}
	}
	
	public void fileWriting(String outputFileName){
		String line = new String();
		// System.out.println(this.bagOfWords.entrySet());
		//writes total words and total frequency in file
		line += "Total Words: " + this.totalwords + "\t Total Frequency: " + this.totalfreq + "\n";

		//concats values and keyys to line
		for (Map.Entry<String, Integer> entry : this.bagOfWords.entrySet()){
	    	line += "word: " + entry.getKey() + "\tfreq: " + entry.getValue() + "\n";		
		}

		//creates file
		try{    
        	FileWriter fw=new FileWriter(outputFileName);    
        	fw.write(line);    
        	fw.close();    
        }catch(Exception e){System.out.println(e);}    

	}

	double findWordCount(String word){
		if (this.bagOfWords.get(word) != null){
			// System.out.println(this.bagOfWords.get(word));
			return (double)this.bagOfWords.get(word);
		}
		return 0;
	}

}