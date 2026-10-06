# 🚀 Google Play Console Par App Publish Karne Ka Step-by-Step Guide (हिंदी + English)

Yeh comprehensive guide aapko **PosterBanao** app ko Google Play Store par bina kisi error ke successfully publish karne me madad karega.

---

## 📋 Table of Contents (विषय सूची)
1. **Prerequisites (शुरुआती ज़रूरतें)**
2. **Google Play Console Account Setup**
3. **Create New App (नया ऐप बनाएं)**
4. **App Content & Policy Declarations (महत्वपूर्ण नीतियां)**
5. **Main Store Listing (स्टोर लिस्टिंग और ग्राफिक्स)**
6. **Upload App Bundle / APK (रिलीज़ अपलोड करें)**
7. **Testing Tracks & 14-Day Testing Requirement (महत्वपूर्ण नियम)**
8. **Production Rollout & Review Submission (अंतिम सबमिशन)**

---

## 1. Prerequisites (शुरुआती ज़रूरतें)
* **Google Play Developer Account:** Ek baar ka $25 USD registration fee hota hai Google ki taraf se.
* **App Bundle (.aab) ya APK:** Is folder me aapko generate kiya hua APK aur AAB mil jayega (`/play_console_publish/bin/`).
* **App Icon:** 512 x 512 px PNG (`/play_console_publish/assets/app_icon_512.png`).
* **Feature Graphic:** 1024 x 500 px PNG/JPG (`/play_console_publish/assets/feature_graphic_1024x500.png`).
* **Screenshots:** Kam se kam 2 phone screenshots (Streaming emulator se screenshot le sakte hain).
* **Privacy Policy URL:** `PRIVACY_POLICY.html` ko free host karein (GitHub Pages, Firebase Hosting, ya Google Sites par).

---

## 2. Play Console Me Naya App Kaise Banayein (Create App)
1. Browser me **[play.google.com/console](https://play.google.com/console)** kholein aur login karein.
2. Dashboard par upar right side me **"Create app" (ऐप बनाएं)** button par click karein.
3. Details bharein:
   * **App name:** `PosterBanao: Festival & Ads`
   * **Default language:** English (India) ya English (United States)
   * **App or Game:** App
   * **Free or Paid:** Free
   * **Declarations:** Developer Program Policies aur US Export laws ke dono checkboxes ko check karein.
4. Neeche **"Create app"** par click karein.

---

## 3. Set Up Your App (ऐप सामग्री और नीतियां)
Play Console ke left sidebar me **"App content" (ऐप सामग्री)** section me jayein. Wahan niche diye gaye sabhi sections ko pura karein:

### A. Privacy Policy (गोपनीयता नीति)
* Apna host kiya hua Privacy Policy link paste karein (e.g., `https://your-username.github.io/privacy-policy.html`).
* Save karein.

### B. App Access (ऐप एक्सेस)
* Option select karein: **"All functionality is available without special access"** (Kyunki app me koi hidden login boundary nahi hai, bina login ke bhi canvas editor access hota hai).

### C. Ads (विज्ञापन)
* Select karein: **"No, my app does not contain ads"** (Agar future me AdMob lagate hain tab Yes karein).

### D. Content Rating (कंटेंट रेटिंग)
* **Start questionnaire** par click karein.
* Apna email address dalein.
* Category select karein: **"All Other App Types"** ya **"Utility / Productivity / Art"**.
* Sawalon ke jawab: Sabhi violence, offensive language, gambling ke sawalon par **"No"** select karein.
* Save aur **Summary** submit karein. Aapko **Everyone (3+)** ya **PEGI 3** rating mil jayegi.

### E. Target Audience and Content (लक्षित दर्शक)
* Target age groups: **13-15**, **16-17**, and **18 and over** select karein.
* "Could your store listing unintentionally appeal to children?": **No** select karein.

### F. News Apps
* Select karein: **"No, this is not a news app"**.

### G. COVID-19 Contact Tracing
* Select karein: **"My app is not a publicly available COVID-19 contact tracing or status app"**.

### H. Data Safety (डेटा सुरक्षा)
* Is folder me maujood `DATA_SAFETY_FORM_ANSWERS.md` file ko dekhkar bilkul wahi vikalp select karein.

### I. Financial Features
* Select karein: **"My app doesn't provide any financial features"**.

---

## 4. Main Store Listing (स्टोर लिस्टिंग)
Left sidebar me **"Grow users" > "Store presence" > "Main store listing"** par jayein:
1. **App details:**
   * App name: `PosterBanao: Festival & Ads`
   * Short description: `Create festival posters, business ads, banners & wedding cards with 3D text.`
   * Full description: `APP_DETAILS_METADATA.md` se copy karke paste karein.
2. **Graphics:**
   * **App icon:** 512x512 PNG upload karein.
   * **Feature graphic:** 1024x500 PNG upload karein.
   * **Phone screenshots:** 2 se 8 screenshots upload karein.
3. Neeche **"Save"** par click karein.

---

## 5. Release Upload Karna (AAB / APK)
Google Play Store par new apps ke liye **Android App Bundle (.aab)** format standard hai:
1. Left sidebar me **"Release" > "Production"** (ya **"Testing" > "Closed testing"**) par jayein.
2. Upar right me **"Create new release"** par click karein.
3. **Play App Signing:** Google Play App Signing automatically enable ho jayega (Recommended).
4. **App bundles:** Apna `.aab` file drag-and-drop karein (ya testing ke liye APK upload karein).
5. **Release name:** `1.0.0 (1)`
6. **Release notes (व्हाट्स न्यू):**
   ```text
   Initial Release of PosterBanao!
   • 100+ Indian festival & business templates
   • AdBanao-style automated personal branding footer
   • PixelLab 3D text engine with Devanagari Hindi fonts
   • Ultra-HD 3000x4000 (300 DPI) print-ready export
   ```
7. **Next** par click karein aur review warnings check karein.

---

## 6. Personal Developer Accounts Ke Liye 14-Day Testing Rule
> ⚠️ **IMPORTANT GOOGLE PLAY POLICY (November 2023 se lagu):**
> Agar aapka Google Play Console account personal hai (Individual account) aur 13 November 2023 ke baad bana hai:
> * Google Play Store par directly Production me publish karne se pehle **Closed Testing** me **20 testers** ko kam se kam **14 din lagatar** opt-in rakhna anivarya hai.
> * **Kaise karein:**
>   1. Play Console me **"Testing" > "Closed testing"** me jayein.
>   2. Ek track banayein aur usme 20 friends/family/colleagues ki Gmail IDs add karein.
>   3. Release create karke submit karein.
>   4. 14 din pure hone ke baad Play Console me **"Apply for production"** ka option khul jayega, jiske baad app public ho jayegi!

---

## 7. Submission & Approval Time
* Submissions aamtaur par **24 se 72 ghante (1 se 3 din)** me review hokar live ho jaate hain.
* Review status aapke Play Console Dashboard par "In review" ke roop me dikhai dega.
