package com.example.facebookintegration.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.facebookintegration.controller.ResponseListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * Created by moorthy on 3/23/2016.
 */
public class FacebookManager {


    private static final String FIELDS = "id,name,email,gender,first_name,last_name,birthday";
    private static FacebookManager mFacebookManager = null;
    private String TAG = "FacebookManager";
    private String[] mFacebookPermissions = {"public_profile", "user_friends", "email","read_custom_friendlists","user_birthday"};


    /**
     * object creation for a class
     *
     * @return
     */
    public static FacebookManager getInstance() {
        if (mFacebookManager != null) {
            return mFacebookManager;
        } else {
            mFacebookManager = new FacebookManager();
            return mFacebookManager;
        }
    }


    /**
     * login with facebook
     *
     * @param mContext
     * @param mFacebookCallback
     * @param mResponseListener
     * @param flag
     */


    public void loginWithFacebook(Context mContext, CallbackManager mFacebookCallback, final ResponseListener mResponseListener, final int flag) {
        logoutFacebook();
        LoginManager.getInstance().registerCallback(mFacebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "accessToken" + loginResult.getAccessToken().getSource());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject mResultObject,
                                                    GraphResponse response) {
                                // Application code
                                Log.v("GraphResponse", response.toString());
                                Log.v("responseObject", mResultObject.toString());

                                if (mResultObject != null) {
                                    mResponseListener.successResponse(mResultObject, flag);
                                } else {
                                    mResponseListener.errorResponse("Error in facebook login!", flag);
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", FIELDS);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                mResponseListener.removeProgress(true);
            }

            @Override
            public void onError(FacebookException e) {
                mResponseListener.errorResponse(e.getMessage(), flag);
            }
        });
        LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList(mFacebookPermissions));
    }


    /**
     * logout facebook
     */

    public void logoutFacebook() {
        try {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get facebook profile
     *
     * @return
     */

    public Profile getFacebookProfile() {
        Profile.fetchProfileForCurrentAccessToken(); // this to get profile picture
        Profile current = Profile.getCurrentProfile();
        return current;
    }


    /**
     * get Profile picture by required size
     *
     * @param size
     * @return
     */

    public String getProfileImage(int size) {
        Uri imageUrl = null;
        Profile.fetchProfileForCurrentAccessToken(); // this to get profile picture
        Profile current = Profile.getCurrentProfile();
        if (current != null) {
            imageUrl = current.getProfilePictureUri(size, size);
        }
        return imageUrl.toString();
    }

    /**
     * get profile picture with default size 250*250
     *
     * @return
     */

    public String getProfileImage() throws NullPointerException{
        int size = 250;
        Uri imageUrl = null;
        Profile.fetchProfileForCurrentAccessToken(); // this to get profile picture
        Profile current = Profile.getCurrentProfile();
        if (current != null) {
            imageUrl = current.getProfilePictureUri(size, size);
            return imageUrl.toString();
        }
        return null;
    }

    /**
     * share image url on facebook wall
     *
     * @param shareDialog
     * @param title
     * @param message
     * @param imageUrl
     */


    public void facebookImageShare(Context mContext, ShareDialog shareDialog, String title, String message, String imageUrl) {

        if (imageUrl != null && imageUrl.length() > 0) {

            if (ShareDialog.canShow(ShareLinkContent.class) && AccessToken.getCurrentAccessToken() != null) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(title)
                        .setContentDescription(message)
                        .setContentUrl(Uri.parse(imageUrl))
                                //.setImageUrl(Uri.parse(imageUrl))
                        .build();
                shareDialog.show(linkContent);
            } else {
                Toast.makeText(mContext, "please login to share!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void facebookImageShare(Context mContext, ShareDialog shareDialog, String caption, Bitmap photo) {

        if (photo != null && shareDialog != null)
            if (ShareDialog.canShow(ShareLinkContent.class) && AccessToken.getCurrentAccessToken() != null) {
                SharePhoto mSharePhoto = new SharePhoto.Builder().setBitmap(photo).build();
                SharePhotoContent mBuilder = new SharePhotoContent.Builder().addPhoto(mSharePhoto).build();
                shareDialog.show(mBuilder);
            } else {
                Toast.makeText(mContext, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
    }



    public void getFriendsList(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{friend-list-id}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.d(TAG,"getFriendsList : "+response.toString());
                    }
                }
        ).executeAsync();
    }



    public void getFacebookHashKey(Context mContext){
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

}
