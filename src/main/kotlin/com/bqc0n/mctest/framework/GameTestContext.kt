package com.bqc0n.mctest.framework

import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

data class GameTestContext(
    /**
     * The world in which the game test is being run.
     */
    val world: WorldServer,
    /**
     * The position of the structure block that defines the test area.
     */
    val structureBlockPos: BlockPos,
)