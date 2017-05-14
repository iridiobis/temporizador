package es.iridiobis.temporizador.presentation.ui.newtask

import android.Manifest
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
import es.iridiobis.temporizador.presentation.ui.images.background.BackgroundFragment
import es.iridiobis.temporizador.presentation.ui.images.image.ImageFragment
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.ThumbnailFragment
import es.iridiobis.temporizador.presentation.ui.newtask.information.InformationFragment
import kotlinx.android.synthetic.main.activity_container.*
import java.io.File
import javax.inject.Inject

class NewTaskActivity : AppCompatActivity(), ComponentProvider<NewTaskComponent>, NewTask.NavigationExecutor {

    @Inject lateinit var navigator: NewTask.Navigator

    private lateinit var component: NewTaskComponent
    private var cropImage: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (lastCustomNonConfigurationInstance == null) {
            component = DaggerNewTaskComponent.builder()
                    .applicationComponent((application as Temporizador).getComponent())
                    .build()
        } else {
            component = lastCustomNonConfigurationInstance as NewTaskComponent
        }
        component.injectMembers(this)
        navigator.attach(this)
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.container, BackgroundFragment()).commit()
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

    override fun getComponent(): NewTaskComponent = component

    override fun goToImageSelection() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, ImageFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToThumbnailSelection() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, ThumbnailFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToInformationInput() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, InformationFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun goToImagePicker() {
        CropImage.startPickImageActivity(this)
    }

    override fun goToCropBackground(origin: Uri) {
        crop(origin, "background.jpeg", Pair(container.width, container.height ))
    }

    override fun goToCropForImage(origin: Uri) {
        crop(origin, "image.jpeg", Pair(2, 1))
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