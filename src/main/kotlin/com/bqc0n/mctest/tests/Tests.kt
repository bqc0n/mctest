package com.bqc0n.mctest.tests

import com.bqc0n.mctest.Tags
import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.IGameTestHelper

@GameTestHolder(Tags.MOD_ID)
class Tests {
    @GameTest
    fun testExample(helper: IGameTestHelper) {
        // Example test method
        println("Running example test")
    }
}