package com.adelforce.psychoai.settings


data class PsychoSettings(

    // 0 = weak memory, 4 = deep memory
    val memoryPowerLevel: Int = 2,


    // 0 = strict matches, 4 = creative connections
    val insightDepthLevel: Int = 2,


    // 0 = short sessions, 4 = long continuity
    val conversationFlowLevel: Int = 2
)