package com.bqc0n.mctest.framework

annotation class GameTest(
    /**
     * The Structure Template name for this test.
     * If not specified, the class name and method name will be used.
     * The location will be `${namespace}:${className}.${templateName}`.
     * e.g.: `mctest:MyTest.testSomething`
     */
    val template: String = "",
    val timeoutTicks: Int = 100,
    val setupTicks: Int = 0,
)
