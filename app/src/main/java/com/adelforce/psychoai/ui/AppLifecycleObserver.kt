package com.adelforce.psychoai.ui

import android.content.ComponentCallbacks2
import android.content.res.Configuration


class AppLifecycleObserver : ComponentCallbacks2 {


    override fun onTrimMemory(level: Int) {

        // Conversation lifecycle is not managed here.

    }


    override fun onConfigurationChanged(
        newConfig: Configuration
    ) {
    }


    override fun onLowMemory() {
    }
}