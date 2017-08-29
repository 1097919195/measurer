package com.npclo.imeasurer.data.user;

import rx.Observable;

import com.npclo.imeasurer.utils.http.account.UserHttpHelper;

/**
 * Created by Endless on 2017/7/20.
 */

public class UserRepository implements UserDataSource {
    @Override
    public Observable<User> signIn(String name, String pwd) {
        return new UserHttpHelper().signIn(name, pwd);
    }

    @Override
    public Observable<User> signUp(String name, String pwd) {
        return new UserHttpHelper().signUp(name, pwd);
    }


}
