package com.bqc0n.mctest.framework

import com.bqc0n.mctest.framework.exception.GameTestAssertException

interface IGameTestListener {
    fun testStructureLoaded(case: GameTestCase)
    fun testPassed(case: GameTestCase)
    fun testFailed(case: GameTestCase, reason: GameTestAssertException)
}