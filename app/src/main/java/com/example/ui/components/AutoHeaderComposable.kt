package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HeaderStyle
import com.example.model.UserProfile
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary

@Composable
fun AutoHeaderComposable(
  profile: UserProfile,
  style: HeaderStyle,
  modifier: Modifier = Modifier
) {
  if (style == HeaderStyle.NONE) return

  when (style) {
    HeaderStyle.AUSPICIOUS_SHREE_GANESH -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(Color(0xFF580C14).copy(alpha = 0.95f), Color(0xFF260408))
            )
          )
          .border(width = 1.dp, color = GoldAccent.copy(alpha = 0.8f))
          .padding(horizontal = 12.dp, vertical = 5.dp)
          .testTag("auto_header_ganesh")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("卐", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "॥ श्री गणेशाय नमः ॥  •  शुभ लाभ",
            color = GoldAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("卐", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    HeaderStyle.BRAND_HOTLINE_STRIP -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(Color(0xEE0F172A))
          .border(width = 1.dp, color = Color(0xFF38325E))
          .padding(horizontal = 10.dp, vertical = 4.dp)
          .testTag("auto_header_hotline")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = profile.businessName,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .background(EmeraldGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "📞 24x7: ${profile.phone}",
              color = EmeraldGreen,
              fontWeight = FontWeight.Bold,
              fontSize = 9.5.sp
            )
          }
        }
      }
    }

    HeaderStyle.OFFER_ANNOUNCEMENT -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFFE11D48), Color(0xFFEA580C), Color(0xFFE11D48))
            )
          )
          .border(width = 1.dp, color = Color(0xFFFFD54F))
          .padding(horizontal = 10.dp, vertical = 5.dp)
          .testTag("auto_header_offer")
      ) {
        Text(
          text = "⚡ SPECIAL FESTIVE OFFER • LIMITED TIME DEALS ⚡",
          color = Color.White,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 10.5.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    HeaderStyle.ROYAL_GOLD_CROWN -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(Color(0xFF1E1B4B).copy(alpha = 0.95f), Color(0xFF0F0C29))
            )
          )
          .border(width = 1.dp, color = GoldAccent)
          .padding(horizontal = 12.dp, vertical = 5.dp)
          .testTag("auto_header_crown")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("👑", fontSize = 11.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "ROYAL HERITAGE & FESTIVE CELEBRATION",
            color = GoldAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 10.5.sp,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("👑", fontSize = 11.sp)
        }
      }
    }

    HeaderStyle.SOCIAL_MEDIA_HANDLES -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(Color(0xEE111827))
          .border(width = 1.dp, color = Color(0xFF38BDF8).copy(alpha = 0.5f))
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .testTag("auto_header_social")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("💬 WhatsApp: ${profile.phone}", color = Color(0xFF38BDF8), fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
          Text("🌐 ${profile.website}", color = Color.White, fontSize = 8.5.sp)
        }
      }
    }

    HeaderStyle.MINIMAL_TAGLINE -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(Color(0xDD0F172A))
          .border(width = 0.8.dp, color = Color(0xFF334155))
          .padding(horizontal = 10.dp, vertical = 4.dp)
          .testTag("auto_header_tagline")
      ) {
        Text(
          text = "“${profile.tagLine}”",
          color = Color(0xFFCBD5E1),
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          fontSize = 9.5.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    HeaderStyle.NONE -> Unit
  }
}
