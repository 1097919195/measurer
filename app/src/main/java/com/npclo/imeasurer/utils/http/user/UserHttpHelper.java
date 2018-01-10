package com.npclo.imeasurer.utils.http.user;

import com.npclo.imeasurer.data.HttpMsg;
import com.npclo.imeasurer.data.user.User;
import com.npclo.imeasurer.data.ValidCode;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.utils.http.HttpHelper;

import rx.Observable;

/**
 * @author Endless
 */
public class UserHttpHelper extends HttpHelper {
    public Observable<HttpMsg> signIn(String name, String pwd) {
        return retrofit.create(UserService.class)
                .signIn(name, pwd)
                .map(new HttpResponseFunc<>());
    }

    public Observable<User> userInfo() {
        return retrofit.create(UserService.class)
                .userInfo()
                .map(new HttpResponseFunc<>());
    }

    public Observable<User> signUp(String name, String pwd, String code) {
        return retrofit.create(UserService.class)
                .signUp(name, pwd, code)
                .map(new HttpResponseFunc<>());
    }

    public Observable<ValidCode> getValidCode(String name, String type) {
        return retrofit.create(UserService.class)
                .getValidCode(name, type)
                .map(new HttpResponseFunc<>());
    }

    public Observable<HttpMsg> resetPwd(String mobile, String pwd, String code) {
        return retrofit.create(UserService.class)
                .resetPwd(mobile, pwd, code)
                .map(new HttpResponseFunc<>());
    }

    public Observable<WechatUser> getUserInfoWithCode(String code, String uid) {
        return retrofit.create(UserService.class)
                .getUserInfoWithCode(code, uid)
                .map(new HttpResponseFunc<>());
    }

    public Observable<HttpMsg> editPwd(String id, String old, String newpwd) {
        return retrofit.create(UserService.class)
                .editPwd(id, old, newpwd)
                .map(new HttpResponseFunc<>());
    }

    public Observable<WechatUser> getUserInfoWithOpenID(String oid, String uid) {
        return retrofit.create(UserService.class)
                .getUserInfoWithOpenID(oid, uid)
                .map(new HttpResponseFunc<>());
    }
}