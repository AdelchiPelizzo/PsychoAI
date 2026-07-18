package com.adelforce.psychoai.data.model

data class ThemeMemory(

    val themeName: String,

    val occurrenceCount: Int,

    val lastDetectedAt: Long,

    val averageConfidence: Float

) {

    fun recurrenceLabel(): String {

        return when {

            occurrenceCount >= 20 ->
                "high recurrence"

            occurrenceCount >= 10 ->
                "medium recurrence"

            occurrenceCount >= 3 ->
                "low recurrence"

            else ->
                "rare occurrence"
        }
    }


    fun daysSinceLastMention(): Long {

        val now =
            System.currentTimeMillis()

        return (
                now - lastDetectedAt
                ) / (1000 * 60 * 60 * 24)
    }


    fun toPromptText(): String {

        return """
        Theme: $themeName
        - detected $occurrenceCount times
        - ${recurrenceLabel()}
        - last mentioned ${daysSinceLastMention()} days ago
        - average confidence ${String.format("%.2f", averageConfidence)}
        """.trimIndent()
    }
}