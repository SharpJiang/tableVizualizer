/*
package tree.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import static tree.view.Form.monitor;

public class ProgressSample implements Runnable {

    JProgressBar progressBar;

    ProgressSample() {
        JFrame f = new JFrame("JProgressBar Sample");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Container content = f.getContentPane();
        progressBar = new JProgressBar();
        progressBar.setValue(25);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Reading...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(300, 100);
        f.setLocationRelativeTo(null);

        JButton autoIncreaseButton = new JButton("Automatic Increase");
        ActionListener autoIncreaseActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        };
        autoIncreaseButton.addActionListener(autoIncreaseActionListener);
        content.add(autoIncreaseButton);

        f.setVisible(true);
    }

    @Override
    public void run() {
        synchronized (monitor) {
            //changeProgressBar(f, progressBar);
            try {
                Thread.sleep(500);
                progressBar.setBorder(BorderFactory.createTitledBorder("Waiting..."));
                progressBar.setValue(26);
                progressBar.updateUI();
                Thread.sleep(200);
                progressBar.setValue(27);
                Thread.sleep(200);
                progressBar.setValue(28);
                Thread.sleep(200);
                progressBar.setValue(29);
                Thread.sleep(200);
                progressBar.setValue(30);
                Thread.sleep(200);
                progressBar.setValue(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.notify();
        }
    }

    public int getProgressBarStatus() {
        return progressBar.getValue();
    }

}
*/
