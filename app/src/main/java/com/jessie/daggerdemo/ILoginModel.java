package com.jessie.daggerdemo;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe M层接口定义
 */

public interface ILoginModel {
    public boolean login(String username,String password);
    public boolean checkUserName(String username);
    public boolean checkPassword(String password);

}
