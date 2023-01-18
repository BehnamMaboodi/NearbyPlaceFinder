package me.behna.nearbyplace.utilities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager

object SystemUtils {

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        val view = activity.currentFocus ?: View(activity)
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getActivity(context: Context): Activity {
        var activity = context
        while (activity is ContextWrapper) {
            if (activity is Activity) return activity

            activity = activity.baseContext
        }
        throw RuntimeException("Cannot find any activity in provided context")
    }
}