package com.uttamsaha.emergencyambulance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import android.content.Intent

class UserActivity : AppCompatActivity() {

    lateinit var userDrawerLayout: DrawerLayout
    lateinit var userCoordinatorLayout: CoordinatorLayout
    lateinit var userToolbar: Toolbar
    lateinit var userFrameLayout: FrameLayout
    lateinit var userNavigationView: NavigationView
    private lateinit var builder : AlertDialog.Builder
    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userDrawerLayout = findViewById(R.id.userDrawerLayout)
        userCoordinatorLayout = findViewById(R.id.userCoordinatorLayout)
        userToolbar = findViewById(R.id.userToolbar)
        userFrameLayout = findViewById(R.id.userFrameLayout)
        userNavigationView = findViewById(R.id.userNavigationView)
        builder = AlertDialog.Builder(this)

        setUpToolbar()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@UserActivity,userDrawerLayout,
            R.string.open_drawer,R.string.close_drawer)

        userDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        userNavigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.userHome-> {
                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,UserHomeFragment()).commit()
                    supportActionBar?.title = "Home"
                    userDrawerLayout.closeDrawers()
                }

                R.id.userMyBookings-> {
                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,UserMyBookingsFragment()).commit()
                    supportActionBar?.title = "My Bookings"
                    userDrawerLayout.closeDrawers()
                }

                R.id.userProfile-> {
                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,UserProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    userDrawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(userToolbar)
        supportActionBar?.title = "User"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==android.R.id.home){
            userDrawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun openHome(){
        val fragment = UserHomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.userFrameLayout,fragment)
        transaction.commit()
        supportActionBar?.title = "Home"
        userNavigationView.setCheckedItem(R.id.userHome)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.userFrameLayout)
        when (frag) {
            !is UserHomeFragment -> openHome()

            else -> super.onBackPressed()

        }
    }
}