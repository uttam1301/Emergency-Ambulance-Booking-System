package com.uttamsaha.emergencyambulance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class OperatorActivity : AppCompatActivity() {

    lateinit var operatorDrawerLayout: DrawerLayout
    lateinit var operatorCoordinatorLayout: CoordinatorLayout
    lateinit var operatorToolbar: Toolbar
    lateinit var operatorFrameLayout: FrameLayout
    lateinit var operatorNavigationView: NavigationView
    private lateinit var builder : AlertDialog.Builder
    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operator)

        operatorDrawerLayout = findViewById(R.id.operatorDrawerLayout)
        operatorCoordinatorLayout = findViewById(R.id.operatorCoordinatorLayout)
        operatorToolbar = findViewById(R.id.operatorToolbar)
        operatorFrameLayout = findViewById(R.id.operatorFrameLayout)
        operatorNavigationView = findViewById(R.id.operatorNavigationView)
        builder = AlertDialog.Builder(this)

        setUpToolbar()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@OperatorActivity,operatorDrawerLayout,
            R.string.open_drawer,R.string.close_drawer)

        operatorDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        operatorNavigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.operatorHome-> {
                    supportFragmentManager.beginTransaction().replace(R.id.operatorFrameLayout,OperatorHomeFragment()).commit()
                    supportActionBar?.title = "Home"
                    operatorDrawerLayout.closeDrawers()
                }

                R.id.operatorEmergencyBookings-> {
                    supportFragmentManager.beginTransaction().replace(R.id.operatorFrameLayout,OperatorEmergencyBookingFragment()).commit()
                    supportActionBar?.title = "Emergency Bookings"
                    operatorDrawerLayout.closeDrawers()
                }

                R.id.operatorBookings-> {
                    supportFragmentManager.beginTransaction().replace(R.id.operatorFrameLayout,OperatorBookingsFragment()).commit()
                    supportActionBar?.title = "Bookings"
                    operatorDrawerLayout.closeDrawers()
                }

                R.id.operatorAmbulanceStatus-> {
                    supportFragmentManager.beginTransaction().replace(R.id.operatorFrameLayout,OperatorAmbulanceStatusFragment()).commit()
                    supportActionBar?.title = "My Bookings"
                    operatorDrawerLayout.closeDrawers()
                }

                R.id.operatorProfile-> {
                    supportFragmentManager.beginTransaction().replace(R.id.operatorFrameLayout,OperatorProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    operatorDrawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(operatorToolbar)
        supportActionBar?.title = "Operator"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==android.R.id.home){
            operatorDrawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun openHome(){
        val fragment = OperatorHomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.operatorFrameLayout,fragment)
        transaction.commit()
        supportActionBar?.title = "Home"
        operatorNavigationView.setCheckedItem(R.id.operatorHome)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.operatorFrameLayout)
        when (frag) {
            !is OperatorHomeFragment -> openHome()

            else -> super.onBackPressed()

        }
    }
}