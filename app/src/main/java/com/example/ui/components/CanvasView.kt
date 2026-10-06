package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.BorderStyle
import com.example.model.FontCategory
import com.example.model.FooterStyle
import com.example.model.HeaderStyle
import com.example.model.PosterBackground
import com.example.model.StickerLayer
import com.example.model.TextLayer
import com.example.model.UserProfile
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary
import kotlin.math.roundToInt

@Composable
fun CanvasView(
  background: PosterBackground,
  textLayers: List<TextLayer>,
  stickers: List<StickerLayer>,
  userProfile: UserProfile?,
  showHeader: Boolean,
  headerStyle: HeaderStyle,
  showFooter: Boolean,
  footerStyle: FooterStyle,
  borderStyle: BorderStyle,
  selectedLayerId: String?,
  onSelectLayer: (String) -> Unit,
  onMoveTextLayer: (id: String, newXRatio: Float, newYRatio: Float) -> Unit,
  onMoveStickerLayer: (id: String, newXRatio: Float, newYRatio: Float) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(3f / 4f) // Standard 3:4 Indian poster aspect ratio
      .shadow(12.dp, RoundedCornerShape(12.dp))
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF0F172A))
      .testTag("interactive_canvas_view")
  ) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
      val canvasWidthPx = constraints.maxWidth.toFloat()
      val canvasHeightPx = constraints.maxHeight.toFloat()

      // 1. Dynamic Background (Preset Image, Gallery Photo, or Rich Gradient)
      when (background) {
        is PosterBackground.DrawableBackground -> {
          Image(
            painter = painterResource(id = background.resId),
            contentDescription = "Poster Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        }
        is PosterBackground.UriBackground -> {
          AsyncImage(
            model = background.uriString,
            contentDescription = "Custom Gallery Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        }
        is PosterBackground.GradientBackground -> {
          val brush = Brush.verticalGradient(background.colorsHex.map { Color(it) })
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(brush)
          )
        }
      }

      // 2. Header Strip pinned at the top
      if (showHeader && userProfile != null && headerStyle != HeaderStyle.NONE) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
        ) {
          AutoHeaderComposable(
            profile = userProfile,
            style = headerStyle
          )
        }
      }

      // 3. Stickers
      stickers.forEach { sticker ->
        val stickerXPx = sticker.xRatio * canvasWidthPx
        val stickerYPx = sticker.yRatio * canvasHeightPx
        val isSelected = sticker.id == selectedLayerId

        Box(
          modifier = Modifier
            .offset {
              IntOffset(
                (stickerXPx - 30.dp.toPx()).roundToInt(),
                (stickerYPx - 30.dp.toPx()).roundToInt()
              )
            }
            .then(
              if (isSelected) {
                Modifier.border(1.5.dp, SaffronPrimary, RoundedCornerShape(8.dp))
              } else Modifier
            )
            .pointerInput(sticker.id) {
              detectDragGestures { change, dragAmount ->
                change.consume()
                val newX = (stickerXPx + dragAmount.x) / canvasWidthPx
                val newY = (stickerYPx + dragAmount.y) / canvasHeightPx
                onMoveStickerLayer(sticker.id, newX.coerceIn(0.05f, 0.95f), newY.coerceIn(0.05f, 0.85f))
              }
            }
            .clickable { onSelectLayer(sticker.id) }
            .padding(4.dp)
            .rotate(sticker.rotationDeg)
        ) {
          Text(
            text = sticker.stickerType.emoji,
            fontSize = (32 * sticker.scale).sp,
            textAlign = TextAlign.Center
          )
        }
      }

      // 3. Text Layers (with 3D depth simulation & shadow)
      textLayers.forEach { layer ->
        val textXPx = layer.xRatio * canvasWidthPx
        val textYPx = layer.yRatio * canvasHeightPx
        val isSelected = layer.id == selectedLayerId

        Box(
          modifier = Modifier
            .offset {
              IntOffset(
                (textXPx - 120.dp.toPx()).roundToInt(),
                (textYPx - 25.dp.toPx()).roundToInt()
              )
            }
            .wrapContentSize(Alignment.Center)
            .pointerInput(layer.id) {
              detectDragGestures { change, dragAmount ->
                change.consume()
                val newX = (textXPx + dragAmount.x) / canvasWidthPx
                val newY = (textYPx + dragAmount.y) / canvasHeightPx
                onMoveTextLayer(layer.id, newX.coerceIn(0.1f, 0.9f), newY.coerceIn(0.05f, 0.82f))
              }
            }
            .clickable { onSelectLayer(layer.id) }
            .then(
              if (isSelected) {
                Modifier
                  .border(1.5.dp, GoldAccent, RoundedCornerShape(8.dp))
                  .padding(4.dp)
              } else Modifier.padding(4.dp)
            )
            .rotate(layer.rotationDeg)
        ) {
          RenderStyledText(layer = layer)
        }
      }

      // 5. Smart Auto-Footer pinned at the bottom
      if (showFooter && userProfile != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
        ) {
          AutoFooterComposable(
            profile = userProfile,
            style = footerStyle
          )
        }
      }

      // 6. Decorative Frame Border Overlay
      if (borderStyle != BorderStyle.NONE) {
        BorderOverlayComposable(
          style = borderStyle,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

@Composable
private fun RenderStyledText(layer: TextLayer) {
  val textColor = Color(layer.textColorHex)
  val color3D = Color(layer.color3DHex)
  val shadowColor = Color(layer.shadowColorHex)

  val fontFamily = when (layer.fontCategory) {
    FontCategory.ROYAL_SERIF -> FontFamily.Serif
    FontCategory.BOLD_DISPLAY -> FontFamily.SansSerif
    FontCategory.HINDI_DEVANAGARI, FontCategory.HINDI_CALLIGRAPHY, FontCategory.MODERN_CLEAN -> FontFamily.Default
  }

  val baseModifier = if (layer.bgBoxColorHex != null) {
    Modifier
      .background(Color(layer.bgBoxColorHex), RoundedCornerShape(8.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp)
  } else Modifier

  Box(modifier = baseModifier) {
    // 3D Depth Layer (Offset extrusion behind text)
    if (layer.is3D) {
      val depth = (layer.depth3D * 0.7f).dp
      Text(
        text = layer.text,
        fontSize = layer.fontSizeSp.sp,
        fontWeight = if (layer.isBold) FontWeight.ExtraBold else FontWeight.Normal,
        fontFamily = fontFamily,
        color = color3D,
        textAlign = TextAlign.Center,
        modifier = Modifier.offset(x = depth, y = depth)
      )
    }

    // Main Fill Text with Shadow
    Text(
      text = layer.text,
      fontSize = layer.fontSizeSp.sp,
      fontWeight = if (layer.isBold) FontWeight.Bold else FontWeight.Normal,
      fontFamily = fontFamily,
      color = textColor,
      textAlign = TextAlign.Center,
      style = TextStyle(
        shadow = if (layer.hasShadow) {
          Shadow(
            color = shadowColor,
            offset = Offset(2f, 4f),
            blurRadius = layer.shadowRadius
          )
        } else null
      )
    )
  }
}
