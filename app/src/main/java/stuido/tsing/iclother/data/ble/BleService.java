package stuido.tsing.iclother.data.ble;

/**
 * Created by Endless on 2017/8/4.
 */

public class BleService {
    private String name;
    private String uuid;

    public BleService(String n, String u) {
        name = n;
        uuid = u;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
