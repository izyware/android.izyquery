

# Phone Setup
While not strictly required, we recommend that you use anydesk on Andoid to remotely navigate the UI.

## Termux + SSH
* Install [f-droid] 
* Install [termux] through f-droid
    * Android 10: You may get an error from "Google Play Protect" saying that Unsafe app blocked. Install anyways.
* Get system info (Android version, Device model, etc.)

        termux-info

* Setup SSH

    
        pkg install root-repo
        pkg upgrade
        pkg install openssh
        # setup a password
        passwd
        # start ssh
        sshd
        
This will setup ssh on port 8022. You can now ssh and rsync into the container (no user name required) using izy.devops:

    izy.devops "ssh?shell" .
    

## Device Automation at startup
Follow these

* Install tools 

        apt install vim rsync nodejs git
        
* Install [termux-boot] through f-droid


Note that the container home ("~") path will be `/data/data/com.termux/files/home`:
    
    izy.devops "rsync?upload" CONTAINER_PATH "~/plat/p/apps/android/termux/home/boot/" "/data/data/com.termux/files/home/.termux/boot/"

To run termux scripts at boot time and have it running in the background `termux-wake-lock` is needed. This will show a warning: "termux will be able to run in the background. Its battery usage wont be restricted".


## Additional Device functionality
* Install [termux.api]: Make sure to install the API app AND the package











# Using izy-proxy macros and functions
The SDK has syntactical parity with izy-proxy. For more information refer to izy-proxy documentation. 

## Izy Query For Android
Izy Query tool for Android devices. Allows you to import/export data to the IzyCloud. 

Usage:


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


We recommend using the IzyCloud API:

```
apps/smstest/mobile:api/clientsubmit
```

If you need to build custome security rules, you should clone the reference API in the IzyCloud dashboard (enterprise only) and customize the package.

for more details, visit https://izyware.com/help/article/using-izycloud-on-android-izycloud-query-app


# Links
* [github]
* location: `apps/android`

# ChangeLog

## V7.3
* 7300003: implement retry loop for bootscript 
* 7300001: izy.devops integration script at startup
* 7300000: Instructions for terminal emulator and Linux environment app 

[f-droid]: https://f-droid.org/en/
[termux.api]:https://f-droid.org/en/packages/com.termux.api/
[termux-boot]: https://github.com/termux/termux-boot
[termux]: https://termux.dev/en/
[github]: https://github.com/izyware/android.izyquery