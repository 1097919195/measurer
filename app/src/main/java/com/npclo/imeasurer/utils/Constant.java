package com.npclo.imeasurer.utils;

import com.npclo.imeasurer.BuildConfig;
import com.npclo.imeasurer.R;

/**
 * @author Endless
 * @date 2017/12/1
 */

public class Constant {
    public static final int[] ICONS_DRAWABLES = new int[]{
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";
    public static final String BASE_API_URL = "://www.npclo.com/api/";
    public static final String MANUAL = "manual";
    public static final String AUTO = "auto";
    public static final int USER_PWD = 1;
    public static final int USER_FEEDBACK = 2;
    public static final int USER_CONTACT = 3;
    public static final int USER_INSTRUCTION = 4;
    public static final String APP_KEY = "hhzjkm3l5akcz5oiflyzmmmitzrhmsfd73lyl3y2";
    public static final String APP_SECRET = "29aa998c451d64d9334269546a4021b8";
    public static final String IMG_BASE_URL = "://img.npclo.com/org_logo/";
    public static final String FILE_PROVIDER_NAME = "gtImage";
    public static final int REQUEST_CODE_WECHATUSER_CODE = 1201;
    public static final int REQUEST_CODE_CONTRACT_CODE = 1202;
    public static final int REQUEST_CODE_WECHATUSER_NUM = 1203;
    public static final int REQUEST_CODE_CONTRACT_NUM = 1204;

    public static String getHttpScheme() {
        if ("production".equals(BuildConfig.ENV)) {
            return SCHEME_HTTP;
        } else if ("development".equals(BuildConfig.ENV)) {
            return SCHEME_HTTP;
        }
        return SCHEME_HTTP;
    }
}
