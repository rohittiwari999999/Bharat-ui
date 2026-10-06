package com.example.ui.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.OtaPatchInfo
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PeacockCyan
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtaUpdateDialog(
  currentPatchInfo: OtaPatchInfo,
  onPatchApplied: () -> Unit,
  onDismiss: () -> Unit
) {
  var isChecking by remember { mutableStateOf(false) }
  var downloadProgress by remember { mutableStateOf(0f) }
  var patchInstalled by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF1E1A38),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38325E)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("ota_update_dialog")
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FlashOn, contentDescription = null, tint = PeacockCyan)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Shorebird OTA Updates",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = Color.White
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131127), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF28234B), RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Active Engine:", fontSize = 12.sp, color = Color.Gray)
              Text("Shorebird Code Push", fontSize = 12.sp, color = PeacockCyan, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Installed Release:", fontSize = 12.sp, color = Color.Gray)
              Text(currentPatchInfo.patchVersionName, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("OTA Patch Status:", fontSize = 12.sp, color = Color.Gray)
              Text(
                if (patchInstalled) "Patch #105 (Latest)" else "Patch #104 Active",
                fontSize = 12.sp,
                color = EmeraldGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Zero App-Store Delay: New festival frames, templates, and UI fixes are deployed seamlessly in background.",
          fontSize = 12.sp,
          color = Color(0xFFCBD5E1),
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (isChecking) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = PeacockCyan, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (downloadProgress < 1f) "Downloading OTA Patch (${(downloadProgress * 100).toInt()}%)..." else "Applying Patch bytecode...",
              fontSize = 12.sp,
              color = Color.LightGray
            )
          }
        } else if (patchInstalled) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(EmeraldGreen.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
              .border(1.dp, EmeraldGreen, RoundedCornerShape(10.dp))
              .padding(10.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen)
              Spacer(modifier = Modifier.width(6.dp))
              Text("OTA Patch #105 Loaded Live!", color = EmeraldGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
        } else {
          Button(
            onClick = {
              scope.launch {
                isChecking = true
                for (p in 1..10) {
                  delay(150)
                  downloadProgress = p / 10f
                }
                delay(300)
                isChecking = false
                patchInstalled = true
                onPatchApplied()
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PeacockCyan),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("check_ota_patch_button")
          ) {
            Icon(Icons.Default.CloudSync, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simulate Shorebird OTA Push", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
