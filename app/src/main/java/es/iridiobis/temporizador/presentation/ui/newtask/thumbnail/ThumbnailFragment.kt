package es.iridiobis.temporizador.presentation.ui.newtask.thumbnail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.squareup.picasso.Transformation
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.extensions.load
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent
import es.iridiobis.temporizador.presentation.ui.newtask.image.DaggerImageComponent
import es.iridiobis.temporizador.presentation.ui.newtask.image.Image
import kotlinx.android.synthetic.main.fragment_new_task_image.*
import kotlinx.android.synthetic.main.fragment_new_task_thumbnail.*
import javax.inject.Inject


class ThumbnailFragment : Fragment(), Thumbnail.View {

    @Inject lateinit var presenter: Thumbnail.Presenter

    override fun showBackground(background: Uri) {
        ntt_background.setBackground(background) { request -> request }
    }

    override fun showThumbnail(thumbnail: Uri, invalid : Boolean) {
        ntt_thumbnail.load(thumbnail, invalid) { request -> request.transform(RoundTransformation()) }
        ntt_continue.isEnabled = true
        ntt_description.visibility = GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_new_task_thumbnail, container, false)
        DaggerThumbnailComponent.builder()
                .newTaskComponent((activity as ComponentProvider<NewTaskComponent>).getComponent())
                .build()
                .injectMembers(this)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ntt_select_thumbnail.setOnClickListener { presenter.pickImage() }
        ntt_crop_background.setOnClickListener { presenter.cropBackground() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            val origin = CropImage.getPickImageResultUri(context, data)
            presenter.crop(origin)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                presenter.thumbnail(result.uri)
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

    class RoundTransformation : Transformation {
        override fun key(): String {
            return "Round"
        }

        override fun transform(source: Bitmap): Bitmap {
            return CropImage.toOvalBitmap(source)
        }

    }

}
