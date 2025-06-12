package com.bqc0n.mctest

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.internal.GameTestHelper

@GameTestHolder("mctest")
class KotlinTests {
    @GameTest
    fun simpleTest(helper: GameTestHelper) {}

    @GameTest(template = "somename")
    fun templateNamed(helper: GameTestHelper) {}

    @GameTest(template = "anotherspace:anothername")
    fun templateNamespaced(helper: GameTestHelper) {}
}