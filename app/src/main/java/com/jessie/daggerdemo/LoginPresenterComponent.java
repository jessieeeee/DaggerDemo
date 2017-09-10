package com.jessie.daggerdemo;

import dagger.Component;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe
 */
@Component (modules = LoginPresenterFactory.class)
public interface LoginPresenterComponent {
    void inject(MainActivity mainActivity);
}
