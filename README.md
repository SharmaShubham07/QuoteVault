# QuoteVault – AI-Enhanced Quote App 📱

A fully-featured quote discovery and personalization application built with **Kotlin**, **MVVM**, **Supabase**, and **Material Design**. Created as part of a mobile developer assignment with required modules implemented using modern Android best practices.

<div align="center">
  
  ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
  ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
  ![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
  ![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white)

</div>

## ✨ Features

### ✅ 1. Authentication
- Email/password sign-up & login
- Password reset functionality
- Secured token storage with `SessionManager`
- User profile fetch & update
- Logout with complete session clearance

### 🎨 2. UI & Personalization
- Light/Dark mode toggle
- Accent color support (3 themes)
- Adjustable font size for quotes
- Settings saved locally & synced to user profile
- Modern Material 3 layouts

### 🏠 3. Home Screen
- Quote of the Day (QOTD)
- Categories with interactive chips
- Real-time search with live filtering
- Recent quotes list
- Favorite button with instant toggle

### ❤️ 4. Favorites
- Save/unsave quotes
- Persistent sync with Supabase
- Dedicated Favorites screen

### 🔔 5. Notifications
- Daily "Quote of the Day" notifications
- Customizable notification time
- Local push notifications
- Background functionality via `AlarmManager`

### 📤 6. Sharing & Export
- Share quotes as plain text
- Generate stylish shareable quote cards
- Multiple card styles (Blue, Sunset, Dark, Nature)
- Export/share quote cards as images

### 🧩 7. Home Screen Widget
- Displays current Quote of the Day
- Auto-updates daily
- Tap widget → opens app to QOTD
- Manual refresh support

## 🛠️ Tech Stack

- **Language**: Kotlin (100%)
- **UI**: XML layouts
- **Architecture**: MVVM (Model-View-ViewModel)
- **Asynchronous**: Coroutines + LiveData
- **Backend**: Supabase (Auth & Database)
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Picasso
- **UI Components**: Material Components
- **Widgets**: App Widgets
- **Scheduling**: AlarmManager + PendingIntent

## 🗄️ Project Structure

```
com.example.brewappsassignment
│
├── data
│   ├── local       # SessionManager, preferences
│   ├── remote      # Supabase APIs, interceptors, DTOs
│   └── repository  # Data repositories
│
├── ui
│   ├── auth        # Login, Signup, Reset screens
│   ├── home        # Home screen, QOTD, categories
│   ├── favorites   # Saved quotes screen
│   ├── profile     # User profile screen
│   └── share       # Quote card generator
│
├── widget          # Home screen widget implementation
└── utils           # Helpers, extensions, utilities
```

## 🔧 Setup Instructions

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/SharmaShubham07/QuoteVault.git
cd quotevault
```

### 2️⃣ Configure Supabase
Create or update `SupabaseConstants.kt` in your project:

```kotlin
object SupabaseConstants {
    const val SUPABASE_URL = "https://zkugfsufspnthnfuepso.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InprdWdmc3Vmc3BudGhuZnVlcHNvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjgyODE5ODcsImV4cCI6MjA4Mzg1Nzk4N30.ATA8tb9rgV-kDIPU3rJKhlpErHsGcdVbyLtiKF5AHv0"
}
```

### 3️⃣ Enable API Permissions in Supabase Dashboard
1. Go to **Authentication** → Enable Email Authentication
2. For `quotes` table: Enable `select`, `insert`, `update` for authenticated users
3. For `favorites` table: Enable REST APIs and proper permissions
4. Create necessary tables with appropriate schemas

### 4️⃣ Install Dependencies
Android Studio will handle dependencies automatically via Gradle. Ensure you have:
- Android Studio Arctic Fox or later
- Android SDK 24+ (minimum SDK 24, target SDK 34)
- Kotlin 1.9.0+

### 5️⃣ Run the Application
1. Open the project in Android Studio
2. Connect an Android device or start an emulator
3. Click **Run** ▶️ or press `Shift + F10`

## 🤖 AI Coding Workflow

I used a hybrid human + AI workflow for development:

### **AI Tools Used:**
- **ChatGPT** for:
  - Generating boilerplate architecture
  - Drafting ViewModels, adapters, and API interfaces
  - Speeding up repetitive UI layout creation
  - Suggesting solutions for errors and logs
  - Writing shareable card generation logic
  - 
  AI-Powered Features (DeepSeek Integration)
  AI Quote Generation: Generate custom quotes based on mood, topic, or inspiration
  Smart Recommendations: Personalized quote suggestions using AI
  Quote Analysis: Deep insights into quote meaning and context
  Language Translation: Translate quotes to multiple languages
  Quote Expansion: Get detailed explanations and interpretations
  
- **Material Design Builder** for UI inspiration
- **GitHub Copilot** (optional) for in-editor suggestions
- **Stitch** for theme references and design mockups

### **Development Approach:**
1. Start with core architecture (MVVM, Retrofit, Supabase)
2. Generate base modules (Auth, Quotes API, Favorites)
3. Iterative refinement of UI components
4. Manual polishing and optimization
5. Feature-by-feature testing
6. AI-assisted debugging for complex scenarios

## 🎨 Design Resources

**Figma Design File:** https://stitch.withgoogle.com/projects/6214879585536515260
## ⚠️ Known Limitations / TODO

- [ ] Some animations can be smoother
- [ ] Expand card templates to more styles
- [ ] In-app theme preview integration in settings
- [ ] Add advanced caching with Room Database
- [ ] Improve widget preview accuracy in XML editor

## 📄 License

This project is licensed under the **MIT License** – free to use, modify, and distribute.

---

<div align="center">
  
  **QuoteVault** – Your daily dose of inspiration, beautifully delivered.
  
</div>
