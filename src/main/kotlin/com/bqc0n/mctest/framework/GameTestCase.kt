package com.bqc0n.mctest.framework

import com.bqc0n.mctest.McTestLogger
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntityStructure
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.WorldServer
import net.minecraft.world.gen.structure.template.Template

private const val HORIZONTAL_INTERVAL = 2
private const val VERTICAL_INTERVAL = 2

class GameTestCase(
    private val context: GameTestContext,
    private val test: GameTestDefinition,
) {
    fun prepare() {
        val template = getStructureTemplate()
        if (template == null) return
        clearSpace()
        val pos = context.structureBlockPos
        val world: WorldServer = context.world
        world.setBlockState(pos, Blocks.STRUCTURE_BLOCK.defaultState)
        val structureTile = world.getTileEntity(pos)
        if (structureTile == null) {
            McTestLogger.error("Structure block at $pos is null, cannot prepare test ${test.testName}")
            return
        }
        if (structureTile !is TileEntityStructure) {
            McTestLogger.error("Tile entity at $pos is not a StructureBlockTileEntity, cannot prepare test ${test.testName}")
            return
        }
        structureTile.mode = TileEntityStructure.Mode.LOAD
        println(test.templateStructure.toString())
        structureTile.name = test.templateStructure.toString()
    }

    fun getStructureTemplate(): Template? {
        val templateManager = context.world.structureTemplateManager
        val template = templateManager.get(context.world.minecraftServer, test.templateStructure)
        if (template == null) {
            context.sender.sendMessage(TextComponentTranslation("mctest.structure_not_found", test.templateStructure.toString()))
        }
        return template
    }

    fun clearSpace() {
        val structureBlockPos = context.structureBlockPos
        val pos1 = structureBlockPos.add(-HORIZONTAL_INTERVAL, -VERTICAL_INTERVAL, -HORIZONTAL_INTERVAL)
        val pos2 = structureBlockPos.add(HORIZONTAL_INTERVAL, VERTICAL_INTERVAL, HORIZONTAL_INTERVAL)
        BlockPos.getAllInBox(pos1, pos2).forEach { pos ->
            val world = context.world
            if (world.isAirBlock(pos)) return@forEach
            world.setBlockToAir(pos)
        }
    }
}