package com.npclo.imeasurer.data.measure;

import java.util.List;

/**
 * @author Endless
 * @date 12/12/2017
 */

public class Contract {
    private int num;
    private List<Item> data;
    private String name;
    private String id;
    private int measured = 0;

    public int getMeasured() {
        return measured;
    }

    public void setMeasured(int measured) {
        this.measured = measured;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
