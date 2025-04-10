package com.devvikram.talkzy.ui.screens.groupchatroom.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devvikram.talkzy.ui.models.Member


@Composable
fun MembersGrid(members: List<Member>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in members.indices step 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in 0..2) {
                    val index = i + j
                    if (index < members.size) {
                        MemberCard(
                            member = members[index],
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}