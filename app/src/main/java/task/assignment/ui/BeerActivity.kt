package task.assignment.ui


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import task.assignment.R
import task.assignment.databinding.ActivityBeerBinding
import task.assignment.interfaces.BeerActivityAction
import task.assignment.network.ConnectionStateMonitor


class BeerActivity : AppCompatActivity(), BeerActivityAction,
    ConnectionStateMonitor.OnNetworkAvailableCallbacks {

    private lateinit var binding: ActivityBeerBinding

    private var connectionStateMonitor: ConnectionStateMonitor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beer)

        setSupportActionBar(binding.tbActivityBeer)
        binding.tbActivityBeer.subtitle = "BeerList"

        supportFragmentManager.beginTransaction().add(R.id.fl_container, BeerListFragment())
            .commit()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    backToBeerListPage()
                } else {
                    finish()
                }
            }
        })
    }


    override fun moveToDetailsPage() {
        displayBackButton()
        binding.tbActivityBeer.subtitle = "BeerDetails"
        val trans = supportFragmentManager.beginTransaction()
        trans.addToBackStack("BeerDetails")
        trans.replace(R.id.fl_container, BeerDetailFragment())
            .commit()
    }

    private fun backToBeerListPage() {
        binding.tbActivityBeer.subtitle = "BeerList"
        hideBackButton()
        supportFragmentManager.popBackStack()
    }

    private fun displayBackButton() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun hideBackButton() {
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            backToBeerListPage()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (connectionStateMonitor == null) {
            connectionStateMonitor = ConnectionStateMonitor(this, this)
        }
        connectionStateMonitor?.enable()
        if (connectionStateMonitor?.hasNetworkConnection() == false) onDisconnected()
        else onConnected()
    }

    override fun onPause() {
        super.onPause()
        //Unregister
        connectionStateMonitor?.disable()
        connectionStateMonitor = null
    }

    override fun onConnected() {
        Log.d("network", "connected")
    }

    override fun onDisconnected() {
        Log.d("network", "not connected")
        val snackBar = Snackbar
            .make(binding.containerLayout, "Please check your network connection", Snackbar.LENGTH_SHORT)
        snackBar.show()
    }

}