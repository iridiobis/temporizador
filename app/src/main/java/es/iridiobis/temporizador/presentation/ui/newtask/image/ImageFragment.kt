package es.iridiobis.temporizador.presentation.ui.newtask.image

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.theartofdev.edmodo.cropper.CropImage
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent
import kotlinx.android.synthetic.main.fragment_new_task_image.*
import javax.inject.Inject


class ImageFragment : Fragment(), Image.View {

    @Inject lateinit var presenter: Image.Presenter

    override fun showBackground(background: Uri) {
        nti_background.setBackground(background) { request -> request }
    }

    override fun showImage(image: Uri) {
        nti_image.setBackground(image) { request -> request }
        nti_continue.isEnabled = true
        nti_description.visibility = GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_new_task_image, container, false)
        DaggerImageComponent.builder()
                .newTaskComponent((activity as ComponentProvider<NewTaskComponent>).getComponent())
                .build()
                .injectMembers(this)
        return rootView
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
