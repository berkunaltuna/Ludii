package app.display.dialogs.QnA_Editor;

/* 
import app.display.dialogs.visual_editor.handler.Handler;
import app.display.dialogs.visual_editor.view.panels.editor.EditorSidebar;
import app.display.dialogs.visual_editor.view.panels.editor.defineEditor.DefineEditor;
import app.display.dialogs.visual_editor.view.panels.editor.gameEditor.GameEditor;
import app.display.dialogs.visual_editor.view.panels.editor.textEditor.TextEditor;
import app.display.dialogs.visual_editor.view.panels.header.HeaderPanel;
*/
import javax.swing.*;
import java.awt.*;

public class QnAEditorPanel extends JPanel
{

    /**
	 * 
	 */



    public QnAEditorPanel()
    {
        setLayout(new BorderLayout());
        add(new JButton("QnAEditor"), BorderLayout.CENTER);

        setFocusable(true);
    }
}
