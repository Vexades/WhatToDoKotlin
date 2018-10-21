package com.example.ionut.whattodokotlin.fragments

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.view.MotionEvent

class LockableViewPager(context: Context) : ViewPager(context) {
    private var swipeable = false

    constructor(context: Context, attrs: DrawerLayout) : this(context)

    fun setSwipeable(swipeable: Boolean) { this.swipeable = swipeable }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return this.swipeable && super.onInterceptTouchEvent(ev)
    }
}