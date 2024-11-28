package com.example.yomics.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yomics.data.Comic
import com.example.yomics.viewmodel.ComicViewModel

@Composable
fun EditComicScreen(viewModel: ComicViewModel, comic: Comic, onEditComplete: () -> Unit) {
    // 定义可修改的漫画属性
    var title by remember { mutableStateOf(comic.title) }
    var author by remember { mutableStateOf(comic.author) }
    var issue by remember { mutableStateOf(comic.issue) }
    var condition by remember { mutableStateOf(comic.condition) }

    // 使用 Column 来布局界面元素
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 输入字段用于编辑漫画标题
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        // 输入字段用于编辑漫画作者
        TextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Author") },
            modifier = Modifier.fillMaxWidth()
        )

        // 输入字段用于编辑漫画描述
        TextField(
            value = issue,
            onValueChange = { issue = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // 输入字段用于编辑漫画状况
        TextField(
            value = condition,
            onValueChange = { condition = it },
            label = { Text("Condition") },
            modifier = Modifier.fillMaxWidth()
        )

        // 保存按钮
        Button(
            onClick = {
                // 更新漫画信息
                val updatedComic = comic.copy(
                    title = title,
                    author = author,
                    issue = issue,
                    condition = condition
                )
                // 调用 ViewModel 的更新方法
                viewModel.update(updatedComic)
                // 编辑完成后的回调
                onEditComplete()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Save Changes")
        }
    }
}
