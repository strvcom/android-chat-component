package com.strv.chat.core.core.ui.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.strv.chat.core.core.ui.extensions.OnClickAction

const val DIALOG_PHOTO_PICKER = "photo_picker"
private const val ARGUMENT_TITLE = "title"
private const val ARGUMENT_ITEMS = "items"

class ItemListDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(title: String?, items: Array<String>) = ItemListDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_TITLE, title)
                putStringArray(ARGUMENT_ITEMS, items)
            }
        }
    }

    private var title: String? = null
    private lateinit var items: Array<String>

    private var onClick: OnClickAction<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true
        retainInstance = true

        arguments?.let(this::processArguments)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // cancelable on touch outside
         dialog?.setCanceledOnTouchOutside(true)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().apply {
            add(this@ItemListDialogFragment, tag)
            commitAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        // http://code.google.com/p/android/issues/detail?id=17423
        if (retainInstance) dialog?.setDismissMessage(null)
        super.onDestroyView()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext()).run {
            if (title != null) setTitle(title)
            setItems(items) { _, position -> onClick?.invoke(position) }
            create()
        }

    fun onClick(onClick: OnClickAction<Int>) {
        this.onClick = onClick
    }

    private fun processArguments(arguments: Bundle) {
        title = arguments.getString(ARGUMENT_TITLE)
        items = arguments.getStringArray(ARGUMENT_ITEMS) ?: arrayOf()
    }
}