package lteAnalyzer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
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
        ChartFrame graphFrame = new ChartFrame("XYLine Chart", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new LogarithmicAxis(Yaxis);
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
        ChartFrame graphFrame = new ChartFrame("XYLine Chart", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
}
