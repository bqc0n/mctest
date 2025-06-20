package com.bqc0n.mctest.framework.front

import com.bqc0n.mctest.framework.GameTestExecutor
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString

object GameTestCommand : CommandBase() {
    override fun getName(): String {
        return "mctest"
    }

    override fun getUsage(sender: ICommandSender): String {
        return """
            Usage: /mctest COMMAND [args...]
            Commands:
              runall - Runs all game tests
            """
            .trimIndent()
    }

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String?>) {
        if (args.isEmpty()) {
            throw CommandException(getUsage(sender))
        }

        when (args[0]) {
            "runall" -> runAll(server, sender, args)
            "help" -> sender.sendMessage(TextComponentString(getUsage(sender)))
            else -> throw CommandException(getUsage(sender))
        }
    }

    private fun runAll(server: MinecraftServer, sender: ICommandSender, args: Array<out String?>) {
        val worldServer = server.worlds[0] // Assuming the first world is the target
        GameTestExecutor.runAll(worldServer, sender)
    }
}