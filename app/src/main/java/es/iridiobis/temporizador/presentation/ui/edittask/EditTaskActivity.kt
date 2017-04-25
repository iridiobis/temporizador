package es.iridiobis.temporizador.presentation.ui.edittask

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogListener
import es.iridiobis.temporizador.presentation.ui.images.background.BackgroundFragment
import es.iridiobis.temporizador.presentation.ui.images.image.ImageFragment
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.ThumbnailFragment
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import java.io.File
import javax.inject.Inject

class EditTaskActivity : AppCompatActivity(), ComponentProvider<EditTaskComponent>, EditTask.NavigationExecutor, DurationDialogListener {

    companion object {

        private val TASK_MODEL_EXTRA = "EditTaskActivity.TASK_MODEL_EXTRA"

        fun editTaskIntent(task: Task, context: Context): Intent {
            val intent = Intent(context, EditTaskActivity::class.java)
            val model = TaskModel(task.id,
                    task.name,
                    task.duration,
                    task.background,
                    task.smallBackground,
                    task.thumbnail)
            intent.putExtra(TASK_MODEL_EXTRA, model)
            return intent
        }
    }

    @Inject lateinit var navigator: EditTask.Navigator
    private lateinit var component: EditTaskComponent
    private var cropImage: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (lastCustomNonConfigurationInstance == null) {
            component = DaggerEditTaskComponent.builder()
                    .applicationComponent((application as Temporizador).getComponent())
                    .editTaskModule(EditTaskModule(intent.extras.getParcelable<TaskModel>(TASK_MODEL_EXTRA)))
                    .build()
        } else {
            component = lastCustomNonConfigurationInstance as EditTaskComponent
        }
        component.injectMembers(this)
        navigator.attach(this)
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.container, EditTaskFragment()).commit()
        }
    }

    override fun onDestroy() {
        navigator.detach(this)
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = component

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragmentManager.findFragmentById(R.id.container).onActivityResult(requestCode, resultCode, data)
    }

    override fun getComponent(): EditTaskComponent = component

    override fun goToBackgroundSelection() {
        fragmentManager.beginTransaction()
                .add(R.id.container, BackgroundFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToImageSelection() {
        fragmentManager.beginTransaction()
                .add(R.id.container, ImageFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToThumbnailSelection() {
        fragmentManager.beginTransaction()
                .add(R.id.container, ThumbnailFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToImagePicker() {
        CropImage.startPickImageActivity(this)
    }

    override fun goToCropBackground(origin: Uri) {
        val metrics = resources.displayMetrics
        crop(origin, "background.jpeg", Pair(metrics.widthPixels, metrics.heightPixels))
    }

    override fun goToCropBackgroundForImage(background: Uri) {
        crop(background, "image.jpeg", Pair(2, 1))
    }

    override fun goToCropForThumbnail(origin: Uri) {
        crop(origin, "thumbnail.jpeg", Pair(1, 1), CropImageView.CropShape.OVAL)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cropImage?.run()
                cropImage = null
            }
        }
    }

    override fun onTimeSet(duration: Long) {
        fragmentManager.findFragmentById(R.id.container)
    }

    override fun goBack() {
        fragmentManager.popBackStackImmediate()
    }

    private fun crop(origin: Uri, name: String, aspectRatio: Pair<Int, Int>, shape: CropImageView.CropShape = CropImageView.CropShape.RECTANGLE) {
        val crop = Runnable {
            CropImage.activity(origin)
                    .setOutputUri(Uri.fromFile(File(externalCacheDir.path, name)))
                    .setCropShape(shape)
                    .setAspectRatio(aspectRatio.first, aspectRatio.second)
                    .start(this)
        }
        if (CropImage.isReadExternalStoragePermissionsRequired(this, origin)) {
            cropImage = crop
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
        } else {
            crop.run()
        }
    }

}