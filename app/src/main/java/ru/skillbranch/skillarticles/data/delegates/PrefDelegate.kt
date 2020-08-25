package ru.skillbranch.skillarticles.data.delegates

import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) : ReadWriteProperty<PrefManager, T?> {

    override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
        return when(defaultValue){
            is String -> thisRef.preferences.getString(property.name, defaultValue) as? T
            is Long -> thisRef.preferences.getLong(property.name, defaultValue) as? T
            is Int -> thisRef.preferences.getInt(property.name, defaultValue) as? T
            is Float -> thisRef.preferences.getFloat(property.name, defaultValue) as? T
            is Boolean -> thisRef.preferences.getBoolean(property.name, defaultValue) as? T
            else -> throw NoSuchElementException()
        }
    }

    override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        with(thisRef.preferences.edit()){
            when(value){
                is String -> putString(property.name, value)
                is Boolean -> putBoolean(property.name, value)
                is Float -> putFloat(property.name, value)
                is Int -> putInt(property.name, value)
                is Long -> putLong(property.name, value)
            }
            apply()
        }
    }

}
