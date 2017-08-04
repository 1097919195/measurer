package stuido.tsing.iclother.measure;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import stuido.tsing.iclother.data.ble.BleService;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {
    private MeasureFragment mFragment;
    private List<BleService> mDatas;
    private OnAdapterItemClickListener onAdapterItemClickListener;

    public ServiceListAdapter(MeasureFragment fragment, List<BleService> bleServiceList) {
        mFragment = fragment;
        mDatas = bleServiceList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView line1;
        @BindView(android.R.id.text2)
        TextView line2;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(String uuid);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BleService bleService = mDatas.get(position);
        holder.line1.setText(String.format(Locale.getDefault(), "Service: (%s)", bleService.getName()));
        holder.line2.setText(String.format(Locale.getDefault(), "%s", bleService.getUuid()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.two_line_list_item, parent, false);
        itemView.setOnClickListener(v -> {
            if (onAdapterItemClickListener != null) {
                String uuid = ((TextView) v.findViewById(android.R.id.text2)).getText().toString();
                onAdapterItemClickListener.onAdapterViewClick(uuid);
            }
        });
        return new ViewHolder(itemView);
    }

    void setOnAdapterItemClickListener(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }
}