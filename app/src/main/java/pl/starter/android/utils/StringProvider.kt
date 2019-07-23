package pl.starter.android.utils

import android.content.Context

interface StringProvider {
    fun getString(textId: Int, vararg objects: Any): String
    fun getQuantityString(textId: Int, count: Int, vararg objects: Any): String
}

class ContextStringProvider(private val context: Context) : StringProvider {

    override fun getString(textId: Int, vararg objects: Any) = context.resources.getString(textId, *objects)

    override fun getQuantityString(textId: Int, count: Int, vararg objects: Any) =
        context.resources.getQuantityString(textId, count, *objects)
}
