package es.iridiobis.temporizador.presentation.ui.images.image

import android.app.Activity.RESULT_OK
import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.extensions.load
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.ui.images.ImagesComponent
import kotlinx.android.synthetic.main.fragment_new_task_image.*
import javax.inject.Inject

class ImageFragment : Fragment(), Image.View {

    @Inject lateinit var presenter: Image.Presenter

    override fun showBackground(background: Uri) {
        nti_background.setBackground(background) { request -> request }
    }

    override fun showImage(image: Uri, invalid : Boolean) {
        nti_image.load(image, invalid) { request -> request }
        nti_continue.isEnabled = true
        nti_description.visibility = GONE
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nti_select_image.setOnClickListener { CropImage.startPickImageActivity(activity) }
        nti_crop_background.setOnClickListener { presenter.cropBackground() }
        nti_continue.setOnClickListener { presenter.next() }
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

}
