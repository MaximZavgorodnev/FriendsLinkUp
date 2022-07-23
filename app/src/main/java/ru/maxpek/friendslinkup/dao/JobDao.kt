package ru.maxpek.friendslinkup.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.maxpek.friendslinkup.entity.JobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM JobEntity ORDER BY id DESC")
    fun getAll(): PagingSource<Int, JobEntity>

    @Query("SELECT COUNT(*) == 0 FROM JobEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: JobEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<JobEntity>)

    @Query("DELETE FROM JobEntity WHERE id = :id")
    suspend fun removeById(id: Long)

//    @Query("UPDATE JobEntity SET read = 1")
//    suspend fun update()   Пока что не знаю нужно или нет

    @Query("SELECT COUNT() FROM JobEntity")
    suspend fun isSize(): Int

    @Query("DELETE FROM JobEntity")
    suspend fun removeAll()
}