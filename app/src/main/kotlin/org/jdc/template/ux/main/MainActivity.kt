package org.jdc.template.ux.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import org.jdc.template.R
import org.jdc.template.databinding.MainActivityBinding
import org.jdc.template.model.webservice.ServiceModule_GetAuthenticatedClientFactory
import org.jdc.template.model.webservice.ServiceModule_GetIndividualServiceFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val navController by lazy { findNavController(R.id.mainNavHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        //Individual Restcall
        //
        // ServiceModule_GetIndividualServiceFactory.get();

        setSupportActionBar(binding.appbar.mainToolbar)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}