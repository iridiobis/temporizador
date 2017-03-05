package es.iridiobis.temporizador.presentation.ui.main

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import es.iridiobis.kotlinexample.inflate
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.domain.model.Task
import kotlinx.android.synthetic.main.view_task_item.view.*
import kotlin.properties.Delegates


class TasksAdapter(
        val runListener: (taskItem: Task) -> Unit,
        val editListener: (taskItem: Task) -> Unit,
        val deleteListener: (taskItem: Task) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskHolder>() {

    var data: List<Task> by Delegates.observable(emptyList()) {
        _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskHolder(parent.inflate(R.layout.view_task_item), runListener, editListener, deleteListener)

    override fun onBindViewHolder(holder: TaskHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size


    class TaskHolder(
            view: View,
            val runListener: (Task) -> Unit,
            val editListener: (taskItem: Task) -> Unit,
            val deleteListener: (taskItem: Task) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bind(task: Task) = with(itemView) {
            task_name.text = task.name
            task_background.setBackground(task.smallBackground) { request -> request }
            task_run.setOnClickListener { runListener(task) }
            task_menu.setOnClickListener { showPopup(it, task) }
        }

        private fun showPopup(view: View, task: Task) {
            val popup = PopupMenu(view.context, view)
            val inflate = popup.menuInflater
            inflate.inflate(R.menu.menu_task, popup.menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit -> {
                        editListener(task)
                    }
                    R.id.delete -> {
                        deleteListener(task)
                    }
                    else -> return@OnMenuItemClickListener false
                }
                false
            })
            popup.show()
        }
    }
}