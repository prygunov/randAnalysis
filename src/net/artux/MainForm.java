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
import java.util.HashMap;
import java.util.Map;

public class MainForm extends JFrame {

    private JPanel rootPanel;
    private JPanel fPanel;
    private JPanel FPanel;
    private JPanel contentPanel;
    private JComboBox<String> comboBox1;
    private JButton button1;
    private JSpinner spinner1;
    private JLabel waiterLabel;
    private JButton clearButton;
    private JLabel dispersionLabel;
    private JLabel squareAverageLabel;
    private JButton piFormButton;
    private JSpinner mSpinner;
    private JSpinner sigmaSpinner;
    private JButton applyButton;
    private JLabel n;
    private JSpinner nSpinner;

    private PiForm piForm;
    private DataModel dataModel = App.getDataModel();

    private JFreeChart fChart;
    private JFreeChart FChart;

    private final DefaultCategoryDataset fCategoryDataset = new DefaultCategoryDataset();
    private final XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

    int iteration;

    private void fillBox(JComboBox<String> box){
        box.addItem("Встроенный - равномерное");
        box.addItem("Собственный - равномерное");
        box.addItem("Собственный - нормальное");
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
        model.setValue(100000);
        model.setMaximum(1000000);
        spinner1.setModel(model);

        mSpinner.setValue(50);
        sigmaSpinner.setValue(16);
        nSpinner.setValue(12);

        button1.addActionListener(e -> {
            int n = (int) spinner1.getValue();
            int m = (int)mSpinner.getValue();
            int sigma = (int)sigmaSpinner.getValue();
            int rn = (int)nSpinner.getValue();

            HashMap<Integer, Integer>  value = dataModel.getValues(comboBox1.getSelectedIndex(), n, rn, m, sigma);

            String title = comboBox1.getSelectedItem() + " ("+n+")";
            XYSeries series = new XYSeries(title);
            double prev = 0;

            for (int i = 0; i < 100; i++) {
                Integer count = value.get(i);

                float p = 0;
                if (count!=null)
                    p = count.intValue() / (float) n;

                fCategoryDataset.addValue(p, iteration +" "+ title, ""+i);
                prev = prev + p;
                series.add(new XYDataItem(i, prev));
                series.add(new XYDataItem(i+1, prev));
            }

            xySeriesCollection.addSeries(series);
            iteration++;

            updateLabels();
            updateCharts();
        });
        clearButton.addActionListener(e -> {
            fCategoryDataset.clear();
            xySeriesCollection.removeAllSeries();
            updateCharts();
            iteration = 0;
        });

        piFormButton.addActionListener(e -> piForm.setVisible(true));
        initCharts();
    }

    void updateLabels(){
        waiterLabel.setText("Мат. ожидание: " + dataModel.getLastAverage());
        dispersionLabel.setText("Дисперсия:" + dataModel.getLastDispersion());
        squareAverageLabel.setText("Среднее квадратичное отклонение:" + dataModel.getLastDeviation());
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
        int w = 750;
        int h = 650;

        fChart = createChart(fCategoryDataset);
        FChart = createChart(xySeriesCollection);

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

    private JFreeChart createChart(final CategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createBarChart(
                "f",  // chart title
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

        if (plot.getRenderer() instanceof BarRenderer renderer) {
            renderer.setDrawBarOutline(false);
            renderer.setItemMargin(0);
            renderer.setShadowVisible(false);
            renderer.setDrawBarOutline(false);
            renderer.setBarPainter(new StandardBarPainter());
        }

        return chart;
    }

    private JFreeChart createChart(XYSeriesCollection dataset)
    {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "F",
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

        if (dataset!=null)
            for (int i = 0; i < dataset.getSeriesCount(); i++) {
                renderer.setSeriesShapesVisible(i, false);
            }
        return chart;
    }

}
