package ru.maxpek.friendslinkup.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.enumeration.AttachmentType

private val gson = Gson()
@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): PagingSource<Int, PostEntity>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

//    @Query("UPDATE PostEntity SET read = 1")
//    suspend fun update()

    @Query("SELECT COUNT() FROM PostEntity")
    suspend fun isSize(): Int

    @Query("DELETE FROM PostEntity")
    suspend fun removeAll()
}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}

class CoordinatesConverter {

    @TypeConverter
    fun coordinatesToJson(coordinates: Coordinates?): String? {
        return if (coordinates == null) {
            null } else {
                gson.toJson(coordinates)
            }
    }

    @TypeConverter
    fun jsonToCoordinates(json: String?): Coordinates? {
        return if (json.isNullOrEmpty()) {
            null
        } else {
            val type = object : TypeToken<Coordinates>() {}.type
            gson.fromJson(json, type)
        }
    }
}
