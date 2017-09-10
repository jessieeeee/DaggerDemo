---
# Dagger 学习入门
---

---

目前android最火的架构就是MVP+Dagger+Retrofit+Rxjava，技术迭代真的是很快啊，程序猿这个职业，需要我们抱着终身学习的热情在互联网急速发展的浪潮中追逐最新的技术，提高开发效率和我们的核心竞争力，好吧，一不小心又灌了一口鸡汤。

架构在大型项目中是很重要的，一个好的架构有着高度的可扩展性，可维护性，低耦合，高聚合，而Dagger正是为了解耦而生，主要是通过依赖注入去实现。依赖注入是一种设计模式，降低了依赖和被依赖对象之间的耦合，方便扩展和单元测试。是面向对象六大原则之一的依赖倒置原则（dependency inversion principle）比如当服务端接口未开发完时，我们可使用本地json注入做一个测试，与MVP结合可以实现更大程度的解耦，当面对需求更改时，进行更灵活的处理，避免项目的大规模修改，妈妈再也不用担心我又加班了～

所以，Dagger值得我们去跳坑，在此做一个学习记录

---

# 依赖注入的场景

依赖注入，这个名词太抽象了，实际上我们平时开发是经常用到的，只不过你不知道这个就叫依赖注入而已，通过一个类的构造函数入参引入另一个类的对象，或者是通过set方法引入另一个类的对象，就是依赖注入。

比如这段代码：

```java
//构造方法依赖注入
public class A {
    B b;
    public void A(B b) {
        this.b = b;
    }
}

//set()方法依赖注入
public class A {
    B b;
    public void setB(B b) {
        this.b = b;
    }
}

```

好吧，这么一说神秘感就没有了，Dagger2不同的地方就是通过注解的方式依赖注入的。如果有一天，产品需要更改需求，A类依赖的不是B而是C，如果我们很多地方都引用到A，并且A都需要依赖于B，那么我们的代码就需要大面积更改了，而Dagger能够让我们避免这种问题的出现。

# Dagger常用注解

@Inject 一般情况下是标注成员属性和构造函数，标注的成员属性不能是private，Dagger 2 中还可以标注方法，一般用于注解需要注入的对象。
@Module 用来标注 Module类，用来生成实例，像一个工厂，用于生成各种类的实例。
@Provides 只能标注方法，必须在 Module类 中，Module类对外提供实例方法的注解。
@Component 只能标注接口或抽象类，声明的注入接口的参数类型必须和目标类一致。Module类的构造函数入参与调用类Activity保持一致。Component一般是个接口，就是将Activity与Module类联系起来，通过它调用Module，传入当前Activity，获得Provides提供的实例，完成注入。
@Named:Dagger2是根据Provide方法返回类型查找对应的依赖的，如果需要注入相同类型的，但是内容不同，多态情况，这个时候就可以使用@Named进行区分。
@Singleton有时候，我们不需要多次创建Component，可以使用一个Component，即单例模式，这个时候就可以使用@Singleton来修饰它，这样就会以单利的模式在生成的 `Daggerxxxxx`中保存 。如果某个moudle也只需要使用同一个，那么也可以使用@Single来修饰provide的方法，同时也需要使用@Singleton注解使用该module的Component。

# Dagger注入过程

当某个变量被@Inject注解时，会先判断注解成员属性的构造函数是否被注解，（Dagger2通过Inject标记可以在需要这个类实例的时候来找到这个构造函数并把相关实例new出来），在建立Component和工厂类的情况下，直接完成注入，如果没有被注解，会通过Component查找对应的Module里的Provides方法生成。

# Dagger的基本用法

以一个简陋的登录功能为例

首先我们定义一个接口ILoginModel

 ```java
public interface ILoginModel {
    public boolean login(String username,String password);
    public boolean checkUserName(String username);
    public boolean checkPassword(String password);
}
 ```

接着我们实现这个接口，创建一个类LoginModel，这里只是模拟一下，随便写点逻辑

```java
public class LoginModel implements ILoginModel {
  
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
```

接着在主Activity中声明这个对象，随意调用里面的一个方法比如`checkUserName(String username)`

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "测试注入";
    LoginModel loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: "+loginModel.checkUserName("admin"));
    }
}
```

是不是在作死呢？loginModel明显会报空指针啊，没错，loginModel没初始化必然报空指针。这里我们使用Dagger依赖注入。

## @Inject注解构造方法

- 给LoginModel增加构造方法，并使用@Inject注解

  ```java
   @Inject
   public LoginModel(){}
  ```

- 创建一个LoginModel的实例工厂LoginModelFactory，专门负责生成LoginModel，记得用@Module注解，这里由于我们已经用@Inject注解了构造方法，所以LoginModel会直接被new出来。

  ```java
  @Module
  public class LoginModelFactory {

    private MainActivity mainActivity;

    //保持与需要被注入的变量所在的类名称一致
    public LoginModelFactory(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
  }
  ```

- 创建一个关联组建LoginModelComponent，关联LoginModelFactory和MainActivity

  ```java
  @Component (modules = LoginModelFactory.class)
  public interface LoginModelComponent {
      void inject(MainActivity mainActivity);
  }
  ```

- 在MainActivity中，将loginModel用@Inject注解，并增加一句代码实现注入

  ```java
  @Inject
  LoginModel loginModel;


  //开始注入
  DaggerLoginModelComponent.create().inject(this);
  ```

注：修改后的完整代码就不再贴了，完整代码已打包上传，文末有传送门。

至此，Dagger的基本使用结束，我们发现loginModel注入成功，`loginModel.checkUserName("admin")`成功调用，并没有抛异常。

下面介绍另一种实现方法。

## @Provides注解实现

先取消LoginModel构造方法的注解。

前面说过@Provides必须在 Module 中，这里我们用@Module注解的类是LoginModelFactory，我们在LoginModelFactory中增加一个方法，生成LoginModel对象，注意用@Provides注解。

```java
 @Provides
 LoginModel provideLoginModel(){
      return new LoginModel();
 }
```

这时主Activity的那句注入代码`DaggerLoginModelComponent.create().inject(this);`报错了，因为这次我们采用的不是注解构造方法的实现方式，而是采用@Provides注解，所以修改如下

```java
 DaggerLoginModelComponent.builder()
                .loginModelFactory(new LoginModelFactory(this))
                .build()
                .inject(this);
```

这时我们发现loginModel依然注入成功，`loginModel.checkUserName("admin")`成功调用，也并没有抛异常。

接下来是比较常用的场景。

# 与MVP架构结合

按传统mvp的架构，我们现在有了M层的接口，M层的实现，还需要V层的接口和V层的实现，以及P层的实现，如果有必要还有P层的接口。

下面就直接贴传统mvp架构缺少的那部分代码

先删除主Activity中，引用到的LoginModel对象

删除或注释LoginModelComponent这个关联组建，否则会报错

V层接口ILoginView

```java
public interface ILoginView {
    public void onCheckUserNameResult(boolean result);
    public void onCheckPasswordResult(boolean result);
    public void onLoginResult(boolean result);
}
```

P层实现LoginPresenter

```java
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
```

主Activity修改，实现ILoginView的接口，声明待注入的LoginPresenter对象

```java
public class MainActivity extends AppCompatActivity implements ILoginView{

    private static final String TAG = "测试Inject注释构造方法";
    @Inject
    LoginPresenter loginPresenter;

    TextInputLayout usernameInput;
    TextInputLayout passwordInput;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = (TextInputLayout) findViewById(R.id.usernameInput);
        passwordInput = (TextInputLayout) findViewById(R.id.passwordInput);
        //开始注入
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
```

修改了注入代码，这里我们需要新建LoginPresenter的实例工厂LoginPresenterFactory，和LoginPresenterFactory与主Activity的关联组件LoginPresenterComponent

```java
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
```

```java
@Component (modules = LoginPresenterFactory.class)
public interface LoginPresenterComponent {
    void inject(MainActivity mainActivity);
}
```

可以看到LoginPresenterFactory提供了3个实例方法，首先必须提供LoginPresenter的实例生成方法，其中LoginPresenter需要依赖MainActivity和LoginModel创建，所以我们可以增加两个实例的生成方法分别是provideLoginModel和proviceMainActivity，其中前者可以直接new出来，后者是通过注入方法从主Activity传进来。到此mvp与Dagger结合的例子就完成了。
