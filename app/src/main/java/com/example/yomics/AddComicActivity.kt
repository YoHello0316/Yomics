package com.example.yomics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.unit.dp
import com.example.yomics.data.Comic
import com.example.yomics.data.ComicDatabase
import com.example.yomics.data.ComicRepository
import com.example.yomics.viewmodel.ComicViewModel
import com.example.yomics.viewmodel.ComicViewModelFactory
import androidx.compose.ui.Modifier

class AddComicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 获取数据库实例和仓库实例
            val database = ComicDatabase.getDatabase(applicationContext)
            val repository = ComicRepository(database.comicDao())
            // 使用工厂类创建 ComicViewModel 实例
            val comicViewModel: ComicViewModel = ViewModelProvider(this, ComicViewModelFactory(repository))[ComicViewModel::class.java]

            AddComicScreen(viewModel = comicViewModel) {
                // 完成添加后，返回上一页
                finish()
            }
        }
    }
}

@Composable
fun AddComicScreen(viewModel: ComicViewModel, onAddComplete: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var issue by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        TextField(value = author, onValueChange = { author = it }, label = { Text("Author") })
        TextField(value = issue, onValueChange = { issue = it }, label = { Text("issue") })
        TextField(value = condition, onValueChange = { condition = it }, label = { Text("condition") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // 创建新漫画对象并插入数据库
            val newComic = Comic(title = title, author = author, issue = issue,condition=condition)
            viewModel.insert(newComic)
            onAddComplete()
        }) {
            Text("Add Comic")
        }
    }
}
