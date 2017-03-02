package es.iridiobis.temporizador.presentation.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import es.iridiobis.kotlinexample.inflate
import es.iridiobis.kotlinexample.load
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.domain.model.Task
import kotlinx.android.synthetic.main.view_task_item.view.*
import kotlin.properties.Delegates

class TasksAdapter(val listener: (taskItem: Task) -> Unit)
    : RecyclerView.Adapter<TasksAdapter.TaskHolder>() {

    var data: List<Task> by Delegates.observable(emptyList()) {
        p, old, new -> notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskHolder(parent.inflate(R.layout.view_task_item), listener)

    override fun onBindViewHolder(holder: TaskHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size


    class TaskHolder(view: View, val listener: (Task) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(task: Task) = with(itemView) {
            task_name.text = task.name
            task_background.setBackground(task.smallBackground) { request -> request }
            itemView.setOnClickListener { listener(task) }
        }
    }
}