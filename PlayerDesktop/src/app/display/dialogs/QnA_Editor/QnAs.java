package app.display.dialogs.QnA_Editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QnAs extends JPanel implements ActionListener {
    // Declare components
    JLabel questionLabel;
    JComboBox<String> answerDropdown;
    JButton submitButton;
    
    // Constructor
    public QnAs() {
        // Set up frame
        //setTitle("Dropdown Question");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(300, 150);
        //setLayout(new FlowLayout());
        
        // Add components
        questionLabel = new JLabel("What color is the sky?");
        answerDropdown = new JComboBox<String>(new String[] {"Select...", "Blue", "Green", "Red"});
        submitButton = new JButton("Submit");
        add(questionLabel);
        add(answerDropdown);
        add(submitButton);
        
        // Add action listener to submit button
        submitButton.addActionListener(this);
    }
    
    // ActionPerformed method for submit button
    public void actionPerformed(ActionEvent e) {
        if (answerDropdown.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
        } else {
            JOptionPane.showMessageDialog(this, "You selected " + answerDropdown.getSelectedItem());
        }
    }
    
    /* 
    // Main method
    public static void main(String[] args) {
        QnAs dq = new QnAs();
        dq.setVisible(true);
    }
*/
}
