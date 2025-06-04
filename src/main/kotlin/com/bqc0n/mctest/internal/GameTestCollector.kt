package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.IGameTestHelper
import net.minecraftforge.fml.common.discovery.ASMDataTable
import java.lang.reflect.Method

object GameTestCollector {
    fun collectGameTests(asmDataTable: ASMDataTable) {
        val methods = mutableListOf<Method>()
        val annotationClassName: String = GameTestHolder::class.java.canonicalName
        val asmDataSet: Set<ASMDataTable.ASMData> = asmDataTable.getAll(annotationClassName)
        for (asmData in asmDataSet) {
            val javaClass = Class.forName(asmData.className)
            if (!javaClass.isAnnotationPresent(GameTestHolder::class.java)) continue
            val gameTestNamespace = javaClass.getAnnotation(GameTestHolder::class.java).namespace
            for (method in javaClass.methods) {
                if (!method.isAnnotationPresent(GameTest::class.java)) continue
                validateTestMethod(method)
                val annotation: GameTest = method.getAnnotation(GameTest::class.java)
                val structureNameSuffix: String = annotation.template.ifEmpty { method.name }
                val structureNamePrefix: String = javaClass.simpleName
                println("${gameTestNamespace}:$structureNamePrefix.$structureNameSuffix")
            }
        }
    }

    private fun validateTestMethod(method: Method) {
        if (method.parameterCount != 1 || method.parameters[0].type != IGameTestHelper::class.java) {
            throw IllegalArgumentException(
                "Game test method '${method.name}' must have exactly one parameter of type IGameTestHelper."
            )
        }
    }
}