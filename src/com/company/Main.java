package com.company;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;
import java.util.Scanner;

public class Main {
    //alamin mo paano magenerate token
    private static final String ACCESS_TOKEN = "jMQjMsTtg3IAAAAAAAAAAVObPlY8Yef9C13w75pqpBPGq0EO_sQoek6XX6BxOCdn";

    public static void main(String args[]) throws FileNotFoundException, DbxException, IOException {
        Main app = new Main();
        app.gettingFiles();


    }

    public void gettingFiles() throws IOException {
        System.out.println("Hi");
        Scanner input = new Scanner(System.in);
        String data;
        //getting files
        try {


            DbxRequestConfig config;
            config = new DbxRequestConfig("dropbox/spring-boot-file-upload");
            DbxClientV2 client;
            client = new DbxClientV2(config, ACCESS_TOKEN);
            FullAccount account;
            DbxUserUsersRequests r1 = client.users();
            account = r1.getCurrentAccount();
            System.out.println(account.getName().getDisplayName());

            // Get files and folder metadata from Dropbox root directory
            ListFolderResult result = client.files().listFolder("");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    System.out.println(metadata.getPathLower());
                    //ito kukuha ng list file mo example /pom.jpeg
                    // fhernand.jpg
                }
                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }
            System.out.println("Which one do you want to download? example: < /john.png >");
            data = input.nextLine();
            DbxDownloader<FileMetadata> downloader = client.files().download(data); //from database ito sasave mo sa query
            /*example
                  column mo is image = profile.jpeg*/

            //C:\Users\john brix17\Downloads\downloaded\pompom.jpeg (your file example)
            FileOutputStream out = new FileOutputStream("C:\\Users\\john brix17\\Downloads\\downloaded\\" + data);
            downloader.download(out);
            out.close();
            System.out.println("Successfully downloaded!");

        } catch (DbxException | FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
    }

    public void uploadingFile() {
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("C:\\Users\\john brix17\\Pictures\\profile\\pompom.jpg")) {
            DbxRequestConfig config;
            config = new DbxRequestConfig("dropbox/spring-boot-file-upload");
            DbxClientV2 client;
            client = new DbxClientV2(config, ACCESS_TOKEN);
            FullAccount account;
            DbxUserUsersRequests r1 = client.users();
            account = r1.getCurrentAccount();
            System.out.println(account.getName().getDisplayName());

            FileMetadata metadata = client.files().uploadBuilder("/pompom.jpg")
                    .uploadAndFinish(in);
            System.out.println("SUccessfully upload!");

        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
    }

    public void downloadingFile() throws DbxException {
        DbxRequestConfig config;
        config = new DbxRequestConfig("dropbox/spring-boot-file-upload");
        DbxClientV2 client;
        client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account;
        DbxUserUsersRequests r1 = client.users();
        account = r1.getCurrentAccount();
        System.out.println(account.getName().getDisplayName());
        System.out.println("waiting...");

        DbxDownloader<FileMetadata> downloader = client.files().download("/pompom.jpeg"); //from database ito sasave mo sa query
        //example
        // column mo is image = profile.jpeg
        try {
            //C:\Users\john brix17\Downloads\downloaded\pompom.jpeg (your file example)
            //access is denied
            FileOutputStream out = new FileOutputStream("C:\\Users\\john brix17\\Downloads\\downloaded\\pompom.jpeg");
            downloader.download(out);
            out.close();
            System.out.println("Successfully downloaded!");
        } catch (DbxException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
