package com.atherapp.common.io

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.NonWritableChannelException
import java.nio.channels.SeekableByteChannel

/**
 * This is intended to downloading
 *
 * @see InputStream
 */
class HttpSeekableByteChannel(
        url: URL,
        val headers: Map<String, String>? = null,
        length: Long = -1,
        startByte: Long = 0,
        /**
         * Lambda for getting a new link, if necessary
         */
        private val refreshFunc: (() -> URL)? = null
) : SeekableByteChannel {
    private val webClient: WebClient = WebClient.create(Vertx.vertx())

    var url = url
        private set

    var length: Long = length
        private set

    var currentByte: Long = startByte
        private set

    init {
        if (this.length == -1L) {
            webClient
                    .head(url.toString()).apply {
                        headers().addAll(headers)
                    }.send {
                        if (it.succeeded())
                            this.length = it.result().getHeader("Content-Length").toLong()
                    }
        }
    }

    override fun isOpen(): Boolean = length != -1L

    override fun close() = webClient.close()

    override fun read(dst: ByteBuffer): Int {
        if (currentByte > length)
            return -1

        var count = 0
        webClient.get(url.toString()).apply {
            headers().addAll(headers)
            putHeader("Range", "bytes=$currentByte-${currentByte + dst.remaining()}")
        }.send {
            if (it.succeeded()) {
                dst.put(it.result().body().bytes)
                count = it.result().getHeader("Content-Length").toInt()
            } else if (it.result().statusCode() == HttpURLConnection.HTTP_FORBIDDEN && refreshFunc != null) {
                url = refreshFunc.invoke()
                read(dst)
            }
        }

        return count
    }

    override fun position(): Long = currentByte

    override fun position(newPosition: Long): SeekableByteChannel {
        if (newPosition < 0)
            throw IllegalArgumentException("Position cannot be negative")

        currentByte = newPosition

        return this
    }

    override fun write(src: ByteBuffer?): Int = 0

    override fun size(): Long = length

    override fun truncate(size: Long): SeekableByteChannel = throw NonWritableChannelException()
}