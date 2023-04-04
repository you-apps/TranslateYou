package com.bnyro.translate.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bnyro.translate.db.obj.Language

@Composable
fun LanguageItem(
    language: Language,
    isPinned: Boolean?,
    selectedLanguage: Language,
    onPinnedChange: () -> Unit,
    onClick: () -> Unit
) {
    val isSelected = language.name.lowercase() == selectedLanguage.name.lowercase() ||
        language.code.lowercase() == selectedLanguage.code.lowercase()

    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(30.dp)
            )
            .clickable {
                onClick.invoke()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 15.dp)
            )
            if (isPinned != null) {
                StyledIconButton(
                    modifier = Modifier.padding(end = 5.dp),
                    imageVector = if (isPinned) Icons.Default.Bookmark else Icons.Default.BookmarkBorder
                ) {
                    onPinnedChange.invoke()
                }
            } else {
                Icon(
                    modifier = Modifier
                        .padding(end = 20.dp),
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null
                )
            }
        }
    }
}
