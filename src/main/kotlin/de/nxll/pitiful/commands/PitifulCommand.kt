package de.nxll.pitiful.commands

import de.nxll.pitiful.Pitiful
import net.minecraft.command.ICommandSender

class PitifulCommand : CommandBase("pitiful", listOf("ptf"), 0) {

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        Pitiful.displayGuiScreen = Pitiful.config.gui()
    }

}