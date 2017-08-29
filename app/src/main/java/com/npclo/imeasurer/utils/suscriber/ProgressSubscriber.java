package com.npclo.imeasurer.utils.suscriber;

import android.content.Context;
import android.widget.Toast;

import com.npclo.imeasurer.utils.ApiException;
import com.npclo.imeasurer.utils.progress.ProgressCancelListener;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import rx.Subscriber;

import com.npclo.imeasurer.utils.progress.ProgressDialogHandler;


public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener listener;
    private Context context;
    private ProgressDialogHandler mProgressDialogHandler;

    public ProgressSubscriber(SubscriberOnNextListener mListener, Context mContext) {
        this.listener = mListener;
        this.context = mContext;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param o 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(Object o) {
        if (listener != null) {
            listener.onNext(o);
        }
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof HttpException) {
            Toast.makeText(context, "当前服务不可用", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ApiException) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "出错啦: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }


    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) this.unsubscribe();
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }
}
