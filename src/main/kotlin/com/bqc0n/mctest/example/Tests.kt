package com.bqc0n.mctest.example

import com.bqc0n.mctest.Tags
import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.internal.GameTestHelper
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos

@GameTestHolder(Tags.MOD_ID)
class Tests {
    @GameTest
    fun testExample(helper: GameTestHelper) {
        val pos = BlockPos.ORIGIN.up()
        // Example test method
        println("Running example test")
        helper.setBlock(pos, Blocks.BEDROCK)
        helper.succeedWhenBlockPresent(Blocks.STONE.defaultState, pos)
    }

    @GameTest(template = "testexample")
    fun placeDirt(helper: GameTestHelper) {
        val pos = BlockPos.ORIGIN.up()
        helper.setBlock(pos, Blocks.DIRT)
        helper.succeedWhenBlockPresent(Blocks.DIRT.defaultState, pos)
    }
}