package com.bqc0n.mctest.framework

import com.bqc0n.mctest.McTest
import com.bqc0n.mctest.McTestLogger
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntityStructure
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

class GameTestExecutor(
    private val context: GameTestContext,
    private val test: GameTestDefinition,
) {
    fun prepare() {
        val pos = context.structureBlockPos
        val world: WorldServer = context.world
        world.setBlockState(pos, Blocks.STRUCTURE_BLOCK.defaultState)
        val structureTile = world.getTileEntity(pos)
        if (structureTile == null) {
            McTestLogger.error("Structure block at $pos is null, cannot prepare test ${test.testName}")
            return
        }
        if (structureTile !is TileEntityStructure) {
            McTestLogger.error("Tile entity at $pos is not a StructureBlockTileEntity, cannot prepare test ${test.testName}")
            return
        }
        structureTile.mode = TileEntityStructure.Mode.LOAD
        structureTile.name = test.templateStructure.toString()
    }
}