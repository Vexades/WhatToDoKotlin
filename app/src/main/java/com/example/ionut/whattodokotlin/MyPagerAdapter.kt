package com.example.ionut.whattodokotlin

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.example.ionut.whattodokotlin.fragments.RecyclerViewCurrent
import com.example.ionut.whattodokotlin.fragments.RecyclerViewDone
import com.example.ionut.whattodokotlin.fragments.RecyclerViewExpired

class MyPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getItem(p0: Int): Fragment {
        return when(p0){
            0 -> {RecyclerViewCurrent()}
            1 -> {
                RecyclerViewDone()
            }
            else -> {RecyclerViewExpired()}
        }
    }

    override fun getCount(): Int {
       return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "In Progress"
            1 -> "Done"
            else -> "Expired"
        }
    }

}