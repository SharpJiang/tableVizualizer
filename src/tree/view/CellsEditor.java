package tree.view;

import model.Tables;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class CellsEditor extends JPanel {

    public static JTextArea[] fields;
    String[] labels = { "Текущее значение: ", "Введите значение: "};
    int[] widths = { 20, 20 };
    String[] tips = { "Текущее значение выбранной ячейки", "Установите новое значение выбранной ячейки"};

    CellsEditor(JTable table, int row, int col, String val) {

        super(new BorderLayout());

        JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
        JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
        add(labelPanel, BorderLayout.WEST);
        add(fieldPanel, BorderLayout.CENTER);
        fields = new JTextArea[labels.length];

        for (int i = 0; i < labels.length; i += 1) {
            fields[i] = new JTextArea(2, 5);
            if(i == 0) {
                fields[i].setText(val.replaceAll("<html><font size=\"3px\" color=\"red\" face=\"Arial\">", "")
                                     .replaceAll("<html><font size=\"3px\" color=\"rgb(255,143,60)\" face=\"Arial\">", "")
                                     .replaceAll("</font></html>", ""));
                fields[i].setForeground(new Color(0xF8F4FF));
                fields[i].setFont(new Font("Verdana", Font.ITALIC,15));
                fields[i].setBackground(new Color(0x2063FF));
                fields[i].setEnabled(false);
            } else {
                fields[i].setForeground(new Color(0x3366FF));
                fields[i].setFont(new Font("Verdana", Font.ITALIC, 15));
            }
            if (i < tips.length)
                fields[i].setToolTipText(tips[i]);
            if (i < widths.length)
                fields[i].setColumns(widths[i]);

            JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
            lab.setFont(new Font("Verdana", Font.BOLD, 18));
            lab.setForeground(new Color(0xFF4D26));
            lab.setLabelFor(fields[i]);


            labelPanel.add(lab);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            //p.add(fields[i]);
            p.add(new JScrollPane(fields[i],JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
            fieldPanel.add(p);
        }

    }


   public static void showWindow(JTable table, int row, int col, String val, Tables tempTable) {

       JFrame f = new JFrame("Редактирование ячейки");

       JButton submit = new JButton("OK");

       submit.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               table.getModel().setValueAt(fields[1].getText().trim(), row, col);
               tempTable.setCell(tempTable.getRows(), row, col, fields[1].getText().trim());
               f.setVisible(false);
               f.dispose();
           }
       });

       submit.setEnabled(false);

       JButton correctness = new JButton("Проверка");
       correctness.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               String newValue = fields[1].getText();
               if (newValue.isEmpty() || newValue.replaceAll(" ", "").isEmpty()) {
                   JOptionPane.showMessageDialog(null, "Ячейка будет пуста. \nВведите новое значение ячейки таблицы!", "Некорректное значение ячейки", JOptionPane.ERROR_MESSAGE);
               } else {
                   submit.setEnabled(true);
               }
           }
       });


       f.getContentPane().add(new CellsEditor(table, row, col,  val), BorderLayout.NORTH);
       JPanel p = new JPanel();
       p.add(submit);
       p.add(correctness);
       f.getContentPane().add(p, BorderLayout.SOUTH);
       f.pack();
       f.setLocationRelativeTo(null);
       f.setSize(530, 200);
       f.setResizable(false);

       f.setVisible(true);

   }

}