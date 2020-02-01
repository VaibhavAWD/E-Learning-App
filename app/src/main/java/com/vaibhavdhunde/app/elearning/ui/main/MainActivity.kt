package com.vaibhavdhunde.app.elearning.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var mainViewModel: MainViewModel

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.dashboardFragment),
            drawer_layout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        NavigationUI.setupWithNavController(nav_view, navController)

        mainViewModel = obtainViewModel(MainViewModel::class.java, factory)
        loadUser()
        updateUI()
    }

    private fun loadUser() {
        mainViewModel.loadUser()
    }

    private fun updateUI() {
        mainViewModel.user.observe(this, Observer { user ->
            val header = nav_view.getHeaderView(0)
            val displayProfileName = header.findViewById<TextView>(R.id.display_profile_name)
            val displayProfileEmail = header.findViewById<TextView>(R.id.display_profile_email)
            displayProfileName.text = user.name
            displayProfileEmail.text = user.email
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
