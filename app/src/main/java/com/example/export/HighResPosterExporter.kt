package com.example.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.graphics.DashPathEffect
import com.example.model.BorderStyle
import com.example.model.ExportConfig
import com.example.model.FooterStyle
import com.example.model.HeaderStyle
import com.example.model.PosterBackground
import com.example.model.StickerLayer
import com.example.model.StickerType
import com.example.model.TextLayer
import com.example.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object HighResPosterExporter {

  data class ExportResult(
    val file: File,
    val width: Int,
    val height: Int,
    val dpi: Int,
    val sizeBytes: Long
  )

  suspend fun renderAndSavePoster(
    context: Context,
    background: PosterBackground,
    textLayers: List<TextLayer>,
    stickers: List<StickerLayer>,
    userProfile: UserProfile?,
    showHeader: Boolean,
    headerStyle: HeaderStyle,
    showFooter: Boolean,
    footerStyle: FooterStyle,
    borderStyle: BorderStyle,
    exportConfig: ExportConfig
  ): ExportResult = withContext(Dispatchers.Default) {
    val width = exportConfig.width
    val height = exportConfig.height

    // 1. Create High-Definition ARGB_8888 bitmap with 300 DPI
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.density = exportConfig.dpi

    val canvas = Canvas(bitmap)

    // 2. Draw Background (Drawable, Gallery Uri, or Rich Gradient)
    when (background) {
      is PosterBackground.DrawableBackground -> {
        val options = BitmapFactory.Options().apply { inScaled = false }
        val rawBg = BitmapFactory.decodeResource(context.resources, background.resId, options)
        if (rawBg != null) {
          val bgPaint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
          }
          val destRect = Rect(0, 0, width, height)
          canvas.drawBitmap(rawBg, null, destRect, bgPaint)
          if (rawBg != bitmap) rawBg.recycle()
        } else {
          canvas.drawColor(AndroidColor.parseColor("#0F172A"))
        }
      }
      is PosterBackground.UriBackground -> {
        try {
          val uri = Uri.parse(background.uriString)
          val inputStream = context.contentResolver.openInputStream(uri)
          val uriBm = BitmapFactory.decodeStream(inputStream)
          inputStream?.close()
          if (uriBm != null) {
            val bgPaint = Paint().apply {
              isAntiAlias = true
              isFilterBitmap = true
              isDither = true
            }
            val destRect = Rect(0, 0, width, height)
            canvas.drawBitmap(uriBm, null, destRect, bgPaint)
            uriBm.recycle()
          } else {
            canvas.drawColor(AndroidColor.parseColor("#0F172A"))
          }
        } catch (e: Exception) {
          canvas.drawColor(AndroidColor.parseColor("#0F172A"))
        }
      }
      is PosterBackground.GradientBackground -> {
        val colorsInt = background.colorsHex.map { it.toInt() }.toIntArray()
        val shader = android.graphics.LinearGradient(
          0f, 0f, 0f, height.toFloat(),
          colorsInt,
          null,
          android.graphics.Shader.TileMode.CLAMP
        )
        val gradPaint = Paint().apply {
          this.shader = shader
          isAntiAlias = true
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradPaint)
      }
    }

    // Scale factor relative to 400px preview width
    val scaleFactor = width / 400f

    // 3. Draw Header Strip
    if (showHeader && userProfile != null && headerStyle != HeaderStyle.NONE) {
      drawAutoHeader(canvas, userProfile, headerStyle, width, height, scaleFactor)
    }

    // 4. Draw Stickers
    stickers.forEach { sticker ->
      drawSticker(canvas, sticker, width, height, scaleFactor)
    }

    // 5. Draw Text Layers (with 3D depth, shadows, strokes, rotation)
    textLayers.forEach { textLayer ->
      drawTextLayer(canvas, textLayer, width, height, scaleFactor)
    }

    // 6. Draw Smart Auto-Footer
    if (showFooter && userProfile != null) {
      drawAutoFooter(canvas, context, userProfile, footerStyle, width, height, scaleFactor)
    }

    // 7. Draw Decorative Frame Border
    if (borderStyle != BorderStyle.NONE) {
      drawBorder(canvas, borderStyle, width, height, scaleFactor)
    }

    // 6. Save as PNG to Cache Directory
    val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
    val filename = "PosterBanao_${System.currentTimeMillis()}_${width}x${height}.png"
    val outputFile = File(exportDir, filename)

    FileOutputStream(outputFile).use { out ->
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    val fileSize = outputFile.length()
    bitmap.recycle()

    ExportResult(
      file = outputFile,
      width = width,
      height = height,
      dpi = exportConfig.dpi,
      sizeBytes = fileSize
    )
  }

  private fun drawSticker(
    canvas: Canvas,
    sticker: StickerLayer,
    canvasW: Int,
    canvasH: Int,
    scaleFactor: Float
  ) {
    val x = sticker.xRatio * canvasW
    val y = sticker.yRatio * canvasH
    val paint = Paint().apply {
      isAntiAlias = true
      textSize = 28f * scaleFactor * sticker.scale
      textAlign = Paint.Align.CENTER
    }

    canvas.save()
    canvas.translate(x, y)
    if (sticker.rotationDeg != 0f) {
      canvas.rotate(sticker.rotationDeg)
    }

    // Shadow for sticker
    val shadowPaint = Paint(paint).apply {
      setShadowLayer(8f * scaleFactor, 0f, 4f * scaleFactor, AndroidColor.parseColor("#80000000"))
    }
    canvas.drawText(sticker.stickerType.emoji, 0f, 0f, shadowPaint)
    canvas.drawText(sticker.stickerType.emoji, 0f, 0f, paint)

    canvas.restore()
  }

  private fun drawTextLayer(
    canvas: Canvas,
    layer: TextLayer,
    canvasW: Int,
    canvasH: Int,
    scaleFactor: Float
  ) {
    val x = layer.xRatio * canvasW
    val y = layer.yRatio * canvasH
    val scaledTextSize = layer.fontSizeSp * scaleFactor

    val typeface = if (layer.isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

    val lines = layer.text.split("\n")
    val lineSpacing = scaledTextSize * 1.25f

    canvas.save()
    canvas.translate(x, y)
    if (layer.rotationDeg != 0f) {
      canvas.rotate(layer.rotationDeg)
    }

    val totalHeight = lines.size * lineSpacing
    val startY = -totalHeight / 2f + scaledTextSize

    // Optional background highlight box
    layer.bgBoxColorHex?.let { boxColor ->
      val maxLineW = lines.maxOfOrNull { line ->
        Paint().apply {
          this.textSize = scaledTextSize
          this.typeface = typeface
        }.measureText(line)
      } ?: 100f

      val padX = 24f * scaleFactor
      val padY = 16f * scaleFactor
      val boxRect = RectF(
        -maxLineW / 2f - padX,
        startY - scaledTextSize - padY / 2f,
        maxLineW / 2f + padX,
        startY + (lines.size - 1) * lineSpacing + padY
      )
      val boxPaint = Paint().apply {
        color = boxColor.toInt()
        isAntiAlias = true
      }
      canvas.drawRoundRect(boxRect, 12f * scaleFactor, 12f * scaleFactor, boxPaint)
    }

    // 3D Depth Layer (PixelLab Style Extrusion)
    if (layer.is3D) {
      val depthPixels = layer.depth3D * scaleFactor
      val depthPaint = Paint().apply {
        isAntiAlias = true
        this.textSize = scaledTextSize
        this.typeface = typeface
        this.textAlign = Paint.Align.CENTER
        color = layer.color3DHex.toInt()
      }

      val steps = depthPixels.toInt().coerceAtLeast(3)
      for (i in steps downTo 1) {
        val offset = i * (depthPixels / steps)
        lines.forEachIndexed { idx, line ->
          canvas.drawText(line, 0f + offset * 0.7f, startY + idx * lineSpacing + offset, depthPaint)
        }
      }
    }

    // Drop Shadow
    if (layer.hasShadow) {
      val shadowPaint = Paint().apply {
        isAntiAlias = true
        this.textSize = scaledTextSize
        this.typeface = typeface
        this.textAlign = Paint.Align.CENTER
        color = layer.textColorHex.toInt()
        setShadowLayer(
          layer.shadowRadius * scaleFactor,
          2f * scaleFactor,
          4f * scaleFactor,
          layer.shadowColorHex.toInt()
        )
      }
      lines.forEachIndexed { idx, line ->
        canvas.drawText(line, 0f, startY + idx * lineSpacing, shadowPaint)
      }
    }

    // Stroke Outline
    if (layer.hasStroke) {
      val strokePaint = Paint().apply {
        isAntiAlias = true
        this.textSize = scaledTextSize
        this.typeface = typeface
        this.textAlign = Paint.Align.CENTER
        style = Paint.Style.STROKE
        strokeWidth = layer.strokeWidth * scaleFactor
        color = layer.strokeColorHex.toInt()
      }
      lines.forEachIndexed { idx, line ->
        canvas.drawText(line, 0f, startY + idx * lineSpacing, strokePaint)
      }
    }

    // Main Text Fill
    val mainPaint = Paint().apply {
      isAntiAlias = true
      this.textSize = scaledTextSize
      this.typeface = typeface
      this.textAlign = Paint.Align.CENTER
      color = layer.textColorHex.toInt()
    }
    lines.forEachIndexed { idx, line ->
      canvas.drawText(line, 0f, startY + idx * lineSpacing, mainPaint)
    }

    canvas.restore()
  }

  private fun drawAutoFooter(
    canvas: Canvas,
    context: Context,
    profile: UserProfile,
    style: FooterStyle,
    canvasW: Int,
    canvasH: Int,
    scaleFactor: Float
  ) {
    val footerH = (65f * scaleFactor).toInt().coerceAtLeast(180)
    val footerTop = (canvasH - footerH).toFloat()

    val bgPaint = Paint().apply { isAntiAlias = true }
    val borderPaint = Paint().apply {
      isAntiAlias = true
      this.style = Paint.Style.STROKE
      strokeWidth = 3f * scaleFactor
    }

    when (style) {
      FooterStyle.CLASSIC_ROYAL -> {
        bgPaint.color = AndroidColor.parseColor("#EE0B132B")
        borderPaint.color = AndroidColor.parseColor("#FFD700")
        canvas.drawRect(0f, footerTop, canvasW.toFloat(), canvasH.toFloat(), bgPaint)
        canvas.drawLine(0f, footerTop, canvasW.toFloat(), footerTop, borderPaint)
      }
      FooterStyle.SAFFRON_FESTIVE -> {
        bgPaint.color = AndroidColor.parseColor("#F5E65100")
        borderPaint.color = AndroidColor.parseColor("#FFF3B0")
        canvas.drawRect(0f, footerTop, canvasW.toFloat(), canvasH.toFloat(), bgPaint)
        canvas.drawLine(0f, footerTop, canvasW.toFloat(), footerTop, borderPaint)
      }
      FooterStyle.MODERN_DARK_GLASS -> {
        bgPaint.color = AndroidColor.parseColor("#DD111827")
        borderPaint.color = AndroidColor.parseColor("#38BDF8")
        canvas.drawRect(0f, footerTop, canvasW.toFloat(), canvasH.toFloat(), bgPaint)
        canvas.drawLine(0f, footerTop, canvasW.toFloat(), footerTop, borderPaint)
      }
      FooterStyle.MINIMAL_ACCENT -> {
        bgPaint.color = AndroidColor.parseColor("#FAFFFFFF")
        borderPaint.color = AndroidColor.parseColor("#E2E8F0")
        canvas.drawRect(0f, footerTop, canvasW.toFloat(), canvasH.toFloat(), bgPaint)
        canvas.drawLine(0f, footerTop, canvasW.toFloat(), footerTop, borderPaint)
      }
      FooterStyle.DUAL_PILL -> {
        bgPaint.color = AndroidColor.parseColor("#EE1E1B4B")
        val pillMargin = 16f * scaleFactor
        val pillRect = RectF(
          pillMargin,
          footerTop + pillMargin / 2,
          canvasW - pillMargin,
          canvasH - pillMargin / 2
        )
        canvas.drawRoundRect(pillRect, 24f * scaleFactor, 24f * scaleFactor, bgPaint)
      }
      FooterStyle.GOLDEN_EMBOSSED -> {
        bgPaint.color = AndroidColor.parseColor("#EEB45309")
        borderPaint.color = AndroidColor.parseColor("#FDE68A")
        canvas.drawRect(0f, footerTop, canvasW.toFloat(), canvasH.toFloat(), bgPaint)
        canvas.drawLine(0f, footerTop, canvasW.toFloat(), footerTop, borderPaint)
      }
      FooterStyle.CORPORATE_STRIP -> {
        bgPaint.color = AndroidColor.parseColor("#EE0F172A")
        borderPaint.color = AndroidColor.parseColor("#0284C7")
        canvas.drawRect(0f, footerTop, canvasW.toFloat(), canvasH.toFloat(), bgPaint)
        canvas.drawLine(0f, footerTop, canvasW.toFloat(), footerTop, borderPaint)
      }
    }

    val isLightStyle = style == FooterStyle.MINIMAL_ACCENT || style == FooterStyle.GOLDEN_EMBOSSED
    val primaryTextColor = if (isLightStyle) AndroidColor.BLACK else AndroidColor.WHITE
    val secondaryTextColor = if (isLightStyle) AndroidColor.DKGRAY else AndroidColor.parseColor("#CBD5E1")
    val accentColor = if (isLightStyle) AndroidColor.parseColor("#B45309") else AndroidColor.parseColor("#FFD700")

    // Draw Profile Photo (Circular Badge with Gold/Cyan border)
    val photoRadius = (footerH * 0.38f)
    val photoCenterX = (photoRadius + 24f * scaleFactor)
    val photoCenterY = footerTop + footerH / 2f

    profile.photoRes?.let { resId ->
      val photoBm = BitmapFactory.decodeResource(context.resources, resId)
      if (photoBm != null) {
        val circularBm = getCircularBitmap(photoBm, (photoRadius * 2).toInt())
        canvas.drawBitmap(
          circularBm,
          photoCenterX - photoRadius,
          photoCenterY - photoRadius,
          Paint(Paint.ANTI_ALIAS_FLAG)
        )
        // Ring border
        val ringPaint = Paint().apply {
          isAntiAlias = true
          this.style = Paint.Style.STROKE
          strokeWidth = 3f * scaleFactor
          color = accentColor
        }
        canvas.drawCircle(photoCenterX, photoCenterY, photoRadius, ringPaint)
      }
    }

    // Text details (Business Name, Owner, Contact & Website)
    val textStartX = photoCenterX + photoRadius + 18f * scaleFactor

    val titlePaint = Paint().apply {
      isAntiAlias = true
      typeface = Typeface.DEFAULT_BOLD
      textSize = 15f * scaleFactor
      color = primaryTextColor
    }

    val ownerPaint = Paint().apply {
      isAntiAlias = true
      textSize = 11f * scaleFactor
      color = secondaryTextColor
    }

    val contactPaint = Paint().apply {
      isAntiAlias = true
      typeface = Typeface.DEFAULT_BOLD
      textSize = 11.5f * scaleFactor
      color = accentColor
    }

    val line1Y = footerTop + footerH * 0.35f
    val line2Y = footerTop + footerH * 0.58f
    val line3Y = footerTop + footerH * 0.82f

    canvas.drawText(profile.businessName, textStartX, line1Y, titlePaint)
    canvas.drawText("${profile.ownerName} (${profile.designation})", textStartX, line2Y, ownerPaint)
    canvas.drawText("📞 ${profile.phone}  |  🌐 ${profile.website}", textStartX, line3Y, contactPaint)

    // Draw Business Logo on top right of the footer
    profile.logoRes?.let { logoResId ->
      val logoBm = BitmapFactory.decodeResource(context.resources, logoResId)
      if (logoBm != null) {
        val logoSize = (footerH * 0.65f).toInt()
        val logoLeft = canvasW - logoSize - (24f * scaleFactor).toInt()
        val logoTop = (footerTop + (footerH - logoSize) / 2f).toInt()
        val logoDest = Rect(logoLeft, logoTop, logoLeft + logoSize, logoTop + logoSize)
        val logoPaint = Paint().apply {
          isAntiAlias = true
          isFilterBitmap = true
        }
        canvas.drawBitmap(logoBm, null, logoDest, logoPaint)
      }
    }
  }

  private fun getCircularBitmap(bitmap: Bitmap, diameter: Int): Bitmap {
    val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint().apply {
      isAntiAlias = true
      isFilterBitmap = true
    }
    val rect = Rect(0, 0, diameter, diameter)
    val rectF = RectF(rect)
    canvas.drawOval(rectF, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, null, rect, paint)
    return output
  }

  private fun drawAutoHeader(
    canvas: Canvas,
    profile: UserProfile,
    style: HeaderStyle,
    canvasW: Int,
    canvasH: Int,
    scaleFactor: Float
  ) {
    if (style == HeaderStyle.NONE) return
    val headerH = (32f * scaleFactor).toInt().coerceAtLeast(80)
    val bgPaint = Paint().apply { isAntiAlias = true }
    val borderPaint = Paint().apply {
      isAntiAlias = true
      this.style = Paint.Style.STROKE
      strokeWidth = 2f * scaleFactor
    }
    val textPaint = Paint().apply {
      isAntiAlias = true
      typeface = Typeface.DEFAULT_BOLD
      textAlign = Paint.Align.CENTER
      textSize = 12f * scaleFactor
    }

    when (style) {
      HeaderStyle.AUSPICIOUS_SHREE_GANESH -> {
        bgPaint.color = AndroidColor.parseColor("#F0580C14")
        borderPaint.color = AndroidColor.parseColor("#FFD700")
        canvas.drawRect(0f, 0f, canvasW.toFloat(), headerH.toFloat(), bgPaint)
        canvas.drawLine(0f, headerH.toFloat(), canvasW.toFloat(), headerH.toFloat(), borderPaint)

        textPaint.color = AndroidColor.parseColor("#FFD700")
        val textY = headerH * 0.65f
        canvas.drawText("卐  ॥ श्री गणेशाय नमः ॥  •  शुभ लाभ  卐", canvasW / 2f, textY, textPaint)
      }
      HeaderStyle.BRAND_HOTLINE_STRIP -> {
        bgPaint.color = AndroidColor.parseColor("#F00F172A")
        borderPaint.color = AndroidColor.parseColor("#38325E")
        canvas.drawRect(0f, 0f, canvasW.toFloat(), headerH.toFloat(), bgPaint)
        canvas.drawLine(0f, headerH.toFloat(), canvasW.toFloat(), headerH.toFloat(), borderPaint)

        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = AndroidColor.WHITE
        canvas.drawText(profile.businessName, 20f * scaleFactor, headerH * 0.65f, textPaint)

        val phonePaint = Paint(textPaint).apply {
          textAlign = Paint.Align.RIGHT
          color = AndroidColor.parseColor("#10B981")
        }
        canvas.drawText("📞 24x7: ${profile.phone}", canvasW - 20f * scaleFactor, headerH * 0.65f, phonePaint)
      }
      HeaderStyle.OFFER_ANNOUNCEMENT -> {
        bgPaint.color = AndroidColor.parseColor("#F5E11D48")
        borderPaint.color = AndroidColor.parseColor("#FBBF24")
        canvas.drawRect(0f, 0f, canvasW.toFloat(), headerH.toFloat(), bgPaint)
        canvas.drawLine(0f, headerH.toFloat(), canvasW.toFloat(), headerH.toFloat(), borderPaint)

        textPaint.color = AndroidColor.WHITE
        canvas.drawText("⚡ SPECIAL FESTIVE OFFER • LIMITED TIME DEALS ⚡", canvasW / 2f, headerH * 0.65f, textPaint)
      }
      HeaderStyle.ROYAL_GOLD_CROWN -> {
        bgPaint.color = AndroidColor.parseColor("#F01E1B4B")
        borderPaint.color = AndroidColor.parseColor("#FFD700")
        canvas.drawRect(0f, 0f, canvasW.toFloat(), headerH.toFloat(), bgPaint)
        canvas.drawLine(0f, headerH.toFloat(), canvasW.toFloat(), headerH.toFloat(), borderPaint)

        textPaint.color = AndroidColor.parseColor("#FFD700")
        canvas.drawText("👑 ROYAL HERITAGE & FESTIVE CELEBRATION 👑", canvasW / 2f, headerH * 0.65f, textPaint)
      }
      HeaderStyle.SOCIAL_MEDIA_HANDLES -> {
        bgPaint.color = AndroidColor.parseColor("#F0111827")
        borderPaint.color = AndroidColor.parseColor("#38BDF8")
        canvas.drawRect(0f, 0f, canvasW.toFloat(), headerH.toFloat(), bgPaint)
        canvas.drawLine(0f, headerH.toFloat(), canvasW.toFloat(), headerH.toFloat(), borderPaint)

        textPaint.color = AndroidColor.parseColor("#38BDF8")
        canvas.drawText("💬 WhatsApp: ${profile.phone}   |   🌐 ${profile.website}", canvasW / 2f, headerH * 0.65f, textPaint)
      }
      HeaderStyle.MINIMAL_TAGLINE -> {
        bgPaint.color = AndroidColor.parseColor("#E00F172A")
        borderPaint.color = AndroidColor.parseColor("#334155")
        canvas.drawRect(0f, 0f, canvasW.toFloat(), headerH.toFloat(), bgPaint)
        canvas.drawLine(0f, headerH.toFloat(), canvasW.toFloat(), headerH.toFloat(), borderPaint)

        textPaint.color = AndroidColor.parseColor("#CBD5E1")
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        canvas.drawText("“${profile.tagLine}”", canvasW / 2f, headerH * 0.65f, textPaint)
      }
      HeaderStyle.NONE -> Unit
    }
  }

  private fun drawBorder(
    canvas: Canvas,
    style: BorderStyle,
    canvasW: Int,
    canvasH: Int,
    scaleFactor: Float
  ) {
    if (style == BorderStyle.NONE) return
    val w = canvasW.toFloat()
    val h = canvasH.toFloat()

    val paint = Paint().apply {
      isAntiAlias = true
      this.style = Paint.Style.STROKE
    }

    when (style) {
      BorderStyle.ROYAL_GOLD_FRAME -> {
        val inset = 12f * scaleFactor
        paint.color = AndroidColor.parseColor("#FFD700")
        paint.strokeWidth = 4f * scaleFactor
        canvas.drawRect(inset, inset, w - inset, h - inset, paint)

        val fillPaint = Paint().apply {
          isAntiAlias = true
          color = AndroidColor.parseColor("#FFD700")
          this.style = Paint.Style.FILL
        }
        val diamondSize = 10f * scaleFactor
        val corners = listOf(
          Pair(inset, inset),
          Pair(w - inset, inset),
          Pair(w - inset, h - inset),
          Pair(inset, h - inset)
        )
        corners.forEach { (cx, cy) ->
          val path = android.graphics.Path().apply {
            moveTo(cx, cy - diamondSize)
            lineTo(cx + diamondSize, cy)
            lineTo(cx, cy + diamondSize)
            lineTo(cx - diamondSize, cy)
            close()
          }
          canvas.drawPath(path, fillPaint)
        }
      }
      BorderStyle.DOUBLE_GOLDEN_LINE -> {
        val outerInset = 8f * scaleFactor
        val innerInset = 16f * scaleFactor
        paint.color = AndroidColor.parseColor("#FFD700")
        paint.strokeWidth = 4f * scaleFactor
        canvas.drawRect(outerInset, outerInset, w - outerInset, h - outerInset, paint)

        paint.strokeWidth = 2f * scaleFactor
        paint.alpha = 200
        canvas.drawRect(innerInset, innerInset, w - innerInset, h - innerInset, paint)
      }
      BorderStyle.FESTIVE_SAFFRON_TRIM -> {
        val inset = 10f * scaleFactor
        paint.color = AndroidColor.parseColor("#FF7A00")
        paint.strokeWidth = 5f * scaleFactor
        canvas.drawRect(inset, inset, w - inset, h - inset, paint)

        val goldFill = Paint().apply {
          isAntiAlias = true
          color = AndroidColor.parseColor("#FFD700")
          this.style = Paint.Style.FILL
        }
        val sq = 12f * scaleFactor
        canvas.drawRect(inset - sq / 2, inset - sq / 2, inset + sq / 2, inset + sq / 2, goldFill)
        canvas.drawRect(w - inset - sq / 2, inset - sq / 2, w - inset + sq / 2, inset + sq / 2, goldFill)
        canvas.drawRect(w - inset - sq / 2, h - inset - sq / 2, w - inset + sq / 2, h - inset + sq / 2, goldFill)
        canvas.drawRect(inset - sq / 2, h - inset - sq / 2, inset + sq / 2, h - inset + sq / 2, goldFill)
      }
      BorderStyle.VINTAGE_CORNER_FLOURISH -> {
        val inset = 14f * scaleFactor
        paint.color = AndroidColor.parseColor("#FFD700")
        paint.strokeWidth = 3f * scaleFactor
        canvas.drawRect(inset, inset, w - inset, h - inset, paint)

        val goldFill = Paint().apply {
          isAntiAlias = true
          color = AndroidColor.parseColor("#FFD700")
          this.style = Paint.Style.FILL
        }
        val circleR = 8f * scaleFactor
        val corners = listOf(
          Pair(inset, inset),
          Pair(w - inset, inset),
          Pair(w - inset, h - inset),
          Pair(inset, h - inset)
        )
        corners.forEach { (cx, cy) ->
          canvas.drawCircle(cx, cy, circleR, goldFill)
        }
      }
      BorderStyle.NEON_CYAN_GLOW -> {
        val inset = 10f * scaleFactor
        paint.color = AndroidColor.parseColor("#06B6D4")
        paint.strokeWidth = 4f * scaleFactor
        canvas.drawRect(inset, inset, w - inset, h - inset, paint)

        val emeraldPaint = Paint().apply {
          isAntiAlias = true
          color = AndroidColor.parseColor("#10B981")
          strokeWidth = 6f * scaleFactor
          strokeCap = Paint.Cap.ROUND
        }
        val cornerL = 25f * scaleFactor
        canvas.drawLine(inset, inset, inset + cornerL, inset, emeraldPaint)
        canvas.drawLine(inset, inset, inset, inset + cornerL, emeraldPaint)
        canvas.drawLine(w - inset, inset, w - inset - cornerL, inset, emeraldPaint)
        canvas.drawLine(w - inset, inset, w - inset, inset + cornerL, emeraldPaint)
        canvas.drawLine(w - inset, h - inset, w - inset - cornerL, h - inset, emeraldPaint)
        canvas.drawLine(w - inset, h - inset, w - inset, h - inset - cornerL, emeraldPaint)
        canvas.drawLine(inset, h - inset, inset + cornerL, h - inset, emeraldPaint)
        canvas.drawLine(inset, h - inset, inset, h - inset - cornerL, emeraldPaint)
      }
      BorderStyle.PEARL_BEADED_BORDER -> {
        val inset = 12f * scaleFactor
        paint.color = AndroidColor.parseColor("#FFD700")
        paint.strokeWidth = 3.5f * scaleFactor
        paint.pathEffect = android.graphics.DashPathEffect(floatArrayOf(10f * scaleFactor, 8f * scaleFactor), 0f)
        canvas.drawRect(inset, inset, w - inset, h - inset, paint)
      }
      BorderStyle.NONE -> Unit
    }
  }
}
