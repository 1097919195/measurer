package stuido.tsing.iclother.data.measure.item.parts;

/**
 * Created by Endless on 2017/8/5.
 */

public class Part {
    protected String cn;
    protected String en;
    protected String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEn() {
        return getClass().toString();
    }

    public void setEn(String en) {
        this.en = en;
    }
}
