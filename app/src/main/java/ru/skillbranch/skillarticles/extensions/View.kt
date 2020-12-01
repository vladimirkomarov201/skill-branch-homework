package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.skillbranch.skillarticles.R


fun View.setMarginOptionally(left:Int = marginLeft, top : Int = marginTop, right : Int = marginRight, bottom : Int = marginBottom){
    val lp = layoutParams as ViewGroup.MarginLayoutParams
    lp.setMargins(left, top, right, bottom)
    layoutParams = lp
}

fun View.setPaddingOptionally(left:Int = paddingEnd, top : Int = paddingTop, right : Int = paddingRight, bottom : Int = paddingBottom){
//    val lp = layoutParams as ViewGroup.MarginLayoutParams
//    lp.setMargins(left, top, right, bottom)
//    layoutParams = lp
}

fun BottomNavigationView.selectDestination(destination: NavDestination){
    when(destination.id){
        R.id.nav_articles -> menu.findItem(R.id.nav_articles).isChecked = true
        R.id.nav_profile -> menu.findItem(R.id.nav_profile).isChecked = true
        R.id.nav_bookmarks -> menu.findItem(R.id.nav_bookmarks).isChecked = true
        R.id.nav_transcriptions -> menu.findItem(R.id.nav_transcriptions).isChecked = true
    }
}