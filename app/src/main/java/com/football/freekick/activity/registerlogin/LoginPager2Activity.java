package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.Login;
import com.football.freekick.beans.Pitches;
import com.football.freekick.chat.FireChatHelper.ChatHelper;
import com.football.freekick.adapter.UsersChatAdapter;
import com.football.freekick.beans.User;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginPager2Activity extends BaseActivity {

    private DatabaseReference mDatabase;
    @Bind(R.id.fl_login_by_facebook)
    FrameLayout mFlLoginByFacebook;
    @Bind(R.id.edt_email)
    EditText mEdtEmail;
    @Bind(R.id.edt_pass_word)
    EditText mEdtPassWord;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_forget_pass_word)
    TextView mTvForgetPassWord;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.facebook)
    LoginButton mFacebook;
    private Context mContext;
    private CallbackManager mCallbackManager;
    private boolean isSecondRun;//記錄是否已登錄,以獲取廣告,場地等信息
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pager2);
        mContext = LoginPager2Activity.this;
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsers = mDatabase.child("users");
        mAuth = FirebaseAuth.getInstance();
//        mEdtEmail.setText("huo@yopmail.com");
//        mEdtEmail.setText("yue@yopmail.com");
//        mEdtEmail.setText("lei@yopmail.com");
//        mEdtEmail.setText("wei@yopmail.com");
//        mEdtEmail.setText("freekick.test1@gmail.com");
//        mEdtPassWord.setText("123456");
        initView();

    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        isSecondRun = PrefUtils.getBoolean(App.APP_CONTEXT, "isSecondRun", false);
        mCallbackManager = CallbackManager.Factory.create();

        mFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Gson gson = new Gson();
                Logger.json(gson.toJson(loginResult));
//                getLoginInfo(loginResult.getAccessToken());
                loginByFacebook(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @OnClick({R.id.fl_login_by_facebook, R.id.tv_login, R.id.tv_forget_pass_word, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login_by_facebook:
//                if ((new DateTime().getMillis() - (31 * 24 * 60 * 60 * 1000)) > PrefUtils.getLong(App.APP_CONTEXT,
//                        "FacebookTime", 0)) {
                mFacebook.performClick();
//                } else {
////                    LoginManager.getInstance().logInWithReadPermissions(LoginPager2Activity.this, Arrays.asList
////                            ("public_profile"));
//                    AccessToken mAccessToken = AccessToken.getCurrentAccessToken();
//                    loginByFacebook(mAccessToken.getUserId());
//                }

//                loginByFacebook(PrefUtils.getString(App.APP_CONTEXT,"uid",null));
//                loginByFacebook("108927019898582");
//                loginTest();
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forget_pass_word:
                startActivity(new Intent(mContext, ForgetPasswordActivity.class));
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    /**
     * 登錄
     */
    private void login() {
        if (StringUtils.isEmpty(mEdtEmail)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_email));
            return;
        }
        if (!MyUtil.checkEmail(StringUtils.getEditText(mEdtEmail))) {
            ToastUtil.toastShort(getString(R.string.email_error));
            return;
        }
        if (StringUtils.isEmpty(mEdtPassWord)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_password));
            return;
        }
        if (StringUtils.getEditText(mEdtPassWord).length() < 6) {
            ToastUtil.toastShort(getString(R.string.password_error));
            return;
        }
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("email", StringUtils.getEditText(mEdtEmail));
        object1.addProperty("password", StringUtils.getEditText(mEdtPassWord));
        object1.addProperty("android_device_token", FirebaseInstanceId.getInstance().getToken());
        object1.addProperty("provider", "email");
        object.add("user", object1);
        Logger.json(object.toString());
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth/sign_in" :
                "http://api.freekick.hk/api/en/auth/sign_in";
        Logger.d(url);
        OkGo.post(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Login login = gson.fromJson(s, Login.class);
                        if (login.getUser() != null) {
                            Login.UserBean user = login.getUser();
                            if (user.getLogin_fail() == 0) {
                                //登錄成功
                                Headers headers = response.headers();
                                String access_token = headers.get("access-token");
                                String client = headers.get("client");
                                String uid = headers.get("uid");
                                String expiry = headers.get("expiry");
                                Logger.d("access-token=" + access_token + "   client=" + client + "   uid=" + uid + "" +
                                        "   expiry=" + expiry);
                                HttpHeaders header = new HttpHeaders();
                                header.put("access-token", access_token);
                                header.put("client", client);
                                header.put("uid", uid);
                                header.put("expiry", expiry);
                                OkGo.getInstance().addCommonHeaders(header);
                                PrefUtils.putString(App.APP_CONTEXT, "access_token", access_token);
                                PrefUtils.putString(App.APP_CONTEXT, "client", client);
                                PrefUtils.putString(App.APP_CONTEXT, "uid", uid);
                                PrefUtils.putString(App.APP_CONTEXT, "expiry", expiry);

                                PrefUtils.putString(App.APP_CONTEXT, "mobile_no", user.getMobile_no() + "");
                                PrefUtils.putString(App.APP_CONTEXT, "username", user.getUsername() + "");
                                if (!isSecondRun) {
                                    //如果是第一次登錄或者卸載重裝過,獲取廣告,場地等信息
                                    getPitches();
                                    getAdvertisements();
                                    loadingDismiss();
                                    PrefUtils.putBoolean(App.APP_CONTEXT, "isSecondRun", true);
                                }
                                if (user.getTeams() != null && user.getTeams().size() <= 0) {//沒有球队則去註冊三頁
                                    Intent intent = new Intent(mContext, RegisterPager1Activity.class);
                                    intent.putExtra("email", StringUtils.getEditText(mEdtEmail));
                                    intent.putExtra("password", StringUtils.getEditText(mEdtPassWord));
                                    loadingDismiss();
                                    startActivity(intent);

                                } else {//有用戶名則直接進入應用
                                    List<Login.UserBean.TeamsBean> teams = user.getTeams();
                                    Login.UserBean.TeamsBean teamsBean = teams.get(teams.size() - 1);
                                    PrefUtils.putString(App.APP_CONTEXT, "team_id", teamsBean.getId() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "color1", teamsBean.getColor1() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "color2", teamsBean.getColor2() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "logourl", teamsBean.getImage().getUrl() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "team_name", teamsBean.getTeam_name() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "size", teamsBean.getSize() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "age_range_max", teamsBean.getAge_range_max
                                            () + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "age_range_min", teamsBean.getAge_range_min
                                            () + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "establish_year", teamsBean
                                            .getEstablish_year() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "average_height", teamsBean
                                            .getAverage_height() + "");
                                    if (teamsBean.getDistrict() != null) {
                                        PrefUtils.putString(App.APP_CONTEXT, "district", teamsBean.getDistrict()
                                                .getDistrict() + "");
                                        PrefUtils.putString(App.APP_CONTEXT, "district_id", teamsBean.getDistrict()
                                                .getId() + "");
                                    }
                                    loadingDismiss();
                                    //註冊FirebaseDatabase
                                    loginFirebaseDatabase(user.getUsername(), teamsBean);
//                                    registerFirebaseDatabase(user.getUsername(),teamsBean.getId()+"");
                                    startActivity(new Intent(mContext, OneTimePagerActivity.class));
                                }
                            } else {

                            }
                        } else {//data為null,登錄失敗
                            loadingDismiss();
                            if (login.getErrors() != null) {
                                ToastUtil.toastShort(login.getErrors().get(0));
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    private void loginFirebaseDatabase(final String username, final Login.UserBean.TeamsBean teamsBean) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(StringUtils.getEditText(mEdtEmail), StringUtils
                .getEditText(mEdtPassWord))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Logger.d("登錄FirebaseDatabase成功了");
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                Logger.json(new Gson().toJson(task));
                                initDatabase(teamsBean.getId());
                                FirebaseDatabase.getInstance()
                                        .getReference().
                                        child("users").
                                        child(teamsBean.getId() + "").
                                        child("connection").
                                        setValue(UsersChatAdapter.ONLINE);
                                FirebaseDatabase.getInstance()
                                        .getReference().
                                        child("users").
                                        child(teamsBean.getId() + "").
                                        child("displayName").
                                        setValue(username + "（" + teamsBean.getTeam_name() + "）");
                                FirebaseDatabase.getInstance()
                                        .getReference().
                                        child("users").
                                        child(teamsBean.getId() + "").
                                        child("team_url").
                                        setValue(teamsBean.getImage().getUrl());
                                FirebaseDatabase.getInstance()
                                        .getReference().
                                        child("users").
                                        child(teamsBean.getId() + "").
                                        child("team_id").
                                        setValue(teamsBean.getId() + "");
                            }
                        } else {
                            Logger.d(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * 註冊FirebaseDatabase
     */
    private void registerFirebaseDatabase(final String username, final String team_id) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(StringUtils.getEditText(mEdtEmail), StringUtils
                .getEditText(mEdtPassWord))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Logger.d("註冊FirebaseDatabase成功了");
                            User user = new User(username, StringUtils.getEditText(mEdtEmail), UsersChatAdapter
                                    .ONLINE, ChatHelper.generateRandomAvatarForUser(),
                                    new Date().getTime(), 0);
                            mDatabase.child("users").child(team_id).setValue(user);
                        } else {
                            Logger.d(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * 獲取場地
     */
    private void getPitches() {
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "pitches";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Pitches pitches = gson.fromJson(s, Pitches.class);
                        App.mPitchesBeanList = pitches.getPitches();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    /**
     * 獲取廣告
     */
    private void getAdvertisements() {
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "advertisements";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Advertisements advertisements = gson.fromJson(s, Advertisements.class);
                        App.mAdvertisementsBean = advertisements.getAdvertisements();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.json(e.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    public void getLoginInfo(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");   //比如:1565455221565
                    String name = object.optString("name");  //比如：Zhang San
                    String gender = object.optString("gender");  //性别：比如 male （男）  female （女）
                    String emali = object.optString("email");  //邮箱：比如：56236545@qq.com

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");

                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体

                    loginByFacebook(id);
//                    Toast.makeText(mContext, "" + object.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone," +
                "age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void loginByFacebook(final String id) {
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("social_token", id);
        object1.addProperty("android_device_token", FirebaseInstanceId.getInstance().getToken());
//        object1.addProperty("username",name);
        object1.addProperty("provider", "facebook");
        object.add("user", object1);
        Logger.json(object.toString());
        String url;
        if (App.isChinese)
            url = "http://api.freekick.hk/api/zh_HK/social_authentication/authentication_success";
        else
            url = "http://api.freekick.hk/api/en/social_authentication/authentication_success";
        Logger.d(url);
        OkGo.post(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Login login = gson.fromJson(s, Login.class);
                        if (login.getUser() != null) {
                            Login.UserBean user = login.getUser();
                            if (user.getLogin_fail() == 0) {
                                PrefUtils.putLong(App.APP_CONTEXT, "FacebookTime", new DateTime().getMillis());
                                //登錄成功
                                Headers headers = response.headers();
                                String access_token = headers.get("access-token");
                                String client = headers.get("client");
                                String uid = headers.get("uid");
                                String expiry = headers.get("expiry");
                                Logger.d("access-token=" + access_token + "   client=" + client + "   uid=" + uid + "" +
                                        "   expiry=" + expiry);
                                HttpHeaders header = new HttpHeaders();
                                header.put("access-token", access_token);
                                header.put("client", client);
                                header.put("uid", uid);
                                header.put("expiry", expiry);
                                OkGo.getInstance().addCommonHeaders(header);
                                PrefUtils.putString(App.APP_CONTEXT, "access_token", access_token);
                                PrefUtils.putString(App.APP_CONTEXT, "client", client);
                                PrefUtils.putString(App.APP_CONTEXT, "uid", uid);
                                PrefUtils.putString(App.APP_CONTEXT, "expiry", expiry);

                                PrefUtils.putString(App.APP_CONTEXT, "mobile_no", user.getMobile_no() + "");
                                PrefUtils.putString(App.APP_CONTEXT, "username", user.getUsername() + "");
                                if (!isSecondRun) {
                                    //如果是第一次登錄或者卸載重裝過,獲取廣告,場地等信息
                                    getPitches();
                                    getAdvertisements();
                                    loadingDismiss();
                                    PrefUtils.putBoolean(App.APP_CONTEXT, "isSecondRun", true);
                                }
                                if (user.getTeams() != null && user.getTeams().size() <= 0) {//沒有球队則去註冊三頁
                                    Intent intent = new Intent(mContext, RegisterPager1Activity.class);
                                    intent.putExtra("social_token", PrefUtils.getString(App.APP_CONTEXT,
                                            "social_token", null));
                                    intent.putExtra("password", StringUtils.getEditText(mEdtPassWord));
                                    loadingDismiss();
                                    startActivity(intent);

                                } else {//有用戶名則直接進入應用
                                    List<Login.UserBean.TeamsBean> teams = user.getTeams();
                                    Login.UserBean.TeamsBean teamsBean = teams.get(teams.size() - 1);
                                    PrefUtils.putString(App.APP_CONTEXT, "team_id", teamsBean.getId() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "color1", teamsBean.getColor1() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "color2", teamsBean.getColor2() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "logourl", teamsBean.getImage().getUrl() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "team_name", teamsBean.getTeam_name() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "size", teamsBean.getSize() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "age_range_max", teamsBean.getAge_range_max
                                            () + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "age_range_min", teamsBean.getAge_range_min
                                            () + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "establish_year", teamsBean
                                            .getEstablish_year() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "average_height", teamsBean
                                            .getAverage_height() + "");
                                    if (teamsBean.getDistrict() != null) {
                                        PrefUtils.putString(App.APP_CONTEXT, "district", teamsBean.getDistrict()
                                                .getDistrict() + "");
                                        PrefUtils.putString(App.APP_CONTEXT, "district_id", teamsBean.getDistrict()
                                                .getId() + "");
                                    }
                                    loadingDismiss();
                                    //註冊FirebaseDatabase
                                    loginFirebaseDatabaseByFacebook(id, user.getUsername(), teamsBean);
//                                    registerFirebaseDatabase(user.getUsername(),teamsBean.getId()+"");
                                    startActivity(new Intent(mContext, OneTimePagerActivity.class));
                                }
                            } else {

                            }
                        } else {//data為null,登錄失敗
                            loadingDismiss();
                            if (login.getErrors() != null) {
                                ToastUtil.toastShort(login.getErrors().get(0));
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    private void loginFirebaseDatabaseByFacebook(String id, final String username, final Login.UserBean.TeamsBean
            teamsBean) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(id + "@yopmail.com", id)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Logger.d("登錄FirebaseDatabase成功了");
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                Logger.json(new Gson().toJson(task));
                                initDatabase(teamsBean.getId());
                                DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");
                                users.
                                        child(teamsBean.getId() + "").
                                        child("connection").
                                        setValue(UsersChatAdapter.ONLINE);
                                users.
                                        child(teamsBean.getId() + "").
                                        child("displayName").
                                        setValue(username + "（" + teamsBean.getTeam_name() + "）");
                                users.
                                        child(teamsBean.getId() + "").
                                        child("team_url").
                                        setValue(teamsBean.getImage().getUrl());
                                users.
                                        child(teamsBean.getId() + "").
                                        child("team_id").
                                        setValue(teamsBean.getId() + "");

                            }
                        } else {
                            Logger.d(task.getException().getMessage());
                        }
                    }
                });
    }

    private void initDatabase(final int id) {

        mDatabase.child("users").limitToFirst(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Logger.d("dataSnapshot--->"+dataSnapshot.getValue());
                Logger.d("dataSnapshot--s->"+s);
                if(dataSnapshot.exists()){
                    final User mUser = dataSnapshot.getValue(User.class);
                    Logger.d(dataSnapshot.getKey()+"<--->"+id);
                    if (dataSnapshot.getKey().equals(id+"")){
                        Logger.d("這裡走了????");
                        PrefUtils.putLong(App.APP_CONTEXT,"createdAt", mUser.getCreatedAt());
                    }
                    mDatabase.child("users").child(mUser.getTeam_id()).child("lastEditTimeWith"+id).addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                            }else {
                                mUsers.child(mUser.getTeam_id()).child("lastEditTimeWith"+id).setValue(0);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
