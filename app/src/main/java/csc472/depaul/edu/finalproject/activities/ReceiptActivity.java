package csc472.depaul.edu.finalproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.models.ScanThread;
import csc472.depaul.edu.finalproject.utils.HttpUtils;
import lecho.lib.hellocharts.model.PieChartData;

public class ReceiptActivity extends AppCompatActivity {
    String imageFileName = null;
    ImageView imageView;
    private final int CAPTURE_PHOTO = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        imageView = (ImageView) findViewById(R.id.receipt_view);
        takePhoto();

        CardView scanPhoto = (CardView) findViewById(R.id.recognize);
        if (scanPhoto != null) {
            scanPhoto.setOnClickListener(onClickScanPhoto);
        }

        CardView retakePhoto = (CardView) findViewById(R.id.retake_photo);
        if (retakePhoto != null) {
            retakePhoto.setOnClickListener(onClickReTakePhoto);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteCurrentImage();
    }

    private View.OnClickListener onClickReTakePhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteCurrentImage();
            takePhoto();
        }
    };

    private void deleteCurrentImage() {
        if (imageFileName != null) {
            File file = new File(imageFileName);
            file.delete();
            imageFileName = null;
        }
    }

    private View.OnClickListener onClickScanPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startScan();
            Intent intent = new Intent(getReceiptActivity(), MainActivity.class);
            getReceiptActivity().startActivity(intent);
        }
    };

    private void startScan() {
        if (imageFileName != null) {
            String postUrl = getReceiptActivity().getString(R.string.sanner_url, getReceiptActivity().getString(R.string.sanner_apikey));
            String resUrl = getReceiptActivity().getString(R.string.sanner_result_url, getReceiptActivity().getString(R.string.sanner_apikey));
            AsyncTask.execute(new ScanThread(imageFileName, postUrl, resUrl, getReceiptActivity()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_PHOTO:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
//                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.test_receipt)); //test image
                    imageFileName = savePhoto(bitmap);
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            Drawable pic = imageView.getDrawable();
            if (pic == null) {
                Intent intent = new Intent(getReceiptActivity(), MainActivity.class);
                getReceiptActivity().startActivity(intent);
            }
        }

    }

    private final ReceiptActivity getReceiptActivity() {
        return this;
    }

    public void takePhoto() {
        int cameraPermission = ActivityCompat.checkSelfPermission(getReceiptActivity(), Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            invokeCamera();
        } else {
            requestCameraPermission();
        }
    }

    private void invokeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_PHOTO);
    }

    private void requestCameraPermission() {
        int REQUEST_CAMERA = 1;

        String[] PERMISSIONS_CAMERA = {
                Manifest.permission.CAMERA
        };

        ActivityCompat.requestPermissions(
                getReceiptActivity(),
                PERMISSIONS_CAMERA,
                REQUEST_CAMERA
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            }
        }
    }

    private String savePhoto(Bitmap bitmap) {
        File dataDir = getReceiptActivity().getFilesDir();
        File dir = new File(dataDir.getAbsolutePath() + "/receipts");
        dir.mkdirs();
        int idx = 0;
        String fileName = "receipt_capture" + Integer.toString(idx) + ".jpg";
        File file = new File(dir + "/" + fileName);
        while (file.exists() && idx < 1000) {
            idx++;
            fileName = "receipt_capture" + Integer.toString(idx) + ".jpg";
            file = new File(dir + "/" + fileName);
        }

        try {
            FileOutputStream outputFile = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputFile);
            outputFile.flush();
            outputFile.close();
            return dir + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
