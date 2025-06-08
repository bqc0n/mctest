package com.bqc0n.mctest.framework

import com.bqc0n.mctest.McTestLogger
import com.bqc0n.mctest.framework.exception.GameTestAssertException
import com.bqc0n.mctest.internal.GameTestHelperImpl
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntityStructure
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.WorldServer
import net.minecraft.world.gen.structure.template.Template

private const val HORIZONTAL_INTERVAL = 2
private const val VERTICAL_INTERVAL = 2

class GameTestCase(
    val context: GameTestContext,
    val definition: GameTestDefinition,
) {
    var isDone = false
        private set

    private var error: Throwable? = null
    private val listeners = mutableListOf<IGameTestListener>()

    fun addListener(listener: IGameTestListener) {
        listeners.add(listener)
    }

    fun prepare() {
        val template = getStructureTemplate()
        if (template == null) return
        clearSpace(template.size)
        encaseStructure(template.size)
        val pos = context.structureBlockPos
        val world: WorldServer = context.world
        world.setBlockState(pos, Blocks.STRUCTURE_BLOCK.defaultState)
        val structureTile = world.getTileEntity(pos)
        if (structureTile == null) {
            McTestLogger.error("Structure block at $pos is null, cannot prepare test ${definition.testName}")
            return
        }
        if (structureTile !is TileEntityStructure) {
            McTestLogger.error("Tile entity at $pos is not a StructureBlockTileEntity, cannot prepare test ${definition.testName}")
            return
        }
        structureTile.mode = TileEntityStructure.Mode.LOAD
        structureTile.name = definition.templateStructure.toString()
        structureTile.load()
        listeners.forEach { it.testStructureLoaded(this) }
    }

    private fun getStructureTemplate(): Template? {
        val templateManager = context.world.structureTemplateManager
        val template = templateManager.get(context.world.minecraftServer, definition.templateStructure)
        if (template == null) {
            context.sender.sendMessage(
                TextComponentTranslation(
                    "mctest.structure_not_found",
                    this@GameTestCase.definition.templateStructure.toString()
                )
            )
        }
        return template
    }

    private fun clearSpace(templateSize: BlockPos) {
        val structureBlockPos = context.structureBlockPos
        val structureBlockY = structureBlockPos.y
        val pos1 = structureBlockPos.add(-HORIZONTAL_INTERVAL, -VERTICAL_INTERVAL, -HORIZONTAL_INTERVAL)
        val pos2 = structureBlockPos
            .add(templateSize).add(-1, -1, -1)
            .add(HORIZONTAL_INTERVAL, VERTICAL_INTERVAL, HORIZONTAL_INTERVAL)
        BlockPos.getAllInBox(pos1, pos2).forEach { pos ->
            val world = context.world
            if (pos.y < structureBlockY) {
                world.setBlockState(pos, Blocks.STONE.defaultState)
            } else {
                world.setBlockToAir(pos)
            }

        }
    }

    private fun encaseStructure(templateSize: BlockPos) {
        val structureBlockPos = context.structureBlockPos
        val pos1 = structureBlockPos.add(-1, 1, -1)
        val pos2 = structureBlockPos
            .add(templateSize).add(0, 1, 0)
        BlockPos.getAllInBox(pos1, pos2).forEach { pos ->
            val world = context.world
            val isEdge = (pos.x == pos1.x || pos.x == pos2.x)
                    || (pos.z == pos1.z || pos.z == pos2.z)
            val isTop = pos.y == pos2.y
            if (isEdge || isTop) {
                world.setBlockState(pos, Blocks.BARRIER.defaultState)
            }
        }
    }

    fun run() {
        val helper = GameTestHelperImpl(context, getStructureTemplate()!!.size)
        try {
            definition.function.accept(helper)
        } catch (e: Throwable) {
            McTestLogger.error("Error running test ${definition.testName}", e)
            throw e
        }
    }

    fun tick() {

    }

    fun succeed() {
        if (this.error != null) {
            this.finish()
            this.isDone = true
            this.listeners.forEach { it.testPassed(this) }
        }
    }

    fun fail(e: GameTestAssertException) {
        this.finish()
        this.error = e
        this.listeners.forEach { it.testFailed(this, e) }
    }

    private fun finish() {
        if (!isDone) {
            this.isDone = true
        }
    }
}