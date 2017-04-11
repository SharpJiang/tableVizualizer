package tree.view;

import model.Tables;
import tree.data.ExtractData;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;

import static tree.view.Form.res;

public class Table extends JFrame {

    private final int WIDTH = 810;
    private final int HEIGHT = 620;
    private List<Tables> tables;
    private ExtractData extData;
    private int number;

    public Table(boolean orientation, boolean useOrientation, List<Tables> tables, int number, ExtractData extData) {
        super(res.getString("tables_number") + " " + (number + 1));
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);
        this.number = number;
        this.tables = tables;
        this.extData = extData;

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

/*        content.add(createLabel("Left"), (useOrientation) ? BorderLayout.LINE_START : BorderLayout.WEST);*/
        content.add(createLabel("Right"), (useOrientation) ? BorderLayout.LINE_END : BorderLayout.EAST);

        content.add(createScrollPanel(createTable(number)), BorderLayout.CENTER);
        if (orientation) {
            content.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        setContentPane(content);
        setSize(WIDTH, HEIGHT);
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
                    System.out.println("Сделать редактор заголовка!");
                    //HeaderEditorFrame headerEditorFrame = new HeaderEditorFrame(tempTable, val, colModel, index, jScrollPane);
                    //headerEditorFrame.setVisible(true);
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
                System.out.println("Сделать редактор ячеек!");
                //CellEditorFrame cellEditorFrame = new CellEditorFrame(theTable, row, col, val, tempTable);
                //cellEditorFrame.setVisible(true);
            }
        }
    }

}