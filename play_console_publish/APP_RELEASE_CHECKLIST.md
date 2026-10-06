# ✅ Google Play Console - Pre-Launch Checklist (पब्लिश करने से पहले की चेकलिस्ट)

Apna app Google Play Store par submit karne se pehle in sabhi items ko check karein:

---

## 1. Store Assets Checklist (ग्राफ़िक्स चेकलिस्ट)
- [x] **App Name:** `PosterBanao: Festival & Ads` (28 chars, max 30)
- [x] **Short Description:** Included in `APP_DETAILS_METADATA.md` (77 chars, max 80)
- [x] **Full Description:** Rich description highlighting Devanagari Hindi support, 3D text, AdBanao footer, and Ultra-HD export
- [x] **High-Res App Icon (512x512):** Ready in `play_console_publish/assets/app_icon_512.jpg`
- [x] **Feature Graphic Banner (1024x500):** Ready in `play_console_publish/assets/feature_graphic_1024x500.jpg`
- [ ] **Phone Screenshots (2-8 images):** Streaming emulator ya real phone se poster editor ke screenshots le kar upload karein

---

## 2. Policy & Compliance Checklist (नीतियां)
- [x] **Privacy Policy:** Ready in `play_console_publish/PRIVACY_POLICY.html` (Free hosting on GitHub Pages / Google Sites / Firebase)
- [x] **Data Safety Form Answers:** Documented step-by-step in `play_console_publish/DATA_SAFETY_FORM_ANSWERS.md`
- [x] **Content Rating:** Standard General Audience (Everyone / PEGI 3) questionnaire
- [x] **Target Audience:** Set to 13+ (Avoids complex children/family policy burdens)
- [x] **Permissions Policy Compliance:** No invasive storage permissions requested (`READ_EXTERNAL_STORAGE` avoided, Android zero-permission Photo Picker used)
- [x] **Ads Declaration:** Declared as "No Ads"

---

## 3. Technical Checklist (तकनीकी चेकलिस्ट)
- [x] **Package Name (Application ID):** `com.aistudio.postermaker.bxzpq` (Unique ID, not default com.example)
- [x] **Target SDK:** 36 (Complies with Google Play requirement of latest Android SDK)
- [x] **Min SDK:** 24 (Supports 95%+ Android devices worldwide)
- [x] **Edge-to-Edge:** Jetpack Compose `enableEdgeToEdge()` enabled
- [x] **Compiled APK / AAB:** Generated in `play_console_publish/bin/`

---

## 4. Play Console Upload Steps
1. Play Console par jayein -> `Production` ya `Closed Testing` -> `Create new release`
2. `play_console_publish/bin/` se `.aab` ya `.apk` file upload karein
3. Release notes paste karein
4. Save and Review Release
5. Submit for Review!
