package logtool.analyzer.bbfilter.ui;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jfree.data.xy.XYSeriesCollection;

import MasterThesisPackage.GraphHandler;

import com.ericsson.logtool.resources.view.AbstractResourceView;
import com.ericsson.reveal.fw.core.BbTimeFrame;
import com.ericsson.reveal.fw.core.Frame;
import com.ericsson.reveal.fw.core.Input;
import com.ericsson.reveal.fw.core.Output;
import com.ericsson.reveal.fw.core.View;

public class CombinedGraphsView extends AbstractResourceView implements View {


	private HashMap<String, XYSeriesCollection[]> nameToXYSeriesCollectionMap;
	private Composite rightSideComposite; 

	private TabFolder folder;
	private Action loadGraphButton;
	private Table table;

	private GraphHandler graphHandler;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		folder = new TabFolder(parent, SWT.BORDER);
		folder.setLayout(new GridLayout());
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GridData gridData = new GridData();
		gridData.widthHint = 200;
		gridData.verticalAlignment = SWT.BEGINNING;
		
		GridData tableGroupGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
		tableGroupGridData.heightHint = 300;
		tableGroupGridData.widthHint = 210;
		
		rightSideComposite = new Composite(parent, SWT.None);
		rightSideComposite.setLayout(new GridLayout(2, false));
		rightSideComposite.setLayoutData(gridData);
		
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

		graphHandler = new GraphHandler();
		graphHandler.initialize(folder);
		
		nameToXYSeriesCollectionMap = new HashMap<String, XYSeriesCollection[]>();

		insertGraphs();
	}


	private void showGraphData(String name) {
		// TODO Auto-generated method stub
		System.out.println("adding graph data to all graphs");
	    graphHandler.addSeriesToGraphs(nameToXYSeriesCollectionMap.get(name), name);
		graphHandler.redrawGraphs();
	}

	private void removeGraphData(String name){
		System.out.println("removing graph data from all graphs");
	    graphHandler.removeSeriesFromGraphs(nameToXYSeriesCollectionMap.get(name));
		graphHandler.redrawGraphs();
	}
	
	private void insertGraphs() {
		//downlink and uplink bits per second over time
		graphHandler.addLineChartToComposite("DL Average Bits per second / timestamp", "time", "throughput [Mbits/s]");
		graphHandler.addLineChartToComposite("UL Average Bits per second / timestamp", "time", "throughput [Mbits/s]");
		graphHandler.addCompositeToTab("dl and ul bps/time"); //verkar inte kunna skriva ut &
		
		//downlink bits per second over CQI
		graphHandler.addLineChartToComposite("DL Bits per second / CQI", "CQI", "throughput [Mbits/s]");
		
		//uplink bits per second over SINR
		graphHandler.addLineChartToComposite("ul Bits per second / SINR", "SINR", "throughput [Mbits/s]");
		graphHandler.addCompositeToTab("dl and ul bps/SINR and CQI");
		
		//downlink and uplink prb per cqi and SINR
		graphHandler.addLineChartToComposite("dl prb / CQI", "CQI", "dl prb");
		graphHandler.addLineChartToComposite("ul prb / SINR", "SINR", "ul prb");
		graphHandler.addCompositeToTab("dl and ul prb/SINR and CQI");

		
		//downlink and uplink spectral efficiency per cqi and SINR
		graphHandler.addLineChartToComposite("dl spectral efficiency / CQI", "CQI", "spectral efficiency [bits/s/Hz]");
		graphHandler.addLineChartToComposite("ul spectral efficiency / SINR", "SINR", "spectral efficiency [bits/s/Hz]");
		graphHandler.addCompositeToTab("dl and ul specEff/SINR and CQI");
		
		//downlink and uplink block error rate per cqi and sinr
		graphHandler.addLineChartToComposite("dl block error rate / CQI", "CQI", "block error rate [%]");
		graphHandler.addLineChartToComposite("ul block error rate / SINR", "SINR", "block error rate [%]");
		graphHandler.addCompositeToTab("dl and ul bler/SINR and CQI");


		graphHandler.addLineChartToComposite("dl rank indicatpr / cqi", "CQI", "rank indicator");
		graphHandler.addCompositeToTab("RI and PMI/CQI");

		graphHandler.addLineChartToComposite("dl cqi / mcs1", "mcs", "CQI");
		graphHandler.addLineChartToComposite("ul SINR / mcs", "mcs", "SINR");
		graphHandler.addCompositeToTab("dl and ul SINR and CQI / mcs");
	}


	private void setActionCollection() {
		loadGraphButton = new Action("Load config") {
			public void run() {
				loadGraphData();
			}

		};

		loadGraphButton.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
		loadGraphButton.setToolTipText("load graph data from file");
		ActionContributionItem contributionLoadCustomProfile = new ActionContributionItem(loadGraphButton);
		addContributionToToolBar(contributionLoadCustomProfile);
	}

	
	private void loadGraphData() {
		XYSeriesCollection[] inData;
		ObjectInputStream inStream;

		try {
			FileDialog fd = new FileDialog(Display.getDefault()
					.getActiveShell());
			fd.setText("Open");
			fd.setFilterPath("C:/Users/erazlin/Desktop");
			String[] filterExt = { "*.coolExtension" };
			fd.setFilterExtensions(filterExt);

			inStream = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(fd.open())));

			// Read from the stream until we hit the end
			try {
				inData = (XYSeriesCollection[]) inStream.readObject();
				
				String collectionName = fd.getFileName().split("\\.")[0];
				nameToXYSeriesCollectionMap.put(collectionName, inData);
				
				addRowToRightsideTable(collectionName);
			    
				graphHandler.addSeriesToGraphs(inData, collectionName);
				graphHandler.redrawGraphs();
				
			} catch (Exception e) {
				System.out.println("couldnt Read data from file");
				System.out.println(e.getStackTrace());
				System.out.println();
				System.out.println(e.getMessage());
			}

			inStream.close();
		} catch (Exception e) {
			System.out.println("couldnt open file");
		}
	}

	
	private void addRowToRightsideTable(String name){
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(name);
		item.setChecked(true);
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
