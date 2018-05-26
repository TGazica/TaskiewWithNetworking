package osc.ada.tomislavgazica.taskie.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefsUtil {
    public static final String TOKEN = "token";
    public static final String IS_REGISTERED = "registered";

    public static void storePreferencesField(Context context, String key,
                                             String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public static void storePreferencesField(Context context, String key,
                                             boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getPreferencesField(Context context, String key, String defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static boolean getPreferencesFieldBoolean(Context context, String key, boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static String getPreferencesField(Context context, String key) {
        return getPreferencesField(context, key, null);
    }

    public static boolean getPreferencesFieldBoolean(Context context, String key) {
        return getPreferencesFieldBoolean(context, key, false);
    }

}
