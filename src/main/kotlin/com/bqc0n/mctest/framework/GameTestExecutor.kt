package com.bqc0n.mctest.framework

import com.bqc0n.mctest.internal.GameTestHelperImpl
import net.minecraft.command.ICommandSender
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

object GameTestExecutor {
    fun run(world: WorldServer, pos: BlockPos, sender: ICommandSender, testName: String): Boolean {
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
        val pos = groundPos.add(3, 0, 3)
        GameTestRegistry.getAllTests().forEach { (name: String, definition: GameTestDefinition) ->
            val context = GameTestContext(world, pos, sender)
            val testCase = GameTestCase(context, definition)
            testCase.prepare()
            testCase.run()
        }
    }
}