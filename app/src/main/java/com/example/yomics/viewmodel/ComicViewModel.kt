package com.example.yomics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import com.example.yomics.data.Comic
import com.example.yomics.data.ComicRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ComicViewModel(private val repository: ComicRepository) : ViewModel() {
    val allComics: StateFlow<List<Comic>> = repository.allComics
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 新增：根据漫画 ID 获取特定的漫画
    fun getComicById(comicId: Int): Flow<Comic?> {
        return repository.getComicById(comicId)
    }

    // 插入新漫画
    fun insert(comic: Comic) = viewModelScope.launch {
        repository.insert(comic)
    }

    // 更新现有漫画
    fun update(comic: Comic) = viewModelScope.launch {
        repository.update(comic)
    }

    // 删除漫画
    fun delete(comic: Comic) = viewModelScope.launch {
        repository.delete(comic)
    }
}
