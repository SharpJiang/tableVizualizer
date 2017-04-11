package model;
import java.util.List;
import java.util.Map;

public class Tables {

    private String id;
    private String title;
    private String description;
    private List<String> headColumns;
    List< List<String>> rows;

    private Map<Integer, Integer> tableErrors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getHeadColumns() {
        return headColumns;
    }

    public void setHeadColumns(List<String> headColumns) {
        this.headColumns = headColumns;
    }

    public List< List<String>> getRows() {
        return rows;
    }

    public void setRows(List< List<String>> rows) {
        this.rows = rows;
    }

    public void setCell(List< List<String>> rows, int row, int column, String value) {
        List<String> r = rows.get(row);
        r.set(column, value);
        rows.set(row, r);
    }

    public void setHeaderCell(List<String> header, int index, String value) {
        header.set(index, value);
    }

    public void setTableErrors(Map<Integer, Integer> tableErrors, int t) {
        this.tableErrors = tableErrors;
        this.tableErrors.put(t, 0);
    }

    public int getTableErrors(Map<Integer, Integer> tableErrors, int tableNumber) {
        return tableErrors.get(tableNumber);
    }

    public Map<Integer, Integer> getTableErrors() {
        return tableErrors;
    }

    public void incrementTableErrors(Map<Integer, Integer> tableErrors, int tableNumber) {
        tableErrors.put(tableNumber, getTableErrors(tableErrors, tableNumber) + 1);
    }

    public void decrementTableErrors(Map<Integer, Integer> tableErrors, int tableNumber) {
        if (getTableErrors(tableErrors, tableNumber) > 0) {
            tableErrors.put(tableNumber, getTableErrors(tableErrors, tableNumber) - 1);
        }
    }

}
