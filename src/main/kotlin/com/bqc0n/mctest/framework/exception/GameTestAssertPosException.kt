package com.bqc0n.mctest.framework.exception

import net.minecraft.util.math.BlockPos

class GameTestAssertPosException(
    message: String,
    relativePos: BlockPos,
    absolutePos: BlockPos,
) : GameTestAssertException(
    message = message,
) {
    override val message: String = "$message at $absolutePos (relative: $relativePos)"
}