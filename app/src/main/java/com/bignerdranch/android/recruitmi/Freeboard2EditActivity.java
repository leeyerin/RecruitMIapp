package com.bignerdranch.android.recruitmi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.opengl.GLES30;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 이예린 on 2018-06-18.
 */

public class Freeboard2EditActivity extends AppCompatActivity {
    private Uri photoUri;
    private String currentPhotoPath; //실제 사진 파일 경로
    String mImageCaptureName; //이미지 이름
    private final int CAMERA_CODE = 1111;
    private final int GALLERY_CODE=1112;
    int[] maxTextureSize = new int[1];
    BitmapDrawable drawable;
    Bitmap bitmapSql;




    SQLiteDatabase db;
    String dbname = "myDB";
    String tablename = "freeboard2";
    String imgTablename ="img";
    String sql;

    public EditText textTitle;
    public EditText textContent;
    public Button insertButton;
    public ImageView ivImage;
    public ImageButton imagebutton;



    String str_input1, str_input2, str_input3;
    String name ="yerin";
    BoardActivity_freeboard_2 bf2 = new BoardActivity_freeboard_2();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board); //기존 : fragment_board

        textTitle = findViewById(R.id.board_title);
        textContent = findViewById(R.id.board_content);
        ivImage = findViewById(R.id.photo_view);
        ivImage.setEnabled(false);

        drawable = (BitmapDrawable) ivImage.getDrawable();
        //bitmapSql = drawable.getBitmap();


        insertButton = (Button) findViewById(R.id.board_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //레코드 삽입
                try{
                    str_input1 = textTitle.getText().toString();
                    str_input2 = textContent.getText().toString();
                    byte[] appIcon = getByteArrayFromDrawable(drawable);


                    sql = "insert into "+tablename+"(name, title, content) values('"+name+"', '"+str_input1+"', '"+str_input2+"');";
                    db.execSQL(sql);
                    SQLiteStatement p = db.compileStatement("insert into "+imgTablename+" values(?);");
                    p.bindBlob(1,appIcon);
                   // p.bindBlob(1,appIcon);
                    System.out.println("insert ok");
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("db error : " + e);
                }

            }
        });

        imagebutton = (ImageButton) findViewById(R.id.imageButton);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.print("눌림@");
                selectGallery();
            }
        });


    }

    private void selectPhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }

        }

    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"

                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;

    }


    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        ivImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                    getPictureForPhoto(); //카메라에서 가져오기
                    break;

                default:
                    break;
            }

        }
    }

    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환


        ivImage.setImageBitmap(rotate( resizeBitmap(bitmap), exifDegree));//이미지 뷰에 비트맵 넣기
        ivImage.setEnabled(true);
        //
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

// Matrix 객체 생성
        Matrix matrix = new Matrix();
// 회전 각도 셋팅
        matrix.postRotate(degree);
// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    Bitmap resizeBitmap(Bitmap  bitmap)

    {

        if(bitmap.getWidth() > GLES30.GL_MAX_TEXTURE_SIZE ||
                bitmap.getHeight()> GLES30.GL_MAX_TEXTURE_SIZE)
        {
            float aspect_ratio = ((float)bitmap.getHeight())/((float)bitmap.getWidth());
            int resizedWidth = 200;

            int resizedHeight = 600;
                    //(int)(GLES30.GL_MAX_TEXTURE_SIZE*0.9);

                    //(int)(GLES30.GL_MAX_TEXTURE_SIZE*0.9*aspect_ratio);
            return bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
        }
        return bitmap;

    }


    public byte[] getByteArrayFromDrawable(Drawable d) {
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte [] data = stream.toByteArray();

        return data;
    }








}
