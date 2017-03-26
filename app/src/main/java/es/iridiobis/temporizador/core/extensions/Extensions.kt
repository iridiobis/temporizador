package es.iridiobis.kotlinexample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
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

fun Context.getPreferencesEditor() : SharedPreferences.Editor {
    return PreferenceManager.getDefaultSharedPreferences(this).edit()
}

fun Context.contains(key : String) : Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).contains(key)
}

fun Context.getLong(key : String) : Long {
    return PreferenceManager.getDefaultSharedPreferences(this).getLong(key, 0)
}

fun View.snack(
        message: String,
        length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, length).show()
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

