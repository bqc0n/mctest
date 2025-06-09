package com.bqc0n.mctest.framework

import com.bqc0n.mctest.internal.GameTestTicker
import com.bqc0n.mctest.internal.ReportingGameTestListener
import net.minecraft.command.ICommandSender
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

/**
 * This object is responsible for executing game tests.
 * Frontend commands will call the methods in this object to run tests.
 */
object GameTestExecutor {
    fun runByName(world: WorldServer, pos: BlockPos, sender: ICommandSender, testName: String): Boolean {
        val def = GameTestRegistry.getTest(testName)
        if (def == null) return false
        val context = GameTestContext(world, pos, sender)
        val testCase = GameTestCase(context, def)
        return true
    }

    fun runAll(world: WorldServer, sender: ICommandSender) {
        val senderPos = sender.position
        val groundPos = BlockPos.getAllInBox(senderPos, senderPos.down(senderPos.y))
            .first { world.isAirBlock(it) }
        GameTestStructureLocator().locateStructures(world, sender).forEach { testCase ->
            this.runTest(testCase)
        }
    }

    private fun runTest(testCase: GameTestCase) {
        val chatReportingListener = ChatReportingTestListener(testCase.context.sender)
        GameTestTicker.add(testCase)
        testCase.addListener(ReportingGameTestListener)
        testCase.addListener(LoggingTestListener)
        testCase.addListener(chatReportingListener)
        testCase.prepare()
        testCase.run(0)
    }
}