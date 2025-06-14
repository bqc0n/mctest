package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTestCase
import com.bqc0n.mctest.framework.exception.GameTestAssertException
import com.bqc0n.mctest.framework.exception.GameTestAssertPosException
import net.minecraft.block.Block
import net.minecraft.block.BlockButton
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
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

    fun spawnItem(item: ItemStack, relPos: BlockPos): EntityItem {
        validateBlockPos(relPos)
        val absolutePos = absolute(relPos)
        val itemEntity = EntityItem(world, absolutePos.x.toDouble(), absolutePos.y.toDouble(), absolutePos.z.toDouble(), item)
        itemEntity.motionX = 0.0
        itemEntity.motionY = 0.0
        itemEntity.motionZ = 0.0
        world.spawnEntity(itemEntity)
        return itemEntity
    }

    fun pressButton(relPos: BlockPos) {
        this.assertBlock(relPos, { it.block is BlockButton }, "Expected a button at $relPos")
        val buttonBlock = getBlockState(relPos).block as BlockButton
        world.setBlockState(absolute(relPos), buttonBlock.defaultState.withProperty(BlockButton.POWERED, true))
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

    fun assertTrue(condition: Boolean, message: String) {
        if (!condition) {
            throw GameTestAssertException(message)
        }
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

    fun assertBlockPresent(block: Block, relPos: BlockPos) {
        this.assertBlock(relPos, { it.block == block }, "Expected $block, but was ${getBlockState(relPos).block}")
    }

    fun assertBlockNotPresent(state: IBlockState, relPos: BlockPos) {
        this.assertBlock(relPos, { it != state }, "Did not expected $state at $relPos.")
    }

    fun assertBlockNotPresent(block: Block, relPos: BlockPos) {
        this.assertBlock(relPos, { it.block != block }, "Did not expected $block at $relPos.")
    }

    fun assertBlockNotPresent(relPos: BlockPos) {
        this.assertBlock(relPos, { it.block.isAir(it, world, absolute(relPos)) }, "Expected air at $relPos, but was ${getBlockState(relPos).block}")
    }
}