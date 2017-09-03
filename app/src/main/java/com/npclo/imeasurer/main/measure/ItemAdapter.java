package com.npclo.imeasurer.main.measure;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.data.measure.item.parts.Part;
import com.npclo.imeasurer.utils.views.MyTextView;

import java.util.ArrayList;

/**
 * Created by Endless on 2017/9/3.
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Part item = mGridData.get(position);

        MyTextView textView = holder.textView;
        textView.setText(item.getCn());
        textView.setTag(item.getEn());
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
    }
}