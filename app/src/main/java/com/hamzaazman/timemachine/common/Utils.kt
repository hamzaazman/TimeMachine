package com.hamzaazman.timemachine.common

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

object Utils {


    fun openInCustomTab(context: Context, url: String) {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .build()
        intent.launchUrl(context, url.toUri())
    }


}