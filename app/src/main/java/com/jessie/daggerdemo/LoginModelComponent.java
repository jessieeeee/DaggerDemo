package com.jessie.daggerdemo;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe 关联组件接口
 * 让LoginModel实例工厂类与需要被注入的变量所在的类之间产生关联
 */

//@Component(modules = LoginModelFactory.class)
public interface LoginModelComponent {
    void inject(MainActivity mainActivity);
}
