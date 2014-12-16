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

public class Plot extends Calculate{

	public static void YvalPerXval(double[] Xval,double[] Yval,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);


		//print2dArray(outputdata);
        for(int k=0;k<Xval.length;k++){
        	if (Xval[k] != NOT_A_VALUE && Yval[k] != NOT_A_VALUE)
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

	public static void normal(double[] outputdata, int smallestX,String header,String Xaxis,String Yaxis, String text){

		XYSeries avgVal 	= new XYSeries(text);
//		XYSeries avgVal2 	= new XYSeries(text+"2");
		
		XYSeriesCollection xyDataset = new XYSeriesCollection();
		xyDataset.addSeries(avgVal);
//		xyDataset.addSeries(avgVal2);


        for(int k=0;k<outputdata.length;k++){
        	if(outputdata[k] != -1337){
        		avgVal.add(k+smallestX,outputdata[k]); //average data values is on row 0
//        		avgVal2.add(k+smallestX,outputdata[k]+(k*1000 % 5000)-3000); //average data values is on row 0
        	}        	

        }
        
//        OptimumAnalyzer.printOptimumSerieInGraph(xyDataset);
//        OptimumAnalyzer.printOptimumAtEachPoint(xyDataset,Xaxis,smallestX);
        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        
        ChartFrame graphFrame = new ChartFrame("Normal plot", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new NumberAxis(Yaxis);
        plot.setRenderer(renderer);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}
	
}
