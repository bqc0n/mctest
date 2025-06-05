package com.bqc0n.mctest.framework

import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

data class GameTestContext(
    val world: WorldServer,
    val structureBlockPos: BlockPos,
)