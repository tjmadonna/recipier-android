package com.inelasticcollision.recipelink.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.net.URI
import java.net.URISyntaxException
import java.util.*

@Entity(tableName = "recipes", indices = [Index(value = ["title"], unique = false)])
data class Recipe(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "last_modified")
    val lastModified: Date = Date(),

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    @ColumnInfo(name = "favorite")
    val favorite: Boolean = false,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "tags")
    val tags: List<String>? = null

) {

    companion object {
        @JvmStatic
        private val URL_PREFIX = arrayOf("www.", "m.")
    }

    val host: String?
        get() {
            return try {
                val uri = URI(url)
                var host = uri.host ?: return null
                for (prefix in URL_PREFIX) {
                    if (host.startsWith(prefix)) {
                        host = host.substring(prefix.length)
                    }
                }
                host
            } catch (e: URISyntaxException) {
                null
            }
        }
}