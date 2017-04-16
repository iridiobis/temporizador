package es.iridiobis.temporizador.presentation.ui.newtask.background

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
import kotlinx.android.synthetic.main.fragment_new_task_background.*
import javax.inject.Inject

class BackgroundFragment : Fragment(), Background.View {

    @Inject lateinit var presenter: Background.Presenter

    override fun showBackground(background: Uri) {
        ntb_background.setBackground(background) { request -> request }
        ntb_continue.isEnabled = true
        ntb_description.visibility = GONE
    }

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
        ntb_select_background.setOnClickListener { CropImage.startPickImageActivity(activity) }
        ntb_continue.setOnClickListener { presenter.next() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            val background = CropImage.getPickImageResultUri(context, data)
            presenter.background(background)
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
