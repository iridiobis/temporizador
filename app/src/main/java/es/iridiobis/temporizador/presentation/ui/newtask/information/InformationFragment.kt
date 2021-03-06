package es.iridiobis.temporizador.presentation.ui.newtask.information

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
import es.iridiobis.temporizador.presentation.transformations.RoundTransformation
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent
import kotlinx.android.synthetic.main.fragment_new_task_information.*
import mobi.upod.timedurationpicker.TimeDurationUtil
import javax.inject.Inject


class InformationFragment : Fragment(), Information.View, DurationDialogFragment.DurationDialogListener {

    @Inject lateinit var presenter: Information.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_new_task_information, container, false)
        DaggerInformationComponent.builder()
                .newTaskComponent((activity as ComponentProvider<NewTaskComponent>).getComponent())
                .build()
                .injectMembers(this)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nt_info_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let { presenter.name(text.toString()) }
            }

        })
        nt_info_duration.setOnClickListener{ presenter.selectDuration() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_write_task, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> consume { presenter.save() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach(this)
        super.onPause()
    }

    override fun displayTask(task: TaskModel) {
        nt_info_name.setText(task.name)
        nt_info_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(task.duration))
        nt_info_background.setBackground(task.background!!) { request -> request }
        nt_info_image.load(task.smallBackground!!) { request -> request }
        nt_info_thumbnail.load(task.thumbnail!!) { request -> request.transform(RoundTransformation()) }
    }

    override fun showDurationSelection(duration: Long) {
        val dialog = DurationDialogFragment(duration)
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager, "")
    }

    override fun onTimeSet(duration: Long) {
        presenter.duration(duration)
        nt_info_duration.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration))
    }

    override fun showErrorMessage() = context.toast("finish it!")

}
