package com.gb.vale.uivalulibrary.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.gb.vale.uivalulibrary.R


@SuppressLint("QueryPermissionsNeeded")
fun AppCompatActivity.uiTayRouteMapIntent(latitude: Double, longitude: Double) {
    val packageManager = this.packageManager
    val geoMaps = Uri.parse("google.navigation:q=${latitude},${longitude}")
    val geoWaz = Uri.parse("waze://?ll=${latitude}, ${longitude}&navigate=yes")
    val intentWaz = Intent(Intent.ACTION_VIEW, geoWaz)
    intentWaz.setPackage(this.getString(R.string.ui_tay_waze_package))
    val intentMap = Intent(Intent.ACTION_VIEW, geoMaps)
    intentMap.setPackage(this.getString(R.string.ui_tay_maps_package))
    try {
        if (intentMap.resolveActivity(packageManager) != null) {
            val chooserIntent =
                Intent.createChooser(intentMap, this.getString(R.string.ui_tay_start_navigation))
            if (intentWaz.resolveActivity(packageManager) != null) {
                val arr = arrayOfNulls<Intent>(1)
                arr[0] = intentWaz
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arr)
                this.startActivity(chooserIntent)
            } else {
                this.startActivity(chooserIntent)
            }
        } else {
            this.uiTaySendUriMap(
                this.getString(R.string.ui_tay_no_maps),
                Uri.parse(this.getString(R.string.ui_tay_maps_store))
            )
        }
    } catch (e: Exception) {
        this.uiTaySendUriMap(
            this.getString(R.string.ui_tay_no_maps),
            Uri.parse(this.getString(R.string.ui_tay_maps_store))
        )
    }
}

fun AppCompatActivity.uiTayLocationMapIntent(
    latitude: String,
    longitude: String
){
    uiTayTryCatch(catch = {uiTayShowToast(this.getString(R.string.tay_ui_error_app_maps))}) {
        val uri = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        this.startActivity(intent)
    }

}

private fun AppCompatActivity.uiTaySendUriMap(messageToast: String, uri: Uri) {
    this.uiTayShowToast(messageToast)
    this.startActivity(Intent(Intent.ACTION_VIEW, uri))
}
