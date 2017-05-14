package es.iridiobis.temporizador.presentation.ui.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import es.iridiobis.temporizador.domain.model.Task

data class TaskModel(
        var id: Long? = null,
        var name: String? = null,
        var duration: Long = 0,
        var background: Uri? = null,
        var smallBackground: Uri? = null,
        var thumbnail: Uri? = null) : Parcelable {

    fun isValid() = !TextUtils.isEmpty(name)
            && duration > 0
            && background != null
            && smallBackground != null
            && thumbnail != null

    fun isComplete() = id != null && isValid()

    /**
     * Converts a complete task model in a task
     */
    fun parse() = Task(id!!, name!!, duration, background!!, smallBackground!!, thumbnail!!)

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<TaskModel> = object : Parcelable.Creator<TaskModel> {
            override fun createFromParcel(source: Parcel): TaskModel = TaskModel(source)
            override fun newArray(size: Int): Array<TaskModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readLong(),
            source.readParcelable<Uri>(Uri::class.java.classLoader),
            source.readParcelable<Uri>(Uri::class.java.classLoader),
            source.readParcelable<Uri>(Uri::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(name)
        dest?.writeLong(duration)
        dest?.writeParcelable(background, 0)
        dest?.writeParcelable(smallBackground, 0)
        dest?.writeParcelable(thumbnail, 0)
    }
}