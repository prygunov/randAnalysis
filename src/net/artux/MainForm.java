package net.artux;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class MainForm extends JFrame {

    private JPanel rootPanel;
    private JPanel fPanel;
    private JPanel FPanel;
    private JPanel contentPanel;
    private JComboBox comboBox1;
    private JButton button1;
    private JSpinner spinner1;
    private JLabel waiterLabel;
    private JButton clearButton;
    private JLabel dispersionLabel;
    private JLabel squareAverageLabel;
    private JButton piFormButton;

    PiForm piForm;

    int value[] = new int[100];
    float n = 0;
    Random random = new Random();
    MyRandom myRandom = new MyRandom(System.currentTimeMillis());

    private JFreeChart fChart;
    private JFreeChart FChart;

    private DefaultCategoryDataset fCategoryDataset = new DefaultCategoryDataset();
    private XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

    int iteration;
    private void fillBox(JComboBox box){
        box.addItem("Встроенный");
        box.addItem("Собственный");
    }

    MainForm(){
        piForm = new PiForm();
        piForm.setVisible(false);

        setContentPane(rootPanel);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1900, 1060){});

        fillBox(comboBox1);
        var model = new SpinnerNumberModel();
        model.setMinimum(100);
        model.setValue(100);
        model.setMaximum(1000000);
        spinner1.setModel(model);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Arrays.fill(value, 0);

                int n = (int) spinner1.getValue();

                if (comboBox1.getSelectedIndex() == 0)
                    for(int i = 1; i < n; i++)
                        value[random.nextInt(100)] += 1;
                else
                    for(int i = 1; i < n; i++)
                        value[myRandom.getRand(100)] += 1;

                String title = comboBox1.getSelectedItem() + " ("+n+")";
                XYSeries series = new XYSeries(title);
                double prev = 0;
                double average = 0d;

                for (int i = 0; i < value.length; i++) {
                    float p = value[i] / (float)n;
                    average += i * p;

                    fCategoryDataset.addValue(p, iteration +" "+ title, ""+i);
                    prev = prev + p;
                    series.add(new XYDataItem(i, prev));
                    series.add(new XYDataItem(i+1, prev));
                }

                double d = 0d;
                for (int i = 0; i < value.length; i++) {
                    float p = value[i] / (float)n;
                    d+=Math.pow(i - average, 2) * p;
                }
                xySeriesCollection.addSeries(series);
                iteration++;

                waiterLabel.setText("Мат. ожидание: " + average);
                dispersionLabel.setText("Дисперсия:" + d);
                squareAverageLabel.setText("Среднее квадратичное отклонение:" + Math.sqrt(d));

                updateCharts();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fCategoryDataset.clear();
                xySeriesCollection.removeAllSeries();
                updateCharts();
                iteration = 0;
            }
        });
        piFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                piForm.setVisible(true);
            }
        });
        initCharts();
    }

    void updateCharts(){
        CategoryPlot categoryPlot = (CategoryPlot) fChart.getPlot();
        categoryPlot.setDataset(fCategoryDataset);

        XYPlot xyPlot = (XYPlot) FChart.getPlot();
        xyPlot.setDataset(xySeriesCollection);

        for (int i = 0; i < xySeriesCollection.getSeriesCount(); i++) {
            ((XYLineAndShapeRenderer) xyPlot.getRenderer()).setSeriesShapesVisible(i, false);
        }

    }

    void initCharts(){
        int w = 900;
        int h = 800;

        fChart = createChart("f", fCategoryDataset);
        FChart = createChart( "F", xySeriesCollection);

        ChartPanel chartPanel = new ChartPanel(fChart) { // this is the trick to manage setting the size of a chart into a panel!
            public Dimension getPreferredSize() {
                return new Dimension(w, h);
            }
        };
        fPanel.removeAll();
        fPanel.add(chartPanel);
        fPanel.revalidate();

        chartPanel = new ChartPanel(FChart) { // this is the trick to manage setting the size of a chart into a panel!
            public Dimension getPreferredSize() {
                return new Dimension(w, h);
            }
        };
        FPanel.removeAll();
        FPanel.add(chartPanel);
        FPanel.revalidate();
    }

    private JFreeChart createChart(String title, final CategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createBarChart(
                title,  // chart title
                null,                  // domain axis label
                null,                     // range axis label
                dataset,                     // data
                PlotOrientation.VERTICAL,    // the plot orientation
                true,                        // legend
                true,                        // tooltips
                false                        // urls
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.getDomainAxis().setCategoryMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setTickLabelsVisible(false);

        if (plot.getRenderer() instanceof BarRenderer) {
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(false);
            renderer.setItemMargin(0);
            renderer.setShadowVisible(false);
            renderer.setDrawBarOutline(false);
            renderer.setBarPainter(new StandardBarPainter());
        }

        return chart;
    }

    private JFreeChart createChart(String title, XYSeriesCollection dataset)
    {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "",             // x-axis label
                "",                // y-axis label
                dataset, PlotOrientation.VERTICAL,true, false, false);
        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.getDomainAxis().setTickLabelsVisible(false);
        plot.getDomainAxis().setTickMarksVisible(false);

        if (dataset!=null)
            for (int i = 0; i < dataset.getSeriesCount(); i++) {
                renderer.setSeriesShapesVisible(i, false);
            }
        return chart;
    }

}
