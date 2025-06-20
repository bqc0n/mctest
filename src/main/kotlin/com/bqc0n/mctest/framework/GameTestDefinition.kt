package com.bqc0n.mctest.framework

import com.bqc0n.mctest.internal.GameTestHelper
import net.minecraft.util.ResourceLocation
import java.util.function.Consumer

data class GameTestDefinition(
    val testName: String,
    val templateStructure: ResourceLocation,
    val setupTicks: Int,
    val timeoutTicks: Int,
    val function: Consumer<GameTestHelper>,
)
