package de.nxll.pitiful.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.StringUtils

object Utils {

    var isDev: Boolean = System.getProperty("pitiful.dev") == "true"

    val isInPit: Boolean // Hi Kotlin! Your null safety is cool
        get() = Minecraft.getMinecraft()?.theWorld?.scoreboard?.getObjectiveInDisplaySlot(1 /* Sidebar */)
            ?.displayName?.let { return@let StringUtils.stripControlCodes(it) == "THE HYPIXEL PIT" } == true

}