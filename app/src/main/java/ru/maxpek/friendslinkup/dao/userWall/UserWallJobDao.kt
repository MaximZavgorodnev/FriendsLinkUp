package ru.maxpek.friendslinkup.dao.userWall

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.maxpek.friendslinkup.entity.JobEntity

interface UserWallJobDao {
    @Query("SELECT * FROM JobEntity ORDER BY id DESC")
    fun getAll(): PagingSource<Int, JobEntity>

    @Query("SELECT COUNT(*) == 0 FROM JobEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: JobEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<JobEntity>)

}