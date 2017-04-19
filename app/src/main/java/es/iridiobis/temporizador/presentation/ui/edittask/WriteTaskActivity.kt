package es.iridiobis.temporizador.presentation.ui.edittask

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.iridiobis.kotlinexample.toast
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.core.extensions.load
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogFragment
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogListener
import es.iridiobis.temporizador.presentation.transformations.RoundTransformation
import kotlinx.android.synthetic.main.activity_write_task.*
import mobi.upod.timedurationpicker.TimeDurationUtil
import javax.inject.Inject

class WriteTaskActivity : AppCompatActivity(), WriteTask.View, DurationDialogListener {

    companion object {

        private val TASK_ID_EXTRA = "WriteTaskActivity.TASK_ID_EXTRA"

        fun addTaskIntent(context: Context) : Intent {
            return Intent(context, WriteTaskActivity::class.java)
        }

        fun editTaskIntent(id : Long, context: Context) : Intent {
            val intent = Intent(context, WriteTaskActivity::class.java)
            intent.putExtra(TASK_ID_EXTRA, id)
            return intent
        }
    }

    @Inject lateinit var  presenter: WriteTask.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_task)
        DaggerWriteTaskComponent.builder()
                .applicationComponent((application as Temporizador).getComponent())
                .writeTaskModule(WriteTaskModule(intent.extras?.getLong(TASK_ID_EXTRA)))
                .build()
                .injectMembers(this)
        write_task_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let { presenter.name(text.toString()) }
            }

        })
        write_task_duration.setOnClickListener { DurationDialogFragment().show(fragmentManager, "") }
        write_task_background.setOnClickListener { CropImage.startPickImageActivity(this) }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach(this)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_done) {
            presenter.save()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            val background = CropImage.getPickImageResultUri(this, data)
            presenter.background(background)
            CropImage.activity(background).setAspectRatio(2, 1).start(this)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                if (presenter.processCrop(result.uri))
                    CropImage.activity(result.uri).setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1, 1).start(this)

            }
        }
    }

    override fun onTimeSet(duration: Long) {
        presenter.duration(duration)
        write_task_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration))
    }

    override fun displayTask(task: TaskModel) {
        write_task_name.setText(task.name)
        write_task_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(task.duration))
        task.background?.let { activity_write_task.setBackground(task.background!!) { request -> request } }
        task.smallBackground?.let { write_task_small_background.load(task.smallBackground!!) { request -> request } }
        task.thumbnail?.let { write_task_thumbnail.load(task.thumbnail!!) { request -> request.transform(RoundTransformation()) } }
    }

    override fun showErrorMessage() {
        toast("Finish it")
    }

    override fun onTaskAdded(task: Task) = finish()

}