package com.fkexample.cointicker.network.utils

import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

/**
 * Utility object to check if the network has internet connectivity by sending a ping to Google's primary DNS
 * If successful, then there is internet connectivity, otherwise not
 */
object DoesNetworkHaveInternet {

    /**
     * Checks if the network has internet connectivity by sending the ping.
     * This method has to be executed on a background thread
     *
     * @param socketFactory The socket factory to create a socket.
     * @return `true` if the network has internet connectivity, `false` otherwise.
     */
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}
