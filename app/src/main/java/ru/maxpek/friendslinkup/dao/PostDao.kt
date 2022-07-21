package ru.maxpek.friendslinkup.dao

import androidx.paging.PagingSource
import androidx.room.*
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.enumeration.TypeEvent

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
    @TypeConverter
    fun toTypeEvent(value: String) = enumValueOf<TypeEvent>(value)
    @TypeConverter
    fun fromTypeEvent(value: TypeEvent) = value.name

}