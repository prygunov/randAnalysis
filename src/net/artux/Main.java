package net.artux;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());

        double a = 1.5;
        System.out.println((int)a);
        a = 1.6;
        System.out.println((int)a);
        a = 1.9;
        System.out.println((int)a);
        a = 2.1;
        System.out.println((int)a);

        MainForm mainForm = new MainForm();
        mainForm.setVisible(true);
    }
}
