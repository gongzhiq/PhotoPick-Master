package com.simaple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.awen.photo.FrescoImageLoader;
import com.awen.photo.photopick.bean.PhotoResultBean;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.awen.photo.photopick.controller.PhotoPickConfig;
import com.awen.photo.photopick.controller.PhotoPreviewConfig;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片归版权者所有
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button://图库
                //方法1
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(15)
                        .showCamera(false)
//                        .showGif(false)
                        .build();

                //方法2
//                PhotoPickBean bean = new PhotoPickBean();
//                bean.setMaxPickSize(15);
//                bean.setShowCamera(false);
//                bean.setSpanCount(3);
//                bean.setPickMode(PhotoPickConfig.MODE_MULTIP_PICK);
//                new PhotoPickConfig.Builder(this)
//                        .setPhotoPickBean(bean)
//                        .build();
                break;
            case R.id.button4://图库(可以启动拍照)
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(15)
                        .showCamera(true)
                        .setOriginalPicture(true)//让用户可以选择原图
                        .setOnPhotoResultCallback(new PhotoPickConfig.Builder.OnPhotoResultCallback() {
                            @Override
                            public void onResult(PhotoResultBean result) {
                                Log.e("MainActivity", "result = " + result.getPhotoLists().size());
                            }
                        })
                        .build();
                break;
            case R.id.button2://裁剪头像
                new PhotoPickConfig.Builder(this)
                        .pickMode(PhotoPickConfig.MODE_SINGLE_PICK)
                        .clipPhoto(true)
                        .build();
                break;
            case R.id.button3://查看(网络)大图
                new PhotoPagerConfig.Builder(this)
                        .setBigImageUrls(ImageProvider.getImageUrls())
                        .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                        .build();
                break;
            case R.id.button5://大图展示前先显示小图
                //方法1
                new PhotoPagerConfig.Builder(this)
                        .setBigImageUrls(ImageProvider.getBigImgUrls())
                        .setLowImageUrls(ImageProvider.getLowImgUrls())
                        .setSavaImage(true)
                        .build();

                //方法2,一张一张图片地添加
//                new PhotoPagerConfig.Builder(this)
//                        //添加大图
//                        .addSingleBigImageUrl("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg")
//                        .addSingleBigImageUrl("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg")
//                        .addSingleLowImageUrl("http://120.27.113.153/frogdrfs/2016/08/22/0ac279d511de46a0858a1812efe9a1ce.jpg&64X64.jpg")
//                        .addSingleLowImageUrl("http://120.27.113.153/frogdrfs/2016/08/29/62e2cd190a174e6c83ea3e948ecba66a.jpg&64X64.jpg")
//                        .setSavaImage(true)
//                        .build();

                //方法3
                //先构建实体类,在实体类里设置好参数
//                PhotoPagerBean bean = new PhotoPagerBean();
//                bean.setBigImgUrls(ImageProvider.getBigImgUrls());
//                bean.setLowImgUrls(ImageProvider.getLowImgUrls());
//                bean.setSaveImage(true);
//                //再设置实体类
//                new PhotoPagerConfig.Builder(this)
//                        .setPhotoPagerBean(bean)
//                        .build();
                break;
            case R.id.button6://获取缓存大小:
                ((Button)findViewById(R.id.button6)).setText("缓存大小：" + FrescoImageLoader.getSDCacheSizeFormat());
                break;
            case R.id.button7://清除所有缓存
                FrescoImageLoader.clearCaches();
                ((Button)findViewById(R.id.button6)).setText("缓存大小：" + FrescoImageLoader.getSDCacheSizeFormat());
                break;
            case R.id.button8:
                new PhotoPagerConfig.Builder(this,MyPhotoPagerActivity.class)
                        .setBigImageUrls(ImageProvider.getImageUrls())
                        .setSavaImage(true)
                        .build();
                break;
            case R.id.button9:
                startActivity(new Intent(this,PhotoPagerAnimaActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case PhotoPickConfig.PICK_REQUEST_CODE://图片
                ArrayList<String> photoLists = data.getStringArrayListExtra(PhotoPickConfig.EXTRA_STRING_ARRAYLIST);
                //用户选择的是否是原图
                boolean isOriginalPicture = data.getBooleanExtra(PhotoPreviewConfig.EXTRA_ORIGINAL_PIC, false);
                if (photoLists != null && !photoLists.isEmpty()) {
                    Log.i("MainActivity", "selected photos = " + photoLists.toString());
                    Toast.makeText(this, "selected photos size = " + photoLists.size() + "\n" + photoLists.toString(), Toast.LENGTH_LONG).show();
                    File file = new File(photoLists.get(0));
                    if (file.exists()) {
                        //you can do something

                    } else {
                        //toast error

                    }
                }
                break;
        }
    }
}
