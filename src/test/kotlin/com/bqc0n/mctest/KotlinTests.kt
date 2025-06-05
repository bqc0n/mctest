package com.bqc0n.mctest

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.IGameTestHelper

@GameTestHolder("mctest")
class KotlinTests {
    @GameTest
    fun simpleTest(helper: IGameTestHelper) {}

    @GameTest(template = "somename")
    fun templateNamed(helper: IGameTestHelper) {}

    @GameTest(template = "anotherspace:anothername")
    fun templateNamespaced(helper: IGameTestHelper) {}
}