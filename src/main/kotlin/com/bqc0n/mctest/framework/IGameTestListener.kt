package com.bqc0n.mctest.framework

interface IGameTestListener {
    fun testStructureLoaded(case: GameTestCase)
    fun testPassed(case: GameTestCase)
    fun testFailed(case: GameTestCase, reason: Throwable)
}