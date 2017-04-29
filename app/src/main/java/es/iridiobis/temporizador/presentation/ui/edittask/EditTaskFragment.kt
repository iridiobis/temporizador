package es.iridiobis.temporizador.presentation.ui.edittask

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.extensions.consume
import es.iridiobis.temporizador.core.extensions.load
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.core.extensions.toast
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogFragment
import es.iridiobis.temporizador.presentation.dialogs.DurationDialogListener
import es.iridiobis.temporizador.presentation.transformations.RoundTransformation
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import kotlinx.android.synthetic.main.fragment_edit_task.*
import mobi.upod.timedurationpicker.TimeDurationUtil
import javax.inject.Inject

class EditTaskFragment : Fragment(), EditTask.View, DurationDialogListener {


    @Inject lateinit var presenter: EditTask.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_task, container, false)
        DaggerEditTaskFragmentComponent.builder()
                .editTaskComponent((activity as ComponentProvider<EditTaskComponent>).getComponent())
                .build()
                .injectMembers(this)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let { presenter.name(text.toString()) }
            }

        })
        et_thumbnail.setOnClickListener { presenter.selectThumbnail() }
        et_duration.setOnClickListener { presenter.selectDuration() }
        et_edit_background.setOnClickListener { presenter.selectBackground() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_write_task, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> consume { presenter.save() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun displayTask(task: TaskModel) {
        et_name.setText(task.name)
        et_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(task.duration))
        et_background.setBackground(task.background!!) { request -> request }
        et_image.load(task.smallBackground!!) { request -> request }
        et_thumbnail.load(task.thumbnail!!) { request -> request.transform(RoundTransformation()) }
    }

    override fun showErrorMessage() = context.toast("finish it!")

    override fun showDurationSelection(duration: Long) {
        val dialog = DurationDialogFragment(duration)
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager, "")
    }

    override fun onTimeSet(duration: Long) {
        presenter.duration(duration)
        et_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration))
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