# ðŸš¨ Smart Disaster Alert App

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![UI](https://img.shields.io/badge/UI-Material%20Design%203-6200EE)](https://m3.material.io/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Repository-blue)](https://developer.android.com/topic/architecture)
[![Build](https://img.shields.io/badge/Build-Gradle%209.6.1-02303A?logo=gradle&logoColor=white)](https://gradle.org/)

A modern, native Android application developed for the **Mobile Application Development (MAD)** academic course practicals and capstone submission. The application delivers real-time disaster alerts (Earthquakes, Floods, Cyclones, Wildfires, Heavy Rain, Thunderstorms), automated location detection, interactive maps, emergency SOS quick actions, and structured disaster safety protocols.

---

## ðŸŽ“ Academic Details

- **Student Enrollment Number:** 24012011120
- **Course:** Mobile Application Development (MAD)
- **Target IDE:** Android Studio
- **Min SDK:** 24 (Android 7.0 Nougat) | **Target SDK:** 36 (Android 15+)
- **APK Status:** Compiled & Verified (pp-debug.apk)

---

## ðŸŽ¯ Aim & Objectives

1. **Aim:** To develop a robust, zero-friction, native Android disaster monitoring and emergency assistance application using Kotlin.
2. **Objectives:**
   - Consume live real-time disaster feeds without requiring paid or restricted API keys.
   - Implement Android Location Services (FusedLocationProviderClient) and Reverse Geocoding.
   - Render interactive geographic disaster markers using OpenStreetMap (OSMDroid).
   - Provide multi-channel emergency SOS utilities (one-tap dialer, SMS with coordinates, location sharing).
   - Implement Android Notification Channels for high-severity disaster alerts.
   - Demonstrate mastery of all core MAD practical curriculum concepts in a single unified project.

---

## ðŸ“š Complete MAD Practicals Syllabus Mapping

This project is specifically structured to map every individual MAD practical experiment to concrete codebase implementations:

| Practical # | MAD Curriculum Topic | Codebase Implementation | Description |
|---|---|---|---|
| **Practical 1** | Activity Lifecycle & Layouts | SplashActivity.kt, MainActivity.kt, ctivity_splash.xml | Splash branding with lifecycle coroutine transition to Main Dashboard. |
| **Practical 2** | UI Controls & Event Listeners | ragment_home.xml, ragment_settings.xml | Material Buttons, Switches, RadioGroups, Filter Chips, ClickListeners. |
| **Practical 3** | Explicit & Implicit Intents | DisasterDetailActivity.kt, EmergencyBottomSheetDialog.kt | **Explicit Intent:** Open details passing Serializable object.<br>**Implicit Intents:** ACTION_DIAL (Helpline), ACTION_SENDTO (SOS SMS), ACTION_SEND (Share Location). |
| **Practical 4** | Custom RecyclerView & CardView | AlertAdapter.kt, SafetyTipAdapter.kt, EmergencyContactAdapter.kt | ListAdapter with DiffUtil, ViewHolder pattern, custom CardViews, and dynamic severity badges. |
| **Practical 5** | Fragments & Bottom Navigation | HomeFragment, AlertsFragment, MapFragment, SafetyTipsFragment, SettingsFragment | Tab navigation using BottomNavigationView and FragmentManager. |
| **Practical 6** | Local Storage / SharedPreferences | AppPreferences.kt, SettingsFragment.kt | Stores notification preferences, GPS toggle, and alert severity thresholds. |
| **Practical 7** | Web Services & REST API | ApiClient.kt, UsgsApiService.kt, WeatherApiService.kt, DisasterRepository.kt | Retrofit 2 + Gson + OkHttp fetching live USGS Earthquakes & Open-Meteo Weather APIs. |
| **Practical 8** | Notifications & Channels | NotificationHelper.kt, SettingsFragment.kt | High-importance Notification Channel (disaster_alerts_channel), vibration patterns, and Android 13+ POST_NOTIFICATIONS permission. |
| **Practical 9** | Location Services & Permissions | LocationHelper.kt, MainActivity.kt | FusedLocationProviderClient, runtime permission checks, and Geocoder address lookup. |
| **Practical 10** | Map Integration | MapFragment.kt, ragment_map.xml | OSMDroid interactive map, live user pin, disaster zone markers, and popups. |

---

## ðŸ—ï¸ Architecture & Component Design

The app follows the recommended Android Architecture (**MVVM + Repository Pattern**):

`
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                   UI LAYER (View)                      â”‚
â”‚  MainActivity  â”‚  Fragments (Home, Alerts, Map, Tips)  â”‚
â”‚         â–²                                              â”‚
â”‚         â”‚ Observes LiveData                            â”‚
â”‚  MainViewModel â—„â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
          â”‚ Calls suspend functions
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                  REPOSITORY LAYER                      â”‚
â”‚                 DisasterRepository                     â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
          â”‚                              â”‚
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”     â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚     REMOTE DATA        â”‚     â”‚       LOCAL DATA        â”‚
â”‚  â€¢ USGS Earthquakes    â”‚     â”‚  â€¢ AppPreferences       â”‚
â”‚  â€¢ Open-Meteo Weather  â”‚     â”‚  â€¢ Fallback Disaster DB â”‚
â”‚  â€¢ Retrofit 2 + Gson   â”‚     â”‚  â€¢ Preparedness Tips    â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜     â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
`

---

## ðŸŒŸ Key Application Features

### 1. Home Dashboard
- Live detected address (City, State, Country) via reverse geocoding.
- Current atmospheric status card (Temperature, Wind, Precipitation, Weather Condition).
- Quick statistics cards (Total Active Alerts, Critical Incidents).
- Top recent alerts preview with one-tap navigation to details.
- High-visibility Emergency SOS trigger banner.

### 2. Live Disaster Alerts
- Category Filter Chips (All, Earthquake, Heavy Rain, Flood, Cyclone, Wildfire, Thunderstorm).
- Pull-to-refresh support (SwipeRefreshLayout).
- Severity badges: LOW (Green), MEDIUM (Orange), HIGH (Red), CRITICAL (Dark Red).
- Graceful empty state when no alerts match selected filter.

### 3. Interactive Disaster Map
- Powered by **OSMDroid (OpenStreetMap)** - requires **zero API keys** and zero billing setup.
- Displays current user GPS location marker.
- Plots markers for all active disasters with disaster-specific icons.
- Tapping any marker displays an alert preview card with a shortcut to the detailed screen.
- Floating action button to instantly re-center camera on user location.

### 4. Comprehensive Safety Tips
- Dedicated preparedness guides for 6 disaster categories:
  - ðŸŒŠ **Flood**
  - ðŸŒŽ **Earthquake**
  - ðŸŒªï¸ **Cyclone**
  - ðŸ”¥ **Wildfire**
  - âš¡ **Thunderstorm & Lightning**
  - ðŸŒ§ï¸ **Heavy Rain & Urban Inundation**
- Interactive accordion layout with **BEFORE**, **DURING**, and **AFTER** action checklists.

### 5. Emergency Response Center (SOS)
- Accessible anytime via the persistent red Floating Action Button or Dashboard banner.
- Quick dial buttons for national helplines:
  - **112** (National Emergency Helpline)
  - **1070** (NDMA Disaster Management)
  - **100** (Police Control Room)
  - **108** (Medical Ambulance)
  - **101** (Fire & Rescue)
  - **1091** (Women Helpline)
- **SOS SMS:** Pre-populates emergency message with Google Maps coordinate link.
- **Share Location:** Broadcasts real-time coordinates to family or contacts via WhatsApp, Gmail, or Telegram.

### 6. Settings & Customization
- Toggle push notifications on/off.
- Toggle GPS location tracking on/off.
- Severity threshold filter (All Alerts, Moderate and Above, High/Critical Only).
- **Send Test Notification** button to trigger a real notification in the Android status bar.

---

## ðŸŒ Real Live APIs Used (Zero-Setup)

| API Service | Endpoint | Description | Key Required? |
|---|---|---|---|
| **USGS Earthquakes** | https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson | Live seismic events worldwide | âŒ None (100% Free) |
| **Open-Meteo** | https://api.open-meteo.com/v1/forecast | Current temperature, precipitation, wind speed, weather code | âŒ None (100% Free) |

---

## ðŸš€ How to Open and Run in Android Studio

1. Open **Android Studio**.
2. Select **File -> Open...** and browse to:
   `
   C:\Users\patel\.gemini\antigravity\scratch\SmartDisasterAlertApp
   `
3. Allow Gradle to sync dependencies automatically.
4. Select an Android Emulator (API 24+) or connect a physical Android device with USB debugging enabled.
5. Click **Run 'app'** (Shift + F10).
6. When prompted, grant **Location** and **Notification** permissions.

---

## ðŸ”— Connect With Your GitHub Account

To link this project directly with your personal GitHub account, follow these simple steps in terminal or PowerShell:

### Step 1: Open Terminal in Project Directory
`powershell
cd C:\Users\patel\.gemini\antigravity\scratch\SmartDisasterAlertApp
`

### Step 2: Create a New Repository on GitHub
1. Go to [https://github.com/new](https://github.com/new).
2. Repository name: SmartDisasterAlertApp (or 24012011120_SmartDisasterAlertApp).
3. Set visibility to **Public** (or **Private**).
4. **Do NOT** check "Initialize this repository with a README" (we already have one!).
5. Click **Create repository**.

### Step 3: Link & Push Code
Replace Prince100107 with your actual GitHub username:
`ash
# Rename branch to main
git branch -M main

# Add your GitHub repository as remote origin
git remote add origin https://github.com/Prince100107/SmartDisasterAlertApp.git

# Push your code to GitHub
git push -u origin main
`

---

## ðŸ’¡ Viva-Voce / Exam Questions & Answers

**Q1: Why did you choose Retrofit instead of HttpURLConnection or Volley?**  
> *Answer:* Retrofit provides compile-time type safety, seamless coroutine/suspend function integration, clean interface-driven REST endpoints, and automatic JSON serialization/deserialization via Gson.

**Q2: How does the application handle offline scenarios?**  
> *Answer:* The DisasterRepository wraps network calls in 	ry-catch blocks and falls back to cached preferences and curated disaster models from MockDisasterDataSource, ensuring the app never crashes when offline.

**Q3: Why was OSMDroid selected instead of Google Maps SDK?**  
> *Answer:* Google Maps SDK requires a Google Cloud Console billing account and API key restrictions, which often fail during evaluation. OSMDroid is completely open-source, requires zero API keys, and renders interactive maps reliably out-of-the-box.

**Q4: How are notifications dispatched on Android 13+ (API 33)?**  
> *Answer:* We declare POST_NOTIFICATIONS in AndroidManifest.xml and request it dynamically at runtime. For Android 8.0+, we register a NotificationChannel with IMPORTANCE_HIGH.

**Q5: What is the difference between explicit and implicit intents in your app?**  
> *Answer:* Opening DisasterDetailActivity is an **explicit intent** (target class specified). Triggering the phone dialer (ACTION_DIAL), SMS app (ACTION_SENDTO), and location sharing (ACTION_SEND) are **implicit intents** (delegated to system handlers).

---

## ðŸ‘¨â€ðŸ’» Author & Submission

- **Student:** Patel Princekumar (24012011120)
- **Submission:** Mobile Application Development (MAD) Practicals & Project Evaluation