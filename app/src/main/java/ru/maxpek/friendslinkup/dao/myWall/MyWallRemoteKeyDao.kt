package ru.maxpek.friendslinkup.dao.myWall

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.maxpek.friendslinkup.entity.PostRemoteKeyEntity

@Dao
interface MyWallRemoteKeyDao {
    @Query("SELECT COUNT(*) == 0 FROM PostRemoteKeyEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT MAX(id) FROM PostRemoteKeyEntity")
    suspend fun max(): Long?

    @Query("SELECT MIN(id) FROM PostRemoteKeyEntity")
    suspend fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: PostRemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<PostRemoteKeyEntity>)

    @Query("DELETE FROM PostRemoteKeyEntity")
    suspend fun removeAll()
}