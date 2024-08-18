// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package org.pytorch.demo.objectdetection;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.createDeviceProtectedStorageContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class Result {
    int classIndex;
    Float score;
    Rect rect;

    public Result(int cls, Float output, Rect rect) {
        this.classIndex = cls;
        this.score = output;
        this.rect = rect;
    }
};

public class PrePostProcessor {
    // for yolov5 model, no need to apply MEAN and STD
    static float[] NO_MEAN_RGB = new float[] {0.0f, 0.0f, 0.0f};
    static float[] NO_STD_RGB = new float[] {1.0f, 1.0f, 1.0f};

    // model input image size
    static int mInputWidth = 416;
    static int mInputHeight = 416;

    // model output is of size 25200*(num_of_class+5)
    private static int mOutputRow = 10647; // as decided by the YOLOv5 model for input image of size 640*640
    private static int mOutputColumn = 7; // left, top, right, bottom, score and 80 class probability
    private static float mThreshold = 0.30f; // score above which a detection is generated
    private static int mNmsLimit = 15;

    static String[] mClasses;

    // The two methods nonMaxSuppression and IOU below are ported from https://github.com/hollance/YOLO-CoreML-MPSNNGraph/blob/master/Common/Helpers.swift
    /**
     Removes bounding boxes that overlap too much with other boxes that have
     a higher score.
     - Parameters:
     - boxes: an array of bounding boxes and their scores
     - limit: the maximum number of boxes that will be selected
     - threshold: used to decide whether boxes overlap too much
     */
    static ArrayList<Result> nonMaxSuppression(ArrayList<Result> boxes, int limit, float threshold) {

        // Do an argsort on the confidence scores, from high to low.
        Collections.sort(boxes,
                new Comparator<Result>() {
                    @Override
                    public int compare(Result o1, Result o2) {
                        return o1.score.compareTo(o2.score);
                    }
                });

        ArrayList<Result> selected = new ArrayList<>();
        boolean[] active = new boolean[boxes.size()];
        Arrays.fill(active, true);
        int numActive = active.length;

        // The algorithm is simple: Start with the box that has the highest score.
        // Remove any remaining boxes that overlap it more than the given threshold
        // amount. If there are any boxes left (i.e. these did not overlap with any
        // previous boxes), then repeat this procedure, until no more boxes remain
        // or the limit has been reached.
        boolean done = false;
        for (int i=0; i<boxes.size() && !done; i++) {
            if (active[i]) {
                Result boxA = boxes.get(i);
                selected.add(boxA);
                if (selected.size() >= limit) break;

                for (int j=i+1; j<boxes.size(); j++) {
                    if (active[j]) {
                        Result boxB = boxes.get(j);
                        if (IOU(boxA.rect, boxB.rect) > threshold) {
                            active[j] = false;
                            numActive -= 1;
                            if (numActive <= 0) {
                                done = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return selected;
    }

    /**
     Computes intersection-over-union overlap between two bounding boxes.
     */
    static float IOU(Rect a, Rect b) {
        float areaA = (a.right - a.left) * (a.bottom - a.top);
        if (areaA <= 0.0) return 0.0f;

        float areaB = (b.right - b.left) * (b.bottom - b.top);
        if (areaB <= 0.0) return 0.0f;

        float intersectionMinX = Math.max(a.left, b.left);
        float intersectionMinY = Math.max(a.top, b.top);
        float intersectionMaxX = Math.min(a.right, b.right);
        float intersectionMaxY = Math.min(a.bottom, b.bottom);
        float intersectionArea = Math.max(intersectionMaxY - intersectionMinY, 0) *
                Math.max(intersectionMaxX - intersectionMinX, 0);
        return intersectionArea / (areaA + areaB - intersectionArea);
    }
   /* private static void saveImage(Bitmap bitmap) {
        String fileName = "object_" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //Toast.makeText(this, "Image capturée avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Erreur lors de la capture de l'image", Toast.LENGTH_SHORT).show();
        }
    }*/
   public static String str_replace(String str, String search, String replace) {
       return str.replaceAll(search, replace);
   }
   private static void saveImage(Bitmap bitmap) {
       String magasin = MainActivity.getMagasin();
       String loc = MainActivity.getLocalisation();

       //String fileName = "magasin"+magasin+"localisation"+loc + System.currentTimeMillis() + ".jpg";


       String dateTime = LocalDateTime.now().toString().replace(":", "-");
       String fileName = "magasin" + str_replace(magasin, " ", "_") + "localisation" + str_replace(loc, " ", "_") +"date"+str_replace(dateTime, "-", "_")  + ".jpg";

       File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

       try (OutputStream outputStream = new FileOutputStream(file)) {
           System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
           bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

       } catch (IOException e) {
           e.printStackTrace();
       }
   }
     static ArrayList<Result> outputsToNMSPredictions(int x1,Bitmap bitmap, float[] outputs, float imgScaleX, float imgScaleY, float ivScaleX, float ivScaleY, float startX, float startY) {
        ArrayList<Result> results = new ArrayList<>();
        int left1=0,top1=0,height1=0,width1=0;
        for (int i = 0; i< mOutputRow; i++) {

            if (outputs[i* mOutputColumn +4] > mThreshold) {

                float x = outputs[i* mOutputColumn];
                float y = outputs[i* mOutputColumn +1];
                float w = outputs[i* mOutputColumn +2];
                float h = outputs[i* mOutputColumn +3];

                float left = imgScaleX * (x - w/2);
                float top = imgScaleY * (y - h/2);
                float right = imgScaleX * (x + w/2);
                float bottom = imgScaleY * (y + h/2);

                float max = outputs[i* mOutputColumn +5];
                int cls = 0;
                for (int j = 0; j < mOutputColumn -5; j++) {
                    if (outputs[i* mOutputColumn +5+j] > max) {
                        max = outputs[i* mOutputColumn +5+j];
                        cls = j;
                    }
                }



                ///////////////////////////////////////////////////


                ////////////////////////////////////////////////////





                Rect rect = new Rect((int)(startX+ivScaleX*left), (int)(startY+top*ivScaleY), (int)(startX+ivScaleX*right), (int)(startY+ivScaleY*bottom));
                //Rect rect = new Rect(0, 0, 720, 1360);
                ////////////////////extraction a partir d'image //////////////////////////////////////////////////////////
                /*Bitmap objectBitmap = Bitmap.createBitmap(bitmap, (rect.left*2250/720), rect.top*4000/720, rect.width()*2250/720, rect.height()*4000/720);
                saveImage(objectBitmap);*/
                //////////////////////////////////////////////////////////////////////////////
                /*Bitmap objectBitmap = Bitmap.createBitmap(bitmap, (rect.left*480/720), rect.top*640/1360, rect.width()*480/720, rect.height()*640/1360);
                saveImage(objectBitmap);*/
                /////////////////////////////////////////////////////////////////////
                if(x1==1) {
                    if ((rect.top > 0) && (rect.left > 0) && (rect.right > 0) && (rect.bottom > 0) && (rect.top < 1360) && (rect.left < 720) && (rect.right < 720) && (rect.bottom < 1360) && (rect.width() > 0)) {
                        left1 = (rect.left * 480 / 720);
                        top1 = rect.top * 640 / 1360;
                        width1 = rect.width() * 480 / 720;
                        height1 = rect.height() * 640 / 1360;
                        System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
                    }
                }
                if(x1==0)
                {left1=(rect.left*2250/720);
                    top1=rect.top*4000/720;
                    width1=rect.width()*2250/720;
                    height1=rect.height()*4000/720;

                    System.out.println("77777777777777777777777777777777777777777777777777777777777");


                }
                Result result = new Result(cls, outputs[i*mOutputColumn+4], rect);
                results.add(result);
            }


        }
        if(x1==1){
         if((top1>0)&&(left1>0)&&(height1>0)&&(width1>0)) {

             Bitmap objectBitmap = Bitmap.createBitmap(bitmap, left1, top1, width1, height1);
             saveImage(objectBitmap);

             System.out.println("555555555555555555555555555555555555555555555555555555555555555555");
         }
        }


        return nonMaxSuppression(results, mNmsLimit, mThreshold);
    }
}
