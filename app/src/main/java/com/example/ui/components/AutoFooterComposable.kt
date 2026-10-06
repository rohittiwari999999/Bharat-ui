package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FooterStyle
import com.example.model.UserProfile
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary

@Composable
fun AutoFooterComposable(
  profile: UserProfile,
  style: FooterStyle,
  modifier: Modifier = Modifier
) {
  when (style) {
    FooterStyle.CLASSIC_ROYAL -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(Color(0xFF0F172A).copy(alpha = 0.95f), Color(0xFF070B19))
            )
          )
          .border(
            width = 1.5.dp,
            color = GoldAccent.copy(alpha = 0.8f)
          )
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("auto_footer_classic")
      ) {
        FooterContentRow(profile, textColor = Color.White, accentColor = GoldAccent)
      }
    }
    FooterStyle.SAFFRON_FESTIVE -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFE11D48))
            )
          )
          .border(width = 1.dp, color = Color(0xFFFFE082))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("auto_footer_saffron")
      ) {
        FooterContentRow(profile, textColor = Color.White, accentColor = Color(0xFFFFF176))
      }
    }
    FooterStyle.MODERN_DARK_GLASS -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(Color(0xEE111827))
          .border(width = 1.5.dp, color = Color(0xFF38BDF8).copy(alpha = 0.7f))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("auto_footer_glass")
      ) {
        FooterContentRow(profile, textColor = Color.White, accentColor = Color(0xFF38BDF8))
      }
    }
    FooterStyle.MINIMAL_ACCENT -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(Color(0xFFF8FAFC))
          .border(width = 1.dp, color = Color(0xFFCBD5E1))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("auto_footer_minimal")
      ) {
        FooterContentRow(profile, textColor = Color(0xFF0F172A), accentColor = SaffronPrimary)
      }
    }
    FooterStyle.DUAL_PILL -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .background(Color(0xEE1E1B4B), RoundedCornerShape(16.dp))
          .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("auto_footer_pill")
      ) {
        FooterContentRow(profile, textColor = Color.White, accentColor = GoldAccent)
      }
    }
    FooterStyle.GOLDEN_EMBOSSED -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFFB45309), Color(0xFFF59E0B), Color(0xFFD97706))
            )
          )
          .border(1.5.dp, Color(0xFFFFFBEB))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("auto_footer_golden")
      ) {
        FooterContentRow(profile, textColor = Color(0xFF0F172A), accentColor = Color(0xFF1E1B4B))
      }
    }
    FooterStyle.CORPORATE_STRIP -> {
      Box(
        modifier = modifier
          .fillMaxWidth()
          .background(Color(0xFF020617))
          .border(1.dp, Color(0xFF334155))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("auto_footer_corporate")
      ) {
        FooterContentRow(profile, textColor = Color.White, accentColor = SaffronPrimary)
      }
    }
  }
}

@Composable
private fun FooterContentRow(
  profile: UserProfile,
  textColor: Color,
  accentColor: Color
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Left: Profile Photo Avatar
    profile.photoRes?.let { photoRes ->
      Box(
        modifier = Modifier
          .size(46.dp)
          .border(2.dp, accentColor, CircleShape)
          .padding(2.dp)
          .clip(CircleShape)
      ) {
        Image(
          painter = painterResource(id = photoRes),
          contentDescription = "Profile Photo",
          modifier = Modifier.size(46.dp),
          contentScale = ContentScale.Crop
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    // Center: Details
    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = profile.businessName,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = "${profile.ownerName} • ${profile.designation}",
        color = textColor.copy(alpha = 0.85f),
        fontSize = 9.5.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "📞 ${profile.phone}",
          color = accentColor,
          fontWeight = FontWeight.SemiBold,
          fontSize = 9.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "🌐 ${profile.website}",
          color = textColor.copy(alpha = 0.75f),
          fontSize = 8.5.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // Right: Business Logo
    profile.logoRes?.let { logoRes ->
      Spacer(modifier = Modifier.width(6.dp))
      Box(
        modifier = Modifier
          .size(38.dp)
          .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
          .padding(2.dp)
      ) {
        Image(
          painter = painterResource(id = logoRes),
          contentDescription = "Business Logo",
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(6.dp)),
          contentScale = ContentScale.Fit
        )
      }
    }
  }
}
