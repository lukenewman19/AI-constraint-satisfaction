import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
/*
 * Class UI builds the Swing Frame to give the user an option between verbose or summary 
 */
public class UI implements ActionListener {
	
	private String verbose = "";
	private String summary = "";
	/*
	 * constructor for UI calls the local search program to run the search and obtain the output
	 * 	then builds the UI
	 */
	public UI(){
		ProgramC localSearch = new ProgramC();
		this.verbose = localSearch.getVerbose();
		this.summary = localSearch.getSummary();
		JFrame jfrm = new JFrame("Output Choice");
		jfrm.setSize(275,100);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.getContentPane().setLayout(new FlowLayout());
		JButton verboseMode = new JButton("Verbose");
		JButton summaryMode = new JButton("Summary");
		jfrm.getContentPane().add(verboseMode);
		jfrm.getContentPane().add(summaryMode);
		verboseMode.addActionListener(this);
		summaryMode.addActionListener(this);
		jfrm.setVisible(true);
	}
	/*
	 * method called upon button click
	 */
	public void actionPerformed(ActionEvent ae){
		// obtain the name of the component clicked
		String command = ae.getActionCommand();
		// Try-Catch block used for IOException
		try{
			// create a file to write to
			DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream("./localSearch.txt"));
			// switch statement to respond to the different actions
			switch (command){
				case "Verbose":
					dataOutput.writeBytes(verbose);
					System.out.println("Check for file ./localSearch.txt");
					System.out.println(summary);
					System.exit(0);
					break;
				case "Summary":
					dataOutput.writeBytes(summary);
					System.out.println("Check for file ./localSearch.txt");
					System.out.println(summary);
					System.exit(0);
					break;
				default: System.out.println("Invalid option!");
					System.exit(0);
			}
			dataOutput.close();
		} catch (IOException e){
				System.out.println(e.getMessage());
		}
	}
}