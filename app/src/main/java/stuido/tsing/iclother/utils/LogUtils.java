package stuido.tsing.iclother.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class LogUtils {
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_ERROR = 5;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_NONE = 0;
    public static final int LEVEL_VERBOSE = 1;
    public static final int LEVEL_WARN = 4;
    private static int mDebuggable;
    private static final Object mLogLock;
    private static String mTag = "IMeasure_debugger";
    private static long mTimestamp;

    static {
        mDebuggable = LEVEL_ERROR;
        mTimestamp = 0L;
        mLogLock = new Object();
    }

    public static void d(String paramString) {
        if (mDebuggable >= LEVEL_DEBUG)
            Log.d(mTag, paramString);
    }

    public static void e(String paramString) {
        if (mDebuggable >= LEVEL_ERROR)
            Log.e(mTag, paramString);
    }

    public static void e(String paramString, Throwable paramThrowable) {
        if ((mDebuggable >= LEVEL_ERROR) && (paramString != null))
            Log.e(mTag, paramString, paramThrowable);
    }

    public static void e(Throwable paramThrowable) {
        if (mDebuggable >= LEVEL_ERROR)
            Log.e(mTag, "", paramThrowable);
    }

    public static void elapsed(String paramString) {
        long l1 = System.currentTimeMillis();
        long l2 = l1 - mTimestamp;
        mTimestamp = l1;
        e("[Elapsed：" + l2 + "]" + paramString);
    }

    public static void i(String paramString) {
        if (mDebuggable >= LEVEL_INFO)
            Log.i(mTag, paramString);
    }

    public static void log2File(String paramString1, String paramString2) {
        log2File(paramString1, paramString2, true);
    }

    public static void log2File(String paramString1, String paramString2, boolean paramBoolean) {
        synchronized (mLogLock) {
//            FileUtils.writeFile(paramString1 + "\r\n", paramString2, paramBoolean);
            return;
        }
    }

    public static void msgStartTime(String paramString) {
        mTimestamp = System.currentTimeMillis();
        if (!TextUtils.isEmpty(paramString))
            e("[Started：" + mTimestamp + "]" + paramString);
    }

    public static <T> void printArray(T[] paramArrayOfT) {
        if ((paramArrayOfT == null) || (paramArrayOfT.length < LEVEL_VERBOSE))
            return;
        int i = paramArrayOfT.length;
        i("---begin---");
        for (int j = 0; j < i; j++)
            i(j + ":" + paramArrayOfT[j].toString());
        i("---end---");
    }

    public static <T> void printList(List<T> paramList) {
        if ((paramList == null) || (paramList.size() < LEVEL_VERBOSE))
            return;
        int i = paramList.size();
        i("---begin---");
        for (int j = 0; j < i; j++)
            i(j + ":" + paramList.get(j).toString());
        i("---end---");
    }

    public static void v(String paramString) {
        if (mDebuggable >= LEVEL_VERBOSE)
            Log.v(mTag, paramString);
    }

    public static void w(String paramString) {
        if (mDebuggable >= LEVEL_WARN)
            Log.w(mTag, paramString);
    }

    public static void w(String paramString, Throwable paramThrowable) {
        if ((mDebuggable >= LEVEL_WARN) && (paramString != null))
            Log.w(mTag, paramString, paramThrowable);
    }

    public static void w(Throwable paramThrowable) {
        if (mDebuggable >= LEVEL_WARN)
            Log.w(mTag, "", paramThrowable);
    }
}