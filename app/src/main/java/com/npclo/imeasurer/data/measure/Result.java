package com.npclo.imeasurer.data.measure;

/**
 * 根据合同量体的量体保存返回结果类
 *
 * @author Endless
 * @date 12/12/2017
 */

public class Result {
    private int mal;
    private int mno;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMal() {
        return mal;
    }

    public void setMal(int mal) {
        this.mal = mal;
    }

    public int getMno() {
        return mno;
    }

    public void setMno(int mno) {
        this.mno = mno;
    }
}
