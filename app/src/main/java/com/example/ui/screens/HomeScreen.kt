package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.R
import com.example.data.TemplateRepository
import com.example.model.OtaPatchInfo
import com.example.model.PosterTemplate
import com.example.model.TemplateCategory
import com.example.model.UserProfile
import com.example.ui.components.OtaUpdateDialog
import com.example.ui.theme.CardElevated
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PeacockCyan
import com.example.ui.theme.SaffronPrimary

import androidx.compose.material.icons.filled.Palette
import com.example.model.AppThemeMode
import com.example.ui.components.ThemeSelectorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  userProfile: UserProfile,
  appThemeMode: AppThemeMode,
  onSelectAppTheme: (AppThemeMode) -> Unit,
  onSelectTemplate: (PosterTemplate) -> Unit,
  onNavigateToProfile: () -> Unit
) {
  var selectedCategory by remember { mutableStateOf(TemplateCategory.ALL) }
  var showOtaDialog by remember { mutableStateOf(false) }
  var showThemeDialog by remember { mutableStateOf(false) }
  var otaPatchInfo by remember { mutableStateOf(OtaPatchInfo()) }

  val filteredTemplates = remember(selectedCategory) {
    if (selectedCategory == TemplateCategory.ALL) {
      TemplateRepository.sampleTemplates
    } else {
      TemplateRepository.sampleTemplates.filter { it.category == selectedCategory }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SaffronPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text("🎨", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text("PosterBanao", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.White)
              Text("Canva + AdBanao + PixelLab", fontSize = 9.5.sp, color = GoldAccent)
            }
          }
        },
        actions = {
          // Theme Switcher Button
          IconButton(
            onClick = { showThemeDialog = true },
            modifier = Modifier.testTag("home_theme_button")
          ) {
            Icon(Icons.Default.Palette, contentDescription = "Change Theme", tint = GoldAccent)
          }

          // Shorebird OTA Status Badge
          Box(
            modifier = Modifier
              .clickable { showOtaDialog = true }
              .background(PeacockCyan.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
              .border(1.dp, PeacockCyan.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("ota_status_badge")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CloudDone, contentDescription = null, tint = PeacockCyan, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Shorebird OTA", fontSize = 10.sp, color = PeacockCyan, fontWeight = FontWeight.Bold)
            }
          }
          Spacer(modifier = Modifier.width(8.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF131127))
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          // Open default diwali or first template
          TemplateRepository.sampleTemplates.firstOrNull()?.let { onSelectTemplate(it) }
        },
        containerColor = SaffronPrimary,
        contentColor = Color.White,
        modifier = Modifier.testTag("fab_create_poster")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Palette, contentDescription = "Create Poster")
          Spacer(modifier = Modifier.width(6.dp))
          Text("Design Poster", fontWeight = FontWeight.Bold)
        }
      }
    },
    containerColor = Color(0xFF0F0C20)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // 1. Personal Branding Hero Card (AdBanao Style)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp)
          .testTag("branding_hero_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1633)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38325E))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // User Avatar & Brand info
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            userProfile.photoRes?.let { photo ->
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .border(1.5.dp, GoldAccent, CircleShape)
                  .padding(2.dp)
                  .clip(CircleShape)
              ) {
                Image(
                  painter = painterResource(id = photo),
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = userProfile.businessName,
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.5.sp,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
              }
              Text(
                text = "${userProfile.ownerName} • 📞 ${userProfile.phone}",
                color = Color(0xFFCBD5E1),
                fontSize = 10.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "⚡ Auto-Footer Branding Ready",
                color = EmeraldGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
              )
            }
          }

          // Edit Branding Button
          Box(
            modifier = Modifier
              .clickable { onNavigateToProfile() }
              .background(SaffronPrimary.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
              .border(1.dp, SaffronPrimary, RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("edit_branding_btn")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = SaffronPrimary, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Edit", color = SaffronPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // 2. Category Filter Tabs
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(TemplateCategory.entries) { category ->
          val isSelected = category == selectedCategory
          Box(
            modifier = Modifier
              .background(
                if (isSelected) SaffronPrimary else Color(0xFF1E1A38),
                RoundedCornerShape(20.dp)
              )
              .border(
                1.dp,
                if (isSelected) GoldAccent else Color(0xFF38325E),
                RoundedCornerShape(20.dp)
              )
              .clickable { selectedCategory = category }
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = category.title,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // 3. Grid of Poster Templates
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 10.dp),
        contentPadding = PaddingValues(bottom = 80.dp, top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(filteredTemplates) { template ->
          PosterCard(
            template = template,
            userProfile = userProfile,
            onClick = { onSelectTemplate(template) }
          )
        }
      }
    }
  }

  if (showOtaDialog) {
    OtaUpdateDialog(
      currentPatchInfo = otaPatchInfo,
      onPatchApplied = {
        otaPatchInfo = otaPatchInfo.copy(
          patchVersionName = "v2.4.3 (Latest Patch)",
          lastUpdated = "Just now"
        )
      },
      onDismiss = { showOtaDialog = false }
    )
  }

  if (showThemeDialog) {
    ThemeSelectorDialog(
      currentAppTheme = appThemeMode,
      onSelectAppTheme = onSelectAppTheme,
      onDismiss = { showThemeDialog = false }
    )
  }
}

@Composable
private fun PosterCard(
  template: PosterTemplate,
  userProfile: UserProfile,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("template_card_${template.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A38)),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF28234B))
  ) {
    Column {
      // Poster Image Preview with Aspect Ratio 3:4
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(3f / 4f)
      ) {
        Image(
          painter = painterResource(id = template.bgDrawableRes),
          contentDescription = template.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay at bottom
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .align(Alignment.BottomCenter)
            .background(
              Brush.verticalGradient(
                listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
              )
            )
        )

        // Auto-Branded Badge at top left
        Box(
          modifier = Modifier
            .padding(6.dp)
            .align(Alignment.TopStart)
            .background(Color(0xDD0F172A), RoundedCornerShape(6.dp))
            .border(0.8.dp, GoldAccent, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨ Auto-Branded", fontSize = 9.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
          }
        }

        // Mini preview of auto footer at bottom of card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(Color(0xEE0B132B))
            .border(0.5.dp, GoldAccent.copy(alpha = 0.5f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = userProfile.businessName,
              fontSize = 7.5.sp,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
            Text(
              text = userProfile.phone,
              fontSize = 7.sp,
              color = GoldAccent,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Title & Action
      Column(modifier = Modifier.padding(10.dp)) {
        Text(
          text = template.title,
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = template.subTitle,
          fontSize = 10.sp,
          color = Color(0xFF94A3B8),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
