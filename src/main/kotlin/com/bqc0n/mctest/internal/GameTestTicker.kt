package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTestCase
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object GameTestTicker {
    private val _cases = mutableListOf<GameTestCase>()

    fun add(testCase: GameTestCase) {
        _cases.add(testCase)
    }

    @SubscribeEvent
    fun onTick(e: TickEvent.ServerTickEvent) {
        if (e.phase == TickEvent.Phase.START) {
            _cases.forEach { it.tick() }
            _cases.removeIf { it.isDone }
        }
    }
}