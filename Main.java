import java.util.Scanner;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class Main{
	static String spamFile;
	static String hamFile;
	static String messageFile;
	public static void main(String[] args) {
		JFrame frame = new JFrame("Main");
		frame.setPreferredSize(new Dimension(700,500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container con = frame.getContentPane();
		con.setLayout(new GridLayout(1,3));

		JPanel spamPanel = new JPanel();
		spamPanel.setLayout(new FlowLayout());
		JPanel hamPanel = new JPanel();
		hamPanel.setLayout(new FlowLayout());
		JPanel classifyPanel = new JPanel();
		classifyPanel.setLayout(new FlowLayout());

		spamPanel.setBackground(Color.GRAY);
		hamPanel.setBackground(Color.GRAY);
		classifyPanel.setBackground(Color.GRAY);
		con.add(spamPanel);
		con.add(hamPanel);
		con.add(classifyPanel);

		JButton spamButton = new JButton("Enter Spam Folder");
		JButton hamButton = new JButton("Enter Ham Folder");
		JButton messageButton = new JButton("Enter Classify Folder");
		JButton filterButton = new JButton("Filter");
		
		JLabel totalSpam = new JLabel("Total Words in Spam: ");
		JLabel totalSpamCount = new JLabel("0");
		JLabel totalHam = new JLabel("Total Words in Ham: ");
		JLabel totalHamCount = new JLabel("0");

		DefaultTableModel spammodel = new DefaultTableModel();
        spammodel.addColumn("Word");
        spammodel.addColumn("Frequency");

        JTable spamTable = new JTable(spammodel);
        spamTable.setPreferredScrollableViewportSize(new Dimension(200,350));


		DefaultTableModel hammodel = new DefaultTableModel();
        hammodel.addColumn("Word");
        hammodel.addColumn("Frequency");

        JTable hamTable = new JTable(hammodel);
        hamTable.setPreferredScrollableViewportSize(new Dimension(200,350));


   		spamPanel.add(spamButton);
   		spamPanel.add(totalSpam);
   		spamPanel.add(totalSpamCount);
   		spamPanel.add(new JScrollPane(spamTable), BorderLayout.CENTER);

   		hamPanel.add(hamButton);
   		hamPanel.add(totalHam);
   		hamPanel.add(totalHamCount);
   		hamPanel.add(new JScrollPane(hamTable), BorderLayout.CENTER);

		DefaultTableModel classmodel = new DefaultTableModel();
        classmodel.addColumn("Filename");
        classmodel.addColumn("Class");
        classmodel.addColumn("P(Spam)");
        JTable classTable = new JTable(classmodel);
        classTable.setPreferredScrollableViewportSize(new Dimension(200,350));


   		classifyPanel.add(messageButton);
   		classifyPanel.add(filterButton);
   		classifyPanel.add(new JScrollPane(classTable), BorderLayout.CENTER);


		spamButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser spamFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
 				spamFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int returnValue = spamFC.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedSpamFile = spamFC.getSelectedFile();
					spamFile = selectedSpamFile.getAbsolutePath();
					System.out.println(selectedSpamFile.getAbsolutePath());
					System.out.println(spamFile);

				}
			}
		});

		hamButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser hamFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
 				hamFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int returnValue = hamFC.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedHamFile = hamFC.getSelectedFile();
					hamFile = selectedHamFile.getAbsolutePath();
					System.out.println(selectedHamFile.getAbsolutePath());
					System.out.println(hamFile);

				}
			}
		});

		messageButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser messageFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
 				messageFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = messageFC.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedMessageFile = messageFC.getSelectedFile();
					messageFile = selectedMessageFile.getAbsolutePath();
					System.out.println(selectedMessageFile.getAbsolutePath());
					System.out.println(messageFile);
				}
			}
		});


		filterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("spamFile: " + spamFile);
				System.out.println("hamFile: " + hamFile);
				System.out.println("messageFile: " + messageFile);
				BagOfWords spam = new BagOfWords(spamFile);
				BagOfWords ham = new BagOfWords(hamFile);
				// BagOfWords message = new BagOfWords(messageFile);

				spam.readFile();
				spam.cleanLine();
				spam.createDictionary();
				spam.fileWriting("spam.out");

				ham.readFile();
				ham.cleanLine();
				ham.createDictionary();
				ham.fileWriting("ham.out");

				SpamFilter message = new SpamFilter(messageFile,spam,ham);
				message.readFolder();

				totalSpamCount.setText(Integer.toString(spam.totalfreq));
				totalHamCount.setText(Integer.toString(ham.totalfreq));
				
				for (Map.Entry<String, Integer> s : spam.bagOfWords.entrySet()){
					spammodel.addRow(new Object[]{s.getKey(), s.getValue()});
				}
				for (Map.Entry<String, Integer> h : ham.bagOfWords.entrySet()){
					hammodel.addRow(new Object[]{h.getKey(), h.getValue()});
				}

				for (Map.Entry<String, Double> c : message.filename_res.entrySet()){
					classmodel.addRow(new Object[]{c.getKey(), c.getValue(), message.classify(c.getValue())});
				}
			}

		});


		frame.setFocusable(true); 
		frame.pack();
		frame.setVisible(true);

	}
}