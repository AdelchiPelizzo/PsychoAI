package com.adelforce.psychoai.memory

class ThemeExtractor {

    suspend fun extractThemes(
        message: String
    ): List<String> {

        val words =
            message
                .lowercase()
                .replace(Regex("[^a-z ]"), "")
                .split(" ")
                .filter { it.isNotBlank() }


        val detectedThemes =
            mutableListOf<String>()


        ThemeDictionary.themes.forEach { (theme, keywords) ->

            val found =
                keywords.any { keyword ->
                    words.contains(keyword)
                }

            if (found) {
                detectedThemes.add(theme)
            }
        }

        return detectedThemes
    }
}