package com.example.ionut.whattodokotlin

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.ionut.whattodokotlin.fragments.FragmentAddItem
import com.example.ionut.whattodokotlin.fragments.LockableViewPager


class MainActivity : AppCompatActivity() {
    companion object {
        const val ADD_NOTE_FRAGMENT_TAG = "addNoteFragment"
    }

    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)
        val fab: FloatingActionButton? = findViewById(R.id.floatingActionButton)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val lockableViewPager = LockableViewPager(applicationContext)
        lockableViewPager.setSwipeable(false)
        setSupportActionBar(toolbar)
        val fragmentAdapter =   MyPagerAdapter(fragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.viewPager_main)
        viewPager.offscreenPageLimit = 2
        val tabs = findViewById<TabLayout>(R.id.tabs_main)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        fab?.setOnClickListener {
            var fragment = fragmentManager.findFragmentById(R.id.frameLayout)
            if(fragment == null){
                fragment = FragmentAddItem()
                fragmentManager.beginTransaction().add(R.id.frameLayout,fragment, ADD_NOTE_FRAGMENT_TAG).addToBackStack(null).commit()
            }else{
                fragmentManager.beginTransaction().add(R.id.frameLayout,fragment, ADD_NOTE_FRAGMENT_TAG).addToBackStack(null).commit()
            }
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack(supportFragmentManager.getBackStackEntryAt(0).id,FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }else{
            super.onBackPressed()
        }
    }
}
