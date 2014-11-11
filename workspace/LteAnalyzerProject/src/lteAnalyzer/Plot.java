package lteAnalyzer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Plot {
	
	public static void log(float[] outputdata,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<outputdata.length;k++){
        	if(outputdata[k] != 0){
        		avgVal.add(k,outputdata[k]); //average data values is on row 0
        	}        	

        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        ChartFrame graphFrame = new ChartFrame("Log plot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new LogarithmicAxis(Yaxis);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
	
	public static void normal2(double[] Xval,double[] Yval,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<Xval.length;k++){
    		avgVal.add(Xval[k],Yval[k]); //average data values is on row 0
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        ChartFrame graphFrame = new ChartFrame("Log plot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
	
	public static void YvalPerXval(double[] Xval,double[] Yval,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<Xval.length;k++){
    		avgVal.add(Xval[k],Yval[k]); //average data values is on row 0
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        ChartFrame graphFrame = new ChartFrame("Log plot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
	public static void normal(float[] outputdata,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<outputdata.length;k++){
        	if(outputdata[k] != 0){
        		avgVal.add(k,outputdata[k]); //average data values is on row 0
        	}        	

        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        ChartFrame graphFrame = new ChartFrame("Normal plot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
	
	public static void normal(double[] outputdata,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<outputdata.length;k++){
        	if(outputdata[k] != 0){
        		avgVal.add(k,outputdata[k]); //average data values is on row 0
        	}        	

        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        XYToolTipGenerator tt1 = new XYToolTipGenerator() {

    	 public String generateToolTip(XYDataset dataset, int series, int item) {
    		 StringBuffer sb = new StringBuffer();
    		 Number x = dataset.getX(series, item);
    		 Number y = dataset.getY(series, item);
    		 String output = "x: " + x.doubleValue() + "\n y: " + y.doubleValue();
    		 return output;
	 		}
    	 };
    	 renderer.setToolTipGenerator(tt1);
    	 renderer.setItemLabelGenerator(new StandardXYItemLabelGenerator());


        
        
        ChartFrame graphFrame = new ChartFrame("Normal plot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        plot.setRenderer(renderer);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
	
	public static void overTime(double[] timeStamp,double[] outputdata,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<outputdata.length;k++){
        	if(outputdata[k] != 0)
        		avgVal.add(timeStamp[k],outputdata[k]); 
        	       	

        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset);
        
        //JFreeChart chart2 = ChartFactory.

        final XYPlot plot = chart.getXYPlot();
        ChartFrame graphFrame = new ChartFrame("OvertimePlot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        rangeAxis.setAutoRangeIncludesZero(false);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
}
