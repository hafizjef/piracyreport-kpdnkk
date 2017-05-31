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
- Firebase (Realtime DB, Data Storage, Authentication)
- Timber (Logging)
- Android Support libs (Material Design & Compatibility Support. E.g: Cardview, RecyclerView)
- Butterknife (View Binding, no more findViewById yeay!)
- Glide (Image loading & caching. OOM?)
- Image picker (picking image, lol)
- Leku (Location picker)
- Compressor (Image compression)
- Parceler (POJO -> Android Parcelable)
- gson (JSON Parser)
- Retrofit (HTTP Client)
- MPAndroidChart (Charting)

## Dev Setups
