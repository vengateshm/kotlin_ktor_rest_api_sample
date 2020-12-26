package com.facts.dao

import com.facts.model.Fact
import java.io.Closeable

interface FactDao : Closeable {
    fun createFact(description: String)
    fun getAllFacts(): List<Fact>
}