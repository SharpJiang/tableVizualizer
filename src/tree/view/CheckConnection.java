package tree.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;

import static tree.view.Form.res;

/**
 * Created by Victor on 23.04.2017.
 */
public class CheckConnection extends JFrame {

    private final int WIDTH = 600;
    private final int HEIGHT = 260;
    private static String locale;

    public CheckConnection(boolean orientation, boolean useOrientation, String locale) {
        super("Конфигурация системы");
        this.locale = locale;
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);
        JPanel content = new JPanel(new BorderLayout(5, 5));

        content.add(createLabel("<html><p style=\"color:red; font-size:15px\"><i>" +
                        "Приветствую Вас в программе TableVizualizer!</i></p></html>"),
                (useOrientation) ? BorderLayout.PAGE_START : BorderLayout.NORTH);

        content.add(createPaneForMetaData(createEditorPaneDesc("<b style = \"color:#184AFF;\">" +
                        "Для корректной работы программе необходим доступ к сети Интернет.<br>" +
                        "<i style = \"color:red;\">Пожалуйста, нажмите на кнопку Play справа<br>для проверки доступа к глобальной сети.</i></b>"
                        )),
                (useOrientation) ? BorderLayout.PAGE_END : BorderLayout.CENTER);


        content.add(createPanel(this), (useOrientation) ? BorderLayout.LINE_END : BorderLayout.EAST);

        content.add(createLabel("<html><p style=\"text-align:center;\">&copy TableVizualizer <br />" +
                        "version: 1.0</p></html>"),
                (useOrientation) ? BorderLayout.PAGE_START : BorderLayout.SOUTH);

        if (orientation) {
            content.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(content);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private JLabel createLabel(String caption) {
        JLabel lbl = new JLabel(caption);
        lbl.setPreferredSize(new Dimension(100, 50));
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0x11FF44), 5));
        return lbl;
    }

    private JScrollPane createPaneForMetaData(JEditorPane component) {
        JScrollPane scroll = new JScrollPane(component);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scroll.setPreferredSize(new Dimension(250, 145));
        //scroll.setMinimumSize(new Dimension(10, 10));
        return scroll;
    }

    private JEditorPane createEditorPaneDesc(String str) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText("<html><p style = \"text-align:center;\">" + str + "</p></html>\n");
        Border border = new LineBorder(new Color(0x9AFF6F), 5);
        editorPane.setBorder(border);

        editorPane.setEditable(false);
        return editorPane;
    }

    private JPanel createPanel(JFrame frame) {
        BorderLayout borlay = new BorderLayout();
        JPanel pnl = new JPanel(borlay);
        pnl.setPreferredSize(new Dimension(100, 50));
        pnl.setBorder(BorderFactory.createLineBorder(new Color(0xFF3818), 5));
        JButton playButton = null;
        ImageIcon img = new ImageIcon("images/Play-icon.png");
        playButton = new JButton();
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                netIsAvailable(frame);
            }
        });
        playButton.setIcon(img);
        playButton.setBackground(new Color(0xDCFFF1));
        pnl.add(playButton, BorderLayout.CENTER);
        return pnl;
    }

/*    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CheckConnection(false, false, "RU");
            }
        });
    }*/

    private static void netIsAvailable(JFrame frame) {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection conn = url.openConnection();
            conn.connect();
            frame.setVisible(false);
            Form form = new Form(locale);
            frame.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Отсутствует доступ к сети Интернет! \nПожалуйста, подключите компьютер к глобальной сети.", "Нет доступа к сети", JOptionPane.ERROR_MESSAGE);
        }
    }

}
