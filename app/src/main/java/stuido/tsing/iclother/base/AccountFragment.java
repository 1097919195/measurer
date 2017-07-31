package stuido.tsing.iclother.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public abstract class AccountFragment extends Fragment {
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        initView();
        initEvent();
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterCreate(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initEvent();

    protected void afterCreate(Bundle savedInstanceState) {
    }

    protected void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    public void switchContent(@NonNull Fragment from, @NonNull Fragment to, int id) {
        checkNotNull(from);
        checkNotNull(to);

//        if (mContent != to) {
//            mContent = to;
        FragmentTransaction transaction = getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (!to.isAdded()) {    // 先判断是否被add过
            transaction.hide(from).add(id, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
//        }
    }

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //判断fragment是否已添加到activity中 // FIXME: 2017/7/24 is it right?
        if (!fragment.isAdded()) {
            transaction.add(frameId, fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
    }

}
