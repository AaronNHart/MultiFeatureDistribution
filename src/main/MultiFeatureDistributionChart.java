package main;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MultiFeatureDistributionChart {
  
  private MultiFeatureDistributionChart() {  }
  
  public static JFreeChart createChart(Map<Integer, ArrayList<Double>> dataMap) {
    XYSeriesCollection data=new XYSeriesCollection();
    
    Optional<Integer> maxDepth = dataMap.values().stream().map(ArrayList::size).max(Integer::compare);
    Optional<Double>  zMax = dataMap.values().stream().flatMap(ArrayList::stream).max(Double::compareTo);
    if (maxDepth.isPresent()&&zMax.isPresent()){
      
      //For each entry create a list of series containing shell coordinates.
      for (Entry<Integer, ArrayList<Double>> e: dataMap.entrySet()){
        e.getValue().size();
        double nShell = maxDepth.get();
        int i = e.getKey();
        double nSeg = dataMap.size();
        double dSeg = 1d/dataMap.size();
        double aSeg = dSeg*Math.PI*(nShell*nShell);
        double aShell = nSeg/aSeg;
        
        
        //Calculate Series x Values.
        double x = (double)i/nSeg*360;
        double delta = 360/dataMap.size()/2d;
        double xMin = x - delta;
        double xMax = x + delta;
        
        double radiusPrevious = 0;
        for (int j=0;j<maxDepth.get();j++){

          double yMin = radiusPrevious;
          double yMax = j;//Math.sqrt((aShell+dSeg*Math.PI*radiusPrevious*radiusPrevious)/dSeg*Math.PI);
          XYSeries s = new XYSeries(i*maxDepth.get()+j);
          s.add(xMin, yMin);
          s.add(xMin, yMax);
          s.add(xMax, yMax);
          s.add(xMax, yMin);
          data.addSeries(s);
          radiusPrevious = yMax;
        }
      }
      JFreeChart chart = ChartFactory.createPolarChart("Title", data, true, false, false);
      
      PolarPlot plot = (PolarPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.white);
      plot.setAngleGridlinePaint(Color.black);
      plot.setRadiusGridlinePaint(Color.white);
      DefaultPolarItemRenderer  renderer = new DefaultPolarItemRenderer();
      for (int i=0;i<data.getSeriesCount();i++){
        Paint seriesPaint = calculateSeriesPaint(i, dataMap, maxDepth.get(), zMax.get().floatValue());
        renderer.setSeriesPaint(i,  seriesPaint);
        renderer.setSeriesOutlinePaint(i, new Color(0,0,0,0));
        renderer.setSeriesFilled(i, true);
        renderer.setSeriesShape(i, new Rectangle(0,0));
      }
      plot.getAxis().setAxisLineVisible(false);
      
      
      plot.setRenderer(renderer);
      chart.removeLegend();
      chart.setTitle("Multi Feature Distribution Chart");
      return chart;
      
    } else {
      throw new IllegalArgumentException();
    }
 
  }

  private static Paint calculateSeriesPaint(int i, Map<Integer, ArrayList<Double>> dataMap, int maxDepth, float zMax) {
    int feature = (i/maxDepth);
    int position = i%maxDepth;
    if (feature<dataMap.size()){
      float alpha = (float) (dataMap.get(feature).get(position)/zMax);
      return new Color(0f,0f,0f,alpha);
    } else {
      return null;
    }
  }

}
