package com.package.socialapp.uiscreens.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.package.socialapp.uiscreens.viewmodel.AuthViewModel
import com.package.socialapp.uiscreens.viewmodel.PostViewModel
import com.package.socialapp.uiscreens.viewmodel.UserSettingsViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel = hiltViewModel(),
    userSettingsViewModel: UserSettingsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // Kullanıcının oturum bilgisini alıyoruz
    val user by authViewModel.user.collectAsState(initial = null)

    // Kullanıcı ayarlarını her girişte güncelliyoruz.
    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            userSettingsViewModel.loadUserSettings(uid)
        }
    }

    // Firestore’dan güncel ayarlar
    val userSettings = userSettingsViewModel.userSettings
    // Gönderi listesini state olarak alıyoruz 
    val posts by postViewModel.posts.collectAsState(initial = emptyList())

    var postText by remember { mutableStateOf("") }
    val maxChar = 250

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Main Page") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ayarlar"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        // Tüm içerik verticalScroll ile sarmalanmıştır.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = postText,
                onValueChange = { if (it.length <= maxChar) postText = it },
                label = { Text("Create a text (max $maxChar character)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (postText.isNotBlank() && user != null) {
                        // Kullanıcının güncel ayarlarından alınan isim ve cinsiyet ile post oluşturuluyor
                        postViewModel.createPost(
                            text = postText,
                            authorId = user!!.uid,
                            authorEmail = user!!.email ?: "",
                            authorName = userSettings.userName,
                            gender = userSettings.gender
                        )
                        postText = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("SEND")
            }
            // Gönderiler için Column kullanıyoruz; 
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                posts.forEach { post ->
                    PostItem(
                        post = post,
                        currentUserId = user?.uid ?: "",
                        postViewModel = postViewModel
                    )
                }
            }
            Button(
                onClick = { authViewModel.signOut() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("EXIT")
            }
        }
    }
}
