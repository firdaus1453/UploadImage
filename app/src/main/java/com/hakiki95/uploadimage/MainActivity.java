package com.hakiki95.uploadimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hakiki95.uploadimage.Api.ApiServices;
import com.hakiki95.uploadimage.Api.RetroClient;
import com.hakiki95.uploadimage.Model.MakananResponse;
import com.hakiki95.uploadimage.Model.ResponseApiModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Bitmap bitmap;

    Button btnUpload, btnGalery;
    ImageView imgHolder;
    String part_image;
    ProgressDialog pd;
    final int REQUEST_GALLERY = 9544;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpload = (Button) findViewById(R.id.btnupload);
        btnGalery= (Button) findViewById(R.id.btngallery);
        imgHolder = (ImageView) findViewById(R.id.imgHolder);
        pd = new ProgressDialog(this);
        pd.setMessage("loading ... ");

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"open gallery"),1);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                part_image = getPath(filePath);

                File imagefile = new File(part_image);
                RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imagefile);
                MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imagefile.getName(),reqBody);

                RequestBody namaMakanan = RequestBody.create(MediaType.parse("multipart/form-data"),"Ayam bakar");
                RequestBody descMakanan = RequestBody.create(MediaType.parse("multipart/form-data"),"Enak enak enak");
                RequestBody datetime = RequestBody.create(MediaType.parse("multipart/form-data"),sdf);

                ApiServices api = RetroClient.getApiServices();
                Call<MakananResponse> upload = api.uploadMakanan(2,12,namaMakanan,descMakanan,datetime,partImage);
                upload.enqueue(new Callback<MakananResponse>() {
                    @Override
                    public void onResponse(Call<MakananResponse> call, Response<MakananResponse> response) {
                        pd.dismiss();
                        Log.d("RETRO", "ON RESPONSE  : " + response.body().toString());

                        if(response.body().getResult() == 1)
                        {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MakananResponse> call, Throwable t) {
                        Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                        pd.dismiss();
                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgHolder.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                part_image = getPath(filePath);
//
////                String[] imageprojection = {MediaStore.Images.Media.DATA};
////                Cursor cursor = getContentResolver().query(filePath,null,null,null,null);
////
////
////                if (cursor != null)
////                {
////                    cursor.moveToFirst();
////                    int indexImage = cursor.getColumnIndex(imageprojection[0]);
////                    part_image = cursor.getString(indexImage);
////                }
//
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//
//                imgHolder.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

//        if (resultCode == RESULT_OK)
//        {
//            if(requestCode == 1)
//            {
//                Uri dataimage = data.getData();
//                String[] imageprojection = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(dataimage,imageprojection,null,null,null);
//
//                if (cursor != null)
//                {
//                    cursor.moveToFirst();
//                    int indexImage = cursor.getColumnIndex(imageprojection[0]);
//                    part_image = cursor.getString(indexImage);
//
//                    if(part_image != null)
//                    {
//                        File image = new File(part_image);
//                        imgHolder.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
//                    }
//                }
//            }
//        }
    }

    private String getPath(Uri filepath) {
        Cursor cursor = getContentResolver().query(filepath, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
