
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


class Main {
    public static void main(String args[]){
    	
	    String file_location = "C:/Users/DanielB/humanitiesProject/edipus.html";
	  
	    System.out.println("Starting main");

	    Converter a = new Converter(file_location);
	    
	    a.start();
	   
	    while(!a.isFinished()) {}
	    
	    System.out.println("Converter finished");
	    
	    XMLBuilder xmlBuilder = new XMLBuilder(a);
	    
	    //// GUI
	    
	    // Additional Settings
	    JFrame additional_settings = new JFrame("Additional Settings");
	    //additional_settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    JTextArea participants_area = new JTextArea(a.participantsToString());  
	    JTextArea dic_area = new JTextArea(xmlBuilder.dic.dicToString());  
	    JTextArea stage_area = new JTextArea(xmlBuilder.dic.stageToString());  
	    
	    //participants_area.setBounds(10, 10, 230, 60);
	    
	    JScrollPane participants_scroll = new JScrollPane();
	    JScrollPane dic_scroll = new JScrollPane();
	    JScrollPane stage_scroll = new JScrollPane();
	    
	    JLabel participants_label = new JLabel("Add more participants (format: name$job, when job is optional)");
	    JLabel dic_label = new JLabel("Dictionary: (format: word$translation)");
	    JLabel stage_label = new JLabel("Stage words (words like scene, strophe, act, etc...): (format: word$translation)");
	    
	    participants_label.setBounds(10, 10, 600, 15);
	    participants_scroll.setBounds(10, 30, 550 ,60);
	    participants_scroll.setViewportView(participants_area);
	    
	    dic_label.setBounds(10, 95, 600, 15);
	    dic_scroll.setBounds(10, 115, 550 ,60);
	    dic_scroll.setViewportView(dic_area);
	    
	    stage_label.setBounds(10, 180, 600, 15);
	    stage_scroll.setBounds(10, 200, 550 ,60);
	    stage_scroll.setViewportView(stage_area);
	    
	    JButton additional_submit = new JButton("Submit");
	    additional_submit.setBounds(10, 265, 100, 30);
	    
	    additional_submit.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		    a.participantsFromString(participants_area.getText().trim());
    	            xmlBuilder.setParticipants(a.getParticipants());
    	            xmlBuilder.setSpeeches(a.getSpeeches());
    	            xmlBuilder.dic.dicFromString(dic_area.getText().trim());
    	            xmlBuilder.dic.stageFromString(stage_area.getText().trim());
	    	  } 
	    });
	    
	    additional_settings.add(participants_scroll);
	    additional_settings.add(participants_label);
	    additional_settings.add(dic_scroll);
	    additional_settings.add(dic_label);
	    additional_settings.add(stage_scroll);
	    additional_settings.add(stage_label);
	    additional_settings.add(additional_submit);
	    
	    additional_settings.setSize(600, 600);
	    additional_settings.setLayout(null);

	    
	    
	    // Main GUI
	    JFrame frame = new JFrame("Drama TEI Converter");
	    Color button_color = new Color(147, 225, 209);
	    JLabel frame_headline = new JLabel("<html><span style='font-size:13px'>"+"Chosen file: " 
	    										+"<span style='color:#348964;'><i>"+file_location+"</i></span>"
	    										+"</span></html>");
	    frame_headline.setBounds(20, 70, 700, 100);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    
	    JLabel logo = new JLabel("<html><span style='font-size:35px; color:#348964'>"
	    		+ "Drama TEI Converter"
				+"</span></html>");
	    logo.setBounds(20, -10, 700, 100);
	    JLabel sublogo = new JLabel("<html><span style='font-size:15px'>"
	    		+ "A Digital Humanities Project"
				+"</span></html>");
	    sublogo.setBounds(30, 55, 700, 30);
	    

	    JButton show_person_list = new JButton("Show person list (xml)");
	    show_person_list.setBounds(540, 30, 140, 40);
	    show_person_list.setBackground(button_color);
	    
	    JButton show_plain_text = new JButton("Show plain text");
	    show_plain_text.setBounds(540, 80, 140, 40);
	    show_plain_text.setBackground(button_color);
	    
	    JButton show_raw_speeches = new JButton("Raw speeches");
	    show_raw_speeches.setBounds(685, 80, 140, 40);
	    show_raw_speeches.setBackground(button_color);
	    
	    JButton chooseFile = new JButton("Choose file");
	    chooseFile.setBounds(685, 30, 140, 40);
	    chooseFile.setBackground(button_color);
	    
	    JButton openAdditionalSettings = new JButton("Add. Settings");
	    openAdditionalSettings.setBounds(830, 30, 140, 40);
	    openAdditionalSettings.setBackground(button_color);
	    
	    JButton text_attribute = new JButton("Text Attr.");
	    text_attribute.setBounds(830, 80, 140, 40);
	    text_attribute.setBackground(button_color);
	    
	    JLabel p1 = new JLabel("Paticipant regex:");
	    p1.setBounds(540, 130, 300, 30);
	    
	    JTextField t1 = new JTextField(a.PARTICIPANT_REGEX);
	    t1.setBounds(540, 155, 230, 20);
	    
	    JLabel p2 = new JLabel("Paticipant job:");
	    p2.setBounds(540, 175, 300, 30);
	    
	    JTextField t2 = new JTextField(a.PARTICIPANT_JOB);
	    t2.setBounds(540, 200, 230, 20);
	    
	    JLabel p3 = new JLabel("Paticipant head line:");
	    p3.setBounds(540, 220, 300, 30);
	    
	    JTextField t3 = new JTextField(a.PARTICIPANT_HEADLINE);
	    t3.setBounds(540, 245, 230, 20);
	    
	    JLabel p4 = new JLabel("Paticipant end line:");
	    p4.setBounds(540, 265, 300, 30);
	    
	    JTextField t4 = new JTextField(a.PARTICIPANT_ENDLINE);
	    t4.setBounds(540, 290, 230, 20);
	    
	    JLabel p5 = new JLabel("Last line in play:");
	    p5.setBounds(540, 310, 300, 30);
	    
	    JTextField t5 = new JTextField(a.LAST_LINE_IN_PLAY);
	    t5.setBounds(540, 335, 230, 20);
	    
	    
	    JLabel p6 = new JLabel("Speeker wrap: ");
	    p6.setBounds(540, 355, 300, 30);
	    
	    JTextField t6 = new JTextField(a.SPEEKER_P);
	    t6.setBounds(540, 380, 230, 20);
	    
	    JLabel p7 = new JLabel("Speech line start: ");
	    p7.setBounds(540, 400, 300, 30);
	    
	    JTextField t7 = new JTextField(a.BLOCKQUOTE);
	    t7.setBounds(540, 425, 230, 20);
	    
	    JLabel p8 = new JLabel("Speech line end: ");
	    p8.setBounds(540, 445, 300, 30);
	    
	    JTextField t8 = new JTextField(a.END_BLOCKQUOTE);
	    t8.setBounds(540, 470, 230, 20);
	    
	    JLabel p9 = new JLabel("Split line: ");
	    p9.setBounds(540, 490, 300, 30);
	    
	    JTextField t9 = new JTextField(a.SPLIT_LINE.replace("\\", "\\\\"));
	    t9.setBounds(540, 515, 230, 20);
	    
	    JLabel p10 = new JLabel("Square brackets: ");
	    p10.setBounds(540, 535, 300, 30);
	    
	    JTextField t10 = new JTextField(a.SQUARE_BRACKETS);
	    t10.setBounds(540, 560, 230, 20);
	    
	    JLabel p11 = new JLabel("Start from line:");
	    p11.setBounds(790, 130, 300, 30);
	    
	    JTextField t11 = new JTextField(String.valueOf(a.START_LINE));
	    t11.setBounds(790, 155, 100, 20);
	    
	    JButton submit_button = new JButton("SUBMIT");
	    submit_button.setBounds(540, 585, 230, 30);
	    
	    JButton exportXML = new JButton("Export XML");
	    exportXML.setBounds(775, 560, 140, 55);
	    exportXML.setBackground(new Color(147, 225, 209));
	    
	    JTextArea area = new JTextArea();  
	    JScrollPane scroll = new JScrollPane();
        
	    area.setLineWrap(false);
	    area.setBackground(new Color(236, 253, 250));
	    //frame.getContentPane().add(button);
	    //frame.getContentPane().add(label);
	    
	    area.setBounds(20,30,500,640);  
	    scroll.setBounds(18,130,520,485);
	    
	    scroll.setViewportView(area);
	    
	    //frame.add(area);
	    
	    show_person_list.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		    area.setText(xmlBuilder.getListPerson(0));
	    	  } 
	    });
	    
	    chooseFile.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    	        JFileChooser chooser = new JFileChooser();
	    	        int returnVal = chooser.showOpenDialog(frame);
	    	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	        	if(!chooser.getSelectedFile().getName().toLowerCase().endsWith(".html")) {
	    	        		area.setText("***********\nOnly .html files are accepted\n***********");
	    	        		return;
	    	        	}
	    	            System.out.println("You chose to open this file: " +
	    	                    chooser.getSelectedFile().getName());
	    	            a.setFileLocation(chooser.getSelectedFile().getAbsolutePath());
	    	            a.start();
	    	            while(!a.isFinished()) {}
	    	            frame_headline.setText("<html><span style='font-size:13px'>"+"Chosen file: " 
								+"<span style='color:#348964;'><i>"+a.getFileLocation()+"</i></span>"
								+"</span></html>");
	    	            xmlBuilder.setParticipants(null);
	    	            xmlBuilder.setSpeeches(null);
	    	            area.setText("***********\nA new file has been choosen.\nPlease fill all the settings,\n"
	    	            		+ "Then press \"Submit\"\n***********");
	    	            t1.setText("");
	    	            t2.setText("");
	    	            t3.setText("");
	    	            t4.setText("");
	    	            t5.setText("");
	    	            t6.setText("");
	    	            t7.setText("");
	    	            t8.setText("");
	    	            t9.setText("");
	    	            t10.setText("");
	    	            t11.setText("");  
	    	        }
	    	  } 
	    });

	    show_plain_text.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		  /*area.setText("Loading, it may take a while");
	    		  Thread t = new Thread(() -> area.setText(a.getFileContent()));
	    		  t.start();*/
	    		  try {
	    			File file = new File(a.getFileLocation());
					java.awt.Desktop.getDesktop().open(file);
				  } catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				  }
	    	  } 
	    });
	    
	    show_raw_speeches.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		  area.setText("Loading, it may take a while");
	    		  Thread t = new Thread(() -> area.setText(xmlBuilder.showRawSpeeches()));
	    		  t.start();
	    	  } 
	    });
	    
	    openAdditionalSettings.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		  additional_settings.setVisible(true);
	    	  } 
	    });
	    
	    text_attribute.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		  area.setText("Loading, it may take a while");
	    		  Thread t = new Thread(() -> area.setText(xmlBuilder.fullXML()));
	    		  t.start();
	    	  } 
	    });
	    
	    submit_button.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) { 
	    		    a.PARTICIPANT_REGEX = t1.getText().trim();
	    		    a.PARTICIPANT_JOB = t2.getText().trim();
	    		    a.PARTICIPANT_HEADLINE = t3.getText().trim();
	    		    a.PARTICIPANT_ENDLINE = t4.getText().trim();
	    		    a.LAST_LINE_IN_PLAY = t5.getText().trim();
	    		    a.SPEEKER_P = t6.getText().trim();
	    		    a.BLOCKQUOTE = t7.getText().trim();
	    		    a.END_BLOCKQUOTE = t8.getText().trim();
	    		    a.SPLIT_LINE = t9.getText().trim();
	    		    a.SQUARE_BRACKETS = t10.getText().trim();
	    		    a.START_LINE = Integer.valueOf(t11.getText().trim());
	    		    System.out.println("Setting submitted.");
	    		    xmlBuilder.setParticipants(a.getParticipants());
	    		    xmlBuilder.setSpeeches(a.getSpeeches());
	    		    area.setText("***********\nSettings submitted.\n***********");
	    	  } 
	    });
	    
	    exportXML.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) {
	    		  	String[] fileName = a.getFileLocation().split("\\.");
	    		  	String newFileName = fileName[0] + ".xml";
	    		    Path path = Paths.get(newFileName);
	    		    byte[] strToBytes = xmlBuilder.fullXML().getBytes();
	    		    try {
						Files.write(path, strToBytes);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	  } 
	    });
	    
        //frame.add(chooser);
        
	    frame.add(show_person_list);
	    frame.add(show_plain_text);
	    frame.add(show_raw_speeches);
	    frame.add(chooseFile);
	    frame.add(openAdditionalSettings);
	    frame.add(text_attribute);
	    frame.add(scroll);
	    frame.add(frame_headline);
	    frame.add(logo);
	    frame.add(sublogo);
	    frame.add(p1);
	    frame.add(t1);
	    frame.add(p2);
	    frame.add(t2);
	    frame.add(p3);
	    frame.add(t3);
	    frame.add(p4);
	    frame.add(t4);
	    frame.add(p5);
	    frame.add(t5);
	    frame.add(p6);
	    frame.add(t6);
	    frame.add(p7);
	    frame.add(t7);
	    frame.add(p8);
	    frame.add(t8);
	    frame.add(p9);
	    frame.add(t9);
	    frame.add(p10);
	    frame.add(t10);
	    frame.add(p11);
	    frame.add(t11);
	    
	    frame.add(exportXML);
	    frame.add(submit_button);
	   
	    
	    frame.setSize(1000, 680);
	    frame.setLayout(null);

	    frame.getContentPane().setBackground(new Color(243, 243, 243));
	    frame.setVisible(true);
	    
	    //a.createParticipants();

	    PlayRelations relations = new PlayRelations();
	    relations.relationMatrix(a);
	    
	    
    }
}