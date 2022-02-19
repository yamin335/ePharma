package com.rtchubs.pharmaerp.util

import com.rtchubs.pharmaerp.util.AtomicNumberGenerator
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by ramananda on 5/9/17.
 */
object AtomicNumberGenerator {
    private val c = AtomicInteger(0)
    val uniqueNumber: Int
        get() = c.incrementAndGet()
}