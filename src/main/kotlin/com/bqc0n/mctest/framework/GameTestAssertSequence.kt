package com.bqc0n.mctest.framework

import com.bqc0n.mctest.framework.exception.GameTestAssertException
import java.util.function.Supplier

class GameTestAssertSequence(
    val parent: GameTestCase,
) {

    private val assertions: MutableList<GameTestAssertion> = mutableListOf()

    fun thenWaitUntil(criterion: Runnable): GameTestAssertSequence {
        this.assertions.add(GameTestAssertion.of(criterion))
        return this
    }

    fun thenSucceed(): GameTestAssertSequence {
        this.assertions.add(GameTestAssertion.of(this.parent::succeed))
        return this
    }

    fun thenFail(exceptionSupplier: Supplier<GameTestAssertException>): GameTestAssertSequence {
        this.assertions.add(GameTestAssertion.of { this.parent.fail(exceptionSupplier.get()) })
        return this
    }

    fun tick() {
        try {
            this.tickAssertions()
        } catch (e: GameTestAssertException) {
            // just retry
        }
    }

    private fun tickAssertions() {
        val iterator = this.assertions.iterator()
        while (iterator.hasNext()) {
            val assertion = iterator.next()
            assertion.assertion.run()
            iterator.remove() // Remove the assertion if it succeeded
        }
    }
}