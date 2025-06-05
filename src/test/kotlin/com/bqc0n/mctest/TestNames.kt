package com.bqc0n.mctest

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.IGameTestHelper
import com.bqc0n.mctest.internal.GameTestCollector
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.lang.reflect.Method

class TestNames : StringSpec({

    lateinit var gameTestHolder: GameTestHolder
    lateinit var clazz: Class<*>
    lateinit var simpleTest: Method
    lateinit var templateNamed: Method
    lateinit var templateNamespaced: Method

    beforeTest {
        clazz = KotlinTests::class.java
        gameTestHolder = clazz.getAnnotation(GameTestHolder::class.java)
        for (method in clazz.declaredMethods) {
            println(method)
        }
        simpleTest = clazz.getMethod("simpleTest", IGameTestHelper::class.java)
        templateNamed = clazz.getMethod("templateNamed", IGameTestHelper::class.java)
        templateNamespaced = clazz.getMethod("templateNamespaced", IGameTestHelper::class.java)
    }

    "Test simpleTest name" {
        val testName = GameTestCollector.createTestName(gameTestHolder, clazz, simpleTest)
        testName shouldBe  "mctest.kotlintests.simpletest"
    }

    "Test simpleTest structure location" {
        val structureLocation = GameTestCollector.createStructureLocation(gameTestHolder, clazz, simpleTest)
        structureLocation.toString() shouldBe "mctest:kotlintests.simpletest"
    }

    "Test templateNamed name" {
        val testName = GameTestCollector.createTestName(gameTestHolder, clazz, templateNamed)
        testName shouldBe "mctest.kotlintests.templatenamed"
    }

    "Test templateNamed structure location" {
        val structureLocation = GameTestCollector.createStructureLocation(gameTestHolder, clazz, templateNamed)
        structureLocation.toString() shouldBe "mctest:kotlintests.somename"
    }

    "Test templateNamespaced name" {
        val testName = GameTestCollector.createTestName(gameTestHolder, clazz, templateNamespaced)
        testName shouldBe "mctest.kotlintests.templatenamespaced"
    }

    "Test templateNamespaced structure location" {
        val structureLocation = GameTestCollector.createStructureLocation(gameTestHolder, clazz, templateNamespaced)
        structureLocation.toString() shouldBe "anotherspace:anothername"
    }
})