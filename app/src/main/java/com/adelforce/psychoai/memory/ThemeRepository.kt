package com.adelforce.psychoai.memory

import com.adelforce.psychoai.data.local.MessageThemeDao
import com.adelforce.psychoai.data.local.MessageThemeEntity
import com.adelforce.psychoai.data.local.ThemeDao
import com.adelforce.psychoai.data.local.ThemeEntity

class ThemeRepository(
    private val themeDao: ThemeDao,
    private val messageThemeDao: MessageThemeDao
) {

    suspend fun saveThemesForMessage(
        messageId: Long,
        themes: List<String>
    ) {

        val now =
            System.currentTimeMillis()

        themes.forEach { themeName ->

            val existingTheme =
                themeDao.findByName(themeName)

            val themeId =
                if (existingTheme != null) {

                    themeDao.touchTheme(
                        existingTheme.id,
                        now
                    )

                    existingTheme.id

                } else {

                    themeDao.insert(
                        ThemeEntity(
                            name = themeName,
                            createdAt = now,
                            lastUsedAt = now
                        )
                    )
                }

            messageThemeDao.insert(
                MessageThemeEntity(
                    messageId = messageId,
                    themeId = themeId,
                    confidence = 1.0f
                )
            )
        }
    }

    suspend fun getOrCreateTheme(
        name: String
    ): Long {

        val existing =
            themeDao.findByName(name)

        if (existing != null) {
            return existing.id
        }

        val now =
            System.currentTimeMillis()

        return themeDao.insert(
            ThemeEntity(
                name = name,
                createdAt = now,
                lastUsedAt = now
            )
        )
    }
}