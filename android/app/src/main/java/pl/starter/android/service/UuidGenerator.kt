package pl.starter.android.service

import java.util.*

interface UuidGenerator {
    fun generate(): String
}

class UuidGeneratorImpl :UuidGenerator{
    override fun generate(): String {
        return UUID.randomUUID().toString()
    }

}
