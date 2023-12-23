package id.dimas.kasirpintar.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    private static final String SHARED_PREFERENCES_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_SAVED_PIN = "isSavedPin";
    private static final String KEY_IS_SHOW_PROFIT = "isShowProfit";
    private static final String KEY_SHOP_ID = "idOutlet";
    private static final String KEY_SHOP_NAME = "shopName";
    private static final String KEY_SHOP_ADDRESS = "shopAddress";

    private final SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveShopId(String shopId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHOP_ID, shopId);
        editor.apply();
    }

    public String getShopId() {
        return sharedPreferences.getString(KEY_SHOP_ID, "");
    }

    public void saveShopName(String shopName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHOP_NAME, shopName);
        editor.apply();
    }

    public String getShopName() {
        return sharedPreferences.getString(KEY_SHOP_NAME, "");
    }

    public void saveShopAddress(String shopAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHOP_ADDRESS, shopAddress);
        editor.apply();
    }

    public String getShopAddress() {
        return sharedPreferences.getString(KEY_SHOP_ADDRESS, "");
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setShowProfit(boolean isShowProfit) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_SHOW_PROFIT, isShowProfit);
        editor.apply();
    }

    public boolean isShowProfit() {
        return sharedPreferences.getBoolean(KEY_IS_SHOW_PROFIT, true);
    }


    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setIsSavedPin(boolean isSavedPin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_SAVED_PIN, isSavedPin);
        editor.apply();
    }

    public boolean isSavedPin() {
        return sharedPreferences.getBoolean(KEY_IS_SAVED_PIN, false);
    }

    public void clearAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}