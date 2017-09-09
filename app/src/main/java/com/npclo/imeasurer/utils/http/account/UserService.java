package com.npclo.imeasurer.utils.http.account;

import com.npclo.imeasurer.data.HttpMsg;
import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.data.user.User;
import com.npclo.imeasurer.data.user.ValidCode;
import com.npclo.imeasurer.data.wuser.WechatUser;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Endless on 2017/7/19.
 */

public interface UserService {
    @POST("clientUser/signIn")
    Observable<HttpResponse<User>> signIn(@Body User user);

    @POST("clientUser/signUp")
    Observable<HttpResponse<User>> signUp(@Body User user);

    @FormUrlEncoded
    @POST("clientUser/validcode")
    Observable<HttpResponse<ValidCode>> getValidCode(@Field("name") String name, @Field("type") String type);

    @FormUrlEncoded
    @POST("clientUser/resetpwd")
    Observable<HttpResponse<HttpMsg>> resetPwd(@Field("mobile") String mobile, @Field("pwd") String pwd,
                                               @Field("code") String code);

    @GET("clientUser/getInfoWithQrcode")
    Observable<HttpResponse<WechatUser>> getUserInfoWithCode(@Query("code") String code);

    @FormUrlEncoded
    @POST("clientUser/editPwd")
    Observable<HttpResponse<HttpMsg>> editPwd(@Field("id") String id, @Field("old") String old,
                                              @Field("new") String newpwd);

    @GET("clientUser/getInfoWithOpenID")
    Observable<HttpResponse<WechatUser>> getUserInfoWithOpenID(@Query("openid") String id);
}