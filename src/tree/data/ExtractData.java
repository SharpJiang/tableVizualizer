package tree.data;

import model.Tables;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtractData {

    private int numberOfTables = 0;

    private List<Tables> tables;

    private ResourceBundle res;

    public ExtractData(ResourceBundle res) {
        this.res = res;
    }

    public int getNumberOfTables(File xml) {
        try {

            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("tables-number");
            for (int i = 0; i < nList.getLength(); i++) {
                numberOfTables = Integer.parseInt(nList.item(i).getTextContent());
            }

            NodeList nTable = doc.getElementsByTagName("table");
            boolean excessTable = false;

            for (int temp = 0; temp < nTable.getLength(); temp++) {
                excessTable = false;
                Node nNode = nTable.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NamedNodeMap attributes = eElement.getAttributes();
                    int numAttrs = attributes.getLength();
                    for (int i = 0; i < numAttrs; i++) {
                        Attr attr = (Attr) attributes.item(i);
                        if (attr.getNodeName().equals("hidden") && attr.getNodeValue().equals("true")) {
                            excessTable = true;
                        }
                    }
                }
                if (excessTable) {
                    numberOfTables--;
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {}

        return numberOfTables;
    }

    public List<Tables> getContentOfAllTables(File xml) {
        try {

            tables = new ArrayList<>();

            Tables varTable = null;
            List<String> headColumns = null;
            List<String> columns = null;
            List< List<String>> lines = null;

            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("table");
            boolean excessTable = false;

            int t = 0;

            Map<Integer, Integer> tableErrors = new HashMap<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {

                excessTable = false;
                varTable = new Tables();

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    NamedNodeMap attributes = eElement.getAttributes();
                    int numAttrs = attributes.getLength();
                    for (int i = 0; i < numAttrs; i++) {
                        Attr attr = (Attr) attributes.item(i);
                        if (attr.getNodeName().equals("hidden") && attr.getNodeValue().equals("true")) {
                            excessTable = true;
                        }
                    }
                    if (excessTable) {
                        continue;
                    } else {

                        varTable.setTableErrors(tableErrors, t);
                        t++;

                        varTable.setId(eElement.getAttribute("id").trim());
                        varTable.setTitle(eElement.getElementsByTagName("table-title")
                                .item(0)
                                .getTextContent().trim().replaceAll(",", "."));
                        varTable.setDescription(eElement.getElementsByTagName("table-description")
                                .item(0)
                                .getTextContent().trim()
                                .replaceAll("\\s+", " "));
                        Node head = eElement.getElementsByTagName("head-row").item(0);

                        headColumns = new ArrayList<>();

                        NodeList headList = head.getChildNodes();
                        for (int i = 0; i < headList.getLength(); i++) {
                            Node headNode = headList.item(i);
                            if (headNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element secondeElement = (Element) headNode;
                                headColumns.add(secondeElement.getTextContent().trim().replaceAll(",", ".")
                                .replaceAll("\\s+", " "));
                            }
                        }

                        varTable.setHeadColumns(headColumns);

                        NodeList rows = eElement.getElementsByTagName("row");

                        lines = new ArrayList<>();

                        for (int j = 0; j < rows.getLength(); j++) {
                            Node rowNode = rows.item(j);
                            columns = new ArrayList<>();
                            NodeList cellsList = rowNode.getChildNodes();
                            for (int k = 0; k < cellsList.getLength(); k++) {
                                Node cellNode = cellsList.item(k);
                                if (cellNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element thirdeElement = (Element) cellNode;
                                    columns.add(thirdeElement.getTextContent().trim().replaceAll(",", ".")
                                    .replaceAll("\\s+", " ")
                                    .replaceAll("[Xx]+", res.getString("undefined"))
                                    .replaceAll("\\-$", res.getString("absence"))
                                    .replaceAll("^$", res.getString("absence")));
                                }
                            }
                            lines.add(columns);
                        }
                        varTable.setRows(lines);
                    }
                }
                tables.add(varTable);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {}

        return tables;

    }

    public String getTitle(Tables table) {
       return table.getTitle();
    }

    public String getDescription(Tables table) {
        return table.getDescription();
    }

    public List<String> getHeadColumns(Tables table) {
        return table.getHeadColumns();
    }

    public List< List<String>> getRows(Tables table) {
        return table.getRows();
    }

}
