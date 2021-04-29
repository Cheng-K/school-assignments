package com.company;
import java.io.*;

public class FileServer {
    /*

     */

    public static String[] readFile (String subDirectory, String fileName){
        String fileContent = "";
        File file = null;

        if (subDirectory.isEmpty()) {
             file = new File(fileName);
        }
        else {
             file = new File(subDirectory, fileName);
        }
        try {
            FileReader fr = new FileReader(file);
            int charValue = fr.read();
            while (charValue != -1){
                fileContent += (char) charValue ;
                charValue = fr.read();
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.split("\n");
    }

    public static int writeFile (String subDirectory, String fileName, String fileContent){
        File file = null;
        if (subDirectory.isEmpty()) {
            file = new File(fileName);
        }
        else {
            file = new File(subDirectory, fileName);
        }
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(fileContent);
            writer.close();
            return 0;
        } catch (IOException e){
            e.printStackTrace();
        }
        return 1;
    }

    public static int appendFile (String subDirectory, String fileName, String fileContent){
        File file = null;
        if (subDirectory.isEmpty()) {
            file = new File(fileName);
        }
        else {
            file = new File(subDirectory, fileName);
        }
        try {
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(fileContent);
            writer.close();
            return 0;
        } catch (IOException e){
            e.printStackTrace();
        }
        return 1;
    }







}
