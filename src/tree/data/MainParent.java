package tree.data;

/**
 * Created by Victor on 31.03.2017.
 */
public class MainParent {
    public String value;

    public MainParent(String value) {
        this.value = value;
    }

    public MainParent(String value, int number) {
        this.value = value + number;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
