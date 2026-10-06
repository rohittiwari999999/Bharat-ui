package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TemplateRepository
import com.example.export.HighResPosterExporter
import com.example.model.AppThemeMode
import com.example.model.BorderStyle
import com.example.model.ExportResolutions
import com.example.model.FontCategory
import com.example.model.FooterStyle
import com.example.model.HeaderStyle
import com.example.model.PosterBackground
import com.example.model.PosterColorTheme
import com.example.model.PosterTemplate
import com.example.model.PresetColorThemes
import com.example.model.StickerLayer
import com.example.model.StickerType
import com.example.model.TextLayer
import com.example.model.UserProfile
import com.example.ui.components.AiQuoteDialog
import com.example.ui.components.CanvasView
import com.example.ui.components.ExportSuccessDialog
import com.example.ui.components.ThemeSelectorDialog
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
  template: PosterTemplate,
  userProfile: UserProfile,
  appThemeMode: AppThemeMode,
  onSelectAppTheme: (AppThemeMode) -> Unit,
  onNavigateToProfile: () -> Unit,
  onBack: () -> Unit
) {
  BackHandler { onBack() }

  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  var activeBackground by remember {
    mutableStateOf<PosterBackground>(
      PosterBackground.DrawableBackground(template.bgDrawableRes, template.title)
    )
  }
  var textLayers by remember { mutableStateOf(template.defaultTexts) }
  var stickers by remember { mutableStateOf(template.defaultStickers) }
  var selectedLayerId by remember { mutableStateOf(textLayers.firstOrNull()?.id) }
  var showHeader by remember { mutableStateOf(false) }
  var headerStyle by remember { mutableStateOf(HeaderStyle.AUSPICIOUS_SHREE_GANESH) }
  var showFooter by remember { mutableStateOf(true) }
  var footerStyle by remember { mutableStateOf(FooterStyle.CLASSIC_ROYAL) }
  var borderStyle by remember { mutableStateOf(BorderStyle.NONE) }

  // Bottom dock tabs: 0: Text/3D, 1: Background, 2: Header, 3: Footer, 4: Border, 5: Themes, 6: Stickers, 7: AI Quotes, 8: Layers
  var selectedTab by remember { mutableIntStateOf(0) }

  // Dialogs
  var showAiQuotesDialog by remember { mutableStateOf(false) }
  var showExportDialog by remember { mutableStateOf(false) }
  var showThemeDialog by remember { mutableStateOf(false) }
  var exportConfig by remember { mutableStateOf(ExportResolutions.first()) }
  var isExporting by remember { mutableStateOf(false) }
  var exportResult by remember { mutableStateOf<HighResPosterExporter.ExportResult?>(null) }
  var editTextDialogLayerId by remember { mutableStateOf<String?>(null) }

  // Zero-permission Android Photo Picker for Custom Backgrounds
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = { uri ->
      if (uri != null) {
        activeBackground = PosterBackground.UriBackground(uri.toString(), "Custom Gallery Photo")
      }
    }
  )

  val selectedTextLayer = textLayers.find { it.id == selectedLayerId }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(template.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("PixelLab Pro Engine • 300 DPI Export", fontSize = 10.sp, color = GoldAccent)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        actions = {
          IconButton(
            onClick = { showThemeDialog = true },
            modifier = Modifier.testTag("open_theme_dialog_btn")
          ) {
            Icon(Icons.Default.Palette, contentDescription = "Change Theme", tint = GoldAccent)
          }
          IconButton(
            onClick = { showAiQuotesDialog = true },
            modifier = Modifier.testTag("open_ai_quotes_btn")
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "Gemini AI Quotes", tint = GoldAccent)
          }
          Button(
            onClick = {
              exportResult = null
              showExportDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .padding(end = 8.dp)
              .testTag("export_top_btn")
          ) {
            Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Export HD", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF131127))
      )
    },
    containerColor = Color(0xFF0B091A)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // 1. Live Interactive Canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
      ) {
        CanvasView(
          background = activeBackground,
          textLayers = textLayers,
          stickers = stickers,
          userProfile = userProfile,
          showHeader = showHeader,
          headerStyle = headerStyle,
          showFooter = showFooter,
          footerStyle = footerStyle,
          borderStyle = borderStyle,
          selectedLayerId = selectedLayerId,
          onSelectLayer = { selectedLayerId = it },
          onMoveTextLayer = { id, newX, newY ->
            textLayers = textLayers.map {
              if (it.id == id) it.copy(xRatio = newX, yRatio = newY) else it
            }
          },
          onMoveStickerLayer = { id, newX, newY ->
            stickers = stickers.map {
              if (it.id == id) it.copy(xRatio = newX, yRatio = newY) else it
            }
          }
        )
      }

      // 2. PixelLab Bottom Tool Dock
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF15122B))
          .border(androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF28234B)))
      ) {
        ScrollableTabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color(0xFF15122B),
          contentColor = Color.White,
          edgePadding = 10.dp,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = SaffronPrimary,
              height = 3.dp
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Text & 3D", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.TextFields, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Background", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            text = { Text("Header", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.ViewHeadline, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 3,
            onClick = { selectedTab = 3 },
            text = { Text("Footer", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 4,
            onClick = { selectedTab = 4 },
            text = { Text("Border", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.CropSquare, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 5,
            onClick = { selectedTab = 5 },
            text = { Text("Themes", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 6,
            onClick = { selectedTab = 6 },
            text = { Text("Stickers", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.Mood, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 7,
            onClick = { selectedTab = 7 },
            text = { Text("AI Quotes", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
          Tab(
            selected = selectedTab == 8,
            onClick = { selectedTab = 8 },
            text = { Text("Layers", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            icon = { Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp)) }
          )
        }

        // Tab Content
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(12.dp)
        ) {
          when (selectedTab) {
            0 -> TextToolPanel(
              selectedLayer = selectedTextLayer,
              onAddText = {
                val newLayer = TextLayer(
                  text = "नया टेक्स्ट (Add Your Text)",
                  xRatio = 0.5f,
                  yRatio = 0.5f,
                  fontSizeSp = 24f,
                  textColorHex = 0xFFFFD700,
                  is3D = true,
                  depth3D = 6f
                )
                textLayers = textLayers + newLayer
                selectedLayerId = newLayer.id
              },
              onEditText = { editTextDialogLayerId = selectedTextLayer?.id },
              onUpdateLayer = { updated ->
                textLayers = textLayers.map { if (it.id == updated.id) updated else it }
              }
            )
            1 -> BackgroundToolPanel(
              currentBackground = activeBackground,
              onSelectBackground = { activeBackground = it },
              onPickGallery = {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              }
            )
            2 -> HeaderToolPanel(
              showHeader = showHeader,
              onToggleHeader = { showHeader = it },
              selectedStyle = headerStyle,
              onSelectStyle = { headerStyle = it },
              userProfile = userProfile
            )
            3 -> FooterToolPanel(
              showFooter = showFooter,
              onToggleFooter = { showFooter = it },
              selectedStyle = footerStyle,
              onSelectStyle = { footerStyle = it },
              userProfile = userProfile,
              onNavigateToProfile = onNavigateToProfile
            )
            4 -> BorderToolPanel(
              selectedStyle = borderStyle,
              onSelectStyle = { borderStyle = it }
            )
            5 -> ThemesToolPanel(
              currentAppTheme = appThemeMode,
              onSelectAppTheme = onSelectAppTheme,
              onApplyPosterTheme = { theme ->
                footerStyle = theme.footerStyle
                textLayers = textLayers.mapIndexed { idx, layer ->
                  if (idx == 0) {
                    layer.copy(
                      textColorHex = theme.primaryTextHex,
                      color3DHex = theme.color3DHex,
                      shadowColorHex = theme.shadowColorHex
                    )
                  } else {
                    layer.copy(
                      textColorHex = theme.secondaryTextHex,
                      color3DHex = theme.color3DHex,
                      shadowColorHex = theme.shadowColorHex
                    )
                  }
                }
              }
            )
            6 -> StickersPanel(
              onAddSticker = { type ->
                val newSticker = StickerLayer(
                  stickerType = type,
                  xRatio = 0.5f,
                  yRatio = 0.4f,
                  scale = 1.1f
                )
                stickers = stickers + newSticker
                selectedLayerId = newSticker.id
              }
            )
            7 -> AiMagicTabPanel(
              template = template,
              onSelectQuote = { quote ->
                val newLayer = TextLayer(
                  text = quote,
                  xRatio = 0.5f,
                  yRatio = 0.45f,
                  fontSizeSp = 18f,
                  textColorHex = 0xFFFFFFFF,
                  hasShadow = true
                )
                textLayers = textLayers + newLayer
                selectedLayerId = newLayer.id
              },
              onOpenFullAiDialog = { showAiQuotesDialog = true }
            )
            8 -> LayersPanel(
              textLayers = textLayers,
              stickers = stickers,
              selectedId = selectedLayerId,
              onSelect = { selectedLayerId = it },
              onDelete = { id ->
                textLayers = textLayers.filter { it.id != id }
                stickers = stickers.filter { it.id != id }
                if (selectedLayerId == id) selectedLayerId = textLayers.firstOrNull()?.id
              },
              onDuplicate = { id ->
                val textToDup = textLayers.find { it.id == id }
                if (textToDup != null) {
                  val dup = textToDup.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    yRatio = (textToDup.yRatio + 0.05f).coerceAtMost(0.8f)
                  )
                  textLayers = textLayers + dup
                  selectedLayerId = dup.id
                }
              }
            )
          }
        }
      }
    }
  }

  // Edit Text Dialog
  if (editTextDialogLayerId != null) {
    val layerToEdit = textLayers.find { it.id == editTextDialogLayerId }
    var textInput by remember { mutableStateOf(layerToEdit?.text ?: "") }

    AlertDialog(
      onDismissRequest = { editTextDialogLayerId = null },
      title = { Text("Edit Text (Hindi / English)", color = Color.White, fontWeight = FontWeight.Bold) },
      text = {
        OutlinedTextField(
          value = textInput,
          onValueChange = { textInput = it },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SaffronPrimary,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        )
      },
      confirmButton = {
        Button(
          onClick = {
            if (layerToEdit != null) {
              textLayers = textLayers.map {
                if (it.id == layerToEdit.id) it.copy(text = textInput) else it
              }
            }
            editTextDialogLayerId = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
        ) {
          Text("Apply")
        }
      },
      dismissButton = {
        TextButton(onClick = { editTextDialogLayerId = null }) {
          Text("Cancel", color = Color.LightGray)
        }
      },
      containerColor = Color(0xFF1E1A38)
    )
  }

  // Theme Selector Dialog
  if (showThemeDialog) {
    ThemeSelectorDialog(
      currentAppTheme = appThemeMode,
      onSelectAppTheme = onSelectAppTheme,
      onApplyPosterTheme = { theme ->
        footerStyle = theme.footerStyle
        textLayers = textLayers.mapIndexed { idx, layer ->
          if (idx == 0) {
            layer.copy(
              textColorHex = theme.primaryTextHex,
              color3DHex = theme.color3DHex,
              shadowColorHex = theme.shadowColorHex
            )
          } else {
            layer.copy(
              textColorHex = theme.secondaryTextHex,
              color3DHex = theme.color3DHex,
              shadowColorHex = theme.shadowColorHex
            )
          }
        }
      },
      onDismiss = { showThemeDialog = false }
    )
  }

  // AI Quotes Dialog
  if (showAiQuotesDialog) {
    AiQuoteDialog(
      initialCategory = template.category.title,
      businessName = userProfile.businessName,
      onQuoteSelected = { quote ->
        val newLayer = TextLayer(
          text = quote,
          xRatio = 0.5f,
          yRatio = 0.45f,
          fontSizeSp = 18f,
          textColorHex = 0xFFFFD700,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          hasShadow = true
        )
        textLayers = textLayers + newLayer
        selectedLayerId = newLayer.id
      },
      onDismiss = { showAiQuotesDialog = false }
    )
  }

  // Export Dialog
  if (showExportDialog) {
    ExportSuccessDialog(
      exportResult = exportResult,
      isExporting = isExporting,
      selectedConfig = exportConfig,
      onSelectConfig = { exportConfig = it },
      onStartExport = {
        scope.launch {
          isExporting = true
          val res = HighResPosterExporter.renderAndSavePoster(
            context = context,
            background = activeBackground,
            textLayers = textLayers,
            stickers = stickers,
            userProfile = userProfile,
            showHeader = showHeader,
            headerStyle = headerStyle,
            showFooter = showFooter,
            footerStyle = footerStyle,
            borderStyle = borderStyle,
            exportConfig = exportConfig
          )
          exportResult = res
          isExporting = false
        }
      },
      onDismiss = { showExportDialog = false }
    )
  }
}

@Composable
private fun BackgroundToolPanel(
  currentBackground: PosterBackground,
  onSelectBackground: (PosterBackground) -> Unit,
  onPickGallery: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    // 1. Pick from Gallery Button
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("Change Poster Background", fontSize = 12.5.sp, color = GoldAccent, fontWeight = FontWeight.Bold)

      Button(
        onClick = onPickGallery,
        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("pick_gallery_bg_btn")
      ) {
        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("From Gallery", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 2. Preset HD Festive Wallpapers
    Text("Indian Festival & Business Backdrops:", fontSize = 11.sp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(6.dp))

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(TemplateRepository.presetBackgrounds) { bgPreset ->
        val isChosen = currentBackground is PosterBackground.DrawableBackground &&
            currentBackground.resId == bgPreset.resId

        Box(
          modifier = Modifier
            .width(80.dp)
            .height(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
              width = if (isChosen) 2.5.dp else 1.dp,
              color = if (isChosen) GoldAccent else Color(0xFF38325E),
              shape = RoundedCornerShape(8.dp)
            )
            .clickable { onSelectBackground(bgPreset) }
        ) {
          Image(
            painter = painterResource(id = bgPreset.resId),
            contentDescription = bgPreset.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter)
              .background(Color.Black.copy(alpha = 0.7f))
              .padding(2.dp)
          ) {
            Text(
              text = bgPreset.name,
              fontSize = 9.sp,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
          }
          if (isChosen) {
            Icon(
              Icons.Default.Check,
              contentDescription = null,
              tint = GoldAccent,
              modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
                .padding(2.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 3. Rich Gradients
    Text("Festive Indian Color Gradients:", fontSize = 11.sp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(6.dp))

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(TemplateRepository.presetGradients) { gradPreset ->
        val isChosen = currentBackground is PosterBackground.GradientBackground &&
            currentBackground.name == gradPreset.name

        val brush = Brush.horizontalGradient(gradPreset.colorsHex.map { Color(it) })
        Box(
          modifier = Modifier
            .width(100.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(brush)
            .border(
              width = if (isChosen) 2.5.dp else 1.dp,
              color = if (isChosen) GoldAccent else Color.White.copy(alpha = 0.3f),
              shape = RoundedCornerShape(8.dp)
            )
            .clickable { onSelectBackground(gradPreset) }
            .padding(4.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = gradPreset.name.split(" ").take(2).joinToString(" "),
            fontSize = 9.5.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 4. Solid Canvas Colors
    Text("Solid Color Canvas:", fontSize = 11.sp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(6.dp))

    val solids = listOf(
      0xFF0F172A to "Navy",
      0xFF000000 to "Black",
      0xFFEA580C to "Saffron",
      0xFF991B1B to "Crimson",
      0xFF064E3B to "Emerald",
      0xFF431407 to "Maroon",
      0xFFFFFFFF to "White"
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      solids.forEach { (colorHex, name) ->
        val isChosen = currentBackground is PosterBackground.GradientBackground &&
            currentBackground.colorsHex == listOf(colorHex, colorHex)

        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color(colorHex))
            .border(
              width = if (isChosen) 2.5.dp else 1.dp,
              color = if (isChosen) GoldAccent else Color.Gray,
              shape = CircleShape
            )
            .clickable {
              onSelectBackground(
                PosterBackground.GradientBackground(
                  name = "Solid $name",
                  colorsHex = listOf(colorHex, colorHex)
                )
              )
            }
        )
      }
    }
  }
}

@Composable
private fun ThemesToolPanel(
  currentAppTheme: AppThemeMode,
  onSelectAppTheme: (AppThemeMode) -> Unit,
  onApplyPosterTheme: (PosterColorTheme) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    // 1-Tap Poster Color Palettes
    Text(
      text = "⚡ 1-Tap Poster Harmonious Palettes",
      fontSize = 12.5.sp,
      color = GoldAccent,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Recolors all typography, 3D depths, and footer style simultaneously.",
      fontSize = 10.sp,
      color = Color.LightGray
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(PresetColorThemes) { palette ->
        Box(
          modifier = Modifier
            .width(140.dp)
            .background(Color(0xFF1E1A38), RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFF38325E), RoundedCornerShape(10.dp))
            .clickable { onApplyPosterTheme(palette) }
            .padding(8.dp)
        ) {
          Column {
            Text(
              text = palette.name,
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              palette.previewColors.forEach { cHex ->
                Box(
                  modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color(cHex))
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Tap to apply",
              fontSize = 9.sp,
              color = SaffronPrimary,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // App UI Theme Switcher
    Text(
      text = "📱 App UI Themes (Change Overall Look):",
      fontSize = 12.sp,
      color = GoldAccent,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(6.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      AppThemeMode.entries.forEach { mode ->
        val isCurrent = mode == currentAppTheme
        Box(
          modifier = Modifier
            .weight(1f)
            .background(
              if (isCurrent) SaffronPrimary.copy(alpha = 0.3f) else Color(0xFF1E1A38),
              RoundedCornerShape(8.dp)
            )
            .border(
              1.5.dp,
              if (isCurrent) Color(mode.primaryColorHex) else Color(0xFF28234B),
              RoundedCornerShape(8.dp)
            )
            .clickable { onSelectAppTheme(mode) }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(Color(mode.primaryColorHex))
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = mode.title.split(" ").first(),
              fontSize = 10.sp,
              fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
private fun TextToolPanel(
  selectedLayer: TextLayer?,
  onAddText: () -> Unit,
  onEditText: () -> Unit,
  onUpdateLayer: (TextLayer) -> Unit
) {
  if (selectedLayer == null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Button(
        onClick = onAddText,
        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("add_text_button")
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Add New Text", fontWeight = FontWeight.Bold)
      }
    }
    return
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    // Quick action buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        onClick = onEditText,
        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f).testTag("edit_text_string_button")
      ) {
        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Edit Words", fontSize = 12.sp)
      }

      Button(
        onClick = onAddText,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28234B)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("New Text", fontSize = 12.sp)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 3D Extrusion Control (PixelLab Style)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Layers, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("PixelLab 3D Extrusion Effect", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
      }
      Switch(
        checked = selectedLayer.is3D,
        onCheckedChange = { onUpdateLayer(selectedLayer.copy(is3D = it)) },
        colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary, checkedTrackColor = GoldAccent)
      )
    }

    if (selectedLayer.is3D) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Depth: ${selectedLayer.depth3D.toInt()}px", fontSize = 11.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.width(8.dp))
        Slider(
          value = selectedLayer.depth3D,
          onValueChange = { onUpdateLayer(selectedLayer.copy(depth3D = it)) },
          valueRange = 2f..15f,
          colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = SaffronPrimary),
          modifier = Modifier.weight(1f)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Font Size Slider
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(Icons.Default.FormatSize, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("Size: ${selectedLayer.fontSizeSp.toInt()}sp", fontSize = 11.sp, color = Color.White)
      Spacer(modifier = Modifier.width(8.dp))
      Slider(
        value = selectedLayer.fontSizeSp,
        onValueChange = { onUpdateLayer(selectedLayer.copy(fontSizeSp = it)) },
        valueRange = 12f..52f,
        colors = SliderDefaults.colors(thumbColor = SaffronPrimary, activeTrackColor = SaffronPrimary),
        modifier = Modifier.weight(1f)
      )
    }

    // Color Presets
    Text("Text Colors:", fontSize = 11.sp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(4.dp))
    val colors = listOf(
      0xFFFFFFFF, 0xFFFFD700, 0xFFFF7A00, 0xFFE11D48, 0xFF06B6D4, 0xFF10B981, 0xFFFBBF24, 0xFF000000
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      colors.forEach { hex ->
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color(hex))
            .border(
              width = if (selectedLayer.textColorHex == hex) 2.5.dp else 1.dp,
              color = if (selectedLayer.textColorHex == hex) GoldAccent else Color.Gray,
              shape = CircleShape
            )
            .clickable { onUpdateLayer(selectedLayer.copy(textColorHex = hex)) }
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Font Family Presets
    Text("Devanagari & Typography:", fontSize = 11.sp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(4.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      items(FontCategory.entries) { fontCat ->
        val isChosen = selectedLayer.fontCategory == fontCat
        Box(
          modifier = Modifier
            .background(if (isChosen) SaffronPrimary else Color(0xFF1E1A38), RoundedCornerShape(8.dp))
            .border(1.dp, if (isChosen) GoldAccent else Color(0xFF38325E), RoundedCornerShape(8.dp))
            .clickable { onUpdateLayer(selectedLayer.copy(fontCategory = fontCat)) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(fontCat.label, fontSize = 11.sp, color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun HeaderToolPanel(
  showHeader: Boolean,
  onToggleHeader: (Boolean) -> Unit,
  selectedStyle: HeaderStyle,
  onSelectStyle: (HeaderStyle) -> Unit,
  userProfile: UserProfile
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("Top Header Ribbon", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
        Text("Traditional Sanskrit, Auspicious, Hotline or Offer bar", color = Color.Gray, fontSize = 10.sp)
      }
      Switch(
        checked = showHeader,
        onCheckedChange = onToggleHeader,
        colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary, checkedTrackColor = GoldAccent),
        modifier = Modifier.testTag("toggle_header_switch")
      )
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Choose Header Template:", fontSize = 11.5.sp, color = GoldAccent, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(6.dp))

    HeaderStyle.entries.forEach { style ->
      val isSelected = style == selectedStyle && (showHeader || style == HeaderStyle.NONE)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) SaffronPrimary.copy(alpha = 0.25f) else Color(0xFF1E1A38))
          .border(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) GoldAccent else Color(0xFF28234B),
            shape = RoundedCornerShape(8.dp)
          )
          .clickable {
            onSelectStyle(style)
            onToggleHeader(style != HeaderStyle.NONE)
          }
          .padding(8.dp)
          .testTag("header_template_${style.name}")
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                when (style) {
                  HeaderStyle.NONE -> "🚫"
                  HeaderStyle.AUSPICIOUS_SHREE_GANESH -> "卐"
                  HeaderStyle.BRAND_HOTLINE_STRIP -> "📞"
                  HeaderStyle.OFFER_ANNOUNCEMENT -> "⚡"
                  HeaderStyle.ROYAL_GOLD_CROWN -> "👑"
                  HeaderStyle.SOCIAL_MEDIA_HANDLES -> "💬"
                  HeaderStyle.MINIMAL_TAGLINE -> "✨"
                },
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                style.displayName,
                fontSize = 12.sp,
                color = if (isSelected) GoldAccent else Color.White,
                fontWeight = FontWeight.Bold
              )
            }
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
            }
          }
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            style.description,
            fontSize = 9.5.sp,
            color = if (isSelected) Color(0xFFFDE68A) else Color.LightGray,
            maxLines = 2
          )
        }
      }
    }
  }
}

@Composable
private fun FooterToolPanel(
  showFooter: Boolean,
  onToggleFooter: (Boolean) -> Unit,
  selectedStyle: FooterStyle,
  onSelectStyle: (FooterStyle) -> Unit,
  userProfile: UserProfile,
  onNavigateToProfile: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text("Smart Auto-Footer (AdBanao)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
        Text("Appends your photo, logo & contact info", color = Color.Gray, fontSize = 10.sp)
      }
      Switch(
        checked = showFooter,
        onCheckedChange = onToggleFooter,
        colors = SwitchDefaults.colors(checkedThumbColor = SaffronPrimary, checkedTrackColor = GoldAccent),
        modifier = Modifier.testTag("toggle_footer_switch")
      )
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Choose Footer Template:", fontSize = 11.5.sp, color = GoldAccent, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(6.dp))

    FooterStyle.entries.forEach { style ->
      val isSelected = style == selectedStyle && showFooter
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) SaffronPrimary.copy(alpha = 0.25f) else Color(0xFF1E1A38))
          .border(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) GoldAccent else Color(0xFF28234B),
            shape = RoundedCornerShape(8.dp)
          )
          .clickable {
            onSelectStyle(style)
            onToggleFooter(true)
          }
          .padding(8.dp)
          .testTag("footer_template_${style.name}")
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                when (style) {
                  FooterStyle.CLASSIC_ROYAL -> "👑"
                  FooterStyle.SAFFRON_FESTIVE -> "🪔"
                  FooterStyle.MODERN_DARK_GLASS -> "💎"
                  FooterStyle.MINIMAL_ACCENT -> "⚪"
                  FooterStyle.DUAL_PILL -> "💊"
                  FooterStyle.GOLDEN_EMBOSSED -> "✨"
                  FooterStyle.CORPORATE_STRIP -> "💼"
                },
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                style.displayName,
                fontSize = 12.sp,
                color = if (isSelected) GoldAccent else Color.White,
                fontWeight = FontWeight.Bold
              )
            }
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
            }
          }
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            style.description,
            fontSize = 9.5.sp,
            color = if (isSelected) Color(0xFFFDE68A) else Color.LightGray,
            maxLines = 2
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Button(
      onClick = onNavigateToProfile,
      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28234B)),
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("edit_profile_from_editor_btn")
    ) {
      Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("Edit Business Details (${userProfile.businessName})", fontSize = 11.sp)
    }
  }
}

@Composable
private fun BorderToolPanel(
  selectedStyle: BorderStyle,
  onSelectStyle: (BorderStyle) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Text("Decorative Poster Frames & Borders", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
    Text("Adds ornate framing, gold corners or modern neon trims", color = Color.Gray, fontSize = 10.sp)

    Spacer(modifier = Modifier.height(8.dp))
    Text("Choose Border Template:", fontSize = 11.5.sp, color = GoldAccent, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(6.dp))

    BorderStyle.entries.forEach { style ->
      val isSelected = style == selectedStyle
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) SaffronPrimary.copy(alpha = 0.25f) else Color(0xFF1E1A38))
          .border(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) GoldAccent else Color(0xFF28234B),
            shape = RoundedCornerShape(8.dp)
          )
          .clickable { onSelectStyle(style) }
          .padding(8.dp)
          .testTag("border_template_${style.name}")
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                when (style) {
                  BorderStyle.NONE -> "🚫"
                  BorderStyle.ROYAL_GOLD_FRAME -> "🖼️"
                  BorderStyle.DOUBLE_GOLDEN_LINE -> "🔲"
                  BorderStyle.FESTIVE_SAFFRON_TRIM -> "🎗️"
                  BorderStyle.VINTAGE_CORNER_FLOURISH -> "⚜️"
                  BorderStyle.NEON_CYAN_GLOW -> "💠"
                  BorderStyle.PEARL_BEADED_BORDER -> "📿"
                },
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                style.displayName,
                fontSize = 12.sp,
                color = if (isSelected) GoldAccent else Color.White,
                fontWeight = FontWeight.Bold
              )
            }
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
            }
          }
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            style.description,
            fontSize = 9.5.sp,
            color = if (isSelected) Color(0xFFFDE68A) else Color.LightGray,
            maxLines = 2
          )
        }
      }
    }
  }
}

@Composable
private fun StickersPanel(
  onAddSticker: (StickerType) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Text("Tap to add Indian Festive & Business Badges:", fontSize = 12.sp, color = GoldAccent)
    Spacer(modifier = Modifier.height(8.dp))

    val stickers = StickerType.entries
    val chunked = stickers.chunked(4)

    chunked.forEach { rowStickers ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        rowStickers.forEach { sticker ->
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .clickable { onAddSticker(sticker) }
              .background(Color(0xFF1E1A38), RoundedCornerShape(10.dp))
              .border(1.dp, Color(0xFF28234B), RoundedCornerShape(10.dp))
              .padding(8.dp)
              .width(65.dp)
          ) {
            Text(sticker.emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = sticker.label,
              fontSize = 9.sp,
              color = Color.White,
              maxLines = 1,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AiMagicTabPanel(
  template: PosterTemplate,
  onSelectQuote: (String) -> Unit,
  onOpenFullAiDialog: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("AI Generated Festival Shayaris:", fontSize = 12.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
      TextButton(onClick = onOpenFullAiDialog) {
        Text("Custom Prompt >", fontSize = 11.sp, color = SaffronPrimary)
      }
    }

    val quotes = when (template.category) {
      com.example.model.TemplateCategory.FESTIVALS -> listOf(
        "दीपों का यह पावन पर्व आपके जीवन में सुख, शांति और समृद्धि लेकर आए।",
        "रंगों और खुशियों से महके आपका संसार। हार्दिक शुभकामनाएं!",
        "May the divine light illuminate your path with boundless success."
      )
      com.example.model.TemplateCategory.WEDDING -> listOf(
        "मंगलम् भगवान विष्णुः मंगलम् गरुड़ध्वजः।",
        "Two souls, one sacred bond. Join us to celebrate our wedding.",
        "शुभ विवाह निमंत्रण: पधारकर नवदंपति को आशीर्वाद प्रदान करें।"
      )
      com.example.model.TemplateCategory.BUSINESS -> listOf(
        "धमाका फेस्टिवल सेल! फ्लैट 50% की भारी छूट सीमित समय के लिए।",
        "Best quality, genuine prices. Order today and celebrate!",
        "Special Festive Discount on All New Arrivals!"
      )
      else -> listOf(
        "आपका दिन मंगलमय और सुखमय हो।",
        "Start your day with positive vibes and great thoughts."
      )
    }

    quotes.forEach { quote ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp)
          .background(Color(0xFF1E1A38), RoundedCornerShape(8.dp))
          .border(1.dp, Color(0xFF28234B), RoundedCornerShape(8.dp))
          .clickable { onSelectQuote(quote) }
          .padding(8.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(quote, fontSize = 11.5.sp, color = Color.White, modifier = Modifier.weight(1f))
          Spacer(modifier = Modifier.width(6.dp))
          Icon(Icons.Default.Add, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}

@Composable
private fun LayersPanel(
  textLayers: List<TextLayer>,
  stickers: List<StickerLayer>,
  selectedId: String?,
  onSelect: (String) -> Unit,
  onDelete: (String) -> Unit,
  onDuplicate: (String) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    Text("Layer Management (${textLayers.size + stickers.size} elements):", fontSize = 12.sp, color = GoldAccent)
    Spacer(modifier = Modifier.height(6.dp))

    textLayers.forEach { layer ->
      val isSelected = layer.id == selectedId
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 2.dp)
          .background(if (isSelected) SaffronPrimary.copy(alpha = 0.2f) else Color(0xFF1E1A38), RoundedCornerShape(8.dp))
          .border(1.dp, if (isSelected) SaffronPrimary else Color(0xFF28234B), RoundedCornerShape(8.dp))
          .clickable { onSelect(layer.id) }
          .padding(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TextFields, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = layer.text.take(24),
              fontSize = 12.sp,
              color = Color.White,
              maxLines = 1
            )
          }

          Row {
            IconButton(onClick = { onDuplicate(layer.id) }, modifier = Modifier.size(28.dp)) {
              Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = Color.LightGray, modifier = Modifier.size(14.dp))
            }
            IconButton(onClick = { onDelete(layer.id) }, modifier = Modifier.size(28.dp)) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE11D48), modifier = Modifier.size(14.dp))
            }
          }
        }
      }
    }

    stickers.forEach { sticker ->
      val isSelected = sticker.id == selectedId
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 2.dp)
          .background(if (isSelected) SaffronPrimary.copy(alpha = 0.2f) else Color(0xFF1E1A38), RoundedCornerShape(8.dp))
          .border(1.dp, if (isSelected) SaffronPrimary else Color(0xFF28234B), RoundedCornerShape(8.dp))
          .clickable { onSelect(sticker.id) }
          .padding(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(sticker.stickerType.emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(sticker.stickerType.label, fontSize = 12.sp, color = Color.White)
          }
          IconButton(onClick = { onDelete(sticker.id) }, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE11D48), modifier = Modifier.size(14.dp))
          }
        }
      }
    }
  }
}
