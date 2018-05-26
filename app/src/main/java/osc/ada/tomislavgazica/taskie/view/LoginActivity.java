package osc.ada.tomislavgazica.taskie.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.LoginResponse;
import osc.ada.tomislavgazica.taskie.model.RegistrationToken;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edittext_login_email)
    EditText mUserEmail;

    @BindView(R.id.edittext_login_password)
    EditText mUserPwd;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login_login)
    void onLoginButtonClick(){
        loginUser();
    }

    @OnClick(R.id.button_login_register)
    void onRegisterButtonClick() {
        startRegisterActivity();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.CALLED_FROM_LOGIN, true);
        startActivity(intent);
    }

    private void loginUser() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();

        ApiService apiService = retrofit.create(ApiService.class);

        RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.email = mUserEmail.getText().toString();
        registrationToken.password = mUserPwd.getText().toString();

        final Call<LoginResponse> loginCall = apiService.loginUser(registrationToken);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    SharedPrefsUtil.storePreferencesField(LoginActivity.this, SharedPrefsUtil.TOKEN, loginResponse.token);
                    Toast.makeText(getApplicationContext(), loginResponse.token, Toast.LENGTH_SHORT).show();
                    startNotesActivity();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    private void startNotesActivity() {
        Intent intent = new Intent();
        intent.setClass(this, TasksActivity.class);
        startActivity(intent);
    }
}
