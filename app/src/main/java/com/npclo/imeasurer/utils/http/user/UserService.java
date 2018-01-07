package com.npclo.imeasurer.utils.http.user;

import com.npclo.imeasurer.data.HttpMsg;
import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.data.ValidCode;
import com.npclo.imeasurer.data.user.User;
import com.npclo.imeasurer.data.wuser.WechatUser;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * @author Endless
 * @date 2017/7/19
 */

public interface UserService {
    @FormUrlEncoded
    @POST("clientUser/signIn")
    Observable<HttpResponse<User>> signIn(@Field("name") String name, @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("clientUser/signUp")
    Observable<HttpResponse<User>> signUp(@Field("name") String name, @Field("pwd") String pwd, @Field("code") String code);

    @FormUrlEncoded
    @POST("clientUser/validcode")
    Observable<HttpResponse<ValidCode>> getValidCode(@Field("name") String name, @Field("type") String type);

    @FormUrlEncoded
    @POST("clientUser/resetpwd")
    Observable<HttpResponse<HttpMsg>> resetPwd(@Field("mobile") String mobile, @Field("pwd") String pwd,
                                               @Field("code") String code);

    @GET("clientUser/getInfoWithQrcode")
    Observable<HttpResponse<WechatUser>> getUserInfoWithCode(@Query("code") String code, @Query("uid") String uid);

    @FormUrlEncoded
    @POST("clientUser/editPwd")
    Observable<HttpResponse<HttpMsg>> editPwd(@Field("id") String id, @Field("old") String old,
                                              @Field("new") String newpwd);

    @GET("clientUser/getInfoWithOpenID")
    Observable<HttpResponse<WechatUser>> getUserInfoWithOpenID(@Query("openid") String id, @Query("uid") String uid);
}