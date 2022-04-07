package com.lomoware.lomorage.logic

import android.location.Geocoder
import android.media.ExifInterface
import com.lomoware.lomorage.LomorageApplication
import com.lomoware.lomorage.R
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.lang.Math.abs
import java.util.*


data class LomoExifInfo(
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var assetInfoAll: String = ""
)

object GpsUtil {
    var pi = 3.14159265358979324
    var a = 6378245.0
    var ee = 0.00669342162296594323
    const val x_pi = 3.14159265358979324 * 3000.0 / 180.0
    fun wgs2bd(lat: Double, lon: Double): DoubleArray {
        val wgs2gcj = wgs2gcj(lat, lon)
        return gcj2bd(wgs2gcj[0], wgs2gcj[1])
    }

    fun gcj2bd(lat: Double, lon: Double): DoubleArray {
        val z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * x_pi)
        val theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * x_pi)
        val bd_lon = z * Math.cos(theta) + 0.0065
        val bd_lat = z * Math.sin(theta) + 0.006
        return doubleArrayOf(bd_lat, bd_lon)
    }

    fun bd2gcj(lat: Double, lon: Double): DoubleArray {
        val x = lon - 0.0065
        val y = lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi)
        val gg_lon = z * Math.cos(theta)
        val gg_lat = z * Math.sin(theta)
        return doubleArrayOf(gg_lat, gg_lon)
    }

    fun wgs2gcj(lat: Double, lon: Double): DoubleArray {
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return doubleArrayOf(mgLat, mgLon)
    }

    private fun transformLat(lat: Double, lon: Double): Double {
        var ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat))
        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLon(lat: Double, lon: Double): Double {
        var ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat))
        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0
        return ret
    }

    fun getAddressByLaLg(latitude: Double, longitude: Double) : String {
        val geocoder = Geocoder(LomorageApplication.appContext, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)  // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        Timber.i("getAddressByLaLg: ${addresses.toString()}")

        return addresses.toString()
    }

}

object AssetExifInfoHelper {

    /**
     * convert 112/1,58/1,390971/10000 ====> 112.99434397362694
     * @param inputScore degree, minutes, second
     * @return degree
     */
    private fun score2dimensionality(inputScore: String?): Double {
        var dimensionality = 0.0
        if (null == inputScore || inputScore.isEmpty()) {
            return dimensionality
        }

        // split via , as 3 parts
        val split = inputScore.split(",".toRegex()).toTypedArray()
        for (i in split.indices) {
            val s = split[i].split("/".toRegex()).toTypedArray()
            // us 112/1 got degree min second
            val v = s[0].toDouble() / s[1].toDouble()
            // 将分秒分别除以60和3600得到度，并将度分秒相加
            dimensionality = dimensionality + v / Math.pow(60.0, i.toDouble())
        }
        return dimensionality
    }

    private fun adjustLatitude(lati: Double, gpsRef: String?): Double {
        if (null == gpsRef || gpsRef.isEmpty()) {
            return lati
        }

        if (gpsRef == "S") {
            return lati * -1.0
        } else if (gpsRef == "N") {
            return abs(lati)
        }

        return lati
    }

    private fun adjustLongtitude(longitude: Double, gpsRef: String?): Double {
        if (null == gpsRef) {
            return longitude
        }

        if (gpsRef == "W") {
            return longitude * -1.0
        } else if (gpsRef == "E") {
            return abs(longitude)
        }

        return longitude
    }

    private fun exifUIName(resId: Int): String {
        return LomorageApplication.appContext.getString(resId)
    }

    /**
     * @param path path of image
     */
    fun getAssetInfo(path: String): LomoExifInfo {
        val file = File(path)

        val fileInfo = "${exifUIName(R.string.Exif_LocalFilePath)} $path\n"

        if (path.isEmpty() || !file.exists() || !LomoUtils.isSupportExifInfo(file.extension)) {
            Timber.e("File format not support yet. file.ext = ${file.extension}")
            return LomoExifInfo(assetInfoAll = fileInfo + "\n" +   exifUIName(R.string.asset_no_exif_info))
        }

        try {
            val exifInterface = ExifInterface(path)
            if (exifInterface != null) {
                val guangquan = exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER)
                val shijain = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
                val digiTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
                val origTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)

                val baoguangshijian = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
                val jiaoju = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
                val chang = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
                val kuan = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
                val moshi = exifInterface.getAttribute(ExifInterface.TAG_MODEL)
                val zhizaoshang = exifInterface.getAttribute(ExifInterface.TAG_MAKE)
                val iso = exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS)
                val jiaodu = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
                val baiph = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)
                val altitude_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF)
                val altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE)
                val latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                val latitude_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                val longitude_ref = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
                val longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                val timestamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)
                val processing_method = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)

                // convert degree, minutes, second to degree.
                // Degrees, minutes and seconds (DMS): 41°24'12.2"N 2°10'26.5"E
                // ==> Degrees and decimal minutes (DMM): 41 24.2028, 2 10.4418
                val lat: Double = adjustLatitude(score2dimensionality(latitude), latitude_ref)
                val lon: Double = adjustLongtitude(score2dimensionality(longitude), longitude_ref)

                val stringBuilder = StringBuilder()
                stringBuilder.append(fileInfo)
                        .append("${exifUIName(R.string.Exif_Aperture)} $guangquan\n")
                        .append("${exifUIName(R.string.Exif_Time)} $shijain\n")
                        .append("${exifUIName(R.string.Exif_DigiTime)} $digiTime\n")
                        .append("${exifUIName(R.string.Exif_OrigTime)} $origTime\n")
                        // .append("${exifUIName(R.string.Exif_ExposureTime)} $baoguangshijian\n")
                        // .append("${exifUIName(R.string.Exif_FocalLength)} $jiaoju\n")
                        .append("${exifUIName(R.string.Exif_Width)} $chang\n")
                        .append("${exifUIName(R.string.Exif_Height)} $kuan\n")
                        .append("${exifUIName(R.string.Exif_Model)} $moshi\n")
                        .append("${exifUIName(R.string.Exif_Make)} $zhizaoshang\n")
                        // .append("${exifUIName(R.string.Exif_ISO)} $iso\n")
                        .append("${exifUIName(R.string.Exif_Orientation)} $jiaodu\n")
                        // .append("${exifUIName(R.string.Exif_WhiteBalance)} $baiph\n")
                        .append("${exifUIName(R.string.Exif_Altitude_Ref)} $altitude_ref\n")
                        .append("${exifUIName(R.string.Exif_Altitude)} $altitude\n")
                        .append("${exifUIName(R.string.Exif_GPSTimeStamp)} $timestamp\n")
                        .append("${exifUIName(R.string.Exif_GPSProcessingMethod)} $processing_method\n")
                        .append("${exifUIName(R.string.Exif_GPSLatitudeRef)} $latitude_ref\n")
                        .append("${exifUIName(R.string.Exif_GPSLongitudeRef)} $longitude_ref\n")
                        .append("${exifUIName(R.string.Exif_GPSLatitude)} $lat\n")
                        .append("${exifUIName(R.string.Exif_GPSLongitude)} $lon\n")

                // val place = GpsUtil.getAddressByLaLg(lat, -lon)
                // stringBuilder.append("address: $place")

                return LomoExifInfo(latitude = lat, longitude = lon, stringBuilder.toString())
            }

            // convert WGS to Baidu coordinate. use this get the addr via Baidu SDK
            // val wgs2bd: DoubleArray = GpsUtil.wgs2bd(lat, lon)
        } catch (e: IOException) {
            // e.printStackTrace()
            Timber.e("getAssetExifInfo exception: ${e.message}")
        }

        return LomoExifInfo(assetInfoAll = fileInfo + "\n" +   exifUIName(R.string.asset_no_exif_info))
    }
}