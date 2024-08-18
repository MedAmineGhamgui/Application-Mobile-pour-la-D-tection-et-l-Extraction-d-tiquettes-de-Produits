package org.pytorch.demo.objectdetection;

import static androidx.camera.core.CameraX.getContext;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/*
public class capture extends AppCompatActivity {
   int x,y,z,w;

    Bitmap bitmap;
    public capture(Bitmap bitmap,int x,int y,int z,int w){
        this.x=x;
        this.y=y;
        this.z=z;
        this.w=w;
        this.bitmap=bitmap;
    }

   public void takeCapture(){
  // Créer une instance de la classe Bitmap pour capturer l'image
        Bitmap capturedBitmap = Bitmap.createBitmap(bitmap, x, y, z, w);

        // Enregistrer l'image capturée dans un fichier
        File file = new File(Environment.getExternalStorageDirectory() + "/captured_object1.png");
        try {


            FileOutputStream fos = new FileOutputStream(file);
            capturedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();
            System.out.println("achref");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

*/

public class capture {
    int x, y, z, w;
    Bitmap bitmap;
    Context context;
    int i=0;
    public capture( Bitmap bitmap, int x, int y, int z, int w) {

        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void takeCapture() {
/*
        // Créer une instance de la classe Bitmap pour capturer l'image
        Bitmap capturedBitmap = Bitmap.createBitmap(bitmap, x, y, z, w);
        //File file = new File(Environment.getExternalStorageDirectory() + "/captured_object1.png");
        // Enregistrer l'image capturée dans un fichier
        File file = new File(Environment.getExternalStorageDirectory() + "captured_object"+i+".png");
        //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captured_object"+i+".png");
        i++;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            capturedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            //Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show();

            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        // Recadrer l'image pour ne capturer que l'objet
        Bitmap objectBitmap = Bitmap.createBitmap(bitmap, x, y, z, w);

        // Enregistrer l'image capturée sur le stockage de l'appareil
        saveImage(objectBitmap);


    }



    // Fonction pour enregistrer l'image capturée sur le stockage de l'appareil
    private void saveImage(Bitmap bitmap) {
        String fileName = "object_" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //Toast.makeText(this, "Image capturée avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Erreur lors de la capture de l'image", Toast.LENGTH_SHORT).show();
        }
    }




}





/*
    int x,y,z,w;

    Bitmap bitmap;

    public capture(Bitmap bitmap,int x,int y,int z,int w){
        this.x=x;
        this.y=y;
        this.z=z;
        this.w=w;
        this.bitmap=bitmap;
    }

    @SuppressLint("RestrictedApi")
    public void takeCapture(){
        // Créer une instance de la classe Bitmap pour capturer l'image
        Bitmap capturedBitmap = Bitmap.createBitmap(bitmap, x, y, z, w);

        // Enregistrer l'image capturée dans la galerie
        if (getContext() != null) {
            String savedImageURL = MediaStore.Images.Media.insertImage(
                    getContext().getContentResolver(),
                    capturedBitmap,
                    "captured_object",
                    "Image captured by app"
            );
            if (savedImageURL != null) {
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image to gallery", Toast.LENGTH_SHORT).show();
            }
        }


        // Afficher un message pour indiquer que l'image a été sauvegardée dans la galerie

    }

*/
    /*
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Enregistrez l'image bitmap ici.
        }
    }*/


