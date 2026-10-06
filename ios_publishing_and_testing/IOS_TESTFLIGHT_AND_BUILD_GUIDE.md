# 🍎 iOS App Testing & Distribution Guide (Apple App Store / TestFlight)

Yeh guide batata hai ki **PosterBanao** app ko iPhone / iPad par test karne aur Apple App Store par publish karne ke liye kya zaroori hai.

---

## ⚠️ Important Platform Fact (Android vs iOS)
* **Android:** Linux/Android cloud environment me **APK** aur **AAB** direct build ho jata hai (`play_console_publish/bin/` me available hai).
* **iOS (iPhone/iPad):** Apple ke niyam ke anusaar, iOS apps (`.ipa` file) ko build aur sign karne ke liye **macOS aur Xcode toolchain** ki zaroorat hoti hai.
* Agar aapke paas physical Mac computer nahi hai, to bhi aap **Cloud CI/CD (GitHub Actions / Codemagic)** ke zariye **free macOS runner** par iOS IPA build karke **Apple TestFlight** par test kar sakte hain!

---

## 🛠️ Option 1: Free Cloud CI/CD Se iOS App Build Karna (Without Mac)

Aapko physical Mac khareedne ki zaroorat nahi hai. Humne is project me GitHub Actions workflow file add kar di hai:
📁 **`.github/workflows/ios_build.yml`**

### Kaise Chalayein:
1. Is project ko apne **GitHub Repository** me push karein.
2. GitHub Repository me **Settings > Secrets and variables > Actions** me jayein.
3. Apple Developer account ke credentials add karein:
   * `APP_STORE_CONNECT_API_KEY`
   * `APP_STORE_CONNECT_ISSUER_ID`
4. GitHub Actions tab me jakar **"Build iOS App & IPA"** workflow ko run karein.
5. GitHub automatically Apple ke **`macos-latest` cloud server** par Xcode run karega aur testable `.ipa` file generate karke TestFlight par bhej dega!

---

## 📱 Option 2: TestFlight Par iOS Testing Kaise Karein (Step-by-Step)

Apple TestFlight iPhone users ke liye official beta testing platform hai.

### Step 1: Apple Developer Account
* [developer.apple.com](https://developer.apple.com) par register karein ($99 USD/year Apple Developer fee).

### Step 2: App Store Connect Me App Banayein
1. [appstoreconnect.apple.com](https://appstoreconnect.apple.com) par login karein.
2. **"My Apps"** > **"+" (New App)** par click karein.
3. Details dalein:
   * **Platforms:** iOS
   * **Name:** PosterBanao: Festival & Ads
   * **Primary Language:** English
   * **Bundle ID:** `com.aistudio.postermaker.bxzpq`
   * **SKU:** `posterbanao01`

### Step 3: TestFlight Build Upload Karein
* GitHub Actions ya Xcode ke zariye build kiya gaya `.ipa` App Store Connect me upload ho jayega.
* **TestFlight** tab me jakar build status "Ready to Test" ho jayega.

### Step 4: Testers Ko Invite Karein
* **Internal Testers:** Apni team ke 100 tak Apple IDs add kar sakte hain (Immediate access, no Apple review required).
* **External Testers:** 10,000 tak users ko Public Link ke zariye invite kar sakte hain.
* Testers apne iPhone me **TestFlight App** (App Store se free) download karke link par tap karenge aur PosterBanao app turant unke iPhone me install ho jayegi!

---

## 📄 Option 3: Direct Ad-Hoc / Enterprise IPA Install (Testing ke liye)
Agar bina TestFlight ke kisi specific iPhone par directly install karna ho:
1. Us iPhone ka **UDID** (Unique Device Identifier) Apple Developer Portal me register karein.
2. Ad-Hoc Provisioning Profile generate karein.
3. Build kiya gaya `.ipa` file **Diawi.com** ya **InstallOnAir.com** par upload karke QR code scan karke iPhone me install karein.
