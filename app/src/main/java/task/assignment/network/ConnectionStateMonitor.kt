package task.assignment.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class ConnectionStateMonitor(
    private val context: Context,
    private val onNetworkAvailableCallbacks: OnNetworkAvailableCallbacks
) : ConnectivityManager.NetworkCallback() {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()!!

    fun hasNetworkConnection(): Boolean {
        val capabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return (capabilities != null) && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }

    fun enable() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun disable() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onNetworkAvailableCallbacks.onConnected()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        onNetworkAvailableCallbacks.onDisconnected()
    }

    interface OnNetworkAvailableCallbacks {
        fun onConnected()
        fun onDisconnected()
    }
}