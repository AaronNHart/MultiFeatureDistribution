package main;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class DemoApplication {
  
  private JPanel mainPanel;

  public DemoApplication(){
    Map<Integer, ArrayList<Double>> dataMap = createData();
    JFreeChart chart = MultiFeatureDistributionChart.createChart(dataMap);
    mainPanel = new ChartPanel(chart);
  }
  
  private Map<Integer, ArrayList<Double>> createData() {
    int featureCount = 8;
    int maxDepth = 20;
    Map<Integer, ArrayList<Double>> data = new HashMap<>();
    Random rnjesus = new Random();
    for (int i=0;i<featureCount;i++){
      ArrayList<Double> dimensionScores = new ArrayList<>();
      Double topScore = rnjesus.nextDouble();
      for (int j=0;j<maxDepth;j++){
        Double mult = (double) (maxDepth-j);
        dimensionScores.add(topScore*mult);
      }
      data.put(i, dimensionScores);
    }
    return data;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Demo");
    DemoApplication app = new DemoApplication();
    frame.add(app.getMainPanel());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(600,600));
    frame.pack();
    frame.setVisible(true);
  }

  private Component getMainPanel() {
    return mainPanel;
  }
}
