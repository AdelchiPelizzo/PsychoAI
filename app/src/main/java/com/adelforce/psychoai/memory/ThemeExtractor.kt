package com.adelforce.psychoai.memory

class ThemeExtractor {

    suspend fun extractThemes(
        message: String
    ): List<String> {

        val text =
            message.lowercase()

        val detectedThemes =
            mutableListOf<String>()

        ThemeDictionary.themes.forEach { (theme, keywords) ->

            val found =
                keywords.any { keyword ->

                    text.contains(keyword)

                }

            if (found) {
                detectedThemes.add(theme)
            }
        }

        return detectedThemes
    }
}