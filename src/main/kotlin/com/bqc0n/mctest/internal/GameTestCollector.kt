package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestHolder
import net.minecraftforge.fml.common.discovery.ASMDataTable
import java.lang.reflect.Method

object GameTestCollector {
    fun collectGameTests(asmDataTable: ASMDataTable) {
        val methods = mutableListOf<Method>()
        val annotationClassName: String = GameTestHolder::class.java.canonicalName
        val asmDataSet: Set<ASMDataTable.ASMData> = asmDataTable.getAll(annotationClassName)
        for (asmData in asmDataSet) {
            val javaClass = Class.forName(asmData.className)
            val testMethods = javaClass.methods.filter { it.isAnnotationPresent(GameTest::class.java) }
            methods.addAll(testMethods)
        }
    }
}