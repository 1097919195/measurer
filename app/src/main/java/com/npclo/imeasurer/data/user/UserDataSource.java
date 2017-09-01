package com.npclo.imeasurer.data.user;

import rx.Observable;

/**
 * Created by Endless on 2017/7/20.
 */

public interface UserDataSource {
    Observable<User> signIn(String name, String pwd);

    Observable<User> signUp(String name, String pwd, String code);

    Observable<ValidCode> getValidCode(String name, String type);
}
