package com.jessie.daggerdemo;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe M层实现
 */

public class LoginModel implements ILoginModel {

//    @Inject
    public LoginModel(){

    }
    @Override
    public boolean login(String username,String password) {
        if("admin".equals(username)&&"123456".equals(password)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean checkUserName(String username) {
        if("admin".equals(username)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean checkPassword(String password) {
        if("123456".equals(password)){
            return true;
        }else{
            return false;
        }
    }
}
