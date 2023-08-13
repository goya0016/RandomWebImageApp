package com.example.randomwebimageapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetConnected( val context: Context) {
    fun checkNetworkConnectivity(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return postMarshmallowInternetCheck(connectivityManager)
    }

    private fun postMarshmallowInternetCheck(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection != null && (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}