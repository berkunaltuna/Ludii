package app.display.dialogs.QnA_Editor;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import org.apache.batik.gvt.Marker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import app.PlayerApp;
import app.DesktopApp;
import app.utils.GameSetup;


public class QnAs extends JPanel implements ActionListener {
    // Declare components
    /* 
    JLabel questionLabel;
    JTextArea gameName;
    
    JButton compileButton;
    JLabel questionLabel1;
    JComboBox<String> answerDropdown1;
    JButton submitButton1;
    //private PlayerApp app;
*/
    private JComboBox<String> answerDropdown , answerDropdown1, answerDropdown2 , answerDropdown3 , answerDropdown4 , answerDropdown5, answerDropdown6, answerDropdown7, answerDropdown8;   
    private JButton compileButton = new JButton("Compile");
    private JTextArea questionArea;
    private String currentQuestion;
    private JTextField gameName;
    private String desc = "(";
    private String piece = "";
    private boolean byAdding = false;
    private boolean playClauseAdded = false;
    //private Font f = new Font(36);

    
    // Constructor
    public QnAs() {
        // Add components
        //questionLabel = new JLabel("What is the name of your game");
        //gameName = new JTextArea(null, "Enter Game name", 1, 1);
        //add(questionLabel);
        //add(gameName);  
        
        setLayout(new BorderLayout());
         
        questionArea = new JTextArea("What is the name of your game?", 15, 15);
        questionArea.setLineWrap(false);
        questionArea.setWrapStyleWord(false);
        questionArea.setEditable(false);
        questionArea.setFont(new Font("Monaco", Font.PLAIN ,36));
        questionArea.setAlignmentX(StyleConstants.ALIGN_CENTER);
        questionArea.setAlignmentY(StyleConstants.ALIGN_CENTER);
        add(questionArea,BorderLayout.CENTER); // is there a way to center text?
        //add(Box.createVerticalStrut(10));
        gameName = new JTextField();
        gameName.setColumns(25);
        gameName.addActionListener(this);
        //gameName.setSize(46, 25);
        //gameName.setLocation(120, 240);
        add(gameName, BorderLayout.SOUTH);

        //add(compileButton,BorderLayout.SOUTH);
        //compileButton.setVisible(false);
        compileButton.addActionListener(this);
        
        currentQuestion = "What is the name of your game?"; // Q1
        
        answerDropdown = new JComboBox<String>(new String[] {"Select...", "2"}); // Q2
        answerDropdown.addActionListener(this);

        answerDropdown1 = new JComboBox<String>(new String[] {"Select...", "Square 3", "Square 4", "Square 5", "Square 6", "Square 9", "(Concentric 1 8)", "(hex 5)", "(hex Triangle 11)" }); // Q3
        answerDropdown1.addActionListener(this);

        answerDropdown2 = new JComboBox<String>(new String[] {"Select...", "Marker", "Queen"}); // Q4
        answerDropdown2.addActionListener(this);

        answerDropdown3 = new JComboBox<String>(new String[] {"Select...", "By moving", "By adding"}); // Q5
        answerDropdown3.addActionListener(this);

        answerDropdown4 = new JComboBox<String>(new String[] {"Select...", "Step to empty place", "Like chess queen"}); // Q6
        answerDropdown4.addActionListener(this);

        answerDropdown5 = new JComboBox<String>(new String[] {"Select...", "Yes", "No"}); // Q7
        answerDropdown5.addActionListener(this);

        answerDropdown6 = new JComboBox<String>(new String[] {"Select...", "(place \"Queen1\"   {\"A1\" \"C1\" \"E1\" \"A3\" \"B5\" \"D5\"})", "(place \"Marker1\" (sites Bottom))", "(place \"Marker1\" { 1 8 7})" }); // Q8
        answerDropdown6.addActionListener(this);
        
        answerDropdown7 = new JComboBox<String>(new String[] {"Select...", "(place \"Queen2\"  {\"A5\" \"B1\" \"C5\" \"D1\" \"E3\" \"E5\"})", "(place \"Marker2\" (sites Top))","(place \"Marker2\" { 3 4 5 })"}); // Q9
        answerDropdown7.addActionListener(this);

        answerDropdown8 = new JComboBox<String>(new String[] {"Select...", "Win by filling opponent's initial sites", "Win when pieces are 3 in line.", "Win when pieces are 4 in line.", "Win when pieces are 6 in line.", "Win when pieces are 3 in line and 1 piece is at the center.", "Win when 4 in line, but lose when 3 in line", "Win when 3 sides are connected."}); // Q9
        answerDropdown8.addActionListener(this);
        
        
        /*
        questionArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(questionArea);
        add(scrollPane, BorderLayout.CENTER);

        gameName = new JTextField();
        scrollPane.add(gameName);
        */
        //yesButton = new JButton("Yes");
        //yesButton.addActionListener(this);
        //add(yesButton, BorderLayout.NORTH);
        //noButton = new JButton("No");
        //noButton.addActionListener(this);
        //add(noButton, BorderLayout.CENTER);
        


        //answerDropdown = new JComboBox<String>(new String[] {"Select...", "Yes", "No"});
        //compileButton = new JButton("Compile");
        //add(compileButton);
        //compileButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        String answer = e.getActionCommand(); // Yes no Button input
        String name = gameName.getText();
        String dropdownAnswer = (String) answerDropdown.getSelectedItem();
        String dropdownAnswer1 = (String) answerDropdown1.getSelectedItem();
        String dropdownAnswer2 = (String) answerDropdown2.getSelectedItem();
        String dropdownAnswer3 = (String) answerDropdown3.getSelectedItem();
        String dropdownAnswer4 = (String) answerDropdown4.getSelectedItem();
        String dropdownAnswer5 = (String) answerDropdown5.getSelectedItem();
        String dropdownAnswer6 = (String) answerDropdown6.getSelectedItem();
        String dropdownAnswer7 = (String) answerDropdown7.getSelectedItem();
        String dropdownAnswer8 = (String) answerDropdown8.getSelectedItem();


        // update the question based on the answer
        if (currentQuestion.equals("What is the name of your game?")) { // Question 1.
            desc += "game \"" + name + "\"";
            System.out.println(desc);
            currentQuestion = "By how many players is the game played?";
            remove(gameName); //Removing the textfield and adding a dropdown place and the repaint and revalidate for the UI to refresh.
            add(answerDropdown, BorderLayout.SOUTH);
            repaint();
            revalidate();
        } else if (currentQuestion.equals("By how many players is the game played?")) { // Question 2.
            if (dropdownAnswer.equals("2")) {
                currentQuestion = "What type and size do you want your board to be?"; // Set next Question
                remove(answerDropdown);
                add(answerDropdown1, BorderLayout.SOUTH);
                repaint();
                revalidate();
                desc += " (players 2)";
                System.out.println(desc);
            } else {
                //error
            } 
        } else if (currentQuestion.equals("What type and size do you want your board to be?")) { // Question 3.
            if (dropdownAnswer1.equals("Square 3")) {
                desc += " (equipment  { (board (square 3))";
                System.out.println(desc);    
            } else if (dropdownAnswer1.equals("Square 4")) {
                desc += " (equipment  { (board (square 4))";
                System.out.println(desc);
            } else if (dropdownAnswer1.equals("Square 5")) {
                desc += " (equipment  { (board (square 5))";
                System.out.println(desc);
            } else if (dropdownAnswer1.equals("Square 6")) {
                desc += " (equipment  { (board (square 6))";
                System.out.println(desc);
            } else if (dropdownAnswer1.equals("Square 9")) {
                desc += " (equipment  { (board (square 9))";
                System.out.println(desc);
            } else if (dropdownAnswer1.equals("(Concentric 1 8)")) {
                desc += " (equipment  {  (board (concentric {1 8}) use:Vertex)";
                System.out.println(desc);
            } else if (dropdownAnswer1.equals("(hex 5)")) {
                desc += " (equipment  { (board (hex 5))";
                System.out.println(desc);
            } else if (dropdownAnswer1.equals("(hex Triangle 11)")) {
                desc += " (equipment  {  (board (hex Triangle 11))";
                System.out.println(desc);
            }
                currentQuestion = "What is the piece used?";
                remove(answerDropdown1);
                add(answerDropdown2, BorderLayout.SOUTH);
                repaint();
                revalidate();
        } else if (currentQuestion.equals("What is the piece used?")) { // Question 4.
            if (dropdownAnswer2.equals("Marker")) {
                piece = "Marker";
                System.out.println(piece);
            } else if (dropdownAnswer2.equals("Queen")) {
                piece = "Queen";
                System.out.println(piece);
            }
            currentQuestion = "Is the game played by adding or moving pieces?";
                remove(answerDropdown2);
                add(answerDropdown3, BorderLayout.SOUTH);
                repaint();
                revalidate();
        } else if (currentQuestion.equals("Is the game played by adding or moving pieces?")) { // Question 5.
            if (dropdownAnswer3.equals("By moving")) {
                byAdding = false;
                System.out.println(byAdding);
            } else if (dropdownAnswer3.equals("By adding")) {
                byAdding = true;
                desc += " (piece \"Marker\" Each) })";
                System.out.println(byAdding);
                System.out.println(desc);
            }
            currentQuestion = "How do pieces move?"; 
                remove(answerDropdown3);
                add(answerDropdown4, BorderLayout.SOUTH);
                repaint();
                revalidate();
        } else if (currentQuestion.equals("How do pieces move?")) { // Question 6.
            if (dropdownAnswer4.equals("Step to empty place")) {
                if (piece.equals("Marker")) {
                    if (byAdding == false) {
                        desc += " (piece \"Marker\" Each \"StepToEmpty\") } )";
                        System.out.println(desc);
                    }
                }
                System.out.println(piece);
            } else if (dropdownAnswer4.equals("Like chess queen")) {
                if (piece.equals("Queen")) {
                    desc += " (piece \"Queen\" Each (move Slide)) } )";
                    System.out.println(desc);
                }
            }
            currentQuestion = "Is there an initial placement?";
                remove(answerDropdown4);
                add(answerDropdown5, BorderLayout.SOUTH);
                repaint();
                revalidate();
        } else if (currentQuestion.equals("Is there an initial placement?")) { // Question 7.
            if (dropdownAnswer5.equals("Yes")) {
                currentQuestion = "Where do you want to place the pieces of the first player?"; // Question 8.
                remove(answerDropdown5);
                add(answerDropdown6, BorderLayout.SOUTH);
                repaint();
                revalidate();
            } else if (dropdownAnswer5.equals("No")) { // step 10
                if (byAdding == true) {
                    desc += " (rules (play (move Add (to (sites Empty))))";
                }
                else {
                    desc += " (rules (play (forEach Piece))";
                    playClauseAdded = true;
                }
                currentQuestion = "What is the end condition and winning/losing side?";
                remove(answerDropdown5);
                add(answerDropdown8, BorderLayout.SOUTH); // step 11
                repaint();
                revalidate();
            }        
        } else if (currentQuestion.equals("Where do you want to place the pieces of the first player?")) { // Question 8. 
            if (dropdownAnswer6.equals("(place \"Queen1\"   {\"A1\" \"C1\" \"E1\" \"A3\" \"B5\" \"D5\"})")) {
                desc += " (rules (start { (place \"Queen1\"   {\"A1\" \"C1\" \"E1\" \"A3\" \"B5\" \"D5\"})";
                System.out.println(desc);
            } else if (dropdownAnswer6.equals("(place \"Marker1\" (sites Bottom))")) {
                desc += " (rules (start { (place \"Marker1\" (sites Bottom))";
                System.out.println(desc);
            } else if (dropdownAnswer6.equals("(place \"Marker1\" { 1 8 7})")) {
                desc += " (rules (start { (place \"Marker1\" { 1 8 7})";
                System.out.println(desc);
            }
            currentQuestion = "Where do you want to place the pieces of the second player?"; // Question 9.
            remove(answerDropdown6);
            add(answerDropdown7, BorderLayout.SOUTH);
            repaint();
            revalidate();
        } else if (currentQuestion.equals("Where do you want to place the pieces of the second player?")) { // Question 9. 
            if (dropdownAnswer7.equals("(place \"Queen2\"  {\"A5\" \"B1\" \"C5\" \"D1\" \"E3\" \"E5\"})")) {
                desc += " (place \"Queen2\"  {\"A5\" \"B1\" \"C5\" \"D1\" \"E3\" \"E5\"}) })"; // This instance is different from the above. (rules (start is missing here.
                System.out.println(desc);
            } else if (dropdownAnswer7.equals("(place \"Marker2\" (sites Top))")) {
                desc += " (place \"Marker2\" (sites Top)) })"; // This instance is different from the above. (rules (start is missing here.
                System.out.println(desc);
            } else if (dropdownAnswer7.equals("(place \"Marker2\" { 3 4 5 })")) {
                desc += " (place \"Marker2\" { 3 4 5 }) })"; // This instance is different from the above. (rules (start is missing here.
                System.out.println(desc);
            }
            if (playClauseAdded == false) {
                desc += " (play (forEach Piece))";
            }
            currentQuestion = "What is the end condition and winning/losing side?"; // Question 11.
            remove(answerDropdown7);
            add(answerDropdown8, BorderLayout.SOUTH);
            repaint();
            revalidate();
        } else if (currentQuestion.equals("What is the end condition and winning/losing side?")) { // Question 11. 
            if (dropdownAnswer8.equals("Win by filling opponent's initial sites")) {
                desc += " (end (\"FillWin\" (sites Next)))))";  
                System.out.println(desc);
            } else if (dropdownAnswer8.equals("Win when pieces are 3 in line.")) {
                desc += " (end (if (is Line 3) (result Mover Win)))))";  
                System.out.println(desc);
            } else if (dropdownAnswer8.equals("Win when pieces are 4 in line.")) {
                desc += " (end (if (is Line 4) (result Mover Win)))))";  
                System.out.println(desc);
            } else if (dropdownAnswer8.equals("Win when pieces are 6 in line.")) {
                desc += " (end (if (is Line 6) (result Mover Win)))))";  
                System.out.println(desc);
            } else if (dropdownAnswer8.equals("Win when pieces are 3 in line and 1 piece is at the center.")) {
                desc += " (end (if (and (= (what at:(centrePoint)) (mover)) (is Line 3)) (result Mover Win)))))";  
                System.out.println(desc);
            } else if (dropdownAnswer8.equals("Win when 4 in line, but lose when 3 in line")) {
                desc += " (end { (if (is Line 4) (result Mover Win))  (if (is Line 3) (result Mover Loss)) } )))";  
                System.out.println(desc);
            } else if (dropdownAnswer8.equals("Win when 3 sides are connected.")) {
                desc += " (end (if (is Connected 3 Sides) (result Mover Win)))))";  
                System.out.println(desc);
            }
            currentQuestion = ""; // Question 11.
            
            remove(answerDropdown8);
            add(compileButton);
            //compileButton.setVisible(true);
            //add(answerDropdown8, BorderLayout.SOUTH);
            repaint();
            revalidate();
        } else {
            System.out.println(answer);
            if (answer.equals("Compile")) {
                System.out.println("Compiling");
                GameSetup.compileAndShowGame(StartQnAEditor.app, desc, false);

                System.out.println("Compiled");
            } else if (currentQuestion.equals("What color is your cat?")) {
                JOptionPane.showMessageDialog(this, "Your cat's name is " + answer + ".");
                //System.exit(0);
            }
    }
        // display the new question
        questionArea.setText(currentQuestion);
        repaint();
        revalidate();
    }

    // ActionPerformed method for submit button
    /* 
    public void actionPerformed(ActionEvent e) {
        if (answerDropdown.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
        } else if (answerDropdown.getSelectedIndex() == 1) {
            JOptionPane.showMessageDialog(this, "You selected " + answerDropdown.getSelectedItem());
            try
            {
                readTicTacToe(StartQnAEditor.app);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "You selected " + answerDropdown.getSelectedItem());
            question2();
            repaint();
            revalidate();
        }
    }
    */

    public void readTicTacToe(final PlayerApp app) throws IOException {
         // Passing the path to the file as a parameter
         FileReader fr1 = new FileReader("C:\\Users\\berku\\OneDrive\\Documents\\Tic-Tac-Toe1.txt");
         FileReader fr2 = new FileReader("C:\\Users\\berku\\OneDrive\\Documents\\Tic-Tac-Toe2.txt");
        // Declaring loop variable
        int i;
        String str1 = "";
        String str2 = "";
        String str = "";
        // Holds true till there is nothing to read
        while ((i = fr1.read()) != -1)
 
            // Print all the content of a file
            str1 += (char)i;
        while ((i = fr2.read()) != -1)

            // Print all the content of a file
            str2 += (char)i;
        str = str1 + str2;
        System.out.println(str);

        GameSetup.compileAndShowGame(app, str, false);
    }

    public void question(String q, String [] options) {
        JLabel questionLabel;
        JComboBox<String> answerDropdown;

        questionLabel = new JLabel(q);
        answerDropdown = new JComboBox<String>(options);
        add(questionLabel);
        add(answerDropdown);
    }
/* 
    public void question2() {
        //remove(questionLabel);
        //remove(answerDropdown);
        //remove(submitButton);
        questionLabel1 = new JLabel("Is your game tic-tac-toe2?");
        answerDropdown1 = new JComboBox<String>(new String[] {"Select...", "Yes", "No"});
        submitButton1 = new JButton("Submit");
        add(questionLabel1);
        add(answerDropdown1);
        add(submitButton1);
        //repaint();
    }*/
}
