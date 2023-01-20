package me.behna.nearbyplace.utilities.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout


@BindingMethods(
    BindingMethod(
        type = TextView::class,
        attribute = "android:onEditorAction",
        method = "setOnEditorActionListener"
    ),
    BindingMethod(type = TextView::class, attribute = "selected", method = "setSelected"),
    BindingMethod(type = TextView::class, attribute = "enabled", method = "setEnabled"),
    BindingMethod(type = TextView::class, attribute = "pressed", method = "setPressed"),
    BindingMethod(type = TextInputLayout::class, attribute = "error", method = "setError"),
    BindingMethod(
        type = Chip::class,
        attribute = "onCloseIconClick",
        method = "setOnCloseIconClickListener"
    )
)
class BindingAdapters {
}


@BindingAdapter(value = ["text"], requireAll = false)
fun stringResBinding(view: TextView, stringRes: Int) {
    view.setText(stringRes)
}