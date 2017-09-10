package com.jessie.daggerdemo;

/**
 * @author JessieKate
 * @date 09/09/2017
 * @email lyj1246505807@gmail.com
 * @describe P层实现
 */

public class LoginPresenter {

    private ILoginModel iLoginModel;
    private ILoginView iLoginView;

    public LoginPresenter(ILoginModel iLoginModel,ILoginView iLoginView){
        this.iLoginModel=iLoginModel;
        this.iLoginView=iLoginView;

    }

    public void checkPassword(String password){

        if (iLoginModel.checkPassword(password)) {
            iLoginView.onCheckPasswordResult(true);
        }else{
            iLoginView.onCheckPasswordResult(false);
        }

    }

    public void checkUserName(String username){
        if(iLoginModel.checkUserName(username)){
            iLoginView.onCheckUserNameResult(true);
        }else{
            iLoginView.onCheckUserNameResult(false);
        }
    }

    public void login(String username,String password){
        if(iLoginModel.login(username,password)){
           iLoginView.onLoginResult(true);
        }else{
            iLoginView.onLoginResult(false);
        }
    }
}
