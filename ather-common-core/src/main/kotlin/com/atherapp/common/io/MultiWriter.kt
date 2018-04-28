package com.atherapp.common.io

import java.io.Writer

class MultiWriter(private vararg val writers: Writer) : Writer() {
    override fun write(cbuf: CharArray?, off: Int, len: Int) {
        for (writer in writers)
            writer.write(cbuf, off, len)
    }

    override fun flush() {
        for (writer in writers)
            writer.flush()
    }

    override fun close() {
        for (writer in writers)
            writer.close()
    }
}