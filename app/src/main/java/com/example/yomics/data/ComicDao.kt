package com.example.yomics.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {
    @Query("SELECT * FROM comics")
    fun getAllComics(): Flow<List<Comic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comic: Comic)

    @Update
    suspend fun update(comic: Comic)

    @Delete
    suspend fun delete(comic: Comic)

    // 新增方法：根据漫画 ID 获取特定的漫画
    @Query("SELECT * FROM comics WHERE id = :comicId")
    fun getComicById(comicId: Int): Flow<Comic?>
}
