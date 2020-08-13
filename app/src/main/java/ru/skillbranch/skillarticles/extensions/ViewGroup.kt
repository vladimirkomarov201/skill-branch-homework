package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup


fun ViewGroup?.getChildOrNull(index: Int): View?{
    return if (this == null || childCount < index) null else getChildAt(index)
}