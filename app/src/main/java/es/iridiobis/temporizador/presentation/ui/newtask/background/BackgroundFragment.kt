package es.iridiobis.temporizador.presentation.ui.newtask.background

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import es.iridiobis.temporizador.R

class BackgroundFragment : Fragment(), Background.View {

    override fun showBackground(background: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_task_background, container, false)
    }

}
