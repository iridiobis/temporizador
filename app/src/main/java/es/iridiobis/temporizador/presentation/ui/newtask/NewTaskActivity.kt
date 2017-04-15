package es.iridiobis.temporizador.presentation.ui.newtask

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.presentation.ui.newtask.background.BackgroundFragment

class NewTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        supportFragmentManager.beginTransaction().add(R.id.container, BackgroundFragment()).commit()
    }

}