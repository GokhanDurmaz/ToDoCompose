/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.util

import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.util.getRelativeDayLabel
import com.flowintent.core.util.parseDateToLong
import com.flowintent.core.util.toReadableDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class DateHelperTest {

    @Test
    fun `getRelativeDayLabel returns Today for current date`() {
        val now = Calendar.getInstance().timeInMillis
        assertEquals("Today", getRelativeDayLabel(now))
    }

    @Test
    fun `getRelativeDayLabel returns Tomorrow for next day`() {
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.timeInMillis
        assertEquals("Tomorrow", getRelativeDayLabel(tomorrow))
    }

    @Test
    fun `getRelativeDayLabel returns Yesterday for previous day`() {
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
        assertEquals("Yesterday", getRelativeDayLabel(yesterday))
    }

    @Test
    fun `parseDateToLong returns default morning time when input is null`() {
        val result = parseDateToLong(null, emptyList())
        val calendar = Calendar.getInstance().apply { timeInMillis = result }
        
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val now = Calendar.getInstance()
        val nowHour = now.get(Calendar.HOUR_OF_DAY)
        
        val possibleHours = if (nowHour >= 9) {
            setOf((nowHour + 1) % 24, (nowHour + 2) % 24)
        } else {
            setOf(9, 10)
        }
        
        assertTrue("Hour $hour should be one of $possibleHours for nowHour $nowHour", possibleHours.contains(hour))
        assertEquals(0, calendar.get(Calendar.MINUTE))
        assertEquals(0, calendar.get(Calendar.SECOND))
    }

    @Test
    fun `parseDateToLong avoids collisions with existing tasks`() {
        val baseDateStr = "2026-01-01"
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val baseTime = sdf.parse(baseDateStr)!!.time

        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = baseTime
            set(java.util.Calendar.HOUR_OF_DAY, 9)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val expectedBaseTime = calendar.timeInMillis

        val existingTasks = listOf(
            Task(uid = 1, title = "T1", content = TaskRes.TaskContent(""), dueDate = expectedBaseTime)
        )

        val result = parseDateToLong(baseDateStr, existingTasks)
        assertEquals(expectedBaseTime + 30 * 60 * 1000, result)
    }

    @Test
    fun `toReadableDateTime formats long to string correctly`() {
        val calendar = Calendar.getInstance().apply {
            set(2026, Calendar.JANUARY, 1, 10, 30)
        }
        val result = calendar.timeInMillis.toReadableDateTime()
        assertTrue(result.contains("01"))
        assertTrue(result.contains("2026"))
        assertTrue(result.contains("10:30"))
    }
}
