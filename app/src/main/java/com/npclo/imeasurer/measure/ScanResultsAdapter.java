package com.npclo.imeasurer.measure;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.data.ble.BleDevice;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class ScanResultsAdapter extends RecyclerView.Adapter<ScanResultsAdapter.ViewHolder> {

    private MeasureFragment mFragment;
    private List<BleDevice> mDatas;

    public ScanResultsAdapter(MeasureFragment fragment, List<BleDevice> devices) {
        mFragment = fragment;
        mDatas = devices;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_mac)
        TextView txtMac;
        @BindView(R.id.txt_rssi)
        TextView txtRssi;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }

    private OnAdapterItemClickListener onAdapterItemClickListener;

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BleDevice device = mDatas.get(position);
        holder.txtName.setText(String.format(Locale.getDefault(), "%s", device.getName()));
        holder.txtName.setTextColor(mFragment.getResources().getColor(R.color.ble_device_name));
        holder.txtMac.setText(String.format(Locale.getDefault(), "%s", device.getAddress()));
        holder.txtMac.setTextColor(mFragment.getResources().getColor(R.color.ble_device_address));
        holder.txtRssi.setText(String.format(Locale.getDefault(), "%s", device.getRssi()));
        holder.txtRssi.setTextColor(mFragment.getResources().getColor(R.color.black));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ble_list_item, parent, false);
        itemView.setOnClickListener(v -> {
            if (onAdapterItemClickListener != null) {
                onAdapterItemClickListener.onAdapterViewClick(v);
            }
        });
        return new ViewHolder(itemView);
    }

    void setOnAdapterItemClickListener(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }
}
