package es.iridiobis.temporizador.domain.model

import android.net.Uri

data class Task(val id: Long, val name: String, val duration: Long, val background: Uri, val smallBackground: Uri, val thumbnail: Uri)
