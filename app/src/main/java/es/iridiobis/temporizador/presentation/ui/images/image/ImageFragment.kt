package es.iridiobis.temporizador.presentation.ui.images.image

import android.app.Activity.RESULT_OK
import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.extensions.consume
import es.iridiobis.temporizador.core.extensions.load
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.core.extensions.toast
import es.iridiobis.temporizador.presentation.ui.images.ImagesComponent
import kotlinx.android.synthetic.main.fragment_new_task_image.*
import javax.inject.Inject

class ImageFragment : Fragment(), Image.View {

    @Inject lateinit var presenter: Image.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_new_task_image, container, false)
        DaggerImageComponent.builder()
                .imagesComponent((activity as ComponentProvider<ImagesComponent>).getComponent())
                .build()
                .injectMembers(this)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_image_selection, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.is_action_done -> consume { presenter.next() }
        R.id.is_action_select -> consume { presenter.pickImage() }
        R.id.is_action_crop -> consume { presenter.cropBackground() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            val origin = CropImage.getPickImageResultUri(context, data)
            presenter.cropBackground(origin)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                presenter.image(result.uri)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach(this)
        super.onPause()
    }

    override fun showBackground(background: Uri) {
        nti_background.setBackground(background) { request -> request }
    }

    override fun showImage(image: Uri, invalid : Boolean) {
        nti_image.load(image, invalid) { request -> request }
        nti_description.visibility = GONE
    }

    override fun showError(message: String) = context.toast(message)

}
