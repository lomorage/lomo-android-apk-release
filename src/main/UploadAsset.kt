/*
 * File：UploadAsset.kt  Module：app  Project：lomo-android-b
 * Current Modify time：2019-12-18 20:26:30
 * Last modify time：2019-12-18 20:26:30
 * Author：aisnote @ com.lomoware.lomorage
 *
 * Copyright (c) 2019
 *
 *
 */

package com.lomoware.lomorage.network

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.SocketException
import kotlin.system.measureNanoTime

// https://stackoverflow.com/questions/33338181/is-it-possible-to-show-progress-bar-when-upload-image-via-retrofit-2/33384551#33384551

data class ProgressObj(
        val file: String,
        val totalSize: Long,
        val percentage: Float
)

class ProgressRequestBody(val mFile: File, val ignoreFirstNumberOfWriteToCalls: Int = 0, alreadyUploadedSize: Long = 0) : RequestBody() {

    private var mAlreadyUploadedSize = alreadyUploadedSize
    private val mInputStream: FileInputStream = FileInputStream(mFile)

    private var numWriteToCalls = 0

    protected val getProgressSubject: PublishSubject<ProgressObj> = PublishSubject.create<ProgressObj>()

    fun getProgressSubject(): Observable<ProgressObj> {
        return getProgressSubject
    }

    private var mForceToStop: Boolean = false
    fun forceToStop(toStop: Boolean) {
        mForceToStop = toStop
    }

    override fun contentType(): MediaType {
        return MediaType.parse("application/octet-stream")!!
        // return MediaType.parse("application/octet-stream")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        // if (mAlreadyUploadedSize > 0)
        //     return mAlreadyUploadedSize

        return mFile.length() - mAlreadyUploadedSize
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        numWriteToCalls++

        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inStream = mInputStream
        var uploaded: Long = 0

        // resume upload
        uploaded += mAlreadyUploadedSize
        inStream.skip(mAlreadyUploadedSize)

        try {
            var read: Int
            var lastProgressPercentUpdate = 0.0f
            read = inStream.read(buffer)
            while (read != -1) {

                if (mForceToStop) {
                    Timber.i("upg>> progressRequestBody forceToStop")
                    break
                }

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = inStream.read(buffer)

                // when using HttpLoggingInterceptor it calls writeTo and passes data into a local buffer just for logging purposes.
                // the second call to write to is the progress we actually want to track
                if (numWriteToCalls > ignoreFirstNumberOfWriteToCalls ) {
                    val percentage = (uploaded.toFloat() / fileLength.toFloat()) * 100f
                    //prevent publishing too many updates, which slows upload, by checking if the upload has progressed by at least 1 percent
                    if (percentage - lastProgressPercentUpdate > 1 || percentage == 100f) {
                        // publish progress

                        getProgressSubject.onNext( ProgressObj(mFile.name, fileLength, percentage))
                        lastProgressPercentUpdate = percentage
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        } finally {
            // inStream.close()
            // sink.close()
        }
    }

    companion object {
        private val DEFAULT_BUFFER_SIZE = 1024*10
    }
}