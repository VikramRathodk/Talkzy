package com.devvikram.talkzy.ui.screens.groupchatroom.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.ui.models.Member
import com.devvikram.talkzy.ui.reuseables.Avatar
import com.devvikram.talkzy.ui.reuseables.RoleBadge

@Composable
fun MemberCard(member: Member, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Avatar(url = member.imageUrl, size = 48.dp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = member.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            RoleBadge(role = member.role)
        }
    }
}
