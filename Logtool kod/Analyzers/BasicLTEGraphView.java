package logtool.analyzer.bbfilter.ui;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jfree.data.xy.XYSeriesCollection;

import MasterThesisPackage.LTEAnalyzer;

import com.ericsson.logtool.resources.view.AbstractResourceView;
import com.ericsson.logtool.resources.view.DynamicTrace2;
import com.ericsson.logtool.resources.view.DynamicTraceList;
import com.ericsson.reveal.fw.core.BbTimeFrame;
import com.ericsson.reveal.fw.core.Frame;
import com.ericsson.reveal.fw.core.Input;
import com.ericsson.reveal.fw.core.Output;
import com.ericsson.reveal.fw.core.View;

//public class HarqView extends ViewPart {
public class BasicLTEGraphView extends AbstractResourceView implements View {

	private TabFolder folder;
	//private ArrayList<ArrayList<String>> valueMatrix;
	private int columns;
	private int rows;
	private String[][] lteDataMatrix;
	private List<String> header;
	private LTEAnalyzer sinrAnalyzer;
	private Action saveGraphDataAction;
	
	public BasicLTEGraphView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

        folder = new TabFolder(parent, SWT.BORDER);		
		folder.setLayout(new GridLayout());
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));		
		
		setActionCollection();
	}

	
	/**
	 * use .setFocus() on the component that should have focus when the window is loaded
	 */
	@Override
	public void setFocus() {

	}
	
	private void printHeader(List<String> list){
		for(String s: list){
			System.out.println(s);
		} 
	}
	
	private void saveAllGraphData(){
//		DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
//		dialog.setFilterPath("c:\\"); // Windows specific
//		String path = dialog.open() + "\\";
		
		String[] filterExt = { "*.coolExtension" };
		FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
		fd.setFilterPath("C:/Users/erazlin/Desktop"); // Windows specific
		fd.setFilterExtensions(filterExt);
		String path = fd.open();
		
		if(!path.contains(".coolExtension"))
			path += ".coolExtension";
		
		System.out.println("RESULT=" + path);
		XYSeriesCollection [] dataset = sinrAnalyzer.getGraphData();
		
		// serialize the Queue
		try {
//			String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		    FileOutputStream fout = new FileOutputStream(path);
		    ObjectOutputStream oos = new ObjectOutputStream(fout);
		    oos.writeObject(dataset);
		    oos.close();
		 }
		 catch (Exception e) { e.printStackTrace(); }
	}
	
	
	private void setActionCollection(){
		saveGraphDataAction = new Action("Load config") {
			public void run() {
				saveAllGraphData();
			}
		};
		saveGraphDataAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
		saveGraphDataAction.setToolTipText("Save graph data to file");
		ActionContributionItem contributionLoadCustomProfile = new ActionContributionItem(saveGraphDataAction);
		addContributionToToolBar(contributionLoadCustomProfile);
	}
	
	private String[] convertHeader(List<String> list){
		String[] returnArray = new String[list.size()];
		int counter = 0;
		for(String s: list){
			returnArray[counter++] = s;
		}
		return returnArray;
	}
	
	/**
	 * setTraceList is used when a new HarqView object has been created to import the log-file data from 
	 * BbFilterLogToolView. It is then saved as a matrix - ListArray<ListArray<String>>
	 * @param traceList
	*/
	public void setTraceList(DynamicTraceList<DynamicTrace2> traceList) {
		rows = traceList.size();
		columns = traceList.getColumns().size();
		header = traceList.getColumns();

		lteDataMatrix = new String[rows][columns];
		
		System.out.println("Size=" + rows);
		System.out.println("header column size= " + columns);
		
		for(int rowIndex = 0; rowIndex < rows; ++rowIndex){
			for(int columnIndex = 0; columnIndex < columns; ++columnIndex){
//				DynamicTrace2 rowVector = traceList.get(rowIndex);
				
				lteDataMatrix[rowIndex][columnIndex] = traceList.get(rowIndex).getValue(columnIndex).toString();
			}
		}
		printHeader(header);
		handleMatrixData();
		System.out.println("finished producing graphs in basic view");
	}
	

	private void handleMatrixData(){
		sinrAnalyzer = new LTEAnalyzer();
		sinrAnalyzer.initialize(lteDataMatrix, columns, rows, convertHeader(header), folder);
		sinrAnalyzer.createBasicViewGraphs();
	}
	
	
	/* Local Help Functions */
	
	/* End of Local Help Functions */

	@Override
	public void finished(BbTimeFrame arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Output> getOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newInput(Input arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newOutputFrame(BbTimeFrame arg0, Frame arg1) {
		// TODO Auto-generated method stub
		
	}

	
	
}
