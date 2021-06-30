package ru.skillbranch.sbdelivery.domain.entity

import androidx.room.ColumnInfo

data class DishEntity(
    val id: String,
    @ColumnInfo(name = "category")
    val categoryId: String,
    val image: String,
    val price: String,
    @ColumnInfo(name = "name")
    val title: String
)