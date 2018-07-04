package com.elyeproj.locationdetection

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.support.v4.content.ContextCompat

class LocationManager(val activity : Activity) {

    private var allowAskForPermission = true

    fun checkPermissionAndRequest(permissionCode: Int) {
        if (!isLocationPermissionGranted() && allowAskForPermission) {
            activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    fun requestLocationPermissionResult(grantResults: IntArray,
                                        permissions: Array<String>, onSuccess: () -> Unit = {},
                                        onReject: () -> Unit = {}, onDenied: () -> Unit = {}) {
        if (permissions.isEmpty()) return
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onSuccess()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !activity.shouldShowRequestPermissionRationale(permissions[0])) {
            allowAskForPermission = false
            onReject()
        } else {
            onDenied()
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}