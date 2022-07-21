package ru.maxpek.friendslinkup.dao

import androidx.paging.PagingSource
import androidx.room.*
import ru.maxpek.friendslinkup.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): PagingSource<Int, EventEntity>

    @Query("SELECT COUNT(*) == 0 FROM EventEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<EventEntity>)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun removeById(id: Long)

//    @Query("UPDATE EventEntity SET read = 1")
//    suspend fun update()   Пока что не знаю нужно или нет

    @Query("SELECT COUNT() FROM EventEntity")
    suspend fun isSize(): Int

    @Query("DELETE FROM EventEntity")
    suspend fun removeAll()
}
