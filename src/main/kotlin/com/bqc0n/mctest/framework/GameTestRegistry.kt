package com.bqc0n.mctest.framework

import com.google.common.collect.ImmutableMap

object GameTestRegistry {
    private val _tests = mutableMapOf<String, GameTestDefinition>()

    fun register(definition: GameTestDefinition) {
        val name = definition.testName
        if (_tests.containsKey(name)) {
            throw IllegalArgumentException("Game test '$name' is already registered.")
        }
        _tests[name] = definition
    }

    fun getAllTests(): ImmutableMap<String, GameTestDefinition> {
        return ImmutableMap.copyOf(_tests)
    }

    fun getTest(name: String): GameTestDefinition? {
        return _tests[name]
    }
}