package org.avr.notes.common

import kotlinx.datetime.Instant

val Instant.Companion.NONE
    get() = Instant.fromEpochMilliseconds(Long.MIN_VALUE)