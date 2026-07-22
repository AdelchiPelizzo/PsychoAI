package com.adelforce.psychoai.memory

import java.nio.ByteBuffer
import java.nio.ByteOrder

object EmbeddingConverter {

    fun floatListToByteArray(
        embedding: List<Float>
    ): ByteArray {

        val buffer =
            ByteBuffer
                .allocate(embedding.size * 4)
                .order(ByteOrder.LITTLE_ENDIAN)

        for (value in embedding) {
            buffer.putFloat(value)
        }

        return buffer.array()
    }


    fun byteArrayToFloatList(
        bytes: ByteArray
    ): List<Float> {

        require(bytes.size % 4 == 0) {
            "Invalid embedding byte size: ${bytes.size}"
        }

        val buffer =
            ByteBuffer
                .wrap(bytes)
                .order(ByteOrder.LITTLE_ENDIAN)

        val floats =
            ArrayList<Float>(
                bytes.size / 4
            )

        while (buffer.hasRemaining()) {
            floats.add(buffer.float)
        }

        return floats
    }
}