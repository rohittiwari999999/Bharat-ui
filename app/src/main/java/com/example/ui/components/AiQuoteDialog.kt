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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ai.GeminiQuotesService
import com.example.ui.theme.CardElevated
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.launch

@Composable
fun AiQuoteDialog(
  initialCategory: String,
  businessName: String,
  onQuoteSelected: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedCategory by remember { mutableStateOf(initialCategory) }
  var language by remember { mutableStateOf("Hindi") }
  var isGenerating by remember { mutableStateOf(false) }
  var generatedQuotes by remember {
    mutableStateOf(
      listOf(
        "दीपों का यह पावन पर्व आपके जीवन में सुख, शांति, समृद्धि और अपार खुशियाँ लेकर आए। शुभ दीपावली!",
        "लक्ष्मी जी की कृपा आप पर सदा बनी रहे। धन, वैभव और यश से परिपूर्ण हो आपका परिवार।",
        "हर घर में उजियारा हो, हर मन में प्रेम का वास हो। प्रकाश पर्व की हार्दिक बधाई!",
        "इस पावन अवसर पर आपके व्यापार और कारोबार में चार चांद लगें।"
      )
    )
  }

  val scope = rememberCoroutineScope()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color(0xFF1E1A38),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38325E)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("ai_quote_dialog")
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
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Gemini AI Poster Captions",
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

        // Language toggle
        Row(modifier = Modifier.fillMaxWidth()) {
          listOf("Hindi", "English", "Hinglish").forEach { lang ->
            val isSelected = language == lang
            Box(
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = 3.dp)
                .background(
                  if (isSelected) SaffronPrimary else Color(0xFF131127),
                  RoundedCornerShape(8.dp)
                )
                .clickable { language = lang }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = lang,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = {
            scope.launch {
              isGenerating = true
              val result = GeminiQuotesService.generateQuotes(
                category = selectedCategory,
                businessType = businessName,
                language = language
              )
              generatedQuotes = result
              isGenerating = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("generate_quotes_btn")
        ) {
          if (isGenerating) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Consulting Gemini AI...", fontSize = 13.sp)
          } else {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate New AI Shayari & Slogans", fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Tap any quote to add directly to poster:",
          fontSize = 12.sp,
          color = GoldAccent
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
        ) {
          items(generatedQuotes) { quote ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color(0xFF131127), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF28234B), RoundedCornerShape(10.dp))
                .clickable {
                  onQuoteSelected(quote)
                  onDismiss()
                }
                .padding(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = quote,
                  fontSize = 13.sp,
                  color = Color.White,
                  modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                  Icons.Default.ContentCopy,
                  contentDescription = "Select Quote",
                  tint = GoldAccent,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
