package com.npclo.imeasurer.utils.http.account;

import com.npclo.imeasurer.data.user.User;
import com.npclo.imeasurer.data.user.ValidCode;
import com.npclo.imeasurer.utils.http.HttpResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Endless on 2017/7/19.
 */

public interface UserService {
    @POST("clientUser/signIn")
    Observable<HttpResponse<User>> signIn(@Body User user);

    @POST("clientUser/signUp")
    Observable<HttpResponse<User>> signUp(@Body User user);

    @POST("clientUser/validcode")
    Observable<HttpResponse<ValidCode>> getValidCode(@Body String name);
}