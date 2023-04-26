package app.display.dialogs.QnA_Editor;

//import app.display.dialogs.visual_editor.handler.Handler;
//import app.display.dialogs.visual_editor.view.designPalettes.DesignPalette;
//import app.display.dialogs.visual_editor.view.panels.menus.EditorMenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QnAEditorFrame extends JFrame
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8610284030834361704L;
	// Frame Properties
    private static final String TITLE = "Ludii QnA Editor";
    //private static final Dimension DEFAULT_FRAME_SIZE = DesignPalette.DEFAULT_FRAME_SIZE;
    //private static final ImageIcon FRAME_ICON = DesignPalette.LUDII_ICON;


    public QnAEditorFrame()
    {
        // set frame properties
        setTitle(TITLE);
        setSize(800, 1200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        QnAEditorPanel panel = new QnAEditorPanel();
        add(panel);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // [UNCOMMENT FILIP]
                //DocHandler.getInstance().close();
                //StartVisualEditor.controller().close();
                super.windowClosing(e);
            }
        });

        MenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar);
        setVisible(true);
    }
}
