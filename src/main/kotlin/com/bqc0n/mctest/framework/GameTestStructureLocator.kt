package com.bqc0n.mctest.framework

import net.minecraft.command.ICommandSender
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer

class GameTestStructureLocator {

    private val currentPos: BlockPos.MutableBlockPos = BlockPos.MutableBlockPos()

    fun locateStructures(world: WorldServer, sender: ICommandSender): Collection<GameTestCase> {
        val senderPos = sender.position
        val groundPos = BlockPos.getAllInBox(senderPos, senderPos.down(senderPos.y))
            .first { world.isAirBlock(it) }
        val pos = groundPos.add(3, 0, 3)
        currentPos.setPos(pos)

        return GameTestRegistry.getAllTests().map { (name: String, definition: GameTestDefinition) ->
            val context = GameTestContext(world, currentPos.toImmutable(), sender)
            val structure = world.structureTemplateManager.get(world.minecraftServer, definition.templateStructure)
            if (structure == null) {
                throw IllegalStateException("Structure template for test '$name' not found.")
            }
            currentPos.move(EnumFacing.EAST, structure.size.x + 4)
            GameTestCase(context, definition)
        }
    }
}