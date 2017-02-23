package es.iridiobis.temporizador.presentation.ui.addTask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.temporizador.R
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity(), AddTask.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        add_task_background.setOnClickListener { CropImage.startPickImageActivity(this) }
    }
}
