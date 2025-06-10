package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTestCase
import com.bqc0n.mctest.framework.exception.GameTestAssertException
import com.bqc0n.mctest.framework.exception.GameTestAssertPosException
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import java.util.function.Predicate

class GameTestHelper(
    val testCase: GameTestCase,
    val structureSize: BlockPos,
) {
    val world = testCase.context.world
    val absolutePos = testCase.context.structureBlockPos

    private var finalCheckAdded = false

    @Suppress("UNCHECKED_CAST")
    fun <T : TileEntity> getTileEntity(relPos: BlockPos): T? {
        val te =  world.getTileEntity(this.absolute(relPos))
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

    fun getBlockState(relPos: BlockPos): IBlockState {
        validateBlockPos(relPos)
        return world.getBlockState(absolute(relPos))
    }

    private fun validateBlockPos(relPos: BlockPos) {
        if (relPos.x >= structureSize.x || relPos.y >= structureSize.y || relPos.z >= structureSize.z) {
            throw IllegalArgumentException("Relative position $relPos is out of bounds for the structure size $structureSize.")
        }
    }

    // Asserting

    private fun ensureSingleFinalCheck() {
        if (this.finalCheckAdded) {
            throw IllegalStateException("This test already has final clause")
        } else {
            this.finalCheckAdded = true
        }
    }

    fun fail(message: String) {
        throw GameTestAssertException(message)
    }

    fun succeedIf(criteria: Runnable) {
        this.ensureSingleFinalCheck()
        this.testCase.createSequence().thenWaitUntil(criteria).thenSucceed()
    }

    fun succeedWhenBlockPresent(state: IBlockState, relPos: BlockPos) {
        this.succeedIf { this.assertBlockPresent(state, relPos) }
    }

    fun assertBlock(relPos: BlockPos, assertion: Predicate<IBlockState>, message: String) {
        validateBlockPos(relPos)
        val blockState = getBlockState(relPos)
        if (!assertion.test(blockState)) {
            throw GameTestAssertPosException(message, relPos, absolute(relPos))
        }
    }

    fun assertBlockPresent(state: IBlockState, relPos: BlockPos) {
        this.assertBlock(relPos,  { it == state }, "Expected $state, but was ${getBlockState(relPos)}")
    }
}