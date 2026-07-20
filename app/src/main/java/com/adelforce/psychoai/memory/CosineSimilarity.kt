package com.adelforce.psychoai.memory

import kotlin.math.sqrt

object CosineSimilarity {


    fun calculate(
        vectorA: List<Float>,
        vectorB: List<Float>
    ): Float {


        if (vectorA.size != vectorB.size) {
            throw IllegalArgumentException(
                "Vectors must have the same dimension"
            )
        }


        var dotProduct = 0.0

        var magnitudeA = 0.0

        var magnitudeB = 0.0


        for (i in vectorA.indices) {

            val a =
                vectorA[i].toDouble()

            val b =
                vectorB[i].toDouble()


            dotProduct += a * b

            magnitudeA += a * a

            magnitudeB += b * b
        }


        if (magnitudeA == 0.0 || magnitudeB == 0.0) {
            return 0f
        }


        return (
                dotProduct /
                        (
                                sqrt(magnitudeA) *
                                        sqrt(magnitudeB)
                                )
                ).toFloat()
    }
}