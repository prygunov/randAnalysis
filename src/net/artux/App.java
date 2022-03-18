package net.artux;

import javax.swing.*;

public class App {

    private static DataModel dataModel = new DataModel();

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());

        MainForm mainForm = new MainForm();
        mainForm.setVisible(true);
    }

    public static DataModel getDataModel() {
        return dataModel;
    }
}
