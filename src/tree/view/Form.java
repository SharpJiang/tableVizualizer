package tree.view;

import model.Tables;
import tree.data.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Form extends JFrame {
    private JTree tree;
    public static ResourceBundle res;
    private List<Tables> tables;
    private ExtractData extData;

    public Form(String locale) {
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);
        if (!locale.equals("UA")) {
            Locale.setDefault(new Locale(locale.toLowerCase(), locale));
        } else {
            Locale.setDefault(new Locale("uk", "UA"));
        }
        //resources>native2ascii TableVizualizer_ru_RU.txt TableVizualizer_ru_RU.properties
        //Locale.setDefault(new Locale("uk", "UA"));
        //Locale.setDefault(new Locale("en", "US"));
        //native2ascii V:\TableVizualizer\src\resources\TableVizualizer_uk_UA.txt V:\TableVizualizer\src\resources\TableVizualizer_uk_UA.properties
        //native2ascii V:\TableVizualizer\src\resources\TableVizualizer_ru_RU.txt V:\TableVizualizer\src\resources\TableVizualizer_ru_RU.properties
        res = ResourceBundle.getBundle("resources.TableVizualizer");
        //create the root node
        DefaultMutableTreeNode root = new ToolTipTreeNode(new ROOTNode(res.getString("document_title")), res.getString("root_hint"));
        //create the child nodes
        extData = new ExtractData(res);

        int tablesNumber = extData.getNumberOfTables(new File("V:/q_xls_convert.xml"));
        tables = extData.getContentOfAllTables(new File("V:/q_xls_convert.xml"));

        for (int i = 0; i < tablesNumber; ++i) {
            root.add(new ToolTipTreeNode(new TablesNumber(res.getString("tables_number") + " ", i + 1),
                    res.getString("tables_number_hint")));
        }

        for (int i = 0; i < root.getChildCount(); ++i) {

            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            child.add(new ToolTipTreeNode(new Title(extData.getTitle(tables.get(i))), res.getString("tables_title") + " " + (i + 1)));
            child.add(new ToolTipTreeNode(new Description(extData.getDescription(tables.get(i))), res.getString("tables_description") + " " + (i + 1)));
            DefaultMutableTreeNode columnsName = new ToolTipTreeNode(new ColumnsNames(res.getString("columns_name")), res.getString("columns_name_hint") + " " + (i + 1));
            child.add(columnsName);

            for (String colName : extData.getHeadColumns(tables.get(i))) {
                columnsName.add(new ToolTipTreeNode(new ColumnsHeaders(colName), res.getString("columns_headers") + (i + 1)));
            }

            for (int m = 0; m < extData.getRows(tables.get(i)).size(); m++) {
                DefaultMutableTreeNode rowName = new ToolTipTreeNode(new RowsNames(res.getString("rows_names") + " ", m + 1), res.getString("rows_names_hint") + " " + (i + 1));
                child.add(rowName);
                for (int n = 0; n < extData.getRows(tables.get(i)).get(m).size(); n++) {
                    rowName.add(new ToolTipTreeNode(new Rows(extData.getRows(tables.get(i)).get(m).get(n)),
                            res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                    "\" " + res.getString("row_in_table") + " " + (i + 1)));
                }
            }

        }

        //create the tree by passing in the root node
        tree = new JTree(root) {
            public String getToolTipText(MouseEvent evt) {
                if (tree.getRowForLocation(evt.getX(), evt.getY()) == -1) return null;
                TreePath curPath = tree.getPathForLocation(evt.getX(), evt.getY());
                return ((ToolTipTreeNode) curPath.getLastPathComponent()).getToolTipText();
            }
        };

        tree.getSelectionModel().addTreeSelectionListener(new SelectionListener());

        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new TableTreeCellRenderer());
        tree.setToolTipText("");
        add(new JScrollPane(tree));



        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Table Vizualizator");
        this.pack();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

 /*   public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Form();
            }
        });
    }*/

    class TableTreeCellRenderer<T extends MainParent> extends DefaultTreeCellRenderer {

        private JLabel label;
        private T type;

        TableTreeCellRenderer() {
            label = new JLabel();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            type = (T) obj;
            switch (obj.getClass().getName()) {
                case "tree.data.ROOTNode":
                    IconSet(type, label, new ImageIcon("images/ROOT.png"));
                    break;
                case "tree.data.TablesNumber":
                    IconSet(type, label, new ImageIcon("images/TablesNumber.png"));
                    break;
                case "tree.data.Title":
                    IconSet(type, label, new ImageIcon("images/Title.png"));
                    break;
                case "tree.data.Description":
                    IconSet(type, label, new ImageIcon("images/Description.png"));
                    break;
                case "tree.data.ColumnsNames":
                    IconSet(type, label, new ImageIcon("images/ColumnsNames.png"));
                    break;
                case "tree.data.ColumnsHeaders":
                    IconSet(type, label, new ImageIcon("images/ColumnsHeaders.png"));
                    break;
                case "tree.data.RowsNames":
                    IconSet(type, label, new ImageIcon("images/RowsNames.png"));
                    break;
                case "tree.data.Rows":
                    IconSet(type, label, new ImageIcon("images/Rows.png"));
                    break;
            }

            return label;

        }

        private void IconSet(T obj, JLabel label, ImageIcon icon) {
            if (icon != null) {
                label.setIcon(icon);
            }
            label.setText(obj.getValue());
        }

    }

    class ToolTipTreeNode extends DefaultMutableTreeNode {
        private String toolTipText;

        public ToolTipTreeNode(Object str, String toolTipText) {
            super(str);
            this.toolTipText = toolTipText;
        }

        public String getToolTipText() {
            return toolTipText;
        }
    }

    class SelectionListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (!selectedNode.isRoot()) {
                String message = res.getString("main_question_message");
                String title = res.getString("main_question_title");
                Object[] options = {res.getString("main_question_OK"),
                        res.getString("main_question_NO")};
                int reply = JOptionPane.showOptionDialog(null,
                        message,
                        title,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (reply == JOptionPane.YES_OPTION) {

                    DefaultMutableTreeNode aNode = null;
                    Table currTable = null;
                    int index = -1;

                    switch (selectedNode.getUserObject().getClass()
                            .toString().replaceAll("class\\s+", "")) {
                        case "tree.data.TablesNumber":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent();
                            index = aNode.getIndex(selectedNode);
                            break;
                        case "tree.data.Title":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                            index = aNode.getIndex(selectedNode.getParent());
                            break;
                        case "tree.data.Description":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                            index = aNode.getIndex(selectedNode.getParent());
                            break;
                        case "tree.data.ColumnsNames":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                            index = aNode.getIndex(selectedNode.getParent());
                            break;
                        case "tree.data.ColumnsHeaders":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent().getParent();
                            index = aNode.getIndex(selectedNode.getParent().getParent());
                            break;
                        case "tree.data.RowsNames":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                            index = aNode.getIndex(selectedNode.getParent());
                            break;
                        case "tree.data.Rows":
                            aNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent().getParent();
                            index = aNode.getIndex(selectedNode.getParent().getParent());
                            break;
                    }
                    currTable = new Table(false, false, tables, index, extData);
                    currTable.setLocationRelativeTo(null);
                    currTable.setVisible(true);
                }
            }
        }
    }
}
