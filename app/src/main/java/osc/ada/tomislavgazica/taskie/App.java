package osc.ada.tomislavgazica.taskie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.realm.Realm;
import osc.ada.tomislavgazica.taskie.handlers.DatabaseHandler;
import osc.ada.tomislavgazica.taskie.interaction.ApiInteractor;
import osc.ada.tomislavgazica.taskie.interaction.ApiInteractorImpl;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import retrofit2.Retrofit;

public class App extends Application {

    private static ApiInteractor apiInteractor;

    private static SharedPreferences preferences;

    private static DatabaseHandler databaseHandler;

    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();

        final Retrofit retrofit = RetrofitUtil.createRetrofit();
        final ApiService apiService = retrofit.create(ApiService.class);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        apiInteractor = new ApiInteractorImpl(apiService);

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        databaseHandler = DatabaseHandler.getInstance(getApplicationContext());
    }

    public static ApiInteractor getApiInteractor() {
        return apiInteractor;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public static Realm getRealm(){
        return realm;
    }
}