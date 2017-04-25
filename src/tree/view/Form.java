package tree.view;

import model.Tables;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tree.data.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

import static tree.view.LanguageSetUp.res;
import static tree.view.ProgressBarDemo2.createAndShowGUI;

public class Form extends JFrame {    //pack
    public static JTree tree;
    private static List<Tables> tables;
    private static ExtractData extData;
    private static int tabNumber;
    private String version = "1.0_pre";
    private File fileToSave;
    private static String locale;
    private File inputResource;

    JMenuBar menubar;
    JMenu fileMenu;
    JMenuItem openMi;
    JMenuItem saveMi;
    JMenu impMenu;
    JMenuItem newsfMi;
    JMenuItem closeMi;
    JMenuItem exitMi ;
    JMenuItem help;
    JPanel content;
    static JFrame thisFrame;

    public Form(String locale) {
        thisFrame = this;
        this.locale = locale;
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);

        //resources>native2ascii TableVizualizer_ru_RU.txt TableVizualizer_ru_RU.properties
        //Locale.setDefault(new Locale("uk", "UA"));
        //Locale.setDefault(new Locale("en", "US"));
        //native2ascii V:\TableVizualizer\src\resources\TableVizualizer_uk_UA.txt V:\TableVizualizer\src\resources\TableVizualizer_uk_UA.properties
        //native2ascii V:\TableVizualizer\src\resources\TableVizualizer_ru_RU.txt V:\TableVizualizer\src\resources\TableVizualizer_ru_RU.properties

        createMenuBar(this);

        content = new JPanel(new BorderLayout(5, 5));

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setContentPane(content);
        this.setTitle(res.getString("tree_structure"));
        this.pack();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);

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

        this.setVisible(true);

    }

    public Form(int tblNumb, List<Tables> tab, String loc) {
        thisFrame = this;
        this.locale = loc;
        createMenuBar(this);
        saveMi.setEnabled(true);
        newsfMi.setEnabled(true);
        closeMi.setEnabled(true);

        buildTree(null, tblNumb, tab);
        content = new JPanel(new BorderLayout(5, 5));
        content.add(new JScrollPane(tree), BorderLayout.CENTER);
        content.add(createPanel(), BorderLayout.SOUTH);
        revalidate();

        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        setIconImage(image);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setContentPane(content);
        this.setTitle(res.getString("tree_structure"));
        this.pack();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);

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

        this.setVisible(true);

    }

    private void createMenuBar(Form f) {

        ImageIcon iconOpen = new ImageIcon("images/open.png");
        ImageIcon iconSave = new ImageIcon("images/save.png");
        ImageIcon csvImage = new ImageIcon("images/exportCSV.png");
        ImageIcon closeImage = new ImageIcon("images/close.png");
        ImageIcon iconExit = new ImageIcon("exit.png");
        ImageIcon reference = new ImageIcon("images/FAQ.png");

        menubar = new JMenuBar();

        fileMenu = new JMenu("Файл");
        openMi = new JMenuItem(res.getString("opn"), iconOpen);
        openMi.setMnemonic(KeyEvent.VK_O);
        openMi.setToolTipText(res.getString("see_Help"));
        openMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.CTRL_MASK));

        saveMi = new JMenuItem(res.getString("sv"), iconSave);
        saveMi.setMnemonic(KeyEvent.VK_S);
        saveMi.setToolTipText(res.getString("save_current_session"));
        saveMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        saveMi.setEnabled(false);

        impMenu = new JMenu(res.getString("exprt"));

        newsfMi = new JMenuItem("<html><i style=\"color:red;\">CSV</i></html>", csvImage);
        newsfMi.setMnemonic(KeyEvent.VK_1);
        newsfMi.setToolTipText(res.getString("exprt_csv"));
        newsfMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
                ActionEvent.CTRL_MASK));
        newsfMi.setEnabled(false);

        impMenu.add(newsfMi);

        closeMi = new JMenuItem(res.getString("cls"), closeImage);
        closeMi.setMnemonic(KeyEvent.VK_W);
        closeMi.setToolTipText(res.getString("close_current_document"));
        closeMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                ActionEvent.CTRL_MASK));
        closeMi.setEnabled(false);

        exitMi = new JMenuItem(res.getString("ext"), iconExit);
        exitMi.setMnemonic(KeyEvent.VK_F4);
        exitMi.setToolTipText(res.getString("ext_prog"));
        exitMi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
                ActionEvent.ALT_MASK));

        help = new JMenuItem(res.getString("reference"), reference);
        help.setMnemonic(KeyEvent.VK_F1);
        help.setToolTipText(res.getString("detail_info"));
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                ActionEvent.CTRL_MASK));

        openMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();

                UIManager.put("FileChooser.openDialogTitleText", res.getString("opn_fl"));
                UIManager.put("FileChooser.lookInLabelText", res.getString("overview"));
                UIManager.put("FileChooser.openButtonText", res.getString("opn_without_dots"));
                UIManager.put("FileChooser.cancelButtonText", res.getString("cnsl"));
                UIManager.put("FileChooser.fileNameLabelText", res.getString("file_name"));
                UIManager.put("FileChooser.filesOfTypeLabelText", res.getString("file_extension"));
                UIManager.put("FileChooser.upFolderToolTipText", res.getString("up_one_level"));
                UIManager.put("FileChooser.homeFolderToolTipText", res.getString("desktop"));
                UIManager.put("FileChooser.newFolderToolTipText", res.getString("create_new_dir"));
                UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
                UIManager.put("FileChooser.newFolderButtonText", res.getString("create_new_dir"));
                UIManager.put("FileChooser.detailsViewButtonToolTipText", res.getString("detail"));
                SwingUtilities.updateComponentTreeUI(fileopen);

                    /*FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                            "xml files (*.xml)", "xml");
                    fileopen.setFileFilter(xmlfilter);*/

                int ret = fileopen.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    inputResource = fileopen.getSelectedFile();
                    if(FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("xml")) {
                        buildTree(inputResource.getAbsolutePath(), 0, null);
                        content.add(new JScrollPane(tree), BorderLayout.CENTER);
                        content.add(createPanel(), BorderLayout.SOUTH);
                        saveMi.setEnabled(true);
                        newsfMi.setEnabled(true);
                        closeMi.setEnabled(true);
                        revalidate();

                    } else {
                        if (!FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("doc") &&
                                !FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("docx") &&
                                !FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("xls") &&
                                !FilenameUtils.getExtension(inputResource.getAbsolutePath()).equals("odt")) {
                            JOptionPane.showMessageDialog(null,
                                    res.getString("file_open_err") +
                                            "\n" +
                                    res.getString("extns") + " .docx;.doc;.odt" + res.getString("or") + ".xls\n" +
                                            res.getString("file_open_err_add"),
                                    res.getString("file_open_err_two"), JOptionPane.ERROR_MESSAGE);
                        } else {

                            createAndShowGUI(content, f, inputResource);
                                //buildTree(inputResource.getAbsolutePath(), 0, null);
                                saveMi.setEnabled(true);
                                newsfMi.setEnabled(true);
                                closeMi.setEnabled(true);

                            //buildTree(inputResource.getAbsolutePath(), 0, null);
                        }
                    }
                        /*saveAsMenuItem.setEnabled(true);
                        exportMenu.setEnabled(true);
                        closejMenuItem.setEnabled(true);*/
                }
            }
        });

        saveMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle(res.getString("save_as"));

                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    fileToSave = fileChooser.getSelectedFile();

                    if (!FilenameUtils.getExtension(fileToSave.getAbsolutePath()).equals("xml")) {
                        fileToSave = new File(fileToSave.toString() + ".xml");
                    }

                    saveFileExists();

                }
            }
        });

        newsfMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = new String[2];
                options[0] = res.getString("main_question_OK");
                options[1] = res.getString("main_question_NO");
                int n = JOptionPane.showOptionDialog(null, res.getString("export_doc"),
                        res.getString("export_doc_two"), 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (n == 0) {
                    exportToCSV();
                }

            }
        });

        closeMi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = new String[2];
                options[0] = res.getString("main_question_OK");
                options[1] = res.getString("main_question_NO");
                int n = JOptionPane.showOptionDialog(null, res.getString("ask_before_close"),
                        res.getString("ask_before_close_two"), 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (n == 0) {
                    if (fileToSave != null) {
                        saveFileExists();
                    } else {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle(res.getString("save_as"));

                        int userSelection = fileChooser.showSaveDialog(null);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {

                            fileToSave = fileChooser.getSelectedFile();

                            if (!FilenameUtils.getExtension(fileToSave.getAbsolutePath()).equals("xml")) {
                                fileToSave = new File(fileToSave.toString() + ".xml");
                            }

                            saveFileExists();

                        }
                    }
                    setVisible(false);
                    Form form = new Form(locale);
                    dispose();
                } else {
                    setVisible(false);
                    Form f = new Form(locale);
                    dispose();
                }
            }
        });

        exitMi.addActionListener((ActionEvent event) -> {
            String ObjButtons[] = {res.getString("main_question_OK"), res.getString("main_question_NO")};
            int PromptResult = JOptionPane.showOptionDialog(null,
                    res.getString("want_to_leave"),
                    res.getString("trying_to_quit"),
                    JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
            if(PromptResult==JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File htmlFile = new File("reference/help.html");
                try {
                    Desktop.getDesktop().browse(htmlFile.toURI());
                } catch (IOException e1) {}
            }
        });

        fileMenu.add(openMi);
        fileMenu.add(saveMi);
        fileMenu.add(impMenu);
        fileMenu.add(closeMi);
        fileMenu.addSeparator();
        fileMenu.add(exitMi);
        fileMenu.addSeparator();
        fileMenu.add(help);
        menubar.add(fileMenu);

        setJMenuBar(menubar);

    }

    private class MenuItemAction extends AbstractAction {

        public MenuItemAction(String text, ImageIcon icon,
                              Integer mnemonic) {
            super(text);

            putValue(SMALL_ICON, icon);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public static void buildTree(String path, int tabNum, List<Tables> tab) {

        //create the root node
        DefaultMutableTreeNode root = new ToolTipTreeNode(new ROOTNode(res.getString("document_title")), res.getString("root_hint"));
        //create the child nodes
        extData = new ExtractData(res);

        if (path == null) {
            tabNumber = tabNum;
            tables = tab;

        } else {

            tabNumber = extData.getNumberOfTables(new File(path));
            tables = extData.getContentOfAllTables(new File(path));

        }

        for (int i = 0; i < tabNumber; ++i) {
            root.add(new ToolTipTreeNode(new TablesNumber(res.getString("tables_number") + " ", i + 1),
                    res.getString("tables_number_hint")));
        }

        Set<Integer> tablesWithErrHashSet = new HashSet<>();
        Map<String, ArrayList<String>> rowsWithErrHashSet = new HashMap<>();
        //Map<Integer, ArrayList<Integer>> rowsWithErrHashSet = new HashMap<>();

        for (int i = 0; i < root.getChildCount(); ++i) {

            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            Title title = new Title(extData.getTitle(tables.get(i)));
            child.add(new ToolTipTreeNode(title, res.getString("tables_title") + " " + (i + 1)));
            child.add(new ToolTipTreeNode(new Description(extData.getDescription(tables.get(i))), res.getString("tables_description") + " " + (i + 1)));
            DefaultMutableTreeNode columnsName = new ToolTipTreeNode(new ColumnsNames(res.getString("columns_name")), res.getString("columns_name_hint") + " " + (i + 1));
            child.add(columnsName);

            for (String colName : extData.getHeadColumns(tables.get(i))) {
                columnsName.add(new ToolTipTreeNode(new ColumnsHeaders(colName), res.getString("columns_headers") + (i + 1)));
            }
            if (path == null) {
                for (int m = 0; m < extData.getRows(tables.get(i)).size(); m++) {
                    DefaultMutableTreeNode rowName = new ToolTipTreeNode(
                            new RowsNames("<html><font color=\"green\">" + res.getString("rows_names")
                                    + " " + (m + 1) + "</font></html>"
                                    , m + 1), res.getString("rows_names_hint") + " " + (i + 1));
                    child.add(rowName);
                    for (int n = 0; n < extData.getRows(tables.get(i)).get(m).size(); n++) {
                        if (extData.getRows(tables.get(i)).get(m).get(n).equals(res.getString("absence"))) {
                            rowName.add(new ToolTipTreeNode(
                                    new Rows(
                                            "<html><font color=\"red\">" +
                                                    extData.getRows(tables.get(i)).get(m).get(n)
                                                    + "</font></html>"),
                                    res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                            "\" " + res.getString("row_in_table") + " " + (i + 1)));
                            tablesWithErrHashSet.add(i);
                            addValues(rowsWithErrHashSet, String.valueOf(i), String.valueOf(m));

                        } else if (extData.getRows(tables.get(i)).get(m).get(n).equals(res.getString("undefined"))) {
                            rowName.add(new ToolTipTreeNode(
                                    new Rows(
                                            "<html><font color=\"rgb(255,143,60)\">" +
                                                    extData.getRows(tables.get(i)).get(m).get(n)
                                                    + "</font></html>"),
                                    res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                            "\" " + res.getString("row_in_table") + " " + (i + 1)));
                            tablesWithErrHashSet.add(i);
                            addValues(rowsWithErrHashSet, String.valueOf(i), String.valueOf(m));

                        } else {
                            rowName.add(new ToolTipTreeNode(
                                    new Rows(
                                            "<html><font color=\"green\">" +
                                                    extData.getRows(tables.get(i)).get(m).get(n)
                                                    + "</font></html>"),
                                    res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                            "\" " + res.getString("row_in_table") + " " + (i + 1)));
                        }
                    }
                }
            } else {
                for (int m = 0; m < extData.getRows(tables.get(i)).size(); m++) {
                    DefaultMutableTreeNode rowName = new ToolTipTreeNode(
                            new RowsNames("<html><font color=\"green\">" + res.getString("rows_names")
                                    + " " + (m + 1) + "</font></html>"
                                    , m + 1), res.getString("rows_names_hint") + " " + (i + 1));
                    child.add(rowName);
                    for (int n = 0; n < extData.getRows(tables.get(i)).get(m).size(); n++) {
                        if (extData.getRows(tables.get(i)).get(m).get(n).equals(res.getString("absence"))) {
                            rowName.add(new ToolTipTreeNode(
                                    new Rows(
                                            "<html><font color=\"red\">" +
                                                    extData.getRows(tables.get(i)).get(m).get(n)
                                                    + "</font></html>"),
                                    res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                            "\" " + res.getString("row_in_table") + " " + (i + 1)));
                            tablesWithErrHashSet.add(i);
                            addValues(rowsWithErrHashSet, String.valueOf(i), String.valueOf(m));

                        } else if (extData.getRows(tables.get(i)).get(m).get(n).equals(res.getString("undefined"))) {
                            rowName.add(new ToolTipTreeNode(
                                    new Rows(
                                            "<html><font color=\"rgb(255,143,60)\">" +
                                                    extData.getRows(tables.get(i)).get(m).get(n)
                                                    + "</font></html>"),
                                    res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                            "\" " + res.getString("row_in_table") + " " + (i + 1)));
                            tablesWithErrHashSet.add(i);
                            addValues(rowsWithErrHashSet, String.valueOf(i), String.valueOf(m));

                        } else {
                            rowName.add(new ToolTipTreeNode(
                                    new Rows(
                                            "<html><font color=\"green\">" +
                                                    extData.getRows(tables.get(i)).get(m).get(n)
                                                    + "</font></html>"),
                                    res.getString("rows") + " \"" + extData.getHeadColumns(tables.get(i)).get(n) +
                                            "\" " + res.getString("row_in_table") + " " + (i + 1)));
                        }
                    }
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


        for (int index : tablesWithErrHashSet) {
            DefaultTreeModel mdl = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode curr = (DefaultMutableTreeNode) root.getChildAt(index);
            curr.setUserObject(new TablesNumber("<html><font color=\"red\">" +
                    res.getString("tables_number") + " " + (index + 1) + "</font></html>", index + 1));
            mdl.nodeChanged(curr);
        }

        // Get data.
        Iterator it = rowsWithErrHashSet.keySet().iterator();
        ArrayList tempList = null;

        while (it.hasNext()) {
            String key = it.next().toString();
            tempList = rowsWithErrHashSet.get(key);
            if (tempList != null) {
                for (Object value : tempList) {
                    DefaultTreeModel mdl = (DefaultTreeModel) tree.getModel();
                    DefaultMutableTreeNode curr = (DefaultMutableTreeNode)
                            root.getChildAt(Integer.parseInt(key)).getChildAt(Integer.parseInt(value.toString()) + 3);
                    curr.setUserObject(new RowsNames("<html><font color=\"red\">" +
                            res.getString("rows_names") + " " + (Integer.parseInt(value.toString()) + 1) +
                            "</font></html>", Integer.parseInt(value.toString()) + 1));
                    mdl.nodeChanged(curr);
                }
            }
        }

        tree.getSelectionModel().addTreeSelectionListener(new SelectionListener());

        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new TableTreeCellRenderer());
        tree.setToolTipText("");

        //new ProgressSample();
        /*ProgressSample prosSmpl = new ProgressSample();
        Thread progBar = new Thread(prosSmpl);
        progBar.start();

        synchronized (monitor) {
            System.out.println(prosSmpl.getProgressBarStatus());
                try {
                    monitor.wait();
                }
                catch (InterruptedException e) { }
            }*/

        //!!!!!!!!!!!!!!!dfsfs
        //createAndShowGUI(content, tree, this, inputResource);


        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(content, tree);
            }
        });*/

        /*content.add(new JScrollPane(tree), BorderLayout.CENTER);
        content.add(createPanel(), BorderLayout.SOUTH);
        revalidate();*/


        /*System.out.println("In Form: " + isDone);
        if(isDone) {
            System.out.println("G");
            content.add(new JScrollPane(tree), BorderLayout.CENTER);
            content.add(createPanel(), BorderLayout.SOUTH);
            revalidate();
        }*/

    }



    public static JPanel createPanel() {
        BorderLayout borlay = new BorderLayout();
        JPanel pnl = new JPanel(borlay);
        pnl.setPreferredSize(new Dimension(400, 200));
        pnl.setBorder(BorderFactory.createLineBorder(new Color(0x53CFFF), 5));

        JLabel lbls_zero = new JLabel(res.getString("CONVENTIONAL_SYMBOLS"));
        lbls_zero.setHorizontalAlignment(JLabel.CENTER);
        lbls_zero.setBorder(BorderFactory.createLineBorder(new Color(0x2E77FF), 5));

        JLabel lbls_fir = new JLabel("<html>" +
                "<p align=\"middle\" style=\"font-size:13px; text-indent: 1.5em;\">" + res.getString("panel_info") + "<br>" +
                "<i style=\"color:black; font-family:Serif; font-size:12px;\">" + "Текст " + "</i>" + res.getString("or") + " " +
                "<i style=\"color:green; font-family:Serif; font-size:12px;\">Текст</i> - <b>" + res.getString("valid_data") + "</b><br>" +
                "<i style=\"color:rgb(255,143,60); font-family:Serif; font-size:12px;\">Текст</i> - <b><i>" + res.getString("maybe") + "</i>," +
                res.getString("invalid_data") + "</b><br>" +
                "<i style=\"color:red; font-family:Serif; font-size:12px;\">Текст</i> - <b>" + res.getString("invalid_data") +
                "</b></p>" +
                "</html>");
        lbls_fir.setHorizontalAlignment(JLabel.CENTER);
        lbls_fir.setBorder(BorderFactory.createLineBorder(new Color(0xFF1C15), 5));

        JLabel lbls_footer = new JLabel("<html><p style=\"text-align:center;\">&copy TableVizualizer <br />" +
                "version: 1.0</p></html>");
        lbls_footer.setHorizontalAlignment(JLabel.CENTER);

        lbls_footer.setBorder(BorderFactory.createLineBorder(new Color(0x11FF44), 5));
        pnl.add(lbls_zero, BorderLayout.NORTH);
        pnl.add(lbls_fir, BorderLayout.CENTER);
        pnl.add(lbls_footer, BorderLayout.SOUTH);

        return pnl;
    }

    static class TableTreeCellRenderer<T extends MainParent> extends DefaultTreeCellRenderer {

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

    static class ToolTipTreeNode extends DefaultMutableTreeNode {
        private String toolTipText;

        public ToolTipTreeNode(Object str, String toolTipText) {
            super(str);
            this.toolTipText = toolTipText;
        }

        public String getToolTipText() {
            return toolTipText;
        }
    }

    static class SelectionListener implements TreeSelectionListener {
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
                    currTable = new Table(thisFrame, false, false, tables, index, extData, tabNumber, locale);
                    currTable.setLocationRelativeTo(null);
                    currTable.setVisible(true);
                    thisFrame.setVisible(false);
                    thisFrame.dispose();
                }
            }
        }
    }

    private static void addValues(Map<String, ArrayList<String>> hashMap, String key, String value) {
        ArrayList tempList = null;
        if (hashMap.containsKey(key)) {
            tempList = hashMap.get(key);
            if (tempList == null)
                tempList = new ArrayList();
            tempList.add(value);
        } else {
            tempList = new ArrayList();
            tempList.add(value);
        }
        hashMap.put(key, tempList);
    }

    private void saveFileExists() {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = f.newDocumentBuilder();

            Document doc = builder.newDocument();
            Element AllTables = doc.createElement("tables");
            doc.appendChild(AllTables);

            Element parserID = doc.createElement("vizualizer-xml");
            Attr versionAttr = doc.createAttribute("version");
            versionAttr.setValue(version);
            parserID.setAttributeNode(versionAttr);
            AllTables.appendChild(parserID);

            Element tablesNumber = doc.createElement("tables-number");
            tablesNumber.appendChild(doc.createTextNode(Integer.toString(tabNumber)));
            AllTables.appendChild(tablesNumber);

            fileToSave.createNewFile();

            Tables tempTable = null;

            for (int t = 0; t < tables.size(); t++) {

                tempTable = tables.get(t);

                Element currTable = doc.createElement("table");
                Attr idAttr = doc.createAttribute("id");

                idAttr.setValue(tempTable.getId());

                currTable.setAttributeNode(idAttr);
                AllTables.appendChild(currTable);

                addXMLData(doc, tempTable, currTable);

            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fileToSave);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | IOException | TransformerException ex) {
        }
    }

    public static void addXMLData(Document doc, Tables tempTable, Element currTable) {

        Element tableTitle = doc.createElement("table-title");
        tableTitle.appendChild(doc.createTextNode(tempTable.getTitle()));
        currTable.appendChild(tableTitle);

        Element tableDescription = doc.createElement("table-description");
        tableDescription.appendChild(doc.createTextNode(tempTable.getDescription()));
        currTable.appendChild(tableDescription);

        Element tableHeader = doc.createElement("head-row");

        for (int k = 0; k < tempTable.getHeadColumns().size(); k++) {
            Element tableHeaderCell = doc.createElement("cell");
            tableHeaderCell.appendChild(doc.createTextNode(tempTable.getHeadColumns().get(k)));
            tableHeader.appendChild(tableHeaderCell);
        }

        currTable.appendChild(tableHeader);

        for (int m = 0; m < tempTable.getRows().size(); m++) {
            Element tableRow = doc.createElement("row");
            for (int n = 0; n < tempTable.getRows().get(m).size(); n++) {
                Element tableRowCell = doc.createElement("cell");
                tableRowCell.appendChild(doc.createTextNode(tempTable.getRows().get(m).get(n)));
                tableRow.appendChild(tableRowCell);
            }
            currTable.appendChild(tableRow);
        }
    }

    private void exportToCSV() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(res.getString("csv_exprt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File csvFile = fileChooser.getSelectedFile();

            if (!FilenameUtils.getExtension(csvFile.getAbsolutePath()).equals("csv")) {
                csvFile = new File(csvFile.toString() + ".csv");
            }

            try {

                Tables tempTable = null;
                PrintWriter writer = null;

                String path = csvFile.getAbsolutePath();

                StringBuilder sb = null;

                System.out.println("tabNumber: " + tabNumber);


                for (int t = 0; t < tabNumber; t++) {

                    sb = new StringBuilder(path);

                    String str = sb.substring(0, sb.indexOf(".csv"));

                    sb = new StringBuilder(str);

                    tempTable = tables.get(t);

                    sb.append("(").append(transliterate(tempTable.getTitle())).append(+t+").csv");
                    writer = new PrintWriter(new File(sb.toString()), "UTF-8");

                    for (int k = 0; k < tempTable.getHeadColumns().size(); k++) {
                        writer.print(tempTable.getHeadColumns().get(k));
                        if (k != tempTable.getHeadColumns().size() - 1) {
                            writer.print(",");
                        } else {
                            writer.print("\n");
                        }
                    }

                    for (int m = 0; m < tempTable.getRows().size(); m++) {
                        for (int n = 0; n < tempTable.getRows().get(m).size(); n++) {
                            writer.print(tempTable.getRows().get(m).get(n));
                            if (n != tempTable.getRows().get(m).size() - 1) {
                                writer.print(",");
                            } else {
                                writer.print("\n");
                            }
                        }
                    }
                    writer.close();
                }

            } catch (IOException e) {
            }

        }
    }

    public static String transliterate(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Б', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();

    }
}
