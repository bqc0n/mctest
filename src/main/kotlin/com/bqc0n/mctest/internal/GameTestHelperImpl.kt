package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTestContext
import com.bqc0n.mctest.framework.IGameTestHelper
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

class GameTestHelperImpl(
    context: GameTestContext,
) : IGameTestHelper {
    override val world = context.world

    override fun <T : TileEntity> getTileEntity(pos: BlockPos): T? {
        val te =  world.getTileEntity(pos)
        if (te == null) return null
        return te as? T
    }

    override fun absolute(relativePos: BlockPos): BlockPos {
        TODO("Not yet implemented")
    }

    override fun relative(absolutePos: BlockPos): BlockPos {
        TODO("Not yet implemented")
    }

    val absolutePos = context.structureBlockPos
}