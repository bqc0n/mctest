package com.bqc0n.mctest.example

import com.bqc0n.mctest.Tags
import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestAssertion
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.exception.GameTestAssertException
import com.bqc0n.mctest.internal.GameTestHelper
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.math.BlockPos

@GameTestHolder(Tags.MOD_ID)
class Tests {
    @GameTest
    fun testExample(helper: GameTestHelper) {
        val pos = BlockPos.ORIGIN.up()
        // Example test method
        println("Running example test")
        helper.setBlock(pos, Blocks.BEDROCK)
        helper.succeedWhenBlockPresent(Blocks.STONE.defaultState, pos)
    }

    @GameTest(template = "testexample")
    fun placeDirt(helper: GameTestHelper) {
        val pos = BlockPos.ORIGIN.up()
        helper.setBlock(pos, Blocks.DIRT)
        helper.succeedWhenBlockPresent(Blocks.DIRT.defaultState, pos)
    }

    @GameTest(template = "testexample")
    fun testChest(helper: GameTestHelper) {
        val pos = BlockPos.ORIGIN.up()
        helper.setBlock(pos, Blocks.CHEST)
        val chest = helper.getTileEntity<TileEntityChest>(pos)
        if (chest == null) {
            throw GameTestAssertException("Expected a chest at $pos, but found none. Retry..")
        }
        chest.setInventorySlotContents(0, ItemStack(Items.DIAMOND, 64))
        helper.succeedIf {
            val stack = helper.getTileEntity<TileEntityChest>(pos)?.getStackInSlot(0)
            if (!(stack != null && stack.count == 64 && stack.item == Items.DIAMOND)) {
                throw GameTestAssertException("Expected 64 diamonds in chest at $pos, but found ${stack?.count ?: 0} ${stack?.item?.registryName}")
            }
        }
    }
}