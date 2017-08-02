package stuido.tsing.iclother.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stuido.tsing.iclother.R;

/**
 * Created by Endless on 2017/8/1.
 */

public class MeasureFragment extends Fragment implements MeasureContract.View {
    private MeasurePresenter mPresenter;

    public MeasureFragment() {
    }

    public static MeasureFragment newInstance() {
        return new MeasureFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mPresenter.unsubscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.measure_frag, container, false);

        return root;
    }

    @Override
    public void setPresenter(MeasureContract.Presenter presenter) {

    }
}
