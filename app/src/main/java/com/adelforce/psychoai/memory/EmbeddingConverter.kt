package com.adelforce.psychoai.memory

import java.nio.ByteBuffer
import java.nio.ByteOrder

object EmbeddingConverter {

    fun floatArrayToByteArray(
        embedding: FloatArray
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


    fun byteArrayToFloatArray(
        bytes: ByteArray
    ): FloatArray {

        require(bytes.size % 4 == 0) {
            "Invalid embedding byte size: ${bytes.size}"
        }

        val buffer =
            ByteBuffer
                .wrap(bytes)
                .order(ByteOrder.LITTLE_ENDIAN)

        val floats =
            FloatArray(bytes.size / 4)

        var index = 0

        while (buffer.hasRemaining()) {
            floats[index++] = buffer.float
        }

        return floats
    }


    // ----------------------------------------------------------------
    // Temporary compatibility methods
    // Remove these after the migration is complete.
    // ----------------------------------------------------------------

    fun floatListToByteArray(
        embedding: List<Float>
    ): ByteArray =
        floatArrayToByteArray(
            embedding.toFloatArray()
        )


    fun byteArrayToFloatList(
        bytes: ByteArray
    ): List<Float> =
        byteArrayToFloatArray(bytes).toList()
}