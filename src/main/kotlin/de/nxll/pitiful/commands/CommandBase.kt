package de.nxll.pitiful.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

abstract class CommandBase(
    private val name: String,
    private val aliases: List<String> = emptyList(),
    private val permissionLevel: Int = 0,
) : CommandBase() {
    override fun getCommandName() = name
    override fun getCommandUsage(sender: ICommandSender?): String? = "/$name"
    override fun getCommandAliases() = aliases
    override fun getRequiredPermissionLevel() = permissionLevel
}