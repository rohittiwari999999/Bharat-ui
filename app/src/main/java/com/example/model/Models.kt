package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class FooterStyle(val displayName: String, val description: String) {
  CLASSIC_ROYAL("Classic Royal", "Deep navy banner with gold borders and elegant typography"),
  SAFFRON_FESTIVE("Saffron Festive", "Traditional Indian saffron and gold festive ribbon"),
  MODERN_DARK_GLASS("Dark Glass", "Modern translucent glassmorphism with neon accents"),
  MINIMAL_ACCENT("Minimal Clean", "Clean line separator with compact contact pills"),
  DUAL_PILL("Floating Badge", "Floating rounded pill bars with profile badge"),
  GOLDEN_EMBOSSED("Golden Velvet", "Opulent royal gold banner with dark navy typography"),
  CORPORATE_STRIP("Corporate Clean", "High-contrast commercial banner with clear contact buttons")
}

enum class HeaderStyle(val displayName: String, val description: String) {
  NONE("None", "No header bar"),
  AUSPICIOUS_SHREE_GANESH("Shree Ganesh", "॥ श्री गणेशाय नमः ॥ • शुभ लाभ traditional auspicious header"),
  BRAND_HOTLINE_STRIP("Brand Hotline", "Business name with 24x7 Order / Enquiry Hotline"),
  OFFER_ANNOUNCEMENT("Festive Offer", "⚡ SPECIAL FESTIVE OFFER • LIMITED TIME ONLY ⚡"),
  ROYAL_GOLD_CROWN("Royal Filigree", "Golden crown & traditional ornamental wings"),
  SOCIAL_MEDIA_HANDLES("Social Connect", "WhatsApp • Instagram • Facebook top handles"),
  MINIMAL_TAGLINE("Minimal Tagline", "Brand slogan and official website URL")
}

enum class BorderStyle(val displayName: String, val description: String) {
  NONE("None", "Clean edge-to-edge canvas"),
  ROYAL_GOLD_FRAME("Royal Gold Frame", "Inset gold border with ornate diamond corner flourishes"),
  DOUBLE_GOLDEN_LINE("Double Gold Lines", "Classic double line framing (outer thick, inner thin)"),
  FESTIVE_SAFFRON_TRIM("Saffron Festive Trim", "Traditional vermilion and saffron ribbon border"),
  VINTAGE_CORNER_FLOURISH("Vintage Flourish", "Royal corner ornaments with gold inner border"),
  NEON_CYAN_GLOW("Neon Cyan Glow", "Modern luminous high-tech border frame"),
  PEARL_BEADED_BORDER("Festive Beaded Trim", "Traditional gold pearl dot-dash framing")
}

data class UserProfile(
  val id: String = "default_user",
  val businessName: String = "Sharma Jewels & Handicrafts",
  val ownerName: String = "Aarav Sharma",
  val designation: String = "Proprietor / Founder",
  val phone: String = "+91 98765 43210",
  val email: String = "contact@sharmajewels.in",
  val website: String = "www.sharmajewels.in",
  val address: String = "Johari Bazar, Jaipur, Rajasthan",
  val tagLine: String = "Purity & Tradition Since 1984",
  @DrawableRes val photoRes: Int? = null,
  @DrawableRes val logoRes: Int? = null
)

data class TextLayer(
  val id: String = UUID.randomUUID().toString(),
  val text: String,
  val xRatio: Float = 0.5f, // 0.0 to 1.0 (center horizontal)
  val yRatio: Float = 0.4f, // 0.0 to 1.0 (vertical position)
  val fontSizeSp: Float = 24f,
  val textColorHex: Long = 0xFFFFFFFF,
  val fontCategory: FontCategory = FontCategory.HINDI_DEVANAGARI,
  val is3D: Boolean = false,
  val depth3D: Float = 6f, // 3D extrusion pixels
  val color3DHex: Long = 0xFF8A1000,
  val hasShadow: Boolean = true,
  val shadowColorHex: Long = 0xAA000000,
  val shadowRadius: Float = 8f,
  val hasStroke: Boolean = false,
  val strokeColorHex: Long = 0xFF000000,
  val strokeWidth: Float = 3f,
  val rotationDeg: Float = 0f,
  val isBold: Boolean = true,
  val bgBoxColorHex: Long? = null, // Optional highlight background pill
  val isLocked: Boolean = false
)

enum class FontCategory(val label: String, val fontSample: String) {
  HINDI_DEVANAGARI("Hindi Devanagari", "शुभ दीपावली"),
  HINDI_CALLIGRAPHY("Hindi Calligraphy", "हार्दिक बधाई"),
  BOLD_DISPLAY("Impact Display", "BIG FESTIVAL SALE"),
  ROYAL_SERIF("Royal Serif", "Grand Wedding Gala"),
  MODERN_CLEAN("Modern Sans", "Creative Agency")
}

enum class StickerType(val label: String, val emoji: String) {
  DIYA("Golden Diya", "🪔"),
  SWASTIK("Shubh Swastik", "卐"),
  OM("Sacred Om", "ॐ"),
  NAMASTE("Namaste", "🙏"),
  KALASH("Shubh Kalash", "🏺"),
  OFFER_BADGE("50% OFF", "🏷️"),
  SPECIAL_DEAL("Special Deal", "⚡"),
  CALL_NOW("Call Now", "📞"),
  VERIFIED_SEAL("100% Genuine", "✨"),
  WEDDING_RINGS("Royal Vivah", "💍"),
  FIREWORKS("Festival Sparkle", "🎆"),
  PEACOCK("Mayur Motif", "🦚")
}

data class StickerLayer(
  val id: String = UUID.randomUUID().toString(),
  val stickerType: StickerType,
  val xRatio: Float = 0.5f,
  val yRatio: Float = 0.2f,
  val scale: Float = 1.0f,
  val rotationDeg: Float = 0f,
  val isLocked: Boolean = false
)

enum class TemplateCategory(val title: String) {
  ALL("All"),
  FESTIVALS("Festivals"),
  BUSINESS("Business"),
  WEDDING("Wedding"),
  GREETINGS("Daily Greetings")
}

data class PosterTemplate(
  val id: String,
  val title: String,
  val category: TemplateCategory,
  val subTitle: String,
  @DrawableRes val bgDrawableRes: Int,
  val defaultTexts: List<TextLayer>,
  val defaultStickers: List<StickerLayer> = emptyList(),
  val tags: List<String> = emptyList()
)

data class ExportConfig(
  val width: Int,
  val height: Int,
  val dpi: Int,
  val label: String,
  val description: String
)

val ExportResolutions = listOf(
  ExportConfig(
    width = 3000,
    height = 4000,
    dpi = 300,
    label = "Ultra-HD Print (300 DPI)",
    description = "3000 x 4000 px - Ideal for flex banners & large posters"
  ),
  ExportConfig(
    width = 1080,
    height = 1440,
    dpi = 150,
    label = "Full HD Social (WhatsApp / Insta)",
    description = "1080 x 1440 px - Perfect for quick WhatsApp status & posts"
  ),
  ExportConfig(
    width = 3600,
    height = 4800,
    dpi = 300,
    label = "Mega Flex Banner (High DPI)",
    description = "3600 x 4800 px - Crisp printing for hoarding & exhibition"
  )
)

data class OtaPatchInfo(
  val currentPatch: String = "patch_104_release",
  val patchVersionName: String = "v2.4.2",
  val lastUpdated: String = "2 minutes ago",
  val isShorebirdReady: Boolean = true,
  val newPatchAvailable: Boolean = false,
  val availablePatchName: String = "Festival Special 2026.1",
  val changelog: List<String> = listOf(
    "Added 15+ Navratri & Diwali gold foil frames",
    "Enhanced 3D Hindi extrusion engine",
    "Direct WhatsApp business export integration"
  )
)

sealed class PosterBackground {
  data class DrawableBackground(
    @DrawableRes val resId: Int,
    val name: String,
    val category: String = "Festive"
  ) : PosterBackground()

  data class UriBackground(
    val uriString: String,
    val name: String = "Gallery Photo"
  ) : PosterBackground()

  data class GradientBackground(
    val colorsHex: List<Long>,
    val name: String,
    val isRadial: Boolean = false
  ) : PosterBackground()
}

enum class AppThemeMode(
  val title: String,
  val subtitle: String,
  val primaryColorHex: Long,
  val backgroundColorHex: Long,
  val surfaceColorHex: Long
) {
  ROYAL_DARK(
    title = "Royal Midnight",
    subtitle = "Deep Indigo & Golden Glow",
    primaryColorHex = 0xFFFF7A00,
    backgroundColorHex = 0xFF0D0B1C,
    surfaceColorHex = 0xFF17132F
  ),
  SAFFRON_SUNRISE(
    title = "Saffron Festive",
    subtitle = "Traditional Kesari & Vermilion",
    primaryColorHex = 0xFFFF5722,
    backgroundColorHex = 0xFF1C0D05,
    surfaceColorHex = 0xFF2B1408
  ),
  GOLD_LUXURY(
    title = "Golden Velvet",
    subtitle = "Opulent Royal Gold & Obsidian",
    primaryColorHex = 0xFFFFD700,
    backgroundColorHex = 0xFF14120B,
    surfaceColorHex = 0xFF242014
  ),
  PEACOCK_TEAL(
    title = "Peacock Teal",
    subtitle = "Majestic Cyan & Emerald",
    primaryColorHex = 0xFF06B6D4,
    backgroundColorHex = 0xFF05171B,
    surfaceColorHex = 0xFF0B2930
  )
}

data class PosterColorTheme(
  val id: String,
  val name: String,
  val primaryTextHex: Long,
  val secondaryTextHex: Long,
  val color3DHex: Long,
  val shadowColorHex: Long,
  val footerStyle: FooterStyle,
  val previewColors: List<Long>
)

val PresetColorThemes = listOf(
  PosterColorTheme(
    id = "royal_gold",
    name = "Royal Gold & Maroon",
    primaryTextHex = 0xFFFFD700,
    secondaryTextHex = 0xFFFFF1D6,
    color3DHex = 0xFF8A1E00,
    shadowColorHex = 0xDD000000,
    footerStyle = FooterStyle.CLASSIC_ROYAL,
    previewColors = listOf(0xFFFFD700, 0xFF8A1E00, 0xFF0F172A)
  ),
  PosterColorTheme(
    id = "saffron_festive",
    name = "Saffron Festive Fire",
    primaryTextHex = 0xFFFFFFFF,
    secondaryTextHex = 0xFFFFE082,
    color3DHex = 0xFFD84315,
    shadowColorHex = 0xAA000000,
    footerStyle = FooterStyle.SAFFRON_FESTIVE,
    previewColors = listOf(0xFFFF5722, 0xFFFFE082, 0xFFFFFFFF)
  ),
  PosterColorTheme(
    id = "peacock_cyan",
    name = "Peacock & Neon Cyan",
    primaryTextHex = 0xFF38BDF8,
    secondaryTextHex = 0xFFE0F2FE,
    color3DHex = 0xFF0369A1,
    shadowColorHex = 0xCC000000,
    footerStyle = FooterStyle.MODERN_DARK_GLASS,
    previewColors = listOf(0xFF38BDF8, 0xFF0369A1, 0xFF111827)
  ),
  PosterColorTheme(
    id = "emerald_gold",
    name = "Emerald Heritage",
    primaryTextHex = 0xFFFFD54F,
    secondaryTextHex = 0xFFD1FAE5,
    color3DHex = 0xFF064E3B,
    shadowColorHex = 0xCC000000,
    footerStyle = FooterStyle.DUAL_PILL,
    previewColors = listOf(0xFFFFD54F, 0xFF064E3B, 0xFF10B981)
  ),
  PosterColorTheme(
    id = "minimal_silver",
    name = "Monochrome Silver",
    primaryTextHex = 0xFFFFFFFF,
    secondaryTextHex = 0xFFCBD5E1,
    color3DHex = 0xFF1E293B,
    shadowColorHex = 0x99000000,
    footerStyle = FooterStyle.MINIMAL_ACCENT,
    previewColors = listOf(0xFFFFFFFF, 0xFF94A3B8, 0xFF0F172A)
  )
)
