package com.npclo.imeasurer.utils.http.user;

import com.npclo.imeasurer.data.HttpMsg;
import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.data.ThirdMember;
import com.npclo.imeasurer.data.User;
import com.npclo.imeasurer.data.ValidCode;
import com.npclo.imeasurer.data.WechatUser;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Endless
 * @date 2017/7/19
 */

public interface UserService {
    @FormUrlEncoded
    @POST("clientUser/signIn")
    Observable<HttpResponse<HttpMsg>> signIn(@Field("name") String name, @Field("pwd") String pwd);

    @GET("clientUser/userinfo")
    Observable<HttpResponse<User>> userInfo();

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
    Observable<HttpResponse<WechatUser>> getUserInfoWithCode(@Query("code") String code);

    @FormUrlEncoded
    @POST("clientUser/editPwd")
    Observable<HttpResponse<HttpMsg>> editPwd(@Field("old") String old,
                                              @Field("new") String newpwd);

    @GET("clientUser/getInfoWithOpenID")
    Observable<HttpResponse<WechatUser>> getUserInfoWithOpenID(@Query("openid") String id);

    @GET("thirdMember/memberInfo")
    Observable<HttpResponse<ThirdMember>> getThirdMemberInfo(@Query("tid") String tid, @Query("cid") String cid);
}