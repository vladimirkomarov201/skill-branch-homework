package ru.skillbranch.skillarticles.data.local

import androidx.room.TypeConverter
import ru.skillbranch.skillarticles.data.repositories.MarkdownElement
import ru.skillbranch.skillarticles.data.repositories.MarkdownParser
import java.util.*

class DateConverter{

    @TypeConverter
    fun timestampToDate(timeStamp: Long): Date = Date(timeStamp)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time

}

class MarkdownConverter{

    @TypeConverter
    fun toMarkdown(content: String?): List<MarkdownElement>? = content?.run { MarkdownParser.parse(this) }

}