package com.y2.y2qadmin.misc;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by u on 26.01.2017.
 */

public class PermissionChecker
{
    Activity mActivity;
    int mVersion;

    public static final int RequestCode = 101;

    public PermissionChecker(Activity activity)
    {
        mActivity = activity;

        mVersion = Build.VERSION.SDK_INT;

    }

    public boolean checkPermission(String permission)
    {
        if (mVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            if (!checkIfAlreadyhavePermission(permission))
            {
                requestForSpecificPermission(permission);
                return false;
            }
        }
        return true;
    }

    private boolean checkIfAlreadyhavePermission(String permission)
    {
        int result = ContextCompat.checkSelfPermission(mActivity, permission);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void requestForSpecificPermission(String permission)
    {
        ActivityCompat.requestPermissions(mActivity, new String[]{permission}, RequestCode);
    }
}
