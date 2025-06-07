package com.bqc0n.mctest.framework

import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

interface IGameTestHelper {
    val world: WorldServer

    fun <T: TileEntity> getTileEntity(pos: BlockPos): T?

    fun absolute(relativePos: BlockPos): BlockPos
    fun relative(absolutePos: BlockPos): BlockPos
}