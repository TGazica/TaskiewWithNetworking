package osc.ada.tomislavgazica.taskie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.RegistrationToken;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    public static final String CALLED_FROM_LOGIN = "called_from_login";

    @BindView(R.id.edittext_register_email)
    EditText userEmail;

    @BindView(R.id.edittext_register_username)
    EditText username;

    @BindView(R.id.edittext_register_password)
    EditText userPwd;

    boolean callFromLoginActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getIntent().getExtras() != null) {
            if (!getIntent().getExtras().isEmpty()) {
                callFromLoginActivity = getIntent().getExtras().getBoolean(CALLED_FROM_LOGIN);
            }
        }

        if (!SharedPrefsUtil.getPreferencesFieldBoolean(getApplicationContext(), SharedPrefsUtil.IS_REGISTERED)) {
            SharedPrefsUtil.storePreferencesField(getApplicationContext(), SharedPrefsUtil.IS_REGISTERED, true);
        } else if(!callFromLoginActivity){
            startLoginActivity();
        }


        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_register_login)
    void onLoginButtonClick() {
        startLoginActivity();
    }

    @OnClick(R.id.button_register_register)
    void onRegisterButtonClick() {
        registerUser();
    }

    private void registerUser() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.email = userEmail.getText().toString();
        registrationToken.userName = username.getText().toString();
        registrationToken.password = userPwd.getText().toString();

        Call registerCall = apiService.registerUser(registrationToken);
        registerCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                startLoginActivity();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}
