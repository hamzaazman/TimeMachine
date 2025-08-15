package com.hamzaazman.timemachine.common

import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object DateHelper {
    
    @OptIn(ExperimentalTime::class)
    fun millisToLocalDate(millis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }
    
    @OptIn(ExperimentalTime::class)
    fun today(): LocalDate {
        return Clock.System.todayIn(TimeZone.UTC)
    }


    @OptIn(ExperimentalTime::class)
    fun LocalDate.toEpochMillis(): Long {
        val instant = this.atStartOfDayIn(TimeZone.UTC)
        return instant.toEpochMilliseconds()
    }
}