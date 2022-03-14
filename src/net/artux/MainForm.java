package net.artux;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainForm extends JFrame {

    private JPanel rootPanel;
    private JPanel fPanel;
    private JPanel FPanel;
    private JPanel contentPanel;
    private JComboBox comboBox1;
    private JButton button1;
    private JSpinner spinner1;
    private JLabel stat;

    private Timer timer = new Timer();
    private TimerTask task;
    private boolean isTimerRunning;
    int value[] = new int[100];
    float n = 0;
    Random random = new Random();

    private void fillBox(JComboBox box){
        box.addItem("Встроенный Random");
        box.addItem("..");
    }

    MainForm(){
        setContentPane(rootPanel);
        setSize(800, 700);

        fillBox(comboBox1);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTimerRunning)
                    stopTimer();
                else
                    startTimer();


            }
        });
    }

    int plus = 0;

    void startTimer(){
        isTimerRunning = true;
        task = new TimerTask() {
            @Override
            public void run() {
                n++;
                int rand;
                switch (comboBox1.getSelectedIndex()) {
                    case 1:
                        rand = plus;
                        plus++;
                        if (plus>99)
                            plus = 0;
                        break;
                    default:
                        rand = random.nextInt(100);
                        break;
                }
                value[rand] += 1;

                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                DefaultCategoryDataset secondDataset = new DefaultCategoryDataset();

                double av= 0.01d;
                double average = value[0];

                for(int i = 1; i< value.length; i++) {
                    dataset.addValue(value[i], "", ""+i);
                    average = (value[i] + average)/2;
                    secondDataset.addValue(value[i] /n, "", ""+i);
                }
                stat.setText(String.valueOf(average/n));

                JFreeChart chart;
                chart = createChart(dataset);

                ChartPanel chartPanel = new ChartPanel(chart);
                fPanel.removeAll();
                fPanel.add(chartPanel);
                fPanel.revalidate();

                ChartPanel chartPanel1 = new ChartPanel(createChart(secondDataset));
                FPanel.removeAll();
                FPanel.add(chartPanel1);
                FPanel.revalidate();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 25);
    }

    void stopTimer(){
        isTimerRunning = false;
        task.cancel();
    }

    private JFreeChart createChart(CategoryDataset dataset)
    {
        JFreeChart chart = ChartFactory.createBarChart(
                "",
                null,                   // x-axis label
                "",                // y-axis label
                dataset, PlotOrientation.VERTICAL,true, false, false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.getDomainAxis().setCategoryMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setTickLabelsVisible(false);

        if (plot.getRenderer() instanceof LineAndShapeRenderer){
            plot.getRenderer().setSeriesPaint(0, Color.black);
        }else if (plot.getRenderer() instanceof BarRenderer) {
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(false);
            renderer.setItemMargin(0);
            renderer.setShadowVisible(false);
            renderer.setDrawBarOutline(false);
            renderer.setBarPainter(new StandardBarPainter());
            renderer.setSeriesPaint(0, Color.black);
        }



        return chart;
    }

}
