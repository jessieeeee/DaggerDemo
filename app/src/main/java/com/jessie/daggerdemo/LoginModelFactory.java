package com.jessie.daggerdemo;

import dagger.Module;
import dagger.Provides;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe 生成LoginModel实例工厂
 */
@Module
public class LoginModelFactory {

    private MainActivity mainActivity;

    //保持与需要被注入的变量所在的类名称一致
    public LoginModelFactory(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }


    @Provides
    LoginModel provideLoginModel(){
        return new LoginModel();
    }
}
