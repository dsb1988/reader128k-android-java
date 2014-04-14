package net.reader128k.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.reader128k.util.Constants;
import org.apache.http.message.BasicNameValuePair;

public class AccountUtils {
    public static boolean isAuthenticated(Context context) {
        return getSessionCookie(context) != null;
    }

    public static BasicNameValuePair getSessionCookie(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String cookieValue = sp.getString(Constants.READER_COOKIE_HUNCHENTOOT_SESSION, null);
        return cookieValue == null ? null : new BasicNameValuePair("Cookie", cookieValue);
    }

    public static void setSessionCookie(Context context, BasicNameValuePair cookie) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String value = cookie == null ? null : cookie.getValue();
        sp.edit().putString(Constants.READER_COOKIE_HUNCHENTOOT_SESSION, value).commit();
    }
}
