package com.jessie.daggerdemo;

import dagger.Module;
import dagger.Provides;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe 生成LoginPresenter实例工厂
 *
 */
@Module
public class LoginPresenterFactory {
    MainActivity mainActivity;

    public LoginPresenterFactory(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }

    @Provides
    LoginPresenter provideLoginPresenter(LoginModel loginModel,MainActivity mainActivity){
        return new LoginPresenter(loginModel,mainActivity);
    }

    @Provides
    LoginModel provideLoginModel(){
        return new LoginModel();
    }

    @Provides
    MainActivity proviceMainActivity(){
        return mainActivity;
    }
}
