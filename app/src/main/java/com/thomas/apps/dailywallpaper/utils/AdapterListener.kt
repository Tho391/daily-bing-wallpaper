package com.thomas.apps.dailywallpaper.utils

object AdapterListener {
    interface EditTextListener {
        fun doOnTextChanged(text: String)
    }

    interface ItemClickListener<T> {
        fun onItemClick(item: T)
    }

    interface ItemTextChangedListener<T> {
        fun onTextChanged(item: T)
    }

    interface ItemUpdateListener<T> {
        fun onUpdate(item: T, position: Int)
    }

}

typealias OnClickListener<T> = (item: T) -> Unit