package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import com.example.hellotriangle.MyLog;

public class PermissionHelper {

    private static final String TAG = "PermissionHelper";

    private final static int PERMISSION_REQUEST_CODE = 0x1000;

    private static PermissionInterface mPermissionInterface;

    public static boolean MyRequestPermission(@NonNull Context context, String[] permissionList, PermissionInterface permissionInterface) {
        if (null == permissionList) {
            return true;
        }
        mPermissionInterface = permissionInterface;
        boolean bHasPermission = true;
        for (int i = 0; i < permissionList.length; ++i) {
            MyLog.d(TAG, permissionList[i]);
            if (ContextCompat.checkSelfPermission(context, permissionList[i]) != PackageManager.PERMISSION_GRANTED
                    || PermissionChecker.checkSelfPermission(context, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                bHasPermission = false;
                break;
            }
        }

        if (!bHasPermission){
            ActivityCompat.requestPermissions((Activity) context, permissionList, PERMISSION_REQUEST_CODE);
        }
        return bHasPermission;
    }

    public static void onMyRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults)
    {
        if (PERMISSION_REQUEST_CODE == requestCode) {
            boolean bGetPermission = false;
            if (grantResults.length > 0) {
                bGetPermission = true;
                for (int i = 0; i < permissions.length; ++i) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        bGetPermission = false;
                        break;
                    }
                }
            }
            if (bGetPermission && null != mPermissionInterface) {
                mPermissionInterface.doPermissionSucceed();
            }
            else if (!bGetPermission && null != mPermissionInterface) {
                mPermissionInterface.doPermissionFailed();
            }
        }
    }
}
