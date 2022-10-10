package ru.maxpek.friendslinkup.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.ListIds
import ru.maxpek.friendslinkup.dto.ListUserPreview
import ru.maxpek.friendslinkup.dto.UserPreview
import ru.maxpek.friendslinkup.entity.EventEntity
import ru.maxpek.friendslinkup.enumeration.TypeEvent


private val gson = Gson()
@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getAll(): PagingSource<Int, EventEntity>

    @Query("SELECT COUNT(*) == 0 FROM EventEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<EventEntity>)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun removeById(id: Int)

//    @Query("UPDATE EventEntity SET read = 1")
//    suspend fun update()   Пока что не знаю нужно или нет

    @Query("SELECT COUNT() FROM EventEntity")
    suspend fun isSize(): Int

    @Query("DELETE FROM EventEntity")
    suspend fun removeAll()
}


class EventTypeConverters {
    @TypeConverter
    fun toTypeEvent(value: String) = enumValueOf<TypeEvent>(value)
    @TypeConverter
    fun fromTypeEvent(value: TypeEvent) = value.name

}

class ConvertersListIds {
    @TypeConverter
    fun fromListIds(listIds: ListIds): String {
        return Gson().toJson(listIds)
    }
    @TypeConverter
    fun toListIds(sh: String): ListIds {
        return Gson().fromJson(sh, ListIds::class.java)
    }
}
//class UserPreviewConverter {
//    @TypeConverter
//    fun userPreviewToJson(users: ListUserPreview): String {
//
//        return Gson().toJson(users)
//
//    }
//
//    @TypeConverter
//    fun jsonToUserPreview(sh: String): ListUserPreview {
//        val type = object : TypeToken<ListUserPreview>() {}.type
//        return gson.fromJson(sh, type)
//    }
//}
