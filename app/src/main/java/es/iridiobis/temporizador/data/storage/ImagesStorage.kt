package es.iridiobis.temporizador.data.storage

import android.content.ContentResolver.SCHEME_CONTENT
import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.*
import javax.inject.Inject


class ImagesStorage @Inject constructor(val applicationContext: Context) {
    fun getFullBackground(id: Long): Uri {
        return Uri.fromFile(getFile(id.toString()))
    }

    fun getSmallBackground(id: Long): Uri {
        return Uri.fromFile(getFile(id.toString() + "_small"))
    }

    fun getThumbnail(id: Long): Uri {
        return Uri.fromFile(getFile(id.toString() + "_thumbnail"))
    }

    fun setFullBackground(id: Long, background : Uri) {
        saveFile(id.toString(), background)
    }

    fun setSmallBackground(id: Long, smallBackground : Uri) {
        saveFile(id.toString() + "_small", smallBackground)
    }

    fun setThumbnail(id: Long, thumbnail : Uri) {
        saveFile(id.toString() + "_thumbnail", thumbnail)
    }

    private fun getFile(name: String) : File {
        return File(applicationContext.filesDir, name + ".jpeg")

    }
    private fun saveFile(name: String, source: Uri) {

        val destination = getFile(name)
        if (source.toString().contains(destination.toString())) {
            return
        }
        Log.wtf("ASDF", "Saving in " + destination.toString() + " the file from " + source.toString())
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null

        try {
            if (SCHEME_CONTENT == source.scheme) {
                bis = BufferedInputStream(applicationContext.contentResolver.openInputStream(source))
            } else {
                bis = BufferedInputStream(FileInputStream(source.path))
            }
            bos = BufferedOutputStream(FileOutputStream(destination))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
        } catch (e: IOException) {
            Log.e("ASDF", e.toString())
        } finally {
            try {
                bis!!.close()
                bos!!.close()
            } catch (e: IOException) {

            }

        }
    }
}
