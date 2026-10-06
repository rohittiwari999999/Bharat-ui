package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.model.BorderStyle
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PeacockCyan
import com.example.ui.theme.SaffronPrimary

@Composable
fun BorderOverlayComposable(
  style: BorderStyle,
  modifier: Modifier = Modifier
) {
  if (style == BorderStyle.NONE) return

  Canvas(modifier = modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    when (style) {
      BorderStyle.ROYAL_GOLD_FRAME -> {
        val inset = 12.dp.toPx()
        val strokeW = 2.dp.toPx()
        val gold = GoldAccent

        // Main inset rectangle
        drawRect(
          color = gold,
          topLeft = Offset(inset, inset),
          size = Size(w - inset * 2, h - inset * 2),
          style = Stroke(width = strokeW)
        )

        // Ornate Diamond Corner Motifs
        val diamondSize = 6.dp.toPx()
        val corners = listOf(
          Offset(inset, inset),
          Offset(w - inset, inset),
          Offset(w - inset, h - inset),
          Offset(inset, h - inset)
        )

        corners.forEach { center ->
          val diamondPath = Path().apply {
            moveTo(center.x, center.y - diamondSize)
            lineTo(center.x + diamondSize, center.y)
            lineTo(center.x, center.y + diamondSize)
            lineTo(center.x - diamondSize, center.y)
            close()
          }
          drawPath(diamondPath, color = gold)
        }

        // Inner corner ticks
        val tick = 10.dp.toPx()
        corners.forEachIndexed { idx, pt ->
          val dx = if (idx == 0 || idx == 3) tick else -tick
          val dy = if (idx == 0 || idx == 1) tick else -tick
          drawLine(gold, Offset(pt.x + dx, pt.y), Offset(pt.x + dx, pt.y + dy), strokeW * 0.7f)
          drawLine(gold, Offset(pt.x, pt.y + dy), Offset(pt.x + dx, pt.y + dy), strokeW * 0.7f)
        }
      }

      BorderStyle.DOUBLE_GOLDEN_LINE -> {
        val outerInset = 8.dp.toPx()
        val innerInset = 14.dp.toPx()
        val gold = GoldAccent

        // Outer line (thick)
        drawRect(
          color = gold,
          topLeft = Offset(outerInset, outerInset),
          size = Size(w - outerInset * 2, h - outerInset * 2),
          style = Stroke(width = 2.5.dp.toPx())
        )

        // Inner line (thin)
        drawRect(
          color = gold.copy(alpha = 0.8f),
          topLeft = Offset(innerInset, innerInset),
          size = Size(w - innerInset * 2, h - innerInset * 2),
          style = Stroke(width = 1.dp.toPx())
        )
      }

      BorderStyle.FESTIVE_SAFFRON_TRIM -> {
        val inset = 10.dp.toPx()
        val saffron = SaffronPrimary
        val gold = GoldAccent

        // Saffron ribbon border
        drawRect(
          color = saffron,
          topLeft = Offset(inset, inset),
          size = Size(w - inset * 2, h - inset * 2),
          style = Stroke(width = 3.dp.toPx())
        )

        // Golden corner squares
        val sqSize = 8.dp.toPx()
        val cornerSquares = listOf(
          Offset(inset - sqSize / 2, inset - sqSize / 2),
          Offset(w - inset - sqSize / 2, inset - sqSize / 2),
          Offset(w - inset - sqSize / 2, h - inset - sqSize / 2),
          Offset(inset - sqSize / 2, h - inset - sqSize / 2)
        )
        cornerSquares.forEach { pos ->
          drawRect(color = gold, topLeft = pos, size = Size(sqSize, sqSize))
        }
      }

      BorderStyle.VINTAGE_CORNER_FLOURISH -> {
        val inset = 12.dp.toPx()
        val gold = GoldAccent
        val strokeW = 1.5.dp.toPx()

        // Inset border
        drawRect(
          color = gold.copy(alpha = 0.85f),
          topLeft = Offset(inset, inset),
          size = Size(w - inset * 2, h - inset * 2),
          style = Stroke(width = strokeW)
        )

        // Corner circles
        val circleR = 4.dp.toPx()
        val corners = listOf(
          Offset(inset, inset),
          Offset(w - inset, inset),
          Offset(w - inset, h - inset),
          Offset(inset, h - inset)
        )
        corners.forEach { pt ->
          drawCircle(color = gold, radius = circleR, center = pt)
          drawCircle(color = Color(0xFF0F172A), radius = circleR * 0.5f, center = pt)
        }
      }

      BorderStyle.NEON_CYAN_GLOW -> {
        val inset = 8.dp.toPx()
        val cyan = PeacockCyan
        val emerald = EmeraldGreen

        // Cyan luminous frame
        drawRect(
          color = cyan,
          topLeft = Offset(inset, inset),
          size = Size(w - inset * 2, h - inset * 2),
          style = Stroke(width = 2.dp.toPx())
        )

        // Emerald inner accents
        val cornerLength = 16.dp.toPx()
        val corners = listOf(
          Pair(Offset(inset, inset), Offset(inset + cornerLength, inset)),
          Pair(Offset(inset, inset), Offset(inset, inset + cornerLength)),
          Pair(Offset(w - inset, inset), Offset(w - inset - cornerLength, inset)),
          Pair(Offset(w - inset, inset), Offset(w - inset, inset + cornerLength)),
          Pair(Offset(w - inset, h - inset), Offset(w - inset - cornerLength, h - inset)),
          Pair(Offset(w - inset, h - inset), Offset(w - inset, h - inset - cornerLength)),
          Pair(Offset(inset, h - inset), Offset(inset + cornerLength, h - inset)),
          Pair(Offset(inset, h - inset), Offset(inset, h - inset - cornerLength))
        )
        corners.forEach { (p1, p2) ->
          drawLine(emerald, p1, p2, strokeWidth = 3.5.dp.toPx(), cap = StrokeCap.Round)
        }
      }

      BorderStyle.PEARL_BEADED_BORDER -> {
        val inset = 10.dp.toPx()
        val gold = GoldAccent
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()), 0f)

        drawRect(
          color = gold,
          topLeft = Offset(inset, inset),
          size = Size(w - inset * 2, h - inset * 2),
          style = Stroke(width = 2.dp.toPx(), pathEffect = dashEffect)
        )
      }

      BorderStyle.NONE -> Unit
    }
  }
}
