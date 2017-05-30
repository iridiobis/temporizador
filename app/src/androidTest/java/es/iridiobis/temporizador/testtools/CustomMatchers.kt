package es.iridiobis.temporizador.testtools

import android.content.Intent
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import android.widget.TextView

import org.hamcrest.Description
import org.hamcrest.Matcher

import es.iridiobis.temporizador.presentation.ui.main.TasksAdapter

import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`

object CustomMatchers {

    fun chooser(optionMatcher: Matcher<Intent>): Matcher<Intent> {
        return allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(`is`(Intent.EXTRA_INTENT), optionMatcher))
    }

    fun withHolderNamed(taskName: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, TasksAdapter.TaskHolder>(TasksAdapter.TaskHolder::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("No ViewHolder found with a task named: " + taskName)
            }

            override fun matchesSafely(item: TasksAdapter.TaskHolder): Boolean {
                val timeViewText = item.itemView.findViewById(es.iridiobis.temporizador.R.id.task_name) as TextView ?: return false
                return timeViewText.text.toString() == taskName
            }
        }
    }

}
