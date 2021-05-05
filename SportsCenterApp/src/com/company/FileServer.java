package com.company;
import java.io.*;

/*
    Description : FileServer class is responsible for fileIO operations. Class should be used without instantiation.
 */
public class FileServer {

    /*
        Method Name : readFile
        Return : Array of strings that represents each line in the file.
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
        return fileContent.split("\\r?\\n");
    }


    public static String[] readFile (String fileName){
        return readFile("",fileName);
    }

    /*
        Method Name : writeFile
        Return : 0 represents write to file is successful
                 1 represents write to file is unsuccessful
     */
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

    public static int writeFile (String fileName, String fileContent){
        return writeFile("",fileName,fileContent);
    }

    /*
        Method Name : appendFile
        Return        : 0 represents append to file is successful
                        1 represents append to file is unsuccessful
     */
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

    public static int appendFile(String fileName,String fileContent){
        return appendFile("",fileName,fileContent);
    }

}
