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
import stuido.tsing.iclother.R;
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
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_uuid)
        TextView txtUuid;
        @BindView(R.id.txt_type)
        TextView txtType;

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
        holder.txtTitle.setText(String.format(Locale.getDefault(), "服务（%d）", position));
        holder.txtUuid.setText(String.format(Locale.getDefault(), "%s", bleService.getUuid()));
        holder.txtType.setText(String.format(Locale.getDefault(), "类型（%s）", bleService.getName()));
        holder.txtTitle.setTextColor(mFragment.getResources().getColor(R.color.ff5001));
        holder.txtUuid.setTextColor(mFragment.getResources().getColor(R.color.darker_gray));
        holder.txtType.setTextColor(mFragment.getResources().getColor(R.color.darker_gray));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ble_service_item, parent, false);
        itemView.setOnClickListener(v -> {
            if (onAdapterItemClickListener != null) {
                String uuid = ((TextView) v.findViewById(R.id.txt_uuid)).getText().toString();
                onAdapterItemClickListener.onAdapterViewClick(uuid);
            }
        });
        return new ViewHolder(itemView);
    }

    void setOnAdapterItemClickListener(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }
}