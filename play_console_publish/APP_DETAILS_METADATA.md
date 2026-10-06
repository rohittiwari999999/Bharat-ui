# 📱 Google Play Console - Store Listing Metadata

Yeh file Google Play Console ke **Main Store Listing (मुख्य स्टोर प्रविष्टि)** me copy-paste karne ke liye complete details provide karti hai.

---

## 1. Primary App Information (प्राथमिक जानकारी)

| Field | Value (Play Console Limit) | Details & Rules |
| :--- | :--- | :--- |
| **App Name (ऐप का नाम)** | `PosterBanao: Festival & Ads` | **28 characters** (Play Console allows max 30 characters). Emojis, ALL CAPS, or terms like "Free", "Best", "#1" are prohibited by Play Policy. |
| **Short Description (संक्षिप्त विवरण)** | `Create festival posters, business ads, banners & wedding cards with 3D text.` | **77 characters** (Play Console limit: 80 characters). |
| **Package Name (Application ID)** | `com.aistudio.postermaker.bxzpq` | Unique Android package identifier configured in Gradle. |
| **App Category** | `Art & Design` *(Alternative: Business)* | Select **Art & Design** for maximum discoverability. |
| **Tags (टैग)** | `Graphic Design`, `Poster Maker`, `Banner Maker`, `Festival Wishes`, `Social Media` | Select top 5 relevant tags in Play Console. |
| **Target Audience** | `Ages 13 and above (General Audience)` | Suitable for everyone without requiring special children's policy (Families policy). |
| **Contains Ads (विज्ञापन)** | `No` (Set to "Yes" only if you integrate AdMob later). | Currently no third-party banner ads are integrated. |
| **In-App Purchases** | `No` | No billing permission required at initial launch. |

---

## 2. Full Description (विस्तृत विवरण) - 4000 Characters Limit

```text
PosterBanao is India’s all-in-one graphic design studio to create stunning Indian festival posters, business branding banners, promotional flyers, and royal wedding invitations in seconds!

Combining the ease of Canva, automated personal branding of AdBanao, and the professional typography controls of PixelLab, PosterBanao empowers shop owners, entrepreneurs, and digital creators to generate marketing graphics with zero design skills.

✨ KEY FEATURES:

1. AUTOMATED PERSONAL BRANDING (AdBanao Style)
• One-time setup: Add your Business Name, Owner Name, Contact Number, Email, Website, and Logo.
• Smart Auto-Footer: Appends a designer footer strip to every poster automatically with 7 customizable styles (Classic Royal, Saffron Festive, Dark Glassmorphism, Minimal Clean, Floating Badge, Golden Velvet, and Corporate Strip).

2. 100+ INDIAN FESTIVAL & GREETING TEMPLATES
• Diwali, Dhanteras, Bhai Dooj & Chhath Puja
• Holi, Rangpanchami & Dhuleti
• Navratri, Durga Puja & Dussehra
• Eid ul-Fitr & Eid al-Adha
• Raksha Bandhan, Janmashtami & Ganesh Chaturthi
• Independence Day, Republic Day & Gandhi Jayanti
• Daily Suvichar, Good Morning wishes & Motivational quotes

3. PRO-EDITOR (PixelLab 3D Engine)
• Rich Devanagari Hindi & Calligraphy fonts
• True 3D text extrusion with adjustable depth
• Drop shadows, blur radius, strokes, and background highlight pills
• Multi-layer management: Reorder, duplicate, lock, or delete layers
• Festive stickers & emojis (🪔 Diya, 卐 Swastik, ॐ Om, Kalash, Peacock, Discount Badges)

4. CUSTOM BACKGROUNDS & THEMES
• HD festive backdrops curated for Indian business marketing
• Rich gradients (Deep Royal Indigo, Saffron Fire, Peacock Teal)
• Import your own product photos from phone gallery (zero permissions required)
• 4 App Themes (Royal Midnight, Saffron Festive, Golden Velvet, Peacock Teal)

5. GEMINI AI QUOTES & SHAYARI
• Generate custom Hindi shayari, festival greetings, and promotional sales captions with one tap.

6. ULTRA-HD 3000x4000 (300 DPI) EXPORT
• Save uncompressed print-ready posters suitable for large flex printing, banners, hoardings, or instant WhatsApp & Instagram status sharing without pixelation.

Designed proudly in India for local businesses, MSMEs, jewelers, sweet shops, cloth stores, doctors, agencies, and social influencers!
```

---

## 3. Contact & Support Details for Store Listing

* **Developer Email:** `support@sharmajewels.in` *(Apna valid support email dalein)*
* **Developer Phone:** `+91 98765 43210` *(Play Console requires contact details for India)*
* **Website URL:** `https://sharmajewels.in` *(Optional par recommended)*
* **Privacy Policy URL:** `https://your-domain.com/privacy-policy.html` *(Host `privacy_policy.html` from this folder)*

---

## 4. Graphics & Asset Specifications

| Asset | Exact Dimensions | File Format | Status |
| :--- | :--- | :--- | :--- |
| **App Icon** | 512 x 512 px | 32-bit PNG (with alpha) or High-res JPG | Generated in `/play_console_publish/assets/` |
| **Feature Graphic** | 1024 x 500 px | JPG or 24-bit PNG (no alpha) | Generated in `/play_console_publish/assets/` |
| **Phone Screenshots** | Min 2, Max 8 (1080 x 1920 or 1080 x 2400 px) | 16:9 or 9:16 PNG/JPG | Capture directly from streaming emulator or real device |
| **7-inch Tablet Screenshots** | Min 1 (Optional for phone-only apps) | 16:10 or 16:9 | Optional |
| **10-inch Tablet Screenshots** | Min 1 (Optional for phone-only apps) | 16:10 or 16:9 | Optional |
