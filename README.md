# jackandjill
Android Build Experiments

##### Bare minimum compile-able and install-able Android Application
 Bare minimum android application should consist of a AndroidManifest.xml,
 may be an Activity or other android component, and may be a launcher for app icon in res.
 
 See directory *bare_minimum_android_app* for example.
  

##### Building APK with Android SDK Command line Tools

Creating apk using command line tools from Android Sdk consists of the below steps. 
For reference see directory *build_with_sdk_tools*

1. Generate `R.java`
2. Compile all java source files (including R.java)
3. Package
4. Sign
5. ZipAlign

- **Pre-requisites**  
1. Following command line instructions has been tested on Windows environment (Windows 10)
2. Assuming you have `%ANDROID_HOME%` environment variable has been set to Android sdk path
 which was in my case "C:\Users\m.qasim\AppData\Local\Android\Sdk"
3. Assuming you have buildTools/28.0.x/ is at your `%PATH%` for easily running `appt/dx/jarsigner/zilalign` etc.  
4. Also obviously, `jdk` at your `%PATH%` (i've added the studio embedded jdk to `%PATH%`)  
To verify environment variables on windows, use `echo %PATH%` or `echo %ANDROID_HOME%`

- **Generate** `R.java`   
`R.java` contains all the id's generated from resources to be referenced in code.
It can be generated using `aapt` command line tool available in `android_sdk/build-tools/28.0.3/appt`.  
  
  
` aapt package -f -M ./src/main/AndroidManifest.xml -I "%ANDROID_HOME%/platforms/android-28/android.jar" -S src/main/res -J build/gen -m`

- **Compile all Java source files (including `R.java`)**  
Since android 8, jack toolchain has been deprecated, we will use `javac` for compiling source files to 
classes and then will use `dx` for dexing.

Since our example has only two source files, i-e MainActivity.java and R.java, we'll compile them both one by one.  

`javac -d build/classes -bootclasspath %ANDROID_HOME%/platforms/android-28/android.jar src/main/java/io/github/kosmologist/jackandjill/*.java`  
  
`javac -d build/classes -bootclasspath %ANDROID_HOME%/platforms/android-28/android.jar build/gen/io/github/kosmologist/jackandjill/*.java`  
  
Now dexing these class files as Android do not understand java byte directly, instead it uses special bytecode 
written for android runtime (ART) or previously delvik.  

`dx --dex --output=classes.dex build/classes`
  
 - **Package**  
 Since we have transformed our code into dex format, now it's time to package it with our resources as 
 android application package or apk.  
 
 `aapt package -f -M src/main/AndroidManifest.xml -I "%ANDROID_HOME%/platforms/android-28/android.jar" -S src/main/res -F build/handlbuilt.apk`  
 
 Now adding classes.dex file to above created package
  
`aapt add build/handbuilt.apk classes.dex`  
  
 - **Sign**  
 Since every android app must be signed before running on android (both debug and release) variants, we'll use 
 default debug keystore packaged with android sdk to sign our debug test apk.  
 
 `jarsigner -verbose -keystore C:/Users/m.qasim/.android/debug.keystore -storepass android -keypass android build/handbuilt.apk androiddebugkey`  

- **ZipAlign**  
To compress all resources, we should use tool `zipalign` as our last step:  
  
 `zipalign -f 4 build/handbuilt.apk handbuilt-aligned.apk`   
   
- **Install**  
To install the above created apk, we can use `adb` as following or can be manually transfered to sdcard and install
using phone packagemanager.  

`adb install handbuilt-aligned.apk`
 
 
**References**  
- https://czak.pl/2016/05/31/handbuilt-android-project.html
- https://czak.pl/2016/01/13/minimal-android-project.html
- http://tools.android.com/tech-docs/jackandjill#TOC-Compilation-support
- https://github.com/czak/minimal-android-project
- https://medium.com/authmane512/how-to-build-an-apk-from-command-line-without-ide-7260e1e22676
