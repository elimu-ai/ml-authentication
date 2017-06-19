# Gradle Import

To import the library, you first need to add our repository in app/build.gradle:

```
repositories {
    mavenLocal()
    maven {
        url "http://archiva.educativo.eu:8081/repository/internal/"
    }
}
```

Then, add the following dependencies:

```
dependencies {
   ...

   compile 'org.literacyapp:literacyapp-model:1.1.47'
   compile 'org.literacyapp:contentprovider:1.0.7'
   compile 'org.greenrobot:greendao:3.2.0'
}
```

Add the following to the app's main activity:

```
int permissionCheckWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
if (permissionCheckWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        return;
}
        
ContentProvider.initializeDb(this);
```

Also add the following to `AndroidManifest.xml`:

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

You will now have read-only access to the LiteracyApp database and all its content, e.g. letters, numbers, words, story books, audios, images, videos, etc. The content can be accessed via the `org.literacyapp.contentprovider.ContentProvider` helper class.
