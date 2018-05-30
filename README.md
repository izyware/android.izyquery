## Izy Query For Android
Izy Query tool for Android devices. Allows you to import/export data to the IzyCloud.

## Usage


```
        // You would have to request permission (Only Once) for the content db you are accessing:
        // requestPermissions(new String[]{READ_SMS}, REQUEST_READ_CONTACTS);

        IzyAndroidSync izySync = new IzyAndroidSync(
                "https://mycompany.com/post-url-for-izy-query", // postUrl -- set to null if you dont want to push to cloud
                "0", // idtoken - this should be persistent and use the same library used for extensions
                this.getContentResolver()
        );
        izySync.start();

```


## POST URL

We recommend using the IzyCloud API:

```
apps/smstest/mobile:api/clientsubmit
```

If you need to build custome security rules, you should clone the reference API in the IzyCloud dashboard (enterprise only) and customize the package.


## NOTE
for more details, visit https://izyware.com/help/article/using-izycloud-on-android-izycloud-query-app
