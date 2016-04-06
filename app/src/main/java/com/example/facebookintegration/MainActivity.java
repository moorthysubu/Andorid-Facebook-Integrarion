package com.example.facebookintegration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebookintegration.controller.ResponseListener;
import com.example.facebookintegration.facebook.FacebookManager;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {


    private final int FLAG_FACEBOOK_LOGIN = 0;
    private TextView mLoginWithFacebook = null;
    private TextView mLogout = null;
    private TextView mShareImage = null;
    private TextView mResponseText = null;
    private TextView mProfileName = null;
    private ImageView mProfilePicture = null;
    private CallbackManager mCallbackManager;
    private ShareDialog mShareDialog = null;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(MainActivity.this);
        mLoginWithFacebook = (TextView) findViewById(R.id.login_facebook);
        mLogout = (TextView) findViewById(R.id.logout_facebook);
        mShareImage = (TextView) findViewById(R.id.image_share);
        mResponseText = (TextView) findViewById(R.id.response_text);
        mProfileName = (TextView) findViewById(R.id.profile_name);
        mProfilePicture = (ImageView) findViewById(R.id.profile_image);

        //OnClickListener
        mLoginWithFacebook.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mShareImage.setOnClickListener(this);

        FacebookManager.getInstance().getFacebookHashKey(MainActivity.this);

    }


    /**
     * get facebook profile
     *
     * @return
     */

    private Profile getProfile() {
        return FacebookManager.getInstance().getFacebookProfile();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_facebook:
                FacebookManager.getInstance().loginWithFacebook(MainActivity.this, mCallbackManager, this, FLAG_FACEBOOK_LOGIN);
                break;
            case R.id.logout_facebook:
                FacebookManager.getInstance().logoutFacebook();
                mResponseText.setText("");
                break;
            case R.id.image_share:


                //shareBitmap();  this method to post photo from sd card


                /**
                 * Here i have used dummy url pass your respective url to post on facebook wall
                 *
                 * and also you can customize title text & message by passing through parameter
                 *
                 */

               String imageUrl = "http://www.uactiv.co//app//uploads//1457418315017profilepic_20160308115408.jpg";
                FacebookManager.getInstance().facebookImageShare(MainActivity.this, mShareDialog, "Facebook Image Share", "Hello to all!", imageUrl);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void successResponse(String successResponse, int flag) {

    }

    @Override
    public void successResponse(JSONObject jsonObject, int flag) {

        /**
         *  flag 0 for facebook login response
         */

        switch (flag) {
            case FLAG_FACEBOOK_LOGIN:
                if (jsonObject != null) {

                    //Here you can parse  basic facebook profile info from an object
                    mResponseText.setText("" + jsonObject.toString());
                    mProfileName.setText(jsonObject.optString("name"));
                    String profileImageUrl = FacebookManager.getInstance().getProfileImage();
                    Log.d(TAG, "" + profileImageUrl);
                    if (profileImageUrl != null)
                        Picasso.with(MainActivity.this).load(profileImageUrl).into(mProfilePicture); // here you can use your flexible imageloader.
                }
                break;
        }


    }

    @Override
    public void errorResponse(String errorResponse, int flag) {

    }

    @Override
    public void removeProgress(Boolean hideFlag) {

    }

    /** share phto on facebook wall from sd card or from camera
     *
     */

    private void shareBitmap(){
        ShareDialog mShareDialog = new ShareDialog(MainActivity.this);
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap myBitmap = v1.getDrawingCache();
        String caption = "Here I'm sharing my Bitmap";
        shareBitmap(MainActivity.this,mShareDialog,caption,myBitmap);
    }


    private void shareBitmap(Context context,ShareDialog shareDialog,String caption,Bitmap photo){
        FacebookManager.getInstance().facebookImageShare(context,shareDialog,caption,photo);
    }



}
