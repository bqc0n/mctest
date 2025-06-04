package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestDefinition
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.IGameTestHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.discovery.ASMDataTable
import java.lang.reflect.Method

object GameTestCollector {
    fun collectGameTests(asmDataTable: ASMDataTable) {
        val methods = mutableListOf<Method>()
        val annotationClassName: String = GameTestHolder::class.java.canonicalName
        val asmDataSet: Set<ASMDataTable.ASMData> = asmDataTable.getAll(annotationClassName)
        for (asmData in asmDataSet) {
            val javaClass: Class<*> = Class.forName(asmData.className)
            if (!javaClass.isAnnotationPresent(GameTestHolder::class.java)) continue
            val gameTestNamespace = javaClass.getAnnotation(GameTestHolder::class.java)!!.namespace
            for (method in javaClass.methods) {
                if (!method.isAnnotationPresent(GameTest::class.java)) continue
                validateTestMethod(method)
                val annotation: GameTest = method.getAnnotation(GameTest::class.java)!!
                val structureNameSuffix: String = annotation.template.ifEmpty { method.name }.lowercase()
                val structureNamePrefix: String = javaClass.simpleName.lowercase()
                val structureLocation = ResourceLocation(gameTestNamespace, "$structureNamePrefix.$structureNameSuffix")
                val timeOutTicks = annotation.timeoutTicks
                val setupTicks = annotation.setupTicks
                val testName = "${gameTestNamespace}.${method.name.lowercase()}"
                val definition = GameTestDefinition(
                    testName, structureLocation, setupTicks, timeOutTicks,
                    methodIntoConsumer(method)
                )
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

    private fun methodIntoConsumer(method: Method): java.util.function.Consumer<IGameTestHelper> {
        return java.util.function.Consumer { helper: IGameTestHelper ->
            try {
                if (java.lang.reflect.Modifier.isStatic(method.modifiers)) {
                    method.invoke(null, helper)
                } else {
                    val instance = method.declaringClass.getDeclaredConstructor().newInstance()
                    method.invoke(instance, helper)
                }
            } catch (e: Exception) {
                throw RuntimeException("Failed to invoke game test method '${method.name}'", e)
            }
        }
    }
}