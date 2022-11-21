package com.bnyro.translate.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bnyro.translate.db.obj.Language

@Composable
fun LanguageItem(
    language: Language,
    isPinned: Boolean,
    onPinnedChange: () -> Unit,
    onClick: () -> Unit
) {
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
            containerColor = Color.Transparent
        )

    ) {
        Row {
            Text(
                text = language.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
            )
            StyledIconButton(
                imageVector = if (isPinned) Icons.Default.Bookmark else Icons.Default.BookmarkBorder
            ) {
                onPinnedChange.invoke()
            }
        }
    }
}
