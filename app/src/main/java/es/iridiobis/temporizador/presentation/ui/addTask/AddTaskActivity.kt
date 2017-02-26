package es.iridiobis.temporizador.presentation.ui.addTask

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.kotlinexample.snack
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogFragment
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogListener
import kotlinx.android.synthetic.main.activity_add_task.*
import mobi.upod.timedurationpicker.TimeDurationUtil

class AddTaskActivity : AppCompatActivity(), AddTask.View, DurationDialogListener {
    override fun onTaskAdded(task: Task) {
        add_task_save.snack("Task " + task.name + "(" + task.id + ") added")
        finish()
    }

    val presenter: AddTask.Presenter = AddTaskPresenter(TasksStorage())

    override fun onTimeSet(duration: Long) {
        presenter.duration(duration)
        add_task_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        add_task_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let { presenter.name(text.toString()) }
            }

        })
        add_task_duration.setOnClickListener { DurationDialogFragment().show(fragmentManager, "") }
        add_task_background.setOnClickListener { CropImage.startPickImageActivity(this) }
        add_task_save.setOnClickListener { presenter.save() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach(this)
        super.onPause()
    }


}
