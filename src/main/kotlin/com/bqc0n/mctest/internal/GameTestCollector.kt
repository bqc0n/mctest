package com.bqc0n.mctest.internal

import com.bqc0n.mctest.framework.GameTest
import com.bqc0n.mctest.framework.GameTestDefinition
import com.bqc0n.mctest.framework.GameTestHolder
import com.bqc0n.mctest.framework.GameTestRegistry
import com.bqc0n.mctest.framework.IGameTestHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.discovery.ASMDataTable
import org.jetbrains.annotations.VisibleForTesting
import java.lang.reflect.Method

object GameTestCollector {
    fun collectGameTests(asmDataTable: ASMDataTable) {
        val asmDataSet: Set<ASMDataTable.ASMData> = asmDataTable.getAll(GameTestHolder::class.java.canonicalName)
        for (asmData in asmDataSet) {
            val clazz: Class<*> = Class.forName(asmData.className)
            if (!clazz.isAnnotationPresent(GameTestHolder::class.java)) continue
            val holder = clazz.getAnnotation(GameTestHolder::class.java)!!
            for (method in clazz.methods) {
                if (!method.isAnnotationPresent(GameTest::class.java)) continue
                validateTestMethod(method)
                val annotation: GameTest = method.getAnnotation(GameTest::class.java)!!
                val timeOutTicks = annotation.timeoutTicks
                val setupTicks = annotation.setupTicks
                val definition = GameTestDefinition(
                    createTestName(holder, clazz, method),
                    createStructureLocation(holder, clazz, method),
                    setupTicks, timeOutTicks,
                    methodIntoConsumer(method)
                )
                GameTestRegistry.register(definition)
            }
        }
    }

    @VisibleForTesting
    fun createTestName(holder: GameTestHolder, clazz: Class<*>, method: Method): String {
        return "${holder.namespace}.${clazz.simpleName.lowercase()}.${method.name.lowercase()}"
    }

    @VisibleForTesting
    fun createStructureLocation(holder: GameTestHolder, clazz: Class<*>, method: Method): ResourceLocation {
        val gameTest = method.getAnnotation(GameTest::class.java)!!
        if (gameTest.template.isEmpty()) {
            return ResourceLocation(holder.namespace, "${clazz.simpleName.lowercase()}.${method.name.lowercase()}")
        }
        val structureName = gameTest.template
        return if (structureName.contains(":")) {
            ResourceLocation(structureName)
        } else {
            ResourceLocation(holder.namespace, "${clazz.simpleName}.${structureName.lowercase()}")
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