package es.iridiobis.kotlinexample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

/*
Inline: in bytecode, replace function with the code (so we can use type inside code, no warning on casting
 */
inline fun <reified T : View> ViewGroup.inflate(layout: Int, attachToRoot: Boolean = false): T {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot) as T
}

fun ImageView.load(src: Uri) {
    Picasso.with(context).load(src).into(this)
}

inline fun <reified T : View> RecyclerView.ViewHolder.find(id: Int): T {
    return itemView.findViewById(id) as T
}

inline fun <reified T : Activity> Context.startActivity2(vararg params: Pair<String, Int>) {
    val intent = Intent(this, T::class.java)
    params.forEach { intent.putExtra(it.first, it.second) }
    startActivity(intent)
}

