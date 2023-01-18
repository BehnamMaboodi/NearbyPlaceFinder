package me.behna.nearbyplace.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.behna.nearbyplace.R
import me.behna.nearbyplace.base.BaseActivity
import me.behna.nearbyplace.databinding.ActivityMainBinding
import me.behna.nearbyplace.viewmodel.SearchViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .also { it.lifecycleOwner = this }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}