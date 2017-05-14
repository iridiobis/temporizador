package es.iridiobis.temporizador.data.storage

import android.content.ContentResolver.SCHEME_CONTENT
import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.*
import javax.inject.Inject

class ImagesStorage @Inject constructor(val applicationContext: Context) {

    fun getUri(name: String): Uri = Uri.fromFile(getFile(name))

    fun setBackground(id: Long, background : Uri, oldName: String? = null) : String = saveFile(id.toString(), background, oldName)

    fun setImage(id: Long, image : Uri, oldName: String? = null) : String = saveFile(id.toString() + "_small", image, oldName)

    fun setThumbnail(id: Long, thumbnail : Uri, oldName: String? = null) : String = saveFile(id.toString() + "_thumbnail", thumbnail, oldName)

    fun deleteImage(image: Uri) = File(image.path).delete()

    private fun getFile(name: String) = File(applicationContext.filesDir, name + ".jpeg")

    private fun saveFile(name: String, source: Uri, oldName: String?) : String {
        if (source.toString().contains(name)) {
            return name
        }
        val newName = name + "_" + System.currentTimeMillis()
        val destination = getFile(newName)
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        var succesful = false

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
            succesful = true
            if (oldName != null) getFile(oldName).delete()
        } catch (e: IOException) {
            Log.e("ASDF", e.toString())
        } finally {
            try {
                bis!!.close()
                bos!!.close()
            } catch (e: IOException) {

            }
            return if(succesful) newName else oldName ?: ""
        }
    }
}
