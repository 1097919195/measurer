package com.npclo.imeasurer.measure;

import android.support.v4.app.Fragment;
import android.widget.TableRow;

public class MeasureFragment extends Fragment {

    public MeasureFragment() {
    }

    public void updateMeasureData(float length, int angle, int battery) {
//        rulerBattery.setText(battery + "%");
//        rulerState.setTextColor(getResources().getColor(R.color.green));
//        if (sexRadioGroup.getCheckedRadioButtonId() == radioMale.getId()) {
//            for (TableRow row : maleRows) {
//                if (assignValue(length, angle, row)) break;
//            }
//        } else {
//            for (TableRow row : femaleRows) {
//                if (assignValue(length, angle, row)) break;
//            }
//        }
    }

    /**
     * 结果赋值，有几个字段需要的结果为角度
     *
     * @param length 长度
     * @param row    行
     * @return boolean
     */
    private boolean assignValue(float length, float angle, TableRow row) {
//        EditText editText = (EditText) row.getChildAt(1);
//        if (TextUtils.isEmpty(editText.getText().toString())) {// TODO: 2017/8/24 修改赋值
//            String tag = (String) editText.getTag();
//            String cn;
//            try {
//                Part part = (Part) Class.forName(PART_PACKAGE + "." + tag).newInstance();
//                cn = part.getCn();
//                String value;
//                if (angleList.contains(tag)) {
//                    editText.setText(angle + "");
//                    value = angle + "";
//                } else {
//                    editText.setText(length + "");
//                    value = length + "";
//                }
//                if (speechSynthesizer != null) {
//                    String result = cn + "，结果为" + value;
//                    String[] nextString;
//
//                    if (sexRadioGroup.getCheckedRadioButtonId() == radioMale.getId()) {
//                        maleMeasureSequence = getResources().getStringArray(R.array.male_items_sequence);
//                        nextString = getNextString(cn, maleMeasureSequence);
//                    } else {
//                        nextString = getNextString(cn, getResources().getStringArray(R.array.items_sequence));
//                    }
//                    if (!TextUtils.isEmpty(nextString[0]))
//                        speechSynthesizer.playText(result + "      下一个测量部位" + nextString[0]);
//                    if (!TextUtils.isEmpty(nextString[1]))
//                        speechSynthesizer.playText(result + nextString[1]);
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (java.lang.InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
        return false;
    }

    /**
     * 获取下一个测量字段
     *
     * @param cn     当前字段
     * @param arrays 字段数组
     * @return 包含结果的数组
     */
    private String[] getNextString(String cn, String[] arrays) {
        String last = null;
        String next = null;
        String[] strings = new String[2];
        for (int m = 0, l = arrays.length; m < l; m++) {
            if (arrays[m].equals(cn)) {
                if (m == l - 1) {
                    last = "所有部位测量完成";
                } else {
                    next = arrays[m + 1];
                }
            }
        }
        strings[0] = next;
        strings[1] = last;
        return strings;
    }

}