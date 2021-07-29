package com.floridakeys.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @description     Action Utils
 *
 * @author          Adrian
 */
public class ActionUtil {
    /**
     * Phone Call Activity
     *
     * @param context
     * @param phoneNumber
     */
    public static void callPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * Call Web Browser
     *
     * @param context
     * @param url
     */
    public static void callBrowser(Context context, String url) {
        String urlComp;
        if (!url.startsWith("https://") && !url.startsWith("http://"))
            urlComp = "http://" + url;
        else
            urlComp = url;

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlComp));
        context.startActivity(i);
    }

    /**
     * Show navigation from this point
     */
    public static void navigateTo(Context context, double latitude, double longitude) {
        Uri uri = Uri.parse(String.format("geo:%f,%f", latitude, longitude));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
