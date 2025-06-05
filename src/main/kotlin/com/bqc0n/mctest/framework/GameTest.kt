package com.bqc0n.mctest.framework

/**
 * Annotation for marking a function as a game test.
 * The method must have exactly one parameter of type [IGameTestHelper].
 */
@Target(AnnotationTarget.FUNCTION)
annotation class GameTest(
    /**
     * The Structure Template name for this test. Lowercase.
     * You can include a namespace if needed: `namespace:templateName`.
     * namespace defaults to the one you specified in the `@GameTestHolder`.
     *
     * If empty, the class name and method name will be used.
     * The location will be `${namespace}:${className}.${methodName}`.
     * e.g.: `mctest:mytest.testsomething`
     */
    val template: String = "",
    val timeoutTicks: Int = 100,
    val setupTicks: Int = 0,
)
