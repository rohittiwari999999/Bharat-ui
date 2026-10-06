package com.example.data

import com.example.R
import com.example.model.FontCategory
import com.example.model.PosterTemplate
import com.example.model.StickerLayer
import com.example.model.StickerType
import com.example.model.TemplateCategory
import com.example.model.TextLayer
import com.example.model.UserProfile

object TemplateRepository {

  val defaultUserProfile = UserProfile(
    businessName = "Sharma Jewels & Handicrafts",
    ownerName = "Aarav Sharma",
    designation = "Founder & Designer",
    phone = "+91 98765 43210",
    email = "contact@sharmajewels.in",
    website = "www.sharmajewels.in",
    address = "Johari Bazar, Jaipur, Rajasthan",
    tagLine = "Pure Craftsmanship & Heritage Since 1984",
    photoRes = R.drawable.dummy_owner_avatar_1790749967848,
    logoRes = R.drawable.dummy_brand_logo_1790749952225
  )

  val sampleTemplates: List<PosterTemplate> = listOf(
    PosterTemplate(
      id = "diwali_grand_fest",
      title = "शुभ दीपावली (Grand Diwali)",
      category = TemplateCategory.FESTIVALS,
      subTitle = "Glowing Lamps & Festive Gold Lights",
      bgDrawableRes = R.drawable.diwali_poster_bg_1790749881743,
      defaultTexts = listOf(
        TextLayer(
          text = "शुभ दीपावली",
          xRatio = 0.5f,
          yRatio = 0.28f,
          fontSizeSp = 36f,
          textColorHex = 0xFFFFD700,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          is3D = true,
          depth3D = 7f,
          color3DHex = 0xFF8A1E00,
          hasShadow = true,
          shadowRadius = 14f,
          shadowColorHex = 0xFF000000,
          hasStroke = true,
          strokeColorHex = 0xFF4A0000,
          strokeWidth = 2.5f
        ),
        TextLayer(
          text = "दीपों का यह पावन पर्व आपके जीवन में\nसुख, शांति और समृद्धि लेकर आए।",
          xRatio = 0.5f,
          yRatio = 0.42f,
          fontSizeSp = 18f,
          textColorHex = 0xFFFFF7E6,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          hasShadow = true,
          shadowRadius = 8f
        ),
        TextLayer(
          text = "HAPPY DIWALI & PROSPEROUS NEW YEAR",
          xRatio = 0.5f,
          yRatio = 0.52f,
          fontSizeSp = 14f,
          textColorHex = 0xFFFFB300,
          fontCategory = FontCategory.BOLD_DISPLAY,
          hasShadow = true
        )
      ),
      defaultStickers = listOf(
        StickerLayer(stickerType = StickerType.DIYA, xRatio = 0.22f, yRatio = 0.24f, scale = 1.2f),
        StickerLayer(stickerType = StickerType.DIYA, xRatio = 0.78f, yRatio = 0.24f, scale = 1.2f),
        StickerLayer(stickerType = StickerType.SWASTIK, xRatio = 0.5f, yRatio = 0.16f, scale = 1.1f)
      ),
      tags = listOf("Diwali", "Deepavali", "Festival of Lights", "Lakshmi Puja")
    ),
    PosterTemplate(
      id = "holi_celebration",
      title = "रंगों का उत्सव होली (Holi Festival)",
      category = TemplateCategory.FESTIVALS,
      subTitle = "Vibrant Organic Gulal Explosion",
      bgDrawableRes = R.drawable.holi_poster_bg_1790749904246,
      defaultTexts = listOf(
        TextLayer(
          text = "होली की हार्दिक शुभकामनाएं",
          xRatio = 0.5f,
          yRatio = 0.26f,
          fontSizeSp = 30f,
          textColorHex = 0xFFFFFFFF,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          is3D = true,
          depth3D = 6f,
          color3DHex = 0xFFD81B60,
          hasShadow = true,
          shadowRadius = 12f
        ),
        TextLayer(
          text = "प्यार, सौहार्द और खुशियों के रंगों से भरा रहे आपका जीवन।",
          xRatio = 0.5f,
          yRatio = 0.38f,
          fontSizeSp = 17f,
          textColorHex = 0xFFFFF0F5,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          hasShadow = true
        ),
        TextLayer(
          text = "FESTIVAL OF COLORS",
          xRatio = 0.5f,
          yRatio = 0.48f,
          fontSizeSp = 18f,
          textColorHex = 0xFFFFEB3B,
          fontCategory = FontCategory.BOLD_DISPLAY,
          hasShadow = true,
          hasStroke = true,
          strokeColorHex = 0xFF000000
        )
      ),
      defaultStickers = listOf(
        StickerLayer(stickerType = StickerType.FIREWORKS, xRatio = 0.5f, yRatio = 0.15f, scale = 1.2f),
        StickerLayer(stickerType = StickerType.SPECIAL_DEAL, xRatio = 0.85f, yRatio = 0.22f, scale = 1.0f)
      ),
      tags = listOf("Holi", "Colors", "Gulal", "Dhuleti")
    ),
    PosterTemplate(
      id = "wedding_invitation_royal",
      title = "शुभ विवाह (Royal Wedding Invitation)",
      category = TemplateCategory.WEDDING,
      subTitle = "Gold Mandap Silhouette & Velvet Crimson",
      bgDrawableRes = R.drawable.wedding_poster_bg_1790749918889,
      defaultTexts = listOf(
        TextLayer(
          text = "॥ श्री गणेशाय नमः ॥",
          xRatio = 0.5f,
          yRatio = 0.20f,
          fontSizeSp = 22f,
          textColorHex = 0xFFFFD700,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          hasShadow = true
        ),
        TextLayer(
          text = "शुभ विवाह निमंत्रण",
          xRatio = 0.5f,
          yRatio = 0.29f,
          fontSizeSp = 32f,
          textColorHex = 0xFFFFF1D6,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          is3D = true,
          depth3D = 6f,
          color3DHex = 0xFF6B1212,
          hasShadow = true
        ),
        TextLayer(
          text = "रोहन संग प्रियंका",
          xRatio = 0.5f,
          yRatio = 0.40f,
          fontSizeSp = 28f,
          textColorHex = 0xFFFFD54F,
          fontCategory = FontCategory.ROYAL_SERIF,
          hasShadow = true
        ),
        TextLayer(
          text = "SAVE THE DATE\n18th NOVEMBER 2026",
          xRatio = 0.5f,
          yRatio = 0.52f,
          fontSizeSp = 16f,
          textColorHex = 0xFFFFFFFF,
          fontCategory = FontCategory.BOLD_DISPLAY,
          hasShadow = true
        )
      ),
      defaultStickers = listOf(
        StickerLayer(stickerType = StickerType.OM, xRatio = 0.5f, yRatio = 0.12f, scale = 1.1f),
        StickerLayer(stickerType = StickerType.WEDDING_RINGS, xRatio = 0.5f, yRatio = 0.46f, scale = 1.0f)
      ),
      tags = listOf("Wedding", "Vivah", "Invitation", "Save The Date")
    ),
    PosterTemplate(
      id = "business_mega_sale",
      title = "धमाका सेल (Business Mega Sale)",
      category = TemplateCategory.BUSINESS,
      subTitle = "Corporate Modern Flyer & Bold Deals",
      bgDrawableRes = R.drawable.business_poster_bg_1790749938770,
      defaultTexts = listOf(
        TextLayer(
          text = "FESTIVAL MEGA SALE",
          xRatio = 0.5f,
          yRatio = 0.22f,
          fontSizeSp = 30f,
          textColorHex = 0xFFFF7A00,
          fontCategory = FontCategory.BOLD_DISPLAY,
          is3D = true,
          depth3D = 6f,
          color3DHex = 0xFF8A3000,
          hasShadow = true
        ),
        TextLayer(
          text = "FLAT 50% OFF",
          xRatio = 0.5f,
          yRatio = 0.33f,
          fontSizeSp = 40f,
          textColorHex = 0xFFFFFFFF,
          fontCategory = FontCategory.BOLD_DISPLAY,
          is3D = true,
          depth3D = 7f,
          color3DHex = 0xFF0B1930,
          hasShadow = true,
          hasStroke = true,
          strokeColorHex = 0xFFFF9900,
          strokeWidth = 3f
        ),
        TextLayer(
          text = "On All Handcrafted Gold & Diamond Jewellery\nLimited Period Festive Offer!",
          xRatio = 0.5f,
          yRatio = 0.46f,
          fontSizeSp = 16f,
          textColorHex = 0xFFE2E8F0,
          fontCategory = FontCategory.MODERN_CLEAN,
          hasShadow = true
        ),
        TextLayer(
          text = "VISIT US TODAY OR ORDER ONLINE",
          xRatio = 0.5f,
          yRatio = 0.55f,
          fontSizeSp = 14f,
          textColorHex = 0xFFFFD700,
          fontCategory = FontCategory.BOLD_DISPLAY,
          bgBoxColorHex = 0xDD0F172A
        )
      ),
      defaultStickers = listOf(
        StickerLayer(stickerType = StickerType.OFFER_BADGE, xRatio = 0.85f, yRatio = 0.18f, scale = 1.3f),
        StickerLayer(stickerType = StickerType.CALL_NOW, xRatio = 0.18f, yRatio = 0.55f, scale = 1.0f)
      ),
      tags = listOf("Sale", "Offer", "Jewellery", "Discounts", "Business")
    ),
    PosterTemplate(
      id = "navratri_durga_puja",
      title = "शुभ नवरात्रि (Navratri Mahotsav)",
      category = TemplateCategory.FESTIVALS,
      subTitle = "Golden Mandala & Divine Diya Flame",
      bgDrawableRes = R.drawable.navratri_poster_bg_1790752641907,
      defaultTexts = listOf(
        TextLayer(
          text = "शुभ नवरात्रि",
          xRatio = 0.5f,
          yRatio = 0.28f,
          fontSizeSp = 34f,
          textColorHex = 0xFFFFD700,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          is3D = true,
          depth3D = 7f,
          color3DHex = 0xFF8A1000,
          hasShadow = true
        ),
        TextLayer(
          text = "माँ दुर्गा आपके जीवन में सुख, शांति\nऔर यश की वर्षा करें।",
          xRatio = 0.5f,
          yRatio = 0.42f,
          fontSizeSp = 18f,
          textColorHex = 0xFFFFF7ED,
          fontCategory = FontCategory.HINDI_DEVANAGARI,
          hasShadow = true
        ),
        TextLayer(
          text = "HAPPY NAVRATRI & DURGA PUJA",
          xRatio = 0.5f,
          yRatio = 0.52f,
          fontSizeSp = 14f,
          textColorHex = 0xFFFFB300,
          fontCategory = FontCategory.BOLD_DISPLAY,
          hasShadow = true
        )
      ),
      defaultStickers = listOf(
        StickerLayer(stickerType = StickerType.DIYA, xRatio = 0.2f, yRatio = 0.25f, scale = 1.2f),
        StickerLayer(stickerType = StickerType.DIYA, xRatio = 0.8f, yRatio = 0.25f, scale = 1.2f),
        StickerLayer(stickerType = StickerType.KALASH, xRatio = 0.5f, yRatio = 0.16f, scale = 1.2f)
      ),
      tags = listOf("Navratri", "Durga Puja", "Garba", "Dussehra")
    ),
    PosterTemplate(
      id = "royal_gold_invitation",
      title = "शाही आमंत्रण (Royal Gold Gala)",
      category = TemplateCategory.GREETINGS,
      subTitle = "Midnight Indigo & Embossed Gold Foil",
      bgDrawableRes = R.drawable.royal_gold_bg_1790752665985,
      defaultTexts = listOf(
        TextLayer(
          text = "शाही आमंत्रण",
          xRatio = 0.5f,
          yRatio = 0.28f,
          fontSizeSp = 34f,
          textColorHex = 0xFFFFD700,
          fontCategory = FontCategory.ROYAL_SERIF,
          is3D = true,
          depth3D = 6f,
          color3DHex = 0xFF0B1930,
          hasShadow = true
        ),
        TextLayer(
          text = "YOU ARE CORDIALLY INVITED TO CELEBRATE\nOUR GRAND FESTIVE CELEBRATION",
          xRatio = 0.5f,
          yRatio = 0.42f,
          fontSizeSp = 15f,
          textColorHex = 0xFFE2E8F0,
          fontCategory = FontCategory.BOLD_DISPLAY,
          hasShadow = true
        )
      ),
      defaultStickers = listOf(
        StickerLayer(stickerType = StickerType.VERIFIED_SEAL, xRatio = 0.5f, yRatio = 0.16f, scale = 1.2f)
      ),
      tags = listOf("Royal", "Gold", "Invitation", "Celebration")
    )
  )

  val presetBackgrounds: List<com.example.model.PosterBackground.DrawableBackground> = listOf(
    com.example.model.PosterBackground.DrawableBackground(
      resId = R.drawable.diwali_poster_bg_1790749881743,
      name = "Diwali Diyas",
      category = "Festivals"
    ),
    com.example.model.PosterBackground.DrawableBackground(
      resId = R.drawable.navratri_poster_bg_1790752641907,
      name = "Navratri Mandala",
      category = "Festivals"
    ),
    com.example.model.PosterBackground.DrawableBackground(
      resId = R.drawable.holi_poster_bg_1790749904246,
      name = "Holi Gulal",
      category = "Festivals"
    ),
    com.example.model.PosterBackground.DrawableBackground(
      resId = R.drawable.wedding_poster_bg_1790749918889,
      name = "Wedding Mandap",
      category = "Wedding"
    ),
    com.example.model.PosterBackground.DrawableBackground(
      resId = R.drawable.royal_gold_bg_1790752665985,
      name = "Royal Filigree",
      category = "Luxury"
    ),
    com.example.model.PosterBackground.DrawableBackground(
      resId = R.drawable.business_poster_bg_1790749938770,
      name = "Modern Business",
      category = "Business"
    )
  )

  val presetGradients: List<com.example.model.PosterBackground.GradientBackground> = listOf(
    com.example.model.PosterBackground.GradientBackground(
      name = "Royal Midnight Indigo",
      colorsHex = listOf(0xFF0F172A, 0xFF1E1A38, 0xFF070B19)
    ),
    com.example.model.PosterBackground.GradientBackground(
      name = "Saffron Festive Blaze",
      colorsHex = listOf(0xFFB91C1C, 0xFFEA580C, 0xFFF59E0B)
    ),
    com.example.model.PosterBackground.GradientBackground(
      name = "Emerald Green Velvet",
      colorsHex = listOf(0xFF064E3B, 0xFF047857, 0xFF022C22)
    ),
    com.example.model.PosterBackground.GradientBackground(
      name = "Deep Crimson Velvet",
      colorsHex = listOf(0xFF881337, 0xFF4C0519, 0xFF1F030B)
    ),
    com.example.model.PosterBackground.GradientBackground(
      name = "Dark Gold Luxury",
      colorsHex = listOf(0xFF2C2210, 0xFF45361A, 0xFF140F06)
    ),
    com.example.model.PosterBackground.GradientBackground(
      name = "Peacock Teal Ocean",
      colorsHex = listOf(0xFF0E7490, 0xFF083344, 0xFF021720)
    )
  )

  val prebuiltHindiQuotes: Map<String, List<String>> = mapOf(
    "Diwali" to listOf(
      "दीपों का यह पावन पर्व आपके जीवन में सुख, शांति, समृद्धि और अपार खुशियाँ लेकर आए। शुभ दीपावली!",
      "लक्ष्मी जी की कृपा आप पर सदा बनी रहे। धन, वैभव और यश से परिपूर्ण हो आपका परिवार।",
      "हर घर में उजियारा हो, हर मन में प्रेम का वास हो। प्रकाश पर्व की हार्दिक बधाई!",
      "दीपावली के इस पावन अवसर पर आपके व्यापार और कारोबार में चार चांद लगें।"
    ),
    "Holi" to listOf(
      "रंगों के पावन पर्व होली की हार्दिक शुभकामनाएं! प्यार और उमंग के रंग आपके जीवन को महकाएं।",
      "गुलाल की महक, अपनों का प्यार, मुबारक हो आपको रंगों का यह पावन त्योहार!",
      "बुराई पर अच्छाई की विजय और आनंद का प्रतीक होली आपके जीवन में नई उमंगें लाए।"
    ),
    "Wedding" to listOf(
      "मंगलम् भगवान विष्णुः मंगलम् गरुड़ध्वजः। मंगलम् पुण्डरीकाक्षो मङ्गलाय तनो हरिः॥",
      "दो दिलों का पावन बंधन, दो परिवारों का मधुर मिलन। शुभ विवाह निमंत्रण स्वीकार करें।",
      "Together is a wonderful place to be. You are cordially invited to celebrate our wedding."
    ),
    "Business" to listOf(
      "इस त्योहारी सीजन में पाएं बेहतरीन ऑफर्स और 50% तक की भारी छूट। सीमित समय के लिए!",
      "विश्वास और गुणवत्ता की परंपरा। पधारें और हमारे नए संग्रह का अवलोकन करें।",
      "Quality you trust, prices you love! Visit our showroom or shop online today."
    ),
    "Good Morning / Daily" to listOf(
      "आपका आज का दिन शुभ, मंगलमय और सकारात्मक ऊर्जा से भरा रहे। सुप्रभात!",
      "मेहनत का फल और समस्या का हल, देर से ही सही पर मिलता जरूर है। आपका दिन मंगलमय हो।"
    )
  )
}
