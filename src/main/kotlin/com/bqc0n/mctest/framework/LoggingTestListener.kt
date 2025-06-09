package com.bqc0n.mctest.framework

import com.bqc0n.mctest.McTestLogger

object LoggingTestListener : IGameTestListener {
    override fun testStructureLoaded(case: GameTestCase) {
    }

    override fun testPassed(case: GameTestCase) {
        McTestLogger.info("Test passed: ${case.definition.testName}")
    }

    override fun testFailed(case: GameTestCase, reason: Throwable) {
        McTestLogger.error("Test ${case.definition.testName} failed: ${reason.message}")
    }
}