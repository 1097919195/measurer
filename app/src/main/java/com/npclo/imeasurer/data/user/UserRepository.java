package com.npclo.imeasurer.data.user;

import com.npclo.imeasurer.data.HttpMsg;
import com.npclo.imeasurer.utils.http.account.UserHttpHelper;

import rx.Observable;

/**
 * Created by Endless on 2017/7/20.
 */

public class UserRepository implements UserDataSource {
    @Override
    public Observable<User> signIn(String name, String pwd) {
        return new UserHttpHelper().signIn(name, pwd);
    }

    @Override
    public Observable<User> signUp(String name, String pwd, String code) {
        return new UserHttpHelper().signUp(name, pwd, code);
    }

    @Override
    public Observable<ValidCode> getValidCode(String name, String type) {
        return new UserHttpHelper().getValidCode(name, type);
    }

    public Observable<HttpMsg> resetPwd(String mobile, String pwd, String code) {
        return new UserHttpHelper().resetPwd(mobile, pwd, code);
    }
}