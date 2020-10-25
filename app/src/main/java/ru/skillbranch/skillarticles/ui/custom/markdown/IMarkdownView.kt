package ru.skillbranch.skillarticles.ui.custom.markdown

import android.text.Spannable
import android.text.SpannableString
import androidx.core.text.getSpans
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.custom.spans.SearchFocusSpan
import ru.skillbranch.skillarticles.ui.custom.spans.SearchSpan

interface IMarkdownView {
    var fontSize: Float
    val spannableContent: Spannable

    fun renderSearchResult(
        results: List<Pair<Int, Int>>,
        offset: Int
    ) {
        clearSearchResult()
        val offsetResult = results.map {(start, end) ->
            start.minus(offset) to end.minus(offset)
        }
        offsetResult.forEach {(start, end) ->
            spannableContent.setSpan(
                SearchSpan(R.color.color_primary, R.color.color_primary_dark),
                start,
                end,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun renderSearchPosition(
        searchPosition: Pair<Int, Int>,
        offset: Int
    ){
        spannableContent.getSpans<SearchFocusSpan>().forEach { spannableContent.removeSpan(it) }
        spannableContent.setSpan(
            SearchFocusSpan(R.color.color_primary, R.color.color_primary_dark),
            searchPosition.first.minus(offset),
            searchPosition.second.minus(offset),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun clearSearchResult(){
        spannableContent.getSpans<SearchSpan>().forEach { spannableContent.removeSpan(it) }
    }
}