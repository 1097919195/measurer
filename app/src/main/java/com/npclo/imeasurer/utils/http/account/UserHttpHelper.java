package com.npclo.imeasurer.utils.http.account;

import com.npclo.imeasurer.data.user.User;
import com.npclo.imeasurer.utils.http.HttpHelper;

import rx.Observable;

public class UserHttpHelper extends HttpHelper {
    public Observable<User> signIn(String name, String pwd) {
        User user = new User();
        user.setName(name).setPwd(pwd);
        return retrofit.create(UserService.class)
                .signIn(user)
                .map(new HttpResponseFunc<>());
    }

    public Observable<User> signUp(String name, String pwd) {
        User user = new User();
        user.setName(name).setPwd(pwd);
        return retrofit.create(UserService.class)
                .signUp(user)
                .map(new HttpResponseFunc<>());
    }
}