package diaguml;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import swingEditor.EditorActions;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.util.mxGraphActions;

public class CustomKeyBoardHandler extends mxKeyboardHandler {
	
	
	public CustomKeyBoardHandler(mxGraphComponent graphComponent) {
		    super(graphComponent);
		}
	@Override
	protected InputMap getInputMap(int condition) {
		
		    InputMap map = super.getInputMap(condition);
		    if (condition == JComponent.WHEN_FOCUSED && map != null) {
			
			        map.put(KeyStroke.getKeyStroke("F2"), "edit");
			        map.put(KeyStroke.getKeyStroke("DELETE"), "envelerVraiment"); //surcharge pour éviter la possibilité de suppression
			        map.put(KeyStroke.getKeyStroke("RIGHT"), "selectNext");
			        map.put(KeyStroke.getKeyStroke("LEFT"), "selectPrevious");
			        map.put(KeyStroke.getKeyStroke("ENTER"), "expand");
			        map.put(KeyStroke.getKeyStroke("BACK_SPACE"), "collapse");
			        map.put(KeyStroke.getKeyStroke("control A"), "selectAll");
			        map.put(KeyStroke.getKeyStroke("control D"), "selectNone");
			        map.put(KeyStroke.getKeyStroke("control Z"), "undo");
			        map.put(KeyStroke.getKeyStroke("control Y"), "redo");
			        map.put(KeyStroke.getKeyStroke("control C"), "selectNone");
			    }
		
		    return map;
		}
	
	protected ActionMap createActionMap() {
		    ActionMap map = super.createActionMap();
		
		    map.put("save", new EditorActions.SaveAction(false));
		    map.put("saveAs", new EditorActions.SaveAction(true));
		    map.put("new", new EditorActions.NewAction());
		    map.put("open", new EditorActions.OpenAction());
		    map.put("undo", new EditorActions.HistoryAction(true));
		    map.put("redo", new EditorActions.HistoryAction(false));
		    map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
		    map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
		    map.put("supprimeVraiment", mxGraphActions.getDeleteAction());
		
		    return map;
		}
	

}
