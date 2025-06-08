package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTestContext
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

class GameTestHelper(
    context: GameTestContext,
    val structureSize: BlockPos,
) {
    val world = context.world
    val absolutePos = context.structureBlockPos

    @Suppress("UNCHECKED_CAST")
    fun <T : TileEntity> getTileEntity(pos: BlockPos): T? {
        val te =  world.getTileEntity(pos)
        if (te == null) return null
        return te as? T
    }

    fun absolute(relativePos: BlockPos): BlockPos {
        return absolutePos.add(relativePos)
    }

    fun relative(absolutePos: BlockPos): BlockPos {
        return absolutePos.subtract(this.absolutePos)
    }

    fun setBlock(relPos: BlockPos, block: Block) = setBlock(relPos, block.defaultState)

    fun setBlock(relPos: BlockPos, state: IBlockState) {
        validateBlockPos(relPos)
        world.setBlockState(absolute(relPos), state)
    }

    private fun validateBlockPos(relPos: BlockPos) {
        if (relPos.x >= structureSize.x || relPos.y >= structureSize.y || relPos.z >= structureSize.z) {
            throw IllegalArgumentException("Relative position $relPos is out of bounds for the structure size $structureSize.")
        }
    }

    // Asserting

}