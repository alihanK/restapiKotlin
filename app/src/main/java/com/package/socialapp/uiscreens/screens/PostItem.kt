package com.package.socialapp.uiscreens.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.package.socialapp.uiscreens.model.Post
import com.package.socialapp.uiscreens.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PostItem(post: Post, currentUserId: String, postViewModel: PostViewModel) {
    // Arka plan rengi, post oluşturulurken gönderilen cinsiyete göre ayarlanıyor:
    // "male" ise açık mavi, "female" ise pembe, aksi halde varsayılan olarak LightGray
    val backgroundColor = when (post.gender.lowercase()) {
        "male" -> Color(0xFFADD8E6)
        "female" -> Color(0xFFFFC0CB)
        else -> Color.LightGray
    }

    // Timestamp değerini gün ay yıl ve saat:dakika formatında biçimlendiriyoruz.
    val formattedDate = remember(post.timestamp) {
        SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(post.timestamp))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = post.authorName,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Alt satır: Sol kısımda beğeni ikonları, sağ kısımda tarih bilgisi
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol grup: beğeni ikonu, beğeni sayısı ve sil butonu (varsa)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Beğeni İkonu yuvarlak container içinde
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(40.dp)
                    ) {
                        IconButton(
                            onClick = { postViewModel.likePost(post, currentUserId) },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                imageVector = if (post.likes.contains(currentUserId))
                                    Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Like!",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Beğeni sayısını yuvarlak container içinde gösteriyoruz
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "${post.likes.size}")
                        }
                    }
                    // Eğer gönderi mevcut kullanıcıya aitse, sil butonu
                    if (post.authorId == currentUserId) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(
                                onClick = { postViewModel.deletePost(post, currentUserId) },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                // Araya esnek Spacer ekleyerek sol grubu sola, tarih bilgisini sağa itiyoruz.
                Spacer(modifier = Modifier.weight(1f))
                // Sağ kısımda tarih bilgisini gösteriyoruz.
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
