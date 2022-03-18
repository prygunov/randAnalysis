package net.artux;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class PiForm extends JFrame {

    private JSpinner nCircleSpinner;
    private JButton startButton;
    private JLabel piLabel;
    private JPanel circlePanel;
    private JPanel rootPanel;
    private JFreeChart circleChart;

    private DataModel dataModel = App.getDataModel();
    private XYSeriesCollection circleSeriesCollection = new XYSeriesCollection();

    PiForm() {
        setContentPane(rootPanel);

        var model = new SpinnerNumberModel();
        model.setMinimum(100);
        model.setValue(100);
        model.setMaximum(1000000);
        nCircleSpinner.setModel(model);

        setMinimumSize(new Dimension(500, 500) {});
        startButton.addActionListener(e -> {
            int n = (int) nCircleSpinner.getValue();

            circleSeriesCollection.removeAllSeries();
            int s = 0;
            XYSeries circle = new XYSeries("");
            double xStep = 0.01d;
            for (int i = 0; i < 101; i++) {
                double x = xStep*i;
                double y = Math.sqrt(1 - Math.pow(x,2));
                circle.add(new XYDataItem(x, y));
            }
            XYSeries points = new XYSeries("");
            for (int i = 0; i < n; i++) {
                double x = dataModel.getNextDouble();
                double y = dataModel.getNextDouble();
                points.add(new XYDataItem(x, y));

                if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= 1)
                    s++;
            }

            double pi = (double) (4 * s) / (double)n; // значение пи

            piLabel.setText(String.valueOf(pi));
            circleSeriesCollection.addSeries(circle);
            circleSeriesCollection.addSeries(points);
            updateCharts();
        });


        circleChart = createCircleChart("Pi", circleSeriesCollection);
        NumberAxis rangeAxis = (NumberAxis)circleChart.getXYPlot().getDomainAxis();
        rangeAxis.setRange(0,1);

        ChartPanel chartPanel = new ChartPanel(circleChart) { // this is the trick to manage setting the size of a chart into a panel!
            public Dimension getPreferredSize() {
                return new Dimension(400, 400);
            }
        };

        circlePanel.removeAll();
        circlePanel.add(chartPanel);
        circlePanel.revalidate();
    }
    private JFreeChart createCircleChart(String title, XYSeriesCollection dataset)
    {
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                "",             // x-axis label
                "",                // y-axis label
                dataset, PlotOrientation.VERTICAL,true, false, false);
        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        return chart;
    }
    void updateCharts(){
        XYPlot xyPlot = (XYPlot) circleChart.getPlot();
        xyPlot.setDataset(circleSeriesCollection);
        ((XYLineAndShapeRenderer)((XYPlot) circleChart.getPlot()).getRenderer()).setSeriesLinesVisible(0, true);
        ((XYLineAndShapeRenderer)((XYPlot) circleChart.getPlot()).getRenderer()).setSeriesShapesVisible(0, false);
        ((XYLineAndShapeRenderer)((XYPlot) circleChart.getPlot()).getRenderer()).setSeriesLinesVisible(1, false);
    }
}
