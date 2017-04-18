package es.iridiobis.temporizador.presentation.ui.newtask.background

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
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent
import kotlinx.android.synthetic.main.fragment_new_task_background.*
import javax.inject.Inject

class BackgroundFragment : Fragment(), Background.View {

    @Inject lateinit var presenter: Background.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_new_task_background, container, false)
        DaggerBackgroundComponent.builder()
                .newTaskComponent((activity as ComponentProvider<NewTaskComponent>).getComponent())
                .build()
                .injectMembers(this)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ntb_select_background.setOnClickListener { presenter.pickImage() }
        ntb_continue.setOnClickListener { presenter.next() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            val origin = CropImage.getPickImageResultUri(context, data)
            presenter.cropBackground(origin)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                presenter.background(result.uri)
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

    override fun showBackground(background: Uri, invalid : Boolean) {
        ntb_background.setBackground(background, invalid) { request -> request }
        ntb_continue.isEnabled = true
        ntb_description.visibility = GONE
    }

}
