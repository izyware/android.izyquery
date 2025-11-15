

# Phone Setup
While not strictly required, we recommend that you use anydesk on Andoid to remotely navigate the UI. Otherwise, the best interface for automation and devops would be Termux+SSH

# Termux + SSH
* Install [f-droid] 
* Install [termux] through f-droid
    * Android 10: You may get an error from "Google Play Protect" saying that Unsafe app blocked. Install anyways.
* Setup SSH access from termux shell


        pkg install root-repo
        pkg upgrade -y
        pkg install -y openssh
        # setup a password
        passwd
        # start ssh
        sshd
        
You can now manipulate this device using izy.devops and do ssh, rsync, etc. Please note that ssh is setup on port 8022 which you need to capture inside `config/sshport`. 

For extended functionality and apps you should install [termux.api] and make sure to install the API app AND the package per below:


    # Install API
    Open Google Play Store or F-Droid and Install “Termux:API”
    # Increase permissions
    Settings → Apps → Termux → Permissions
    Settings → Apps → Termux → Permissions
    
    
    # Install packages
    pkg install -y which termux-api
    
To run termux scripts in the background run the following:

    termux-wake-lock
    
This will show a warning: "termux will be able to run in the background. Its battery usage wont be restricted".
    
    
From this point on, you should be able to use izy.devops, for example:

    export $ANDROID_ID=`echo ~/izyware/izy-idman-tools/id/_ID_/container`
    
Note that the container home ("~") path will be `/data/data/com.termux/files/home`.

## Socks Server
Copy the server config the container. 

    izy.devops "rsync?upload" $ANDROID_ID "VPN_CONTAINER_PATH" "/data/data/com.termux/files/home/vpn/"
        
Install izy.devops on the container 

    apt install -y vim rsync nodejs git
    curl -o- https://raw.githubusercontent.com/izyware/devops/master/sh/install.sh | bash
    
## Surveilance
On the device, run the script in the background

    ./surveilance/microphone-record.sh 
    
You can then periodically transfer the audio chunks.
    
    izy.devops "rsync?download" $ANDROID_ID "/data/data/com.termux/files/home/izyware/izy-idman-tools/id/me/microphone/complete" $ANDROID_ID/../microphone --remove-source-files
    
    
## Running scripts at device startup
First make sure to install [termux-boot] through f-droid.

Upload boot scripts into the container:

    izy.devops "rsync?upload" $ANDROID_ID "~/plat/p/apps/android/termux/home/boot/" "/data/data/com.termux/files/home/.termux/boot/"
    

Shell into the container:
        
    ln -s ~/.termux/boot/izyware ./run    


# SDK: Using izy-proxy macros and functions
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

## V7.5
* 7500001: implement microphone surveilance
    * allows for 24-7 surveilance and transfer of chunks for post processing


## V7.3
* 7300003: implement retry loop for bootscript 
* 7300001: izy.devops integration script at startup
* 7300000: Instructions for terminal emulator and Linux environment app 

[f-droid]: https://f-droid.org/en/
[termux.api]:https://f-droid.org/en/packages/com.termux.api/
[termux-boot]: https://github.com/termux/termux-boot
[termux]: https://termux.dev/en/
[github]: https://github.com/izyware/android.izyquery