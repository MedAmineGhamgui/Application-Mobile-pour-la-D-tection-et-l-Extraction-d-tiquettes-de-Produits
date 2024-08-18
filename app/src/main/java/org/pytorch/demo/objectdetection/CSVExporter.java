package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportToCSV(Context context, List<String[]> dataList, String fileName) {

        // Vérifier si le stockage externe est disponible pour l'écriture
        if (!isExternalStorageWritable()) {
            return;
        }

        // Récupérer le dossier "Documents" du stockage externe
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        // Créer le fichier CSV dans le dossier "Documents"
        File file = new File(dir, fileName);

        try {
            // Créer le FileWriter pour écrire dans le fichier CSV
            FileWriter writer = new FileWriter(file);

            // Ajouter les données à écrire dans le fichier CSV
            for (String[] data : dataList) {
                for (int i = 0; i < data.length; i++) {
                    writer.append(data[i]);
                    if (i != data.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }

            // Fermer le FileWriter
            writer.flush();
            writer.close();

            // Afficher un message de succès
            Toast.makeText(context, "Fichier CSV créé avec succès", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            // Afficher un message d'erreur
            //Toast.makeText(context, "Erreur lors de la création du fichier CSV", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}

