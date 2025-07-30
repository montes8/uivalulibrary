package com.gb.vale.uivalulibrary.utils

import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.gb.vale.uivalulibrary.model.UITayMetaDataImage
import java.io.File
import java.io.IOException


fun String.uiTayDeleteArchive(){
    val delete = File(this)
    if (delete.exists()) {
        if (delete.delete()) {
            Log.v("","file Deleted :$this")
        } else {
            Log.v("","file not Deleted :$this")
        }
    }
}

fun String.uiTayMetaDataImage(): UITayMetaDataImage {
    var uiTayData = UITayMetaDataImage()
    try {
        val exifInterface = ExifInterface(this)
        uiTayData =  UITayMetaDataImage(
            length  = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)?: UI_TAY_EMPTY,
            width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)?: UI_TAY_EMPTY,
            dateTime  = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)?: UI_TAY_EMPTY,
            take  = exifInterface.getAttribute(ExifInterface.TAG_MAKE)?: UI_TAY_EMPTY,
            model  = exifInterface.getAttribute(ExifInterface.TAG_MODEL)?: UI_TAY_EMPTY,
            orientation  = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)?: UI_TAY_EMPTY,
            whiteBalance  = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)?: UI_TAY_EMPTY,
            focalLength  = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)?: UI_TAY_EMPTY,
            flash  = exifInterface.getAttribute(ExifInterface.TAG_FLASH)?: UI_TAY_EMPTY,
            gpsDatesTamp  = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)?: UI_TAY_EMPTY,
            gpsTimesTamp  = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)?: UI_TAY_EMPTY,
            gpsLatitude  = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)?: UI_TAY_EMPTY,
            gpsLatitudeReferential  = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)?: UI_TAY_EMPTY,
            gpsLongitude  = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)?: UI_TAY_EMPTY,
            gpsLongitudeReferential  = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)?: UI_TAY_EMPTY,
            gpsProcessingMethod  = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)?: UI_TAY_EMPTY
        )

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return uiTayData
}