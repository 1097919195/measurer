package com.npclo.imeasurer.utils.http.app;

import com.npclo.imeasurer.data.HttpMsg;
import com.npclo.imeasurer.data.app.App;
import com.npclo.imeasurer.utils.Gog;
import com.npclo.imeasurer.utils.http.HttpHelper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author Endless
 */
public class AppHttpHelper extends HttpHelper {
    public Observable<App> getLatestVersion() {
        Gog.e("AppHttpHelper class");
        return retrofit.create(AppService.class)
                .getLatestVersion()
                .map(new HttpResponseFunc<>());
    }

    public Observable<HttpMsg> upload(File file) {
        RequestBody uFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return retrofit.create(AppService.class)
                .upload(uFile)
                .map(new HttpResponseFunc<>());
    }

    public Observable<HttpMsg> fixbug(String s) {
        return retrofit.create(AppService.class)
                .fixbug(s)
                .map(new HttpResponseFunc<>());
    }
}
