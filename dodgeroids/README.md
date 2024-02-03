# Dodgeroids Android version

## How to build signed bundle for stores in Android Studio 
- Open Main Menu
- Select `Build`
- Select `Generate Signed Bundle / APK`
- Choose `Andoid App Bundle`
- Fill out the data in the following windows
- `aab` file will be in `release` folder once build finished successfully

## How to upload signed bundle to Google Play store
- Log into Developer Console
- On first Dashboard ("Startseite" in german) go into Details of Dodgeroids ("App ansehen" in german)
- In menu on the left side select Production ("Produktion" in german)
- Press create new release "Neuen Release erstellen" in german" in top right corner
- Follow upcoming flow where you can also upload the signed bundle at some point
- At some point there is the possibility to press the three dots in the just uploaded 
  app bundle entry and upload a ReTrace file which has been created during the build in 
  Android Studio in `build/outputs/mapping/release/mapping.txt`
- After you saved to roll out the app, wait until it got approved

## Privacy Policy
The privacy policy can be found in [PRIVACYPOLICY.md](PRIVACYPOLICY.md) 
and online at https://sites.google.com/view/privacypolicyfordodgeroids.