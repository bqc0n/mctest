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
            for (method in javaClass.methods) {
                if (!method.isAnnotationPresent(GameTest::class.java)) continue

                if (method.parameterCount != 1 || method.parameters[0].type != IGameTestHelper::class.java) {
                    throw IllegalArgumentException(
                        "Game test method '${method.name}' in class '${javaClass.name}' must have exactly one parameter of type IGameTestHelper."
                    )
                }
            }
        }
    }
}