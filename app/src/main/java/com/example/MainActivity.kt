package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.TemplateRepository
import com.example.model.AppThemeMode
import com.example.model.PosterTemplate
import com.example.model.UserProfile
import com.example.ui.screens.EditorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileSetupScreen
import com.example.ui.theme.MyApplicationTheme

enum class ScreenState {
  HOME,
  EDITOR,
  PROFILE
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      var appThemeMode by remember { mutableStateOf(AppThemeMode.ROYAL_DARK) }
      MyApplicationTheme(appThemeMode = appThemeMode, darkTheme = true) {
        PosterBanaoApp(
          appThemeMode = appThemeMode,
          onSelectAppTheme = { appThemeMode = it }
        )
      }
    }
  }
}

@Composable
fun PosterBanaoApp(
  appThemeMode: AppThemeMode,
  onSelectAppTheme: (AppThemeMode) -> Unit
) {
  var currentScreen by remember { mutableStateOf(ScreenState.HOME) }
  var userProfile by remember { mutableStateOf(TemplateRepository.defaultUserProfile) }
  var activeTemplate by remember { mutableStateOf(TemplateRepository.sampleTemplates.first()) }

  Surface(modifier = Modifier.fillMaxSize()) {
    when (currentScreen) {
      ScreenState.HOME -> {
        HomeScreen(
          userProfile = userProfile,
          appThemeMode = appThemeMode,
          onSelectAppTheme = onSelectAppTheme,
          onSelectTemplate = { template ->
            activeTemplate = template
            currentScreen = ScreenState.EDITOR
          },
          onNavigateToProfile = {
            currentScreen = ScreenState.PROFILE
          }
        )
      }
      ScreenState.EDITOR -> {
        EditorScreen(
          template = activeTemplate,
          userProfile = userProfile,
          appThemeMode = appThemeMode,
          onSelectAppTheme = onSelectAppTheme,
          onNavigateToProfile = {
            currentScreen = ScreenState.PROFILE
          },
          onBack = {
            currentScreen = ScreenState.HOME
          }
        )
      }
      ScreenState.PROFILE -> {
        ProfileSetupScreen(
          currentProfile = userProfile,
          onSaveProfile = { updated ->
            userProfile = updated
          },
          onBack = {
            currentScreen = ScreenState.HOME
          }
        )
      }
    }
  }
}
