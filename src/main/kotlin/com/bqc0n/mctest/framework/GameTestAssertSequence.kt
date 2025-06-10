package com.bqc0n.mctest.framework

import com.bqc0n.mctest.framework.exception.GameTestAssertException
import java.util.function.Supplier

class GameTestAssertSequence(
    val parent: GameTestCase,
) {

    private val assertions: MutableList<GameTestAssertion> = mutableListOf()
    private var lastTick = 0L

    fun thenWaitUntil(criterion: Runnable): GameTestAssertSequence {
        this.assertions.add(GameTestAssertion.of(criterion))
        return this
    }

    fun thenExecute(task: Runnable): GameTestAssertSequence {
        this.assertions.add(GameTestAssertion.of { this.executeAndFailIfThrown(task) })
        return this
    }

    fun thenExecuteAfter(tick: Int, task: Runnable): GameTestAssertSequence {
        this.assertions.add(GameTestAssertion.of {
            if (this.parent.tickCount - this.lastTick >= tick) {
                this.executeAndFailIfThrown(task)
            } else {
                throw GameTestAssertException("Expected to execute after $tick ticks, but only ${this.parent.tickCount - this.lastTick} ticks have passed.")
            }
        })
        return this
    }

    fun thenWait(ticks: Int): GameTestAssertSequence {
        this.thenExecuteAfter(ticks) { /* NOOP */ }
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

    fun tickAndContinue(currentTick: Long) {
        try {
            this.tickAssertions(currentTick)
        } catch (_: GameTestAssertException) {
            // just retry
        }
    }

    fun tickAndFailIfNotComplete(currentTick: Long) {
        try {
            this.tickAssertions(currentTick)
        } catch (e: GameTestAssertException) {
            this.parent.fail(e)
        }
    }

    private fun executeAndFailIfThrown(task: Runnable) {
        try {
            task.run()
        } catch (e: GameTestAssertException) {
            this.parent.fail(e)
        }
    }

    /**
     * Executes all assertions in this sequence.
     * If [GameTestAssertException] is thrown, retry from that assertion.
     */
    private fun tickAssertions(currentTick: Long) {
        val iterator = this.assertions.iterator()
        while (iterator.hasNext()) {
            val assertion = iterator.next()
            assertion.assertion.run()
            iterator.remove() // Remove the assertion if it succeeded
            this.lastTick = currentTick
        }
    }
}