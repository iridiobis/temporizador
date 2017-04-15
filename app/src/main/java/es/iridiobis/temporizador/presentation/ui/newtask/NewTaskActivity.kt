package es.iridiobis.temporizador.presentation.ui.newtask

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.presentation.ui.newtask.background.BackgroundFragment

class NewTaskActivity : AppCompatActivity(), ComponentProvider<NewTaskComponent> {

    private lateinit var component : NewTaskComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        component = DaggerNewTaskComponent.builder()
                .applicationComponent((application as Temporizador).getComponent())
                .build()
        supportFragmentManager.beginTransaction().add(R.id.container, BackgroundFragment()).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.findFragmentById(R.id.container).onActivityResult(requestCode, resultCode, data)
    }

    override fun getComponent(): NewTaskComponent = component

}