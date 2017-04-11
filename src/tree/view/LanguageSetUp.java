package tree.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Victor on 01.04.2017.
 */
public class LanguageSetUp extends JFrame {

    private final int WIDTH = 600;
    private final int HEIGHT = 310;
    String[] items = {
            "Русский - RU",
            "Українська - UA",
    };
    private JComboBox comboBox = new JComboBox(items);
    ;

    public LanguageSetUp(boolean orientation, boolean useOrientation) {
        super("Выбор языка");
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);
        JPanel content = new JPanel(new BorderLayout(5, 5));

        content.add(createLabel("<html><p style=\"color:red; font-size:15px\"><i>" +
                        "Выберите язык из списка и нажмите \"PLAY\" справа</i></p></html>"),
                (useOrientation) ? BorderLayout.PAGE_START : BorderLayout.NORTH);

        content.add(createLangSetPanel(),
                (useOrientation) ? BorderLayout.PAGE_START : BorderLayout.CENTER);

        content.add(createPanel(), (useOrientation) ? BorderLayout.LINE_END : BorderLayout.EAST);

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

    private JPanel createLangSetPanel() {
        BorderLayout borlay = new BorderLayout();
        JPanel pnl = new JPanel(borlay);
        //pnl.setPreferredSize(new Dimension(50, 50));
        pnl.setBorder(BorderFactory.createLineBorder(new Color(0x4CBFFF), 5));

        Font font = new Font("Verdana", Font.BOLD, 15);

        //JComboBox comboBox = new JComboBox(items);
        comboBox.setFont(font);
        comboBox.setForeground(new Color(0xFF4D26));
        comboBox.setAlignmentX(CENTER_ALIGNMENT);
        pnl.add(comboBox, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createPanel() {
        BorderLayout borlay = new BorderLayout();
        JPanel pnl = new JPanel(borlay);
        pnl.setPreferredSize(new Dimension(100, 50));
        pnl.setBorder(BorderFactory.createLineBorder(new Color(0xFF3818), 5));
        JButton playButton = null;
        ImageIcon img = new ImageIcon("images/Play-icon.png");
        playButton = new JButton();
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Form form = new Form(comboBox.getSelectedItem().toString()
                        .substring(comboBox.getSelectedItem().toString().length() - 2,
                                comboBox.getSelectedItem().toString().length()));
            }
        });
        playButton.setIcon(img);
        playButton.setBackground(new Color(0xDCFFF1));
        pnl.add(playButton, BorderLayout.CENTER);
        return pnl;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LanguageSetUp(false, false);
            }
        });
    }

}
