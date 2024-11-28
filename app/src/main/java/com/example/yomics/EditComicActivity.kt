package com.example.yomics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yomics.viewmodel.ComicViewModel
import com.example.yomics.viewmodel.ComicViewModelFactory
import com.example.yomics.data.ComicRepository
import com.example.yomics.data.ComicDatabase
import com.example.yomics.screens.EditComicScreen



class EditComicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取传递的漫画 ID
        val comicId = intent.getIntExtra("comicId", -1) // 确保使用正确的键名称

        setContent {
            // 初始化 ViewModel
            val database = ComicDatabase.getDatabase(applicationContext)
            val repository = ComicRepository(database.comicDao())
            val comicViewModel: ComicViewModel = ViewModelProvider(this, ComicViewModelFactory(repository))[ComicViewModel::class.java]

            val comic = comicViewModel.getComicById(comicId).collectAsStateWithLifecycle(initialValue = null).value
            if (comic != null) {
                EditComicScreen(comicViewModel, comic) {
                    finish() // 编辑完成后，关闭活动
                }
            } else {
                Text("Comic not found")
            }
        }
    }
}
