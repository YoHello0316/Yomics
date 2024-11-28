package com.example.yomics.data

import kotlinx.coroutines.flow.Flow

class ComicRepository(private val comicDao: ComicDao) {
    val allComics: Flow<List<Comic>> = comicDao.getAllComics()

    suspend fun insert(comic: Comic) {
        comicDao.insert(comic)
    }

    suspend fun update(comic: Comic) {
        comicDao.update(comic)
    }

    suspend fun delete(comic: Comic) {
        comicDao.delete(comic)
    }

    // 新增方法：根据漫画 ID 获取特定的漫画
    fun getComicById(comicId: Int): Flow<Comic?> {
        return comicDao.getComicById(comicId)
    }
}
