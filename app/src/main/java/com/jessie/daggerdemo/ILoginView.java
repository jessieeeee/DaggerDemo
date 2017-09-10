package com.jessie.daggerdemo;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe V层接口定义
 *
 */

public interface ILoginView {
    public void onCheckUserNameResult(boolean result);
    public void onCheckPasswordResult(boolean result);
    public void onLoginResult(boolean result);
}
