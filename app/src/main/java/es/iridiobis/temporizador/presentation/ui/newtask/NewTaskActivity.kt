package es.iridiobis.temporizador.presentation.ui.newtask

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.presentation.ui.newtask.background.BackgroundFragment
import es.iridiobis.temporizador.presentation.ui.newtask.image.ImageFragment
import java.io.File
import javax.inject.Inject
import android.content.pm.PackageManager


class NewTaskActivity : AppCompatActivity(), ComponentProvider<NewTaskComponent>, NewTask.NavigationExecutor {

    @Inject lateinit var navigator: NewTask.Navigator

    private lateinit var component: NewTaskComponent
    private var cropImage : Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        component = DaggerNewTaskComponent.builder()
                .applicationComponent((application as Temporizador).getComponent())
                .build()
        component.injectMembers(this)
        navigator.attach(this)
        supportFragmentManager.beginTransaction().add(R.id.container, BackgroundFragment()).commit()
    }

    override fun onDestroy() {
        navigator.detach(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.findFragmentById(R.id.container).onActivityResult(requestCode, resultCode, data)
    }

    override fun getComponent(): NewTaskComponent = component

    override fun goToImageSelection() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, ImageFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToImagePicker() {
        CropImage.startPickImageActivity(this)
    }

    override fun goToCropBackground(origin: Uri) {
        val cropBackground = Runnable {
            val metrics = resources.displayMetrics
            CropImage.activity(origin)
                    .setOutputUri(Uri.fromFile(File(externalCacheDir.path, "background.jpeg")))
                    .setAspectRatio(metrics.widthPixels, metrics.heightPixels)
                    .start(this)
        }
        if (CropImage.isReadExternalStoragePermissionsRequired(this, origin)) {
            cropImage = cropBackground
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
        } else {
            cropBackground.run()
        }
    }

    override fun goToCropBackgroundForImage(background: Uri) {
        CropImage.activity(background)
                .setOutputUri(Uri.fromFile(File(externalCacheDir.path, "image.jpeg")))
                .setAspectRatio(2, 1)
                .start(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cropImage?.run()
                cropImage = null
            }
        }
    }

}