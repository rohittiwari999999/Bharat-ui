package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.model.AppThemeMode

fun getThemeColorScheme(themeMode: AppThemeMode) = darkColorScheme(
  primary = Color(themeMode.primaryColorHex),
  onPrimary = PureWhite,
  primaryContainer = Color(themeMode.primaryColorHex).copy(alpha = 0.7f),
  onPrimaryContainer = PureWhite,
  secondary = GoldAccent,
  onSecondary = PureBlack,
  secondaryContainer = CardElevated,
  onSecondaryContainer = GoldAccent,
  tertiary = CrimsonFestive,
  onTertiary = PureWhite,
  background = Color(themeMode.backgroundColorHex),
  onBackground = TextPrimaryDark,
  surface = Color(themeMode.surfaceColorHex),
  onSurface = TextPrimaryDark,
  surfaceVariant = CardElevated,
  onSurfaceVariant = TextSecondaryDark,
  outline = BorderDark
)

@Composable
fun MyApplicationTheme(
  appThemeMode: AppThemeMode = AppThemeMode.ROYAL_DARK,
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    else -> getThemeColorScheme(appThemeMode)
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
