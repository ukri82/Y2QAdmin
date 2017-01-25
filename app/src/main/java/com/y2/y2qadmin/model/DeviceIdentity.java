package com.y2.y2qadmin.model;

import android.content.Context;
import android.provider.Settings;

import java.util.UUID;

/**
 * Created by u on 24.10.2015.
 */
public class DeviceIdentity
{
    static String myIdentity;

    public static void initialize(Context aContext_in)
    {
        String androidId = Settings.Secure.getString(aContext_in.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)androidId.hashCode() << 32) | androidId.hashCode());
        myIdentity = deviceUuid.toString();
    }

    static public String get()
    {
        return myIdentity;
    }
}
