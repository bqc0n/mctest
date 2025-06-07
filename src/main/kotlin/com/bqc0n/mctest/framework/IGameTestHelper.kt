package com.bqc0n.mctest.framework

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

interface IGameTestHelper {
    val world: WorldServer

    /**
     * @return null if TileEntity is not found or not of type T.
     */
    fun <T: TileEntity> getTileEntity(pos: BlockPos): T?

    fun absolute(relativePos: BlockPos): BlockPos
    fun relative(absolutePos: BlockPos): BlockPos

    fun setBlock(relPos: BlockPos, block: Block) = setBlock(relPos, block.defaultState)
    fun setBlock(relPos: BlockPos, state: IBlockState)
}