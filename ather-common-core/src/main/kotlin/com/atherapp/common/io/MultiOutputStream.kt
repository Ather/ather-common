package com.atherapp.common.io

import java.io.OutputStream

class MultiOutputStream(private vararg val streams: OutputStream) : OutputStream() {
    override fun write(b: Int) {
        for (stream in streams)
            stream.write(b)
    }

    override fun write(b: ByteArray?) {
        for (stream in streams)
            stream.write(b)
    }

    override fun write(b: ByteArray?, off: Int, len: Int) {
        for (stream in streams)
            stream.write(b, off, len)
    }

    override fun flush() {
        for (stream in streams)
            stream.flush()
    }

    override fun close() {
        for (stream in streams)
            stream.close()
    }
}