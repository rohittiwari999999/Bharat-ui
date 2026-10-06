package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppThemeMode
import com.example.model.PosterColorTheme
import com.example.model.PresetColorThemes
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary

@Composable
fun ThemeSelectorDialog(
  currentAppTheme: AppThemeMode,
  onSelectAppTheme: (AppThemeMode) -> Unit,
  onApplyPosterTheme: ((PosterColorTheme) -> Unit)? = null,
  onDismiss: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF1E1A38),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38325E)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("theme_selector_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Palette, contentDescription = null, tint = GoldAccent)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Theme Settings",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = Color.White
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (onApplyPosterTheme != null) {
          TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF131127),
            contentColor = Color.White,
            indicator = { tabPositions ->
              TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                color = SaffronPrimary,
                height = 2.5.dp
              )
            }
          ) {
            Tab(
              selected = selectedTab == 0,
              onClick = { selectedTab = 0 },
              text = { Text("App UI Theme", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
              selected = selectedTab == 1,
              onClick = { selectedTab = 1 },
              text = { Text("Poster Palette", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
          }
          Spacer(modifier = Modifier.height(14.dp))
        }

        if (selectedTab == 0 || onApplyPosterTheme == null) {
          // App UI Themes
          Text(
            text = "Choose App Look & Feel:",
            fontSize = 12.sp,
            color = GoldAccent,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(8.dp))

          AppThemeMode.entries.forEach { themeMode ->
            val isSelected = themeMode == currentAppTheme
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(
                  if (isSelected) SaffronPrimary.copy(alpha = 0.2f) else Color(0xFF131127),
                  RoundedCornerShape(12.dp)
                )
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) Color(themeMode.primaryColorHex) else Color(0xFF28234B),
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                  onSelectAppTheme(themeMode)
                }
                .padding(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(Color(themeMode.primaryColorHex))
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = themeMode.title,
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp,
                      color = Color.White
                    )
                    Text(
                      text = themeMode.subtitle,
                      fontSize = 11.sp,
                      color = Color(0xFF94A3B8)
                    )
                  }
                }
                if (isSelected) {
                  Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }
        } else {
          // Poster Color Palette Themes
          Text(
            text = "1-Tap Color Harmonies for Current Poster:",
            fontSize = 12.sp,
            color = GoldAccent,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Instantly recolors all typography, 3D shadows, and branding footer.",
            fontSize = 10.5.sp,
            color = Color.Gray
          )
          Spacer(modifier = Modifier.height(8.dp))

          LazyColumn(modifier = Modifier.height(260.dp)) {
            items(PresetColorThemes) { palette ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .background(Color(0xFF131127), RoundedCornerShape(12.dp))
                  .border(1.dp, Color(0xFF28234B), RoundedCornerShape(12.dp))
                  .clickable {
                    onApplyPosterTheme(palette)
                    onDismiss()
                  }
                  .padding(12.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = palette.name,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = Color.White
                    )
                    Text(
                      text = "Footer: ${palette.footerStyle.displayName}",
                      fontSize = 10.sp,
                      color = Color.LightGray
                    )
                  }

                  // Color Swatches
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    palette.previewColors.forEach { colorHex ->
                      Box(
                        modifier = Modifier
                          .size(20.dp)
                          .clip(CircleShape)
                          .background(Color(colorHex))
                          .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
