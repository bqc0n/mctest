package com.bqc0n.mctest

import com.bqc0n.mctest.framework.GameTestDefinition
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.front.GameTestCommand
import com.bqc0n.mctest.internal.GameTestCollector
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent

@Mod(
    modid = Tags.MOD_ID,
    name = Tags.MOD_NAME,
    version = Tags.VERSION,
    modLanguageAdapter = "io.github.chaosunity.forgelin.KotlinAdapter",
)
object McTest {
    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        val asmDataTable = e.asmData
        GameTestCollector.collectGameTests(asmDataTable)
    }

    @Mod.EventHandler
    fun serverStarting(e: FMLServerStartingEvent) {
        e.registerServerCommand(GameTestCommand)
    }
}