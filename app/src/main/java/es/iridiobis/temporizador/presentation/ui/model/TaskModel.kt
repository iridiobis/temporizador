package es.iridiobis.temporizador.presentation.ui.model

import android.net.Uri
import android.text.TextUtils


class TaskModel {

    var id : Long? = null
    var name: String? = null
    var duration: Long = 0
    var background: Uri? = null
    var smallBackground: Uri? = null
    var thumbnail: Uri? = null

    fun isValid(): Boolean {
        return !TextUtils.isEmpty(name)
                && duration > 0
                && background != null
                && smallBackground != null
                && thumbnail != null
    }

}