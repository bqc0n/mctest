package com.bqc0n.mctest.framework.front

import com.bqc0n.mctest.McTest.testsTemp
import com.bqc0n.mctest.framework.GameTestContext
import com.bqc0n.mctest.framework.GameTestDefinition
import com.bqc0n.mctest.framework.GameTestExecutor
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

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
            throw CommandException("Usage: /mctest COMMAND [args...]. Use /mctest help for usage.")
        }

        when (args[0]) {
            "runall" -> runAll(server, sender, args)
            else -> throw CommandException("Unknown command: ${args[0]}. Use /mctest help for usage.")
        }
    }

    private fun runAll(server: MinecraftServer, sender: ICommandSender, args: Array<out String?>) {
        val worldServer = server.worlds[0] // Assuming the first world is the target
        val pos = sender.position
        val gameTestContext = GameTestContext(worldServer, pos)

        for (test in testsTemp) {
            val executor = GameTestExecutor(gameTestContext, test)
            executor.prepare()
        }
    }
}