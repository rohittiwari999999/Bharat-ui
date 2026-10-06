# 🛡️ Google Play Console - Data Safety Form Guide (डेटा सुरक्षा उत्तर)

Google Play Console me **App Content > Data Safety (ऐप सामग्री > डेटा सुरक्षा)** section bharte samay in exact sawalon ke jawab select karein:

---

### Step 1: Data Collection and Security (डेटा संग्रह और सुरक्षा)

| Question in Play Console | Your Answer | Explanation |
| :--- | :--- | :--- |
| **Does your app collect or share any of the required user data types?** | **No** *(or Yes for local processing if prompted)* | Select **No** if user data is processed only locally on device and not sent to your own private server. |
| **Is all of the user data collected by your app encrypted in transit?** | **Yes** | All network communications (Gemini API, Google Services) use HTTPS (TLS 1.3). |
| **Do you provide a way for users to request that their data be deleted?** | **Yes** | Users can clear profile data inside the app or simply uninstall the app. |

---

### Step 2: Specific Data Types (विशिष्ट डेटा प्रकार)

Agar aap "Yes" select karte hain local branding feature ke liye:

#### 1. Photos and Videos (फ़ोटो और वीडियो)
* **Collected?** No (Images are picked via the zero-permission Android Photo Picker for on-device poster rendering).
* **Shared?** No (Never shared with third parties without user's explicit action).

#### 2. Personal Info (व्यक्तिगत जानकारी - Name, Email, Phone number)
* **Collected?** Only locally stored on device for Auto-Footer branding stamp.
* **Purpose:** App functionality (Personal branding on festival posters).
* **Ephemeral processing?** Yes / Stored locally.
* **Required or Optional?** Optional (user can customize or leave blank).

#### 3. App Info and Performance (क्रैश लॉग / डायग्नोस्टिक्स)
* **Collected?** Handled by Google Play standard analytics if enabled.

---

### Step 3: Device or Other IDs
* **Does your app collect Device or Other IDs?**
  * Answer: **No** (PosterBanao does not use Advertising ID or Device serial numbers).

---

### Summary for Submission:
✅ **Data is encrypted in transit (HTTPS)**
✅ **Zero data sold to third-party data brokers**
✅ **Users can delete their data anytime**
✅ **Complies with Google Play Families & Children policy (Target 13+)**
