package com.jessie.daggerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ILoginView{

    private static final String TAG = "测试Inject注释构造方法";
    @Inject
    LoginPresenter loginPresenter;

//    @Inject
//    LoginModel loginModel;
    TextInputLayout usernameInput;
    TextInputLayout passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = (TextInputLayout) findViewById(R.id.usernameInput);
        passwordInput = (TextInputLayout) findViewById(R.id.passwordInput);
        //开始注入
//        DaggerLoginModelComponent.create()
//                .inject(this);
//        DaggerLoginModelComponent.builder()
//                .loginModelFactory(new LoginModelFactory(this))
//                .build()
//                .inject(this);
        DaggerLoginPresenterComponent.builder()
                .loginPresenterFactory(new LoginPresenterFactory(this))
                .build()
                .inject(this);
        EditText editUsername= (EditText) findViewById(R.id.username);
        EditText editPassword= (EditText) findViewById(R.id.password);
        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               onChangeUsername();
            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               onChangePassword();
            }
        });
    }


    //点击用户名输入回调
    public void onChangeUsername(){
        String username = usernameInput.getEditText().getText().toString().trim();
        //密码输入框已输入
        if(!TextUtils.isEmpty(username)){
          loginPresenter.checkUserName(username);
        }
    }

    //点击密码输入回调
    public void onChangePassword(){
       String password = passwordInput.getEditText().getText().toString().trim();
        //用户名输入框已输入
       if(!TextUtils.isEmpty(password)){
           loginPresenter.checkPassword(password);
       }
    }

    //登录按钮点击回调
    public void onLoginClick(View view) {
        hideKeyboard();
        String username = usernameInput.getEditText().getText().toString().trim();
        String password = passwordInput.getEditText().getText().toString().trim();

        loginPresenter.login(username,password);
    }

    public void hideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onCheckUserNameResult(boolean result) {
        if(result){
            usernameInput.setErrorEnabled(false);

        }else{
            usernameInput.setError("用户名无效!");
        }
    }

    @Override
    public void onCheckPasswordResult(boolean result) {
        if(result){
            passwordInput.setErrorEnabled(false);
        }else{
            passwordInput.setError("密码无效!");

        }
    }

    @Override
    public void onLoginResult(boolean result) {
       if(result){
           Toast.makeText(this,"登录成功！",Toast.LENGTH_SHORT).show();
       }else{
           Toast.makeText(this,"登录失败！",Toast.LENGTH_SHORT).show();
       }
    }
}
