package app.display.dialogs.QnA_Editor; // Get package for the folder.


import app.PlayerApp;
import app.display.dialogs.visual_editor.view.VisualEditorFrame;

import javax.swing.*;

//-----------------------------------------------------------------------------

/**
 * Visual editor view.
 * @author cambolbro
 */
public class StartQnAEditor
{
	public static PlayerApp app; // gets the app for context

	@SuppressWarnings("unused")
	//-------------------------------------------------------------------------

	public static void main(String[] args)
	{
		//System.out.println("giriyorum");
		new StartQnAEditor(null); // Executed when called.
	}

	public StartQnAEditor(final PlayerApp app)
	{

		StartQnAEditor.app = app; // context.
		try
		{
			//System.out.println("AleykumSelam");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception ignored)
        {
        	// Nothing to do
        }

		//controller = new NGramController(5);
		//MainFrame f = new MainFrame(editPanel);
		QnAEditorFrame f = new QnAEditorFrame(); // Set the frame which will create a window.
		f.requestFocus();

		//System.out.println("SelamunAleykum");
	}

	//private static NGramController controller;

	/* 
	public static NGramController controller() 
	{
		return controller;
	}
	*/
}
