package es.iridiobis.temporizador.presentation.ui.writetask

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.extensions.load
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogFragment
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogListener
import kotlinx.android.synthetic.main.activity_write_task.*
import mobi.upod.timedurationpicker.TimeDurationUtil
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import com.theartofdev.edmodo.cropper.CropImageView
import es.iridiobis.temporizador.data.storage.ImagesStorage


class WriteTaskActivity : AppCompatActivity(), WriteTask.View, DurationDialogListener {

    override fun onTaskAdded(task: Task) = finish()

    var presenter: WriteTask.Presenter? = null

    override fun onTimeSet(duration: Long) {
        presenter!!.duration(duration)
        write_task_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_task)
        presenter = WriteTaskPresenter(TasksStorage(ImagesStorage(ContextWrapper(applicationContext))))
        write_task_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let { presenter!!.name(text.toString()) }
            }

        })
        write_task_duration.setOnClickListener { DurationDialogFragment().show(fragmentManager, "") }
        write_task_background.setOnClickListener { CropImage.startPickImageActivity(this) }
        write_task_save.setOnClickListener { presenter!!.save() }
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
    }

    override fun onPause() {
        presenter!!.detach(this)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            val background = CropImage.getPickImageResultUri(this, data)
            presenter!!.background(background)
            CropImage.activity(background).setAspectRatio(2,1).start(this)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                if (presenter!!.processCrop(result.uri))
                    CropImage.activity(result.uri).setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1,1).start(this)

            }
        }
    }

    override fun displayBackground(background: Uri) {
        activity_write_task.setBackground(background) { request -> request }
    }

    override fun displaySmallBackground(smallBackground: Uri) {
        write_task_small_background.load(smallBackground) { request -> request }
    }

    override fun displayThumbnail(thumbnail: Uri) {
        write_task_thumbnail.load(thumbnail) { request -> request.transform(RoundTransformation()) }
    }

    class RoundTransformation : Transformation {
        override fun key(): String {
            return "Round"
        }

        override fun transform(source: Bitmap): Bitmap {
            return CropImage.toOvalBitmap(source)
        }

    }

}