# Andorid-Facebook-Integrarion

Facebook API integration:

link to refer : https://developers.facebook.com/docs/facebook-login/android

#### Facebook sdk version : 4.1.0


1.Create App in facebook developer console

visit https://developers.facebook.com to create app 

click add new app -> choose platform Andorid -enter display name & choose category 

add packagename in Google Play Package Name box then add Class Name then add key hash

#### -Step to get key hash:

1.Programatically get hashKey:

#### FacebookManager.getInstance().getFacebookHashKey();

call getFacebookHashKey method using FacebookManager class in activity onCreate 
then this method will print hashkey in logcate 

2.To generate a hash of your release key, run the following command on Windows substituting your release key alias and the path to your keystore.

#### keytool -exportcert -alias <RELEASE_KEY_ALIAS> -keystore <RELEASE_KEY_PATH> | openssl sha1 -binary | openssl base64

------------------------------------------------------------------------------------------


after creation get App ID from developer account and replace the "facebook_app_id"  key in string.xml


First add facebook sdk to app gradle by adding below line:

#### compile 'com.facebook.android:facebook-android-sdk:4.1.0'

then add FacebookManager class file from facebook package (From provided project)
then add "FacebookSdk.sdkInitialize(this.getApplicationContext());"  this line in your Application class's onCreate() method

#### To login with facebook :

FacebookManager.getInstance().loginWithFacebook(MainActivity.this, mCallbackManager, this, FLAG_FACEBOOK_LOGIN);

#### To logout from facebook:

FacebookManager.getInstance().logoutFacebook();

#### Share Image Url on facebook wall

FacebookManager.getInstance().facebookImageShare(MainActivity.this, mShareDialog, "Facebook Image Share", "Hello to all!", imageUrl);

#### To get current login's user profile picture:
String profileImageUrl = FacebookManager.getInstance().getProfileImage();

#### To get current login's user profile picture with customzied size:
int SIZE = 500;
String profileImageUrl = FacebookManager.getInstance().getProfileImage(SIZE);

#### To get Facebook profile:
FacebookManager.getInstance().getFacebookProfile();



#### Note:
/** facebook initially will allow to access basic info about user such as name,emailId,FirstName,LastName,id,gender

In order to get Date of Birth & other things we have to submit an app for review through developer console .

For that we need following things:
-Privacy Policy URL
-App icons 

**/


