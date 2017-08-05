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
import stuido.tsing.iclother.data.ble.BleCharacter;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.ViewHolder> {

    private List<BleCharacter> mDatas;
    private MeasureFragment mFragment;
    private OnAdapterItemClickListener onAdapterItemClickListener;

    public CharacterListAdapter(MeasureFragment fragment, List<BleCharacter> bleCharacters) {
        mDatas = bleCharacters;
        mFragment = fragment;
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
        final BleCharacter bleCharacter = mDatas.get(position);
        holder.txtTitle.setText(String.format(Locale.getDefault(), "特征（%d）", position));
        holder.txtUuid.setText(String.format(Locale.getDefault(), "%s", bleCharacter.getUuid()));
        holder.txtType.setText(String.format(Locale.getDefault(), "特性（%s）", bleCharacter.getUuid()));
        holder.txtTitle.setTextColor(mFragment.getResources().getColor(R.color.darker_gray));
        holder.txtUuid.setTextColor(mFragment.getResources().getColor(R.color.darker_gray));
        holder.txtType.setTextColor(mFragment.getResources().getColor(R.color.darker_gray));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ble_service_item, parent, false);
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