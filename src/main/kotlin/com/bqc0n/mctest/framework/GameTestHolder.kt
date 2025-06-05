package com.bqc0n.mctest.framework

annotation class GameTestHolder(

    /**
     * The namespace for the game tests defined in this holder, typically the mod ID.
     *
     * This will be used for both structure locations and test names.
     * If you want to use a different namespace for structure locations,
     * you can specify it in the `GameTest` annotation like `@GameTest(template = "modid:template")`.
     */
    @get:JvmName("value")
    val namespace: String,
)
