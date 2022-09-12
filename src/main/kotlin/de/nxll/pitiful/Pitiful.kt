package de.nxll.pitiful

import de.nxll.pitiful.commands.PitifulCommand
import de.nxll.pitiful.config.PitifulVigilant
import de.nxll.pitiful.features.SewerEsp
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod(modid = "pitiful")
class Pitiful {

    companion object {
        lateinit var config: PitifulVigilant

        var displayGuiScreen: GuiScreen? = null
    }

    private fun registerFeatures() {
        arrayOf(
            SewerEsp()
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    private fun registerCommands() {
        ClientCommandHandler.instance.registerCommand(PitifulCommand())
    }

    @EventHandler
    fun onLoadComplete(event: FMLLoadCompleteEvent) {
        config = PitifulVigilant()

        MinecraftForge.EVENT_BUS.register(this)

        registerFeatures()
        registerCommands()

    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {

        if (displayGuiScreen != null) {
            Minecraft.getMinecraft().displayGuiScreen(displayGuiScreen)
            displayGuiScreen = null
        }

    }

}