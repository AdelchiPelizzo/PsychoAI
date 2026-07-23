package com.adelforce.psychoai.ui

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.adelforce.psychoai.settings.SettingsScreen


@Composable
fun MainPager() {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            2
        }
    )

    HorizontalPager(
        state = pagerState
    ) { page: Int ->

        when (page) {

            0 -> {
                ChatScreen()
            }

            1 -> {
                SettingsScreen(
                    onBack = {
                        // later we add animated return
                    }
                )
            }
        }
    }
}