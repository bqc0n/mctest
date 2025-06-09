package com.bqc0n.mctest.framework

import net.minecraft.command.ICommandSender
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting

class ChatReportingTestListener(
    val sender: ICommandSender,
) : IGameTestListener {
    override fun testStructureLoaded(case: GameTestCase) {}

    override fun testPassed(case: GameTestCase) {
        val msg = "${TextFormatting.GREEN}${case.definition.testName} passed."
        sender.sendMessage(TextComponentString(msg))
    }

    override fun testFailed(case: GameTestCase, reason: Throwable) {
        val msg =  "${TextFormatting.RED}${case.definition.testName} failed."
        sender.sendMessage(TextComponentString(msg))
    }
}