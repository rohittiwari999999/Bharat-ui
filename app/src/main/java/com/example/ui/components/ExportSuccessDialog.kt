package com.example.ui.components

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.export.HighResPosterExporter
import com.example.model.ExportConfig
import com.example.model.ExportResolutions
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary

@Composable
fun ExportSuccessDialog(
  exportResult: HighResPosterExporter.ExportResult?,
  isExporting: Boolean,
  selectedConfig: ExportConfig,
  onSelectConfig: (ExportConfig) -> Unit,
  onStartExport: () -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF1E1A38),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38325E)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("export_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Export High-Definition Poster",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (exportResult == null) {
          // Resolution Selector
          Text(
            text = "Select Export Quality (300 DPI Flex Print)",
            fontSize = 13.sp,
            color = GoldAccent,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
          )

          Spacer(modifier = Modifier.height(8.dp))

          ExportResolutions.forEach { config ->
            val isChosen = config == selectedConfig
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(
                  if (isChosen) SaffronPrimary.copy(alpha = 0.2f) else Color(0xFF131127),
                  RoundedCornerShape(12.dp)
                )
                .border(
                  width = if (isChosen) 2.dp else 1.dp,
                  color = if (isChosen) SaffronPrimary else Color(0xFF28234B),
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { onSelectConfig(config) }
                .padding(12.dp)
            ) {
              Column {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = config.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isChosen) GoldAccent else Color.White
                  )
                  Text(
                    text = "${config.dpi} DPI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = EmeraldGreen
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = config.description,
                  fontSize = 11.5.sp,
                  color = Color(0xFF94A3B8)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          if (isExporting) {
            CircularProgressIndicator(
              color = SaffronPrimary,
              modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Rendering ${selectedConfig.width}x${selectedConfig.height} px Ultra-HD Bitmap...",
              fontSize = 12.sp,
              color = Color.LightGray
            )
          } else {
            Button(
              onClick = onStartExport,
              colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("render_export_button")
            ) {
              Icon(Icons.Default.Print, contentDescription = null, tint = Color.White)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Render Ultra-HD Output (300 DPI)", fontWeight = FontWeight.Bold)
            }
          }
        } else {
          // Export Succeeded View
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = EmeraldGreen,
            modifier = Modifier.size(54.dp)
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Poster Rendered Successfully!",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(6.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFF131127), RoundedCornerShape(12.dp))
              .padding(12.dp)
          ) {
            Column {
              Text(
                text = "📐 Dimensions: ${exportResult.width} x ${exportResult.height} pixels",
                color = Color.White,
                fontSize = 13.sp
              )
              Text(
                text = "🖨️ Density: ${exportResult.dpi} DPI (Print & Flex Ready)",
                color = GoldAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "📦 File Size: ${exportResult.sizeBytes / 1024} KB",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp
              )
              Text(
                text = "✨ Zero Pixelation on Banners",
                color = EmeraldGreen,
                fontSize = 12.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(modifier = Modifier.fillMaxWidth()) {
            Button(
              onClick = {
                sharePosterFile(context, exportResult)
              },
              colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("share_whatsapp_button")
            ) {
              Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Share Poster")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
              onClick = onDismiss,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Done", color = Color.White)
            }
          }
        }
      }
    }
  }
}

private fun sharePosterFile(context: Context, exportResult: HighResPosterExporter.ExportResult) {
  try {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "image/png"
      putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(exportResult.file))
      putExtra(Intent.EXTRA_TEXT, "Created with PosterBanao - Ultra HD 300 DPI Poster Designer")
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share High-Res Poster"))
  } catch (e: Exception) {
    // Fallback simple share
    val textIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, "Poster exported at ${exportResult.width}x${exportResult.height} 300 DPI!")
    }
    context.startActivity(Intent.createChooser(textIntent, "Share"))
  }
}
