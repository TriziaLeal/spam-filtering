import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.lang.Double;

public class SpamFilter{
	String fileName;
	File[] files;
	BagOfWords spam;
	BagOfWords ham;
	double totalSpamHamCount;
	double totalmessage;
	List<Double> result;
	Map<String, Double> filename_res;
	SpamFilter(String fileName,BagOfWords spam, BagOfWords ham){
		this.fileName = fileName;
		this.spam = spam;
		this.ham = ham;
		this.totalSpamHamCount = (double)this.spam.totalwords + (double)this.ham.totalwords;
		this.totalmessage = (double)this.spam.numofmsg + (double)this.ham.numofmsg;
		this.result = new ArrayList<Double>();
		this.filename_res = new HashMap<String, Double>();
	}
	void readFolder(){
			File folder = new File(this.fileName);
			this.files = folder.listFiles();
			checkEachMessage();	
	}
	void checkEachMessage(){
		System.out.println("checkEachMessage");
		String line;
		ArrayList<String> message;
		String[] arr;
		for (File f: this.files){
			line = "";

			message = new ArrayList<String>();
			BufferedReader br;
			try{
				String temp;
		 		br = new BufferedReader(new FileReader(f));
		 		while((temp = br.readLine()) != null ){
		 			line += temp + " ";
		 		}
		 		arr = cleanLine(line);
		 		for (String a: arr)
		 			message.add(a);

		 	System.out.println(f.getName());

	 		System.out.println();
			this.result.add(probabiltySgW(message));		
			this.filename_res.put(f.getName(),probabiltySgW(message));


			} catch (FileNotFoundException fileNotFound) {
	            System.err.println("Unable to find the file: fileName");

	        } catch (IOException ioExcept) {
	            System.err.println("Unable to read the file: fileName");
	        }

		}
	 	writeFile(this.files, this.result);
	}
	void writeFile(File[] file,List<Double> result){
		String line = "";
		String classify;

		for(int i = 0; i < file.length; i++){
			if (result.get(i) == -1)
				classify = "Cannot be processed";
			else if (result.get(i) >= 0.50)
				classify = "SPAM";
			else
				classify = "HAM";

			line += file[i].getName() + "\t" + classify + "\t" + result.get(i) + "\n";
		}
		try{    
        	FileWriter fw=new FileWriter("classify.out");    
        	fw.write(line);    
        	fw.close();    
        }catch(Exception e){System.out.println(e);}   
	}

	String classify(double result){
		if (result == -1)
			return "Cannot be processed";
		else if (result >= 0.50)
			return "SPAM";
		else
			return "HAM";		
	}
	public String[] cleanLine(String line){
		line = line.toLowerCase();
		line = line.replaceAll("[^A-Za-z0-9\\s]", "");
		String[] arr = line.trim().split("\\s+");
		return arr;
	}

	double probabiltySpam(){
		double spam = (double)this.spam.numofmsg / this.totalmessage;		
		return (spam);
	}

	double probabiltyHam(){
		double ham = (double)this.ham.numofmsg / this.totalmessage;		
		return (ham);
	}

	double probabiltyWgS(ArrayList<String> message,BagOfWords bag){
		String temp;
		double result = (double)(1);
		for (String m: message){
			if(bag.findWordCount(m) != -1){
				System.out.println(m + ": " + bag.findWordCount(m));
				System.out.println("result" + ": " + result);

				result = result * bag.findWordCount(m)/(double)(bag.totalfreq);
			}
		
		}

		return result;
	}


	double probabiltySgW(ArrayList<String> message){

		double num = probabiltyWgS(message,this.spam)*probabiltySpam();
		double ham = probabiltyWgS(message, this.ham)*probabiltyHam();
		double denom = (num+ham);
		if (num == 0 && denom == 0)
			return -1;
		double isSpam = num/denom;

		return isSpam;
	}

}