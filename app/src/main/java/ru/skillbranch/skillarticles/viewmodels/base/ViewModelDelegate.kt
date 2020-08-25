package ru.skillbranch.skillarticles.viewmodels.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelDelegate<T : ViewModel>(private val clazz: Class<T>, private val arg: Any?) :
    ReadOnlyProperty<FragmentActivity, T> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (clazz.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(arg as String) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}