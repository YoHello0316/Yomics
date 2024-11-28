package com.example.yomics

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.yomics.viewmodel.ComicViewModel
import com.example.yomics.viewmodel.ComicViewModelFactory
import com.example.yomics.data.ComicRepository
import com.example.yomics.data.ComicDatabase
import com.example.yomics.data.Comic
import androidx.compose.foundation.clickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = ComicDatabase.getDatabase(applicationContext)
            val repository = ComicRepository(database.comicDao())
            val comicViewModel: ComicViewModel = ViewModelProvider(this, ComicViewModelFactory(repository))[ComicViewModel::class.java]
            ComicListScreen(viewModel = comicViewModel, activity = this)
        }
    }
}



@Composable
fun ComicListScreen(viewModel: ComicViewModel, activity: ComponentActivity) {
    val comics by viewModel.allComics.collectAsState(initial = emptyList())

    // 对漫画按标题分组，并对每个组内的漫画按 issue 进行升序排序
    val groupedComics = comics.groupBy { it.title }.mapValues { entry ->
        entry.value.sortedBy { comic ->
            comic.issue.toIntOrNull() ?: Int.MAX_VALUE  // 确保按 issue 数字大小升序排列
        }
    }

    var expandedTitle by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Yomics Collection",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF800080),
                fontFamily = FontFamily.Cursive,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            groupedComics.forEach { (title, comicList) ->
                item {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedTitle = if (expandedTitle == title) null else title },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = title, style = MaterialTheme.typography.headlineSmall)
                            Button(onClick = {
                                expandedTitle = if (expandedTitle == title) null else title
                            }) {
                                Text(if (expandedTitle == title) "Collapse" else "Expand")
                            }
                        }
                        if (expandedTitle == title) {
                            comicList.forEach { comic ->
                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp, bottom = 8.dp)
                                        .background(Color(0xFFFFEB3B)) // 添加黄色背景
                                        .padding(8.dp)
                                ) {
                                    Text(text = "Author: ${comic.author}")
                                    Text(text = "Issue: ${comic.issue}")
                                    Text(text = "Condition: ${comic.condition}")
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(onClick = {
                                            // Edit Comic
                                            val intent = Intent(activity, EditComicActivity::class.java)
                                            intent.putExtra("comicId", comic.id)
                                            activity.startActivity(intent)
                                        }) {
                                            Text("Edit")
                                        }
                                        Button(onClick = {
                                            // Delete Comic
                                            viewModel.delete(comic)
                                        }) {
                                            Text("Delete")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Button(onClick = {
            // 使用 Intent 来启动 AddComicActivity
            val intent = Intent(activity, AddComicActivity::class.java)
            activity.startActivity(intent)
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Add New Comic")
        }
    }
}
