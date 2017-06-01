# Piracy Report via Android App
This mobile application are created to act as a replacement of current (piracy) reporting systems that are 
tedious and require human intervention. Current implementation of reporting system require the user to place a
phone call or lodge a report via e-mail. By using this app, the user will submit report directly into the system. No phone calls/email
required.

## Platform support
- Android OS

## Function/Modules
- Lodge report (user)
  - Attach location information
  - Attach images regarding the report (4)
- View & Assign report to specific staff (Administrator)
- Access reports details assigned by admin (Staff)
- Send notification emails (System)

### External Libs
- [Firebase](https://firebase.google.com/) (Realtime DB, Data Storage, Authentication)
- [Timber](https://github.com/JakeWharton/timber) (Logging)
- [Android Support libs](https://developer.android.com/topic/libraries/support-library/index.html) (Material Design & Compatibility Support. E.g: Cardview, RecyclerView)
- [Butterknife](http://jakewharton.github.io/butterknife/) (View Binding, no more findViewById yeay!)
- [Glide](https://github.com/bumptech/glide) (Image loading & caching. OOM?)
- [Image picker](https://github.com/esafirm/android-image-picker) (picking image, lol)
- [Leku](https://github.com/SchibstedSpain/Leku) (Map Location picker)
- [Compressor](https://github.com/zetbaitsu/Compressor) (Image compression)
- [Parceler](http://parceler.org/) (POJO -> Android Parcelable)
- [gson](https://github.com/google/gson) (JSON Parser)
- [Retrofit](http://square.github.io/retrofit/) (HTTP Client)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) (Charting)

## Dev Setups
**Prerequisites**
- Android Studio
- Firebase Account

**Setup**
1. Clone this repository: `git clone https://github.com/sudoes/piracyreport-kpdnkk/`
2. Open project using Android studio
3. Clean & Rebuild the project
4. Open [console.firebase.google.com](https://console.firebase.google.com/) and create a new project
5. Download `google-services.json` from firebase console to the root_project_dir/app
6. Install apk to devices (play service required)
