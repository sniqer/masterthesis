package MasterThesisPackage;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MemoryInformationWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private XYSeriesCollection dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private XYSeries series, maxMemorySeries;
	private final int MB = 1024 * 1024;
	

	int xCounter = 1;
	long time = System.currentTimeMillis();
	
	
	public MemoryInformationWindow(){
		super("MemoryInformationWindow");
		createDataset();
	    chart = createChart(dataset);
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	    
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //3. Create components and put them in the frame.
	  //...create emptyLabel...
//	  this.getContentPane().add("MemoryInformationWindow", BorderLayout.CENTER);

	  //4. Size the frame.
	  this.pack();

	  //5. Show it.
	  this.setVisible(true);
	  System.out.println("done");
	}
	
	public void startWindowUpdate(){
		Thread t = new Thread(new Runnable() {
	         public void run()
	         {
	        	 updateGraphT();
	         }
	    });
	    t.start();
	}

	
	private void updateGraphT(){
		System.out.println("started up thread");
		while(true){
			try{
				if(time + 300 < System.currentTimeMillis()){
					Runtime rt = Runtime.getRuntime();
				    long usedMB = (rt.totalMemory() - rt.freeMemory()) / MB;

				    maxMemorySeries.add(xCounter, rt.maxMemory() / MB);
				    series.add(xCounter++, usedMB);
				    chartPanel.updateUI();
				    time = System.currentTimeMillis();
				    
				}
			}
			catch(Exception error){
				error.printStackTrace();
				return;
			}
		}
	}
	
	private void createDataset() {
	    series = new XYSeries("Memory Usage [MB]");
	    maxMemorySeries = new XYSeries("maxMemoryUsage [MB]");
	    dataset = new XYSeriesCollection();
	    dataset.addSeries(series);
	    dataset.addSeries(maxMemorySeries);
}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset  the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {
    
	    // create the chart...
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        "Line Chart Demo 6",      // chart title
	        "X",                      // x axis label
	        "Y",                      // y axis label
	        dataset,                  // data
	        PlotOrientation.VERTICAL,
	        true,                     // include legend
	        true,                     // tooltips
	        false                     // urls
	    );
	
	    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	    chart.setBackgroundPaint(Color.white);
	
	//    final StandardLegend legend = (StandardLegend) chart.getLegend();
	//      legend.setDisplaySeriesShapes(true);
	    
	    // get a reference to the plot for further customisation...
	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    
	    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    renderer.setSeriesLinesVisible(1, true);
	    renderer.setSeriesShapesVisible(1, false);
	    plot.setRenderer(renderer);
	
	    // change the auto tick unit selection to integer units only...
	    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    // OPTIONAL CUSTOMISATION COMPLETED.
	            
	    return chart;
	    
	}
}
