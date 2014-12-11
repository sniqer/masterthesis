package logtool.analyzer.bbfilter.ui;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JProgressBar;
import javax.xml.crypto.dsig.keyinfo.PGPData;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import MasterThesisPackage.BasicCalc;
import MasterThesisPackage.Calculate;
import MasterThesisPackage.GraphHandler;
import MasterThesisPackage.LTEAnalyzer;
import MasterThesisPackage.Print;
import MasterThesisPackage.ProgressbarWindow;

import com.ericsson.logtool.resources.view.AbstractResourceView;
import com.ericsson.logtool.resources.view.DynamicTrace2;
import com.ericsson.logtool.resources.view.DynamicTraceList;
import com.ericsson.reveal.fw.core.BbTimeFrame;
import com.ericsson.reveal.fw.core.Frame;
import com.ericsson.reveal.fw.core.Input;
import com.ericsson.reveal.fw.core.Output;
import com.ericsson.reveal.fw.core.View;

public class AdvancedLTEGraphView extends AbstractResourceView implements View {

	private TabFolder folder;
//	private LTEAnalyzer localLTEAnalyzer;
	private GraphHandler graphHandler;
	private Table table;
	private Composite rightSideComposite;
	private Action saveDataAction;
	private Action loadDataAction;
	
	private int columns;
	private int rows;
	private String fileOutput = new String();
	
	private List<String> header;
//	private String[][] transferedLTEDataMatrix;
	
	private HashMap<String, String[][]> nameToMatrix;
	private HashMap<String, LTEAnalyzer> nameToAnalyzer;
	private HashMap<Integer, ArrayList<Combo>> comboMap;
	private ArrayList<String> matrixesToShow;
	private ProgressBar progressBar;
	private Label progressInformationLabel;
	private int lastTimeValue;
	
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		comboMap = new HashMap<Integer, ArrayList<Combo>>();
		nameToMatrix = new HashMap<String, String[][]>();
		nameToAnalyzer = new HashMap<String, LTEAnalyzer>();
		matrixesToShow = new ArrayList<String>();
//		localLTEAnalyzer= new LTEAnalyzer();
		
		folder = new TabFolder(parent, SWT.BORDER);
		folder.setLayout(new GridLayout());
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GridData gridData = new GridData();
		gridData.widthHint = 200;
		gridData.verticalAlignment = SWT.BEGINNING;
		
		rightSideComposite = new Composite(parent, SWT.None);
		rightSideComposite.setLayout(new GridLayout(1, false));
		rightSideComposite.setLayoutData(gridData);
		
		GridData tableGroupGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
		tableGroupGridData.heightHint = 300;
		tableGroupGridData.widthHint = 210;
		
		GridData progressGroupData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		progressGroupData.heightHint = 70;
		progressGroupData.widthHint = 210;

		Group progressGroup = new Group(rightSideComposite, SWT.None);
		progressGroup.setText("progress Information");
		progressGroup.setLayout(new GridLayout(1, false));
		progressGroup.setLayoutData(progressGroupData);

		
		progressBar = new ProgressBar(progressGroup, SWT.NONE);
		progressBar.setSelection(0);
		progressBar.setBounds(50, 50, 200, 50);
		progressBar.setVisible(false);
		progressInformationLabel = new Label(progressGroup, SWT.NONE);
		progressInformationLabel.setBounds(0,0, 200, 100);
		progressInformationLabel.setVisible(false);
		progressInformationLabel.setText(" hej                                      ");
		
		
		Group tableGroup = new Group(rightSideComposite, SWT.None);
		tableGroup.setText("List of all loaded files");
		tableGroup.setLayout(new FillLayout());
		tableGroup.setLayoutData(tableGroupGridData);
		
		table = new Table(tableGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

	    table.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	          if (event.detail == SWT.CHECK) {
	        	  System.out.println(((TableItem)event.item).getText());
	  	  		if(((TableItem)event.item).getChecked() == true)
	  	  			showGraphData(((TableItem)event.item).getText());
	  	        else
	  	        	removeGraphData(((TableItem)event.item).getText());
	          }
        }});

		setActionCollection();
	    //NOTE: graphHandler must be initialized before adding the advancedTabs
		graphHandler = new GraphHandler();
		graphHandler.initialize(folder);
		
		//create all the tabs now when we have the data we need in the tabs
		for(int i = 0; i < 10; i++)
			addAdvancedTab(i, parent);		
	}
	
	private void addNameToLoadedFilesTable(String name){
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(name);
		item.setChecked(true);
	}
	
	private void setActionCollection(){
		saveDataAction = new Action("Save Data") {
			public void run() {
				saveData();
			}
		};
		loadDataAction = new Action("Load Data") {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
				    public void run() {
						try {
							loadData();
						} catch (IOException e) {
							e.printStackTrace();
						}
				    }
				});
			}
		};
		
		saveDataAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
		saveDataAction.setToolTipText("Save graph data to file");
		ActionContributionItem contributionLoadCustomProfile = new ActionContributionItem(saveDataAction);
		addContributionToToolBar(contributionLoadCustomProfile);
		
		loadDataAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
		loadDataAction.setToolTipText("Load graph data to file");
		ActionContributionItem contributionLoadFile = new ActionContributionItem(loadDataAction);
		addContributionToToolBar(contributionLoadFile);
	}

	private void saveData(){
		BufferedWriter fileWriter;
		try {

			fileOutput = "";
			String[] filterExt = { "*.csv" };
			FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
			fd.setFilterPath("C:/Users/erazlin/Desktop"); // Windows specific
			fd.setFilterExtensions(filterExt);
			
			String file = fd.open();
			
			//makes sure the extensions is added to the file
			if(!file.contains(".csv"))
				file += ".csv";
			
			/* debug  information*/ 
			System.out.println("RESULT=" + file);
			System.out.println("#Rows: " + rows);
			System.out.println("#Columns: " + columns);
			System.out.println("------ headers -----");
			for(int i = 0; i < columns; i++){
				System.out.println(header.get(i));
			}
			System.out.println("------end of headers----------");
			
			// write the header to a .csv file
			fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			fileWriter.write(header.get(0));

			int currentColumn = 0;
			int currentRow = 0;
			int index =1;
			
			while(index < header.size())
				fileWriter.write(", " + header.get(index++));
			fileWriter.write("\n");
			
			//write the matrix to the csv file
			System.out.println("Writing to file, pls wait...");
			for(int i = 0; i < rows; i++){
				fileOutput += nameToMatrix.get("name")[i][0];
				
				for(int k = 1; k < columns; k++){
					fileOutput += ", " + nameToMatrix.get("name")[i][k];
				}

				currentColumn = 0;
				currentRow++;
				fileOutput += "\n";
				fileWriter.write(fileOutput);
				fileOutput = "";
			}
			
			System.out.println(" --------- done writing to file -----------");
			fileWriter.close();
			
		} catch (IOException ioe) {
			System.out.println("Could not write to file: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}
	
	private void addHeaderToDDLs(){
		for(int tabIndex = 0; tabIndex < 10; tabIndex++){
			addHeaderToDropDownList(comboMap.get(tabIndex).get(0), header);
			addHeaderToDropDownList(comboMap.get(tabIndex).get(1), header);
			
			comboMap.get(tabIndex).get(0).setText("Y-value");
			comboMap.get(tabIndex).get(1).setText("X-value");
		}
		
	}
	
	private void loadData() throws IOException{
		System.out.println("Loading Data...");
		
		String filePath = "";
		
		try{
			String[] filterExt = { "*.csv" };
			FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
			fd.setText("Open");
			fd.setFilterPath("C:/Users/erazlin/Desktop");
			fd.setFilterExtensions(filterExt);
	
			filePath = fd.open();
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			
			//show information to the user about how far the file have been loaded
			progressBar.setSelection(0);
			progressBar.setVisible(true);
			progressInformationLabel.setVisible(true);
			progressInformationLabel.setText("pls wait, loading file..");
			
			//get header, #rows and #columns
			String headerString = reader.readLine();
			rows = countLines(filePath) - 1;           //-1 for the header line
			columns = headerString.split(", ").length;
			progressBar.setMaximum(rows);
			
			//this is not the same lteDataMatrix which is stored in this class
			String[][] lteDataMatrix = new String[rows][columns];
			
			header = new ArrayList<String>(); 
			
//			String[] text = headerString.split(", "); //ett alternativ
			for(int i = 0; i < columns; i++){
				String text = headerString.split(", ")[i];
				header.add(text); //TODO check if .trim() needs to be used for the text variable
//				header.add(text[i]);
			}
			
			addHeaderToDDLs();
			
			System.out.println("#Rows: " + rows);
			System.out.println("headers");
			for(int i = 0; i < columns; i++)
				System.out.println(header.get(i));

			//adds all elements in the loaded file to the dataMatrix
			String[] line = new String[columns];
			for(int i = 0; i < rows; i++){
				line = reader.readLine().split(", ");
				for(int k = 0; k < columns; k++){
					lteDataMatrix[i][k] = line[k];
				}
				progressBar.setSelection(i);
			}
			reader.close();
			progressInformationLabel.setText("updating local data");
			
			addNameToLoadedFilesTable(filePath);
			
			LTEAnalyzer lteAnalyzer = new LTEAnalyzer();
			if(rows == 0)
				System.out.println("ERROR: opens the view with empty data matrix");
			else{
				lteAnalyzer.initialize(lteDataMatrix, columns, rows, convertHeader(header), folder);
				lteAnalyzer.calculateTimeStampData();
				lteAnalyzer.recalculateThroughput();
				
				nameToAnalyzer.put(filePath, lteAnalyzer);
				nameToMatrix.put(filePath, lteDataMatrix); 
				matrixesToShow.add(filePath);
			}
		}
		
		catch(Exception e){
			System.out.println("catched an exeption while loading and handled it");
			e.printStackTrace();
			MessageBox dialog = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("ERROR");
			dialog.setMessage("There occurred an error while reading the file:\n" + filePath );
			dialog.open();
		}
		System.out.println("------------Finished Loading Data----------------\n");
		progressBar.setSelection(0);
		progressBar.setVisible(false);
		progressInformationLabel.setVisible(false);
	}
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n' || c[i] == '\r') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	
	/**
	 * initializes the matrix in AdvancedLTEGraphView and adds all the graphs and layouts needed in each tab
	 * @param traceList
	 */
	public void setTraceList(DynamicTraceList<DynamicTrace2> traceList) {
		if(traceList != null && traceList.size() > 0){
			columns = traceList.getColumns().size();
			rows = traceList.size();	
			header = traceList.getColumns();
			
			LTEAnalyzer lteAnalyzer = new LTEAnalyzer();
			
		    String[][] lteDataMatrix = new String[rows][columns];
	
			System.out.println("Size=" + rows);
			
			for(int rowIndex = 0; rowIndex < rows; ++rowIndex)
				for(int columnIndex = 0; columnIndex < columns; ++columnIndex)
					lteDataMatrix[rowIndex][columnIndex] = traceList.get(rowIndex).getValue(columnIndex).toString();

			if(traceList.size() == 0)
				System.out.println("ERROR: opens the view with empty data matrix");
			else{
				lteAnalyzer.initialize(lteDataMatrix, columns, rows, convertHeader(header), folder);
				lteAnalyzer.calculateTimeStampData();
				lteAnalyzer.recalculateThroughput();
				
				nameToMatrix.put("name", lteDataMatrix);
				nameToAnalyzer.put("name", lteAnalyzer);
				matrixesToShow.add("name");
				addNameToLoadedFilesTable("name");
			}
			
			//adds all fields in header to all Drop Down Lists in all tabs
			addHeaderToDDLs();
		}
		else
			System.out.println("tracelist was null or size was 0");
	}

    String TIMESTAMP = "timeStamp";
    String DL_THROUGHPUT = "dl throughput";
    String UL_THROUGHPUT = "ul throughput";
    
	private void addAdvancedTab(int counter, Composite parent){
		//group component, will contain all DDLs and "generate graph"/"load data" -button
		final int graphIndex = counter;
		
		Group tableGroup = new Group(graphHandler.getCurrentFolderComposite(), SWT.None);
		tableGroup.setText("Custom Axis Chooser");
		tableGroup.setLayout(new GridLayout(5, false));
		
		//components in the group
		final Combo combo1DropDown = new Combo(tableGroup, SWT.DROP_DOWN | SWT.BORDER);
		final Combo combo2DropDown = new Combo(tableGroup, SWT.DROP_DOWN | SWT.BORDER);
		Button generateGraphButton = new Button(tableGroup, SWT.NONE);
		
		ArrayList<Combo> comboArray = new ArrayList<Combo>();
		comboArray.add(0, combo1DropDown);
		comboArray.add(1, combo2DropDown);
		
		comboMap.put(counter, comboArray);

		generateGraphButton.setText("Generate Graph");
		
		final Spinner spinner = new Spinner(tableGroup, SWT.BORDER);
	    spinner.setMinimum(0);
	    spinner.setMaximum(10000);
	    spinner.setSelection(100);
	    spinner.setIncrement(1);
	    spinner.setPageIncrement(50);
		

		Button changeTimeValueButton = new Button(tableGroup, SWT.NONE);
		changeTimeValueButton.setText("clear Graph");
	    
		changeTimeValueButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if(e.type == SWT.Selection){
					graphHandler.clearGraph(graphIndex);
					
				}
			}
		});
		
		
		//layoutdata for the components
		GridData combo1Data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		GridData combo2Data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		GridData buttonData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		GridData spinnerLayoutData = new GridData(SWT.RIGHT, GridData.CENTER, false, false); 
		
		combo1Data.horizontalIndent = 50;
		combo2Data.horizontalIndent = 10;
		buttonData.horizontalIndent = 5;
		spinnerLayoutData.horizontalIndent = 50;
		
		//set the layouts for all components in the group
		combo1DropDown.setLayoutData(combo1Data);
		combo2DropDown.setLayoutData(combo2Data);
		generateGraphButton.setLayoutData(buttonData);
		spinner.setLayoutData(spinnerLayoutData);
//	    spinner.pack();

    	
		generateGraphButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if(e.type == SWT.Selection){
					Display.getDefault().asyncExec(new Runnable() {
					    public void run() {
				            System.out.println("Generating graph...");		            
				            try{
					            String xValue = combo2DropDown.getText();
					            String yValue = combo1DropDown.getText();
					            String sibValue = "bbUeRef";
					            
					        	//show information to the user about how far the file have been loaded
								progressBar.setVisible(true);
								progressBar.setSelection(0);
								progressInformationLabel.setVisible(true);
								progressInformationLabel.setText("pls wait, recalculating time data..");
						
					            
					            //if user has changed the time constant, then update all LTEAnalyzers
					            if(lastTimeValue != Integer.parseInt(spinner.getText()))
					            	for(LTEAnalyzer analyzer : nameToAnalyzer.values()){
					            		analyzer.setTimeConstant(Integer.parseInt(spinner.getText()));
					            		System.out.println("setting time constant = " + Integer.parseInt(spinner.getText()));
					            		analyzer.recalculateThroughput();
					            	}
		
					            
		//			            graphHandler.clearGraph(graphIndex);
					            graphHandler.changeXYAxisLabels(graphIndex, xValue, yValue);
					            
					            for(String name : matrixesToShow){
									progressInformationLabel.setText("pls wait, calculating graph data..");
									
						            int xIndex = header.indexOf(xValue);
						            int yIndex = header.indexOf(yValue);
						            int sibIndex = nameToAnalyzer.get(name).findHeaderIndex(sibValue, 0);
						            
						            
						            double[] xDoubleArr = null;
						            double[] yDoubleArr = null;
		
						            int[] xArray = null;
						            int[] yArray = null;
						            int[] sibArray = nameToAnalyzer.get(name).interpretLTEdata(sibIndex);
						            
						            //if the header exists in LTEAnalyzer class too
						            if(xIndex != -1 && !xValue.equals(TIMESTAMP))
						            	xArray= nameToAnalyzer.get(name).interpretLTEdata(xIndex);
						            if(yIndex != -1 && !yValue.equals(TIMESTAMP))
						            	yArray = nameToAnalyzer.get(name).interpretLTEdata(yIndex);
		
						            //modify the array to contain time difference instead of timestamps
						            if(yValue.equals(TIMESTAMP))
						            	yDoubleArr = nameToAnalyzer.get(name).getTimeStampsInDigits();
						            if(xValue.equals(TIMESTAMP))
						            	xDoubleArr = nameToAnalyzer.get(name).getTimeStampsInDigits();
						            
						            if(xValue.equals(DL_THROUGHPUT)){
					            		xDoubleArr = nameToAnalyzer.get(name).getDLRealThroughput();
						            }
						            if(yValue.equals(DL_THROUGHPUT)){
					            		yDoubleArr = nameToAnalyzer.get(name).getDLRealThroughput();
						            }
						            if(xValue.equals(UL_THROUGHPUT)){
					            		xDoubleArr = nameToAnalyzer.get(name).getULRealThroughput();
					            	}
						            if(yValue.equals(UL_THROUGHPUT)){
					            		yDoubleArr = nameToAnalyzer.get(name).getULRealThroughput();
					            	}
						            
						            //if one of the arrays hasnt been initialized
						            if(xDoubleArr == null)
							            xDoubleArr = BasicCalc.intArr2DoubleArr(nameToAnalyzer.get(name).interpretLTEdata(xIndex));
						            if(yDoubleArr == null)
							            yDoubleArr = BasicCalc.intArr2DoubleArr(nameToAnalyzer.get(name).interpretLTEdata(yIndex));
						            
						            
						            Calculate calculate = new Calculate();
						            BasicCalc basicCalc = new BasicCalc();
						            
						            calculate.setSIB(sibArray);
						            basicCalc.setSIB(sibArray);
		
						            double smallestXValue = basicCalc.getSmallest(xDoubleArr);	
						            
							        double[] XPerYDoubleArray = null;
							        double[][] valueOverTimeArray = null;
							        if(xValue.equals(TIMESTAMP) || yValue.equals(TIMESTAMP)){
							        	if(xValue.equals(TIMESTAMP))
							        		valueOverTimeArray = nameToAnalyzer.get(name).getValuesOverTime(yDoubleArr);
							        	if(yValue.equals(TIMESTAMP))
							        		valueOverTimeArray = swapMatrixRows(nameToAnalyzer.get(name).getValuesOverTime(xDoubleArr));
							        }
							        else
							        	XPerYDoubleArray = calculate.avgYValPerXVal(xDoubleArr, yDoubleArr, 40, smallestXValue);
							        
		
									progressInformationLabel.setText("pls wait, creating graph..");
							        //check whether or not timestamps has been used
							        XYSeries series = new XYSeries(yValue+ "/" + xValue + " " + name);
							        if(valueOverTimeArray != null){
							        	progressBar.setMaximum(valueOverTimeArray[0].length);
							        	for(int i = 0; i < valueOverTimeArray[0].length; i++){
							        		series.add(valueOverTimeArray[0][i], valueOverTimeArray[1][i]);
							        		progressBar.setSelection(i);
							        	}
							        }
							        else{
							        	progressBar.setMaximum(XPerYDoubleArray.length);
							        	for(int i = 0; i < XPerYDoubleArray.length; i++){
							        		if(XPerYDoubleArray[i] != 0){
							        			series.add(i + smallestXValue, XPerYDoubleArray[i]);
							        			progressBar.setSelection(i);
							        			
							        		}
							        	}
							        }
						            graphHandler.addXYSerieToGraph(graphIndex, series);
					            }
					            
					            graphHandler.redrawGraphs();
					            System.out.println("generating graph OK\n");
				            }
				            catch(Exception exception){
				            	System.out.println("Failed to generate graph");
				            	exception.printStackTrace();
				            }
		
							progressBar.setSelection(0);
							progressBar.setVisible(false);
							progressInformationLabel.setVisible(false);
					

				         }
				    });
					
				}	
			}
		 });
		
		tableGroup.pack();

		GridData tableGroupGridData = new GridData(GridData.FILL, GridData.FILL, true, false);
		tableGroupGridData.heightHint = 30;
		tableGroup.setLayoutData(tableGroupGridData);
		
		//add Graph
		graphHandler.addLineChartToComposite("Title", "x-axis", "y-axis");
		graphHandler.addCompositeToTab("tab " + counter);
	}
	
	private double[][] swapMatrixRows(double[][] inMatrix){
		double[][] outMatrix = new double[inMatrix.length][inMatrix[0].length];
		outMatrix[0] = inMatrix[1];
		outMatrix[1] = inMatrix[0];
		return outMatrix;
	}
	
	private void showGraphData(String name) {
		System.out.println("adding lteDataMatrix to the Array");
		matrixesToShow.add(name);
	}

	private void removeGraphData(String name){
		System.out.println("removing lteDataMatrix from the Array");
		matrixesToShow.remove(name);
	}
	
	private void addHeaderToDropDownList(Combo ddl, List<String> header){
		if(header == null){
//			System.out.println("header was null");
			return;
		}
		
		ddl.removeAll();

		ddl.add("dl throughput");
		ddl.add("ul throughput");		
		for(String s : header)
			ddl.add(s);


	}
	
	private String[] convertHeader(List<String> list){
		String[] returnArray = new String[list.size()];
		int counter = 0;
		for(String s: list){
			returnArray[counter++] = s;
		}
		return returnArray;
	}
	
	
	@Override
	public void finished(BbTimeFrame arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void newInput(Input arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newOutputFrame(BbTimeFrame arg0, Frame arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public java.util.List<Output> getOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

}
