package es.iridiobis.temporizador.presentation.transformations

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import com.theartofdev.edmodo.cropper.CropImage


class RoundTransformation : Transformation {
    override fun key(): String {
        return "Round"
    }

    override fun transform(source: Bitmap): Bitmap {
        return CropImage.toOvalBitmap(source)
    }

}