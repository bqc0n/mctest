package com.bqc0n.mctest.framework

class GameTestAssertion private constructor(
    val assertion: Runnable,
    val expectedDelay: Long? = null,
) {
    companion object {

        fun of(assertion: Runnable): GameTestAssertion {
            return GameTestAssertion(assertion, null)
        }

        fun of(assertion: Runnable, expectedDelay: Long? = null): GameTestAssertion {
            return GameTestAssertion(assertion, expectedDelay)
        }
    }
}