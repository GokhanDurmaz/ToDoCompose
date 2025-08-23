package com.flowintent.workspace

import com.flowintent.workspace.util.pow
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ExtensionWithInlineTest {

    @Test
    fun `Test inline with extension function equals`() {
        val pow1 = 5

        var result = 0
        pow1.pow(pow = 2) { n ->  result = n }

        assertEquals(25, result)
    }

    @Test
    fun `Test inline with extension function not equals`() {
        val pow1 = 2

        var result = 0
        pow1.pow(pow = 2) { n ->  result = n }

        assertNotEquals(25, result)
    }

    @Test
    fun `Test inline with extension function with null value`() {
        val pow1 = null

        var result = 0
        pow1?.pow(pow = 2) { n ->  result = n }

        assertNotEquals(4, result)
    }
}