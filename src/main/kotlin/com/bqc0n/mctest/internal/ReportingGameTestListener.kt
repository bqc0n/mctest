package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTestCase
import com.bqc0n.mctest.framework.IGameTestListener
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.math.BlockPos

@Suppress("DEPRECATION")
object ReportingGameTestListener : IGameTestListener {
    override fun testStructureLoaded(case: GameTestCase) {
        spawnBeacon(case)
        updateBeaconGlass(case, EnumDyeColor.GRAY)
    }

    override fun testPassed(case: GameTestCase) {
        updateBeaconGlass(case, EnumDyeColor.LIME)
    }

    override fun testFailed(case: GameTestCase, reason: Throwable) {
        updateBeaconGlass(case, EnumDyeColor.RED)
    }

    private fun spawnBeacon(case: GameTestCase) {
        val context = case.context
        val beaconPos = getBeaconPos(context.structureBlockPos)
        val world = context.world
        world.setBlockState(beaconPos, Blocks.BEACON.defaultState)
        BlockPos.getAllInBox(beaconPos.add(1, -1, 1), beaconPos.add(-1, -1, -1)).forEach { pos ->
            world.setBlockState(pos, Blocks.IRON_BLOCK.defaultState)
        }
    }

    private fun updateBeaconGlass(case: GameTestCase, color: EnumDyeColor) {
        val context = case.context
        val glassPos = getBeaconPos(context.structureBlockPos).add(0, 1, 0)
        context.world.setBlockState(glassPos, Blocks.STAINED_GLASS.getStateFromMeta(color.metadata))
    }

    private fun getBeaconPos(structureBlockPos: BlockPos) = structureBlockPos.add(-1, -2, -1)
}