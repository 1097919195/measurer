package com.npclo.imeasurer.measure;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.data.measure.Part;
import com.npclo.imeasurer.utils.views.MyTextView;

import java.util.ArrayList;

/**
 * @author Endless
 *         gridview 适配器
 */
public class ItemAdapter extends ArrayAdapter<Part> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Part> mGridData = new ArrayList<>();

    public ItemAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Part> objects) {
        super(context, resource);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
    }

    public void setGridData(ArrayList<Part> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null, false);
            holder = new ViewHolder();
            holder.textView = (MyTextView) convertView.findViewById(R.id.item_title);
            holder.img1 = ((ImageButton) convertView.findViewById(R.id.btnDecrease));
            holder.img2 = ((ImageButton) convertView.findViewById(R.id.btnIncrease));
            holder.offsetView = ((EditText) convertView.findViewById(R.id.et_offset));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Part item = mGridData.get(position);

        MyTextView textView = holder.textView;
        textView.setText(item.getCn());
        EditText offsetView = holder.offsetView;

        holder.img1.setFocusable(false);
        holder.img2.setFocusable(false);

        // FIXME: 12/12/2017 使用rxbinding
        holder.img1.setOnClickListener(i -> {
                    float o;
                    String s = offsetView.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        o = Float.valueOf(s.trim());
                    } else {
                        o = 0.0f;
                    }
                    o--;
                    offsetView.setText(String.valueOf(o));
                }
        );
        holder.img2.setOnClickListener(i -> {
                    float o;
                    String s = offsetView.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        o = Float.valueOf(s.trim());
                    } else {
                        o = 0.0f;
                    }
                    o++;
                    offsetView.setText(String.valueOf(o));
                }
        );
        return convertView;
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public Part getItem(int position) {
        return mGridData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        MyTextView textView;
        ImageButton img1, img2;
        EditText offsetView;
    }
}