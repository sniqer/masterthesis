package MasterThesisPackage;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

public class GraphHandler {

	private TabFolder folderReference;
	private HashMap<Integer, Composite> compositeMap;
	private HashMap<Integer, XYSeriesCollection> datasetMap;
	private HashMap<Integer, XYPlot> plotMap;
	
	private int datasetIndex = 0;
	private int compositeIndex = 0;
	
	
	public void initialize(TabFolder folderReference){
		this.folderReference = folderReference;
		compositeMap = new HashMap<Integer, Composite>();
		datasetMap = new HashMap<Integer, XYSeriesCollection>();
		plotMap = new HashMap<Integer, XYPlot>();
	}

	
	public void removeSeriesFromGraphs(XYSeriesCollection[] seriesCollection){
		for(int i = 0; i < seriesCollection.length; i++){
			for (int seriesIndex = 0; seriesIndex < seriesCollection[i].getSeriesCount(); ++seriesIndex){
				XYSeries serie = seriesCollection[i].getSeries(seriesIndex);

				datasetMap.get(i).removeSeries(serie);
			}
		}
	}
	
	/**
	 * clears all the series in the seriesCollection stored in datasetMap.get(datasetIndex) 
	 * @param tabIndex
	 */
	public void clearGraph(int datasetIndex){
		datasetMap.get(datasetIndex).removeAllSeries();
	}
	
	
	public void changeXYAxisLabels(int graphIndex, String x, String y){
		plotMap.get(graphIndex).getDomainAxis().setLabel(x);
		plotMap.get(graphIndex).getRangeAxis().setLabel(y);
	}
	
	
	public void addSeriesToGraphs(XYSeriesCollection[] seriesCollection, String collectionName){
		for(int i = 0; i < seriesCollection.length; i++){
			for (int seriesIndex = 0; seriesIndex < seriesCollection[i].getSeriesCount(); ++seriesIndex){
				XYSeries serie = seriesCollection[i].getSeries(seriesIndex);
				if(!serie.getKey().toString().contains(collectionName))
					serie.setKey(serie.getKey() + " (" + collectionName + ")  ");
				
				addXYSerieToGraph(i, serie);
			}
		}
	}	
	
	private XYSeriesCollection getDataset(){
		if(datasetMap.get(datasetIndex) == null)
			datasetMap.put(datasetIndex, new XYSeriesCollection());
		return(datasetMap.get(datasetIndex));
	}	
	
	public XYSeriesCollection[] getTables(){
		XYSeriesCollection[] output = new XYSeriesCollection[datasetMap.size()];
		for(Integer key : datasetMap.keySet())
			output[key] = datasetMap.get(key);
		
		return output;
	}
	
	/**
	 * not to be used in BasicGraphView !!
	 * @param index the tab index
	 * @param serie the serie you wish to add to a graphs collection
	 */
	public void addXYSerieToGraph(int index, XYSeries serie){
		datasetMap.get(index).addSeries(serie);
	}
	
	public void redrawGraphs(){
		for(Composite comp : compositeMap.values())
			comp.redraw();
	}
	
	public void addDLSeriesToDataset(double[] data, String valueType){
		if(datasetMap.get(datasetIndex) == null)
			datasetMap.put(datasetIndex, new XYSeriesCollection());
		
		XYSeries serie 	= new XYSeries(valueType, true, false);

        for(int k=0;k<data.length;k++){
        	if(data[k] != MasterThesisPackage.Calculate.NOT_A_VALUE)
        		serie.add(k,data[k]); //average data values is on row 0
        }
		
		datasetMap.get(datasetIndex).addSeries(serie);
	}
	
	
	public void addULSeriesToDataset(double[] data, String valueType){
		if(datasetMap.get(datasetIndex) == null)
			datasetMap.put(datasetIndex, new XYSeriesCollection());
		
		XYSeries serie 	= new XYSeries(valueType, true, false);

        for(int k=0;k<data.length;k++){
        	if(data[k] != MasterThesisPackage.Calculate.NOT_A_VALUE)
        		serie.add(k-25,data[k]); //average data values is on row 0
        	
        }
		
		datasetMap.get(datasetIndex).addSeries(serie); 
	}	
	
	public void addSeriesOverTimeToDataset(double[] timeStamps, double[] data, String valueType){
		if(datasetMap.get(datasetIndex) == null)
			datasetMap.put(datasetIndex, new XYSeriesCollection());
		
		XYSeries serie 	= new XYSeries(valueType, true, false);
		
		  for(int k=0;k<data.length-1;k++){
	        	if(timeStamps[k] != 0 &&  timeStamps[k] != MasterThesisPackage.Calculate.NOT_A_VALUE && timeStamps[k] != timeStamps[k+1])
	        		serie.add(timeStamps[k],data[k]); 
	      }
		
		datasetMap.get(datasetIndex).addSeries(serie);
	}
	
	int counter = 0;
	public void retardplotter(double[] array){
		if(datasetMap.get(datasetIndex) == null)
			datasetMap.put(datasetIndex, new XYSeriesCollection());
		
		XYSeries serie 	= new XYSeries("blaha" + counter++, true, false);
		
		  for(int k=0;k<array.length;k++){
	        	if(array[k] != 0)
	        		serie.add(k, array[k]); 
	      }
		
		datasetMap.get(datasetIndex).addSeries(serie);
	}
	

	
	//TODO rewrite a getComposite() the returns the latest composite in compositeMap(or creates and return a new object if none exists)
	public Composite getCurrentFolderComposite(){
		if(compositeMap.get(compositeIndex) == null){
			Composite composite = new Composite(folderReference, SWT.NONE);
			GridLayout fillLayout = new GridLayout(1, true);
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			
			composite.setLayout(fillLayout);
			composite.setLayoutData(gridData);
			
			compositeMap.put(compositeIndex, composite);
		}
		
		return compositeMap.get(compositeIndex);
	}
	
	
	//adds a graph to a composite
	public void addLineChartToComposite(String header,String Xaxis,String Yaxis){
		XYSeriesCollection xyDataset = getDataset();
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				header,
				Xaxis,
				Yaxis,
				xyDataset,
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);
		
		addChartToComposite(Xaxis, Yaxis, chart);
	}


	/*public void overTime(double[] timeStamp,double[] outputdata,String header,String Xaxis,String Yaxis, String text){
		XYSeries avgVal 	= new XYSeries(text);
		
		XYSeriesCollection xyDataset = new XYSeriesCollection();
		xyDataset.addSeries(avgVal);

		//print2dArray(outputdata);
        for(int k=0;k<outputdata.length;k++){
        	if(outputdata[k] != 0)
        		avgVal.add(timeStamp[k],outputdata[k]); 
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, true, true, false);
        
        addChartToComposite(Xaxis, Yaxis, chart);
	}*/
	
	private void addChartToComposite(String Xaxis,String Yaxis, JFreeChart chart){
		if(compositeMap.get(compositeIndex) == null){
			Composite composite = new Composite(folderReference, SWT.NONE);
			GridLayout fillLayout = new GridLayout(1, true);
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			
			composite.setLayout(fillLayout);
			composite.setLayoutData(gridData);
			
			compositeMap.put(compositeIndex, composite);
		}

        NumberAxis domainAxis = new NumberAxis(Xaxis);
        NumberAxis rangeAxis = new NumberAxis(Yaxis);
		ChartComposite chartComposite = new ChartComposite(compositeMap.get(compositeIndex), SWT.NONE, chart, true);
		GridData chartData = new GridData(GridData.FILL, GridData.FILL, true, true);
        // old 
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		XYPlot plot = chart.getXYPlot();

		chartComposite.setLayoutData(chartData);
		
//		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setLegendItemToolTipGenerator(new StandardXYSeriesLabelGenerator("Legend {0}"));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setBaseItemLabelsVisible(true);
        
        plot.setRenderer(renderer);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);

        plotMap.put(datasetIndex, plot);
        
		//increase the datasetIndex to point to a new element since the current dataset has been inserted into a graph
		++datasetIndex;
	}
	
	
	//adds the composite to a new tabFolder
	public void addCompositeToTab(String tabName){
		TabItem item = new TabItem(folderReference, SWT.NONE);
		item.setText(tabName);
		
		folderReference.getItem(compositeIndex).setControl(compositeMap.get(compositeIndex));
		++compositeIndex;
	}


	
}
