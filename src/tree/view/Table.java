package tree.view;

import model.Tables;
import tree.data.ExtractData;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static tree.view.LanguageSetUp.res;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class Table extends JFrame {

    private final int WIDTH = 810;
    private final int HEIGHT = 620;
    private List<Tables> tables;
    private ExtractData extData;
    private int number;
    private int tablesNumber;
    private String locale;

    public Table(JFrame f, boolean orientation, boolean useOrientation, List<Tables> tables, int number, ExtractData extData, int tablesNumber, String locale) {
        super(res.getString("tables_number") + " " + (number + 1));
        f.setVisible(false);
        f.dispose();
        this.locale = locale;
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);
        this.number = number;
        this.tables = tables;
        this.extData = extData;
        this.tablesNumber = tablesNumber;

        JPanel content = new JPanel(new BorderLayout(5, 5));
/*        content.add(createPaneForMetaData(createLabel("Просмотр таблицы № " + (number+1)+ ". " + extData.getTitle(tables.get(number)))),
                (useOrientation) ? BorderLayout.PAGE_START : BorderLayout.NORTH);*/

        content.add(createPaneForMetaData(createEditorPaneTitle("<b style = \"color:#184AFF;\">"+ res.getString("inspect_table") + " " + (number + 1) + "</b>"
                        + "<br />" + "<i>" + extData.getTitle(tables.get(number)) + "</i>")),
                (useOrientation) ? BorderLayout.PAGE_START : BorderLayout.NORTH);

        content.add(createPaneForMetaData(createEditorPaneDesc("<b style = \"color:#184AFF;\">"+ res.getString("tables_description") + " " + (number + 1) + "</b>" +
                        "<br />" + "<i>"
                        + extData.getDescription(tables.get(number)) + "</i>")),
                (useOrientation) ? BorderLayout.PAGE_END : BorderLayout.SOUTH);

        content.add(createPaneForMetaData(createEditorPaneTitle("<i><b style = \"color:red;\">" +"Будьте внимательны в процессе" + "<br>" + "редактирования!" + "</b></i><br>" +
                        "<p align=\"middle\" style=\"font-size:13px; text-indent: 1.5em;\">" + res.getString("panel_info") + "<br>" +
                        "<i style=\"color:black; font-family:Serif; font-size:12px;\">" + "Текст " + "</i>" + res.getString("or") + " " +
                        "<i style=\"color:green; font-family:Serif; font-size:12px;\">Текст</i> - <b>" + res.getString("valid_data") + "</b><br>" +
                        "<i style=\"color:rgb(255,143,60); font-family:Serif; font-size:12px;\">Текст</i> - <b><i>" + res.getString("maybe") + "</i>," +
                        res.getString("invalid_data") + "</b><br>" +
                        "<i style=\"color:red; font-family:Serif; font-size:12px;\">Текст</i> - <b>" + res.getString("invalid_data") +
                        "</b></p>")),
                (useOrientation) ? BorderLayout.LINE_START : BorderLayout.WEST);
        content.add(createPanel(),
                (useOrientation) ? BorderLayout.LINE_END : BorderLayout.EAST);

        content.add(createScrollPanel(createTable(number)), BorderLayout.CENTER);
        if (orientation) {
            content.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        setContentPane(content);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                String ObjButtons[] = {res.getString("main_question_OK"),res.getString("main_question_NO")};
                int PromptResult = JOptionPane.showOptionDialog(null,
                        res.getString("want_to_leave"),
                        res.getString("trying_to_quit"),JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult==JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                } else {

                }
            }
        });
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JLabel createLabel(String caption) {
        JLabel lbl = new JLabel(caption);
        lbl.setPreferredSize(new Dimension(100, 50));
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0x11FF44), 5));
        return lbl;
    }

    private JScrollPane createScrollPanel(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x11FF44), 5));

        return scrollPane;
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

    private JEditorPane createEditorPaneTitle(String str) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setPreferredSize(new Dimension(100, 50));
        editorPane.setContentType("text/html");
        editorPane.setText("<html><p style = \"text-align:center;\">" + str + "</p></html>\n");
        Border border = new LineBorder(new Color(0x75FF94), 5);
        editorPane.setBorder(border);

        editorPane.setEditable(false);
        return editorPane;
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

    private JPanel createPanel() {
        BorderLayout borlay = new BorderLayout();
        JPanel pnl = new JPanel(borlay);
        pnl.setPreferredSize(new Dimension(100, 50));
        pnl.setBorder(BorderFactory.createLineBorder(new Color(0xFF3818), 5));
        JButton playButton = null;
        ImageIcon img = new ImageIcon("images/tree_from_table.jpg");
        playButton = new JButton();
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Form form = new Form(tablesNumber, tables, locale);
                setVisible(false);
                dispose();
            }
        });
        playButton.setToolTipText(res.getString("to_tree"));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        playButton.setIcon(img);
        playButton.setBackground(new Color(0xDCFFF1));
        pnl.add(playButton, BorderLayout.CENTER);
        return pnl;
    }

    private JTable createTable(int number) {

        Object columnNames[] = new String[extData.getHeadColumns(tables.get(number)).size()];
        Object rowData[][] = new String[extData.getRows(tables.get(number)).size()]
                [extData.getHeadColumns(tables.get(number)).size()];

        for (int k = 0; k < extData.getHeadColumns(tables.get(number)).size(); k++) {
            columnNames[k] = extData.getHeadColumns(tables.get(number)).get(k);
        }

        for (int m = 0; m < extData.getRows(tables.get(number)).size(); m++) {
            for (int n = 0; n < extData.getRows(tables.get(number)).get(m).size(); n++) {
                rowData[m][n] = extData.getRows(tables.get(number)).get(m).get(n);
                if(rowData[m][n].equals(res.getString("absence"))) {
                    rowData[m][n]="<html><font size=\"3px\" color=\"red\" face=\"Arial\">"
                            + rowData[m][n] + "</font></html>";
                } else if(rowData[m][n].equals(res.getString("undefined"))) {
                    rowData[m][n]="<html><font size=\"3px\" color=\"rgb(255,143,60)\" face=\"Arial\">"
                            + rowData[m][n] + "</font></html>";
                }
            }
        }

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        table.setShowGrid(true);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setEnabled(false);

        //DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        //centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        Font cellFont = new Font("Verdana", Font.PLAIN, 14);

        for (int x = 0; x < table.getColumnCount(); x++) {
            //table.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
            table.setRowHeight(30);
            table.setFont(cellFont);
        }

        table.setTableHeader(new JTableHeader(table.getColumnModel()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 45;
                return d;
            }
        });

        TableCellRenderer rend = table.getTableHeader().getDefaultRenderer();
        TableColumnModel tcm = table.getColumnModel();
        for (int j = 0; j < tcm.getColumnCount(); j += 1) {
            TableColumn tc = tcm.getColumn(j);
            TableCellRenderer rendCol = tc.getHeaderRenderer(); // likely null
            if (rendCol == null) {
                rendCol = rend;
            }
            Component component = rendCol.getTableCellRendererComponent(table, tc.getHeaderValue(), false, false, 0, j);
            tc.setPreferredWidth(component.getPreferredSize().width);
        }

        table.getTableHeader().addMouseListener(new ColumnHeaderListener());

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));

        table.addMouseListener(new CellsMouseListener());

        return table;
    }

    private static class HeaderRenderer implements TableCellRenderer {

        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
        }
    }

    class ColumnHeaderListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
            JTable table = ((JTableHeader) evt.getSource()).getTable();
            TableColumnModel colModel = table.getColumnModel();

            int index = colModel.getColumnIndexAtX(evt.getX());
            if (index == -1) {
                return;
            } else {
                String val = (String) colModel.getColumn(index).getHeaderValue();
                String[] options = new String[2];
                options[0] = res.getString("main_question_OK");
                options[1] = res.getString("main_question_NO");
                int n = JOptionPane.showOptionDialog(null, res.getString("col_head_editing"),
                        res.getString("attention"), 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (n == 0) {
                    Tables tempTable = tables.get(number);
                    HeaderEditor.showWindow(tempTable, val, colModel, index);
                }
            }
        }
    }

    class CellsMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTable theTable = (JTable) e.getSource();
            int row = theTable.rowAtPoint(e.getPoint());//get mouse-selected row
            int col = theTable.columnAtPoint(e.getPoint());//get mouse-selected col
            String val = (String) theTable.getValueAt(row, col);
            String[] options = new String[2];
            options[0] = res.getString("main_question_OK");
            options[1] = res.getString("main_question_NO");
            int n = JOptionPane.showOptionDialog(null, res.getString("row_editing"), res.getString("attention"), 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
            if (n == 0) {
                Tables tempTable = tables.get(number);
                theTable.setCellSelectionEnabled(true);
                theTable.changeSelection(row, col, false, false);
                theTable.requestFocus();
                CellsEditor.showWindow(theTable, row, col, val, tempTable);
                //revalidate();
                theTable.getSelectionModel().clearSelection();
                //CellEditor cellEditorFrame = new CellEditor(theTable, row, col, val, tempTable);
                //cellEditorFrame.setVisible(true);
            }
        }
    }

}