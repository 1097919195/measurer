package com.npclo.imeasurer.data.measure;


/**
 * @author Endless
 * @date 2017/8/5
 */

public class Part {
    private String cn;
    private float value;
    private float offset;
    private boolean isAngle;

    public Part() {
    }

    public Part(String cn, float value) {
        this.cn = cn;
        this.value = value;
    }

    public Part(String cn, float value, float offset) {
        this.cn = cn;
        this.value = value;
        this.offset = offset;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public Part(String cn, boolean isAngle) {
        this.cn = cn;
        this.isAngle = isAngle;
    }

    public boolean isAngle() {
        return isAngle;
    }

    public void setAngle(boolean angle) {
        isAngle = angle;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
