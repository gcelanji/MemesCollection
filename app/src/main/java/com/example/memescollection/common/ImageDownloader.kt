package com.example.memescollection.common

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.example.memescollection.model.Meme
import com.google.android.material.snackbar.Snackbar
import java.io.File

object ImageDownloader {


    private lateinit var snackbar: Snackbar
    fun download(item: Meme, mgr: DownloadManager, ) {
        downloadFile(item.url, item.name, mgr)
    }

    private fun downloadFile(
        URl: String?,
        imageName: String?,
        mgr: DownloadManager
    ) {
        val direct = File(
            Environment.getExternalStorageDirectory()
                .toString()
        )
        if (!direct.exists()) {
            direct.mkdirs()
        }
        val downloadUri = Uri.parse(URl)
        val request = DownloadManager.Request(
            downloadUri
        )
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI
                    or DownloadManager.Request.NETWORK_MOBILE
        )
            .setAllowedOverRoaming(false).setTitle("$imageName")
            .setDescription("Something useful. No, really.")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${imageName}.jpeg"
        )
        mgr.enqueue(request)
    }

}