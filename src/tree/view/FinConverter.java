/*
package tree.view;

import external_logic.ReadExcel;
import external_logic.ReadXMLFile;
import okhttp3.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

public class FinConverter {

    static String online_converter_ID = "-1";
    static String inputResource = "V:\\34.doc";
    static MediaType mediaType;
    static OkHttpClient client;
    static String job_ID;
    static StringBuilder outputResource = new StringBuilder(Paths.get("").toAbsolutePath().toString());

    public static void main(String[] args) {

        client = new OkHttpClient();

        switch(FilenameUtils.getExtension(inputResource)) {
            case "doc":
                mediaType =
                        MediaType.parse("application/msword");
                convertToODT("odt", outputResource.append("\\current.odt"));
                new ReadXMLFile().convertFile(outputResource.toString());
                break;
            case "docx":
                mediaType =
                        MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                convertToODT("odt", outputResource.append("\\current.odt"));
                new ReadXMLFile().convertFile(outputResource.toString());
                break;
            case "xls":
                new ReadExcel().convertExcelToXml(inputResource);
                break;
            case "xlsx":
                */
/*mediaType =
                        MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");*//*

                //convertToODT("xls", outputResource.append("\\current.xls"));
                //new ReadExcel().convertExcelToXml(inputResource);
                //new ReadExcel().convertExcelToXml("V:\\6.xls");
                break;
        }
    }

    private static void convertToODT (String type, StringBuilder outputResource) {

        RequestBody body = null;

        body = RequestBody.create(mediaType, "{\"conversion\":[{\"target\":\"odt\"}]}");

        File file = new File(inputResource);

        RequestBody requestForUploadBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                .build();
        try {
//21cd5cfb47566d6ec9d2467979c57881
//995f166033ea1548653db41051ad1670

            online_converter_ID = "995f166033ea1548653db41051ad1670";
            Request initilizeJOB = new Request.Builder()
                    .url("http://api2.online-convert.com/jobs")
                    .post(body)
                    .addHeader("x-oc-api-key", online_converter_ID)
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response res = client.newCall(initilizeJOB).execute();

            if (res.code() == 401) {
                online_converter_ID = "c0889234cb2ad47231961a6ba1bda4f8";
                initilizeJOB = new Request.Builder()
                        .url("http://api2.online-convert.com/jobs")
                        .post(body)
                        .addHeader("x-oc-api-key", online_converter_ID)
                        .addHeader("cache-control", "no-cache")
                        .build();
                client.newCall(initilizeJOB).execute();
            }

            Request getAllJOBs = new Request.Builder()
                    .url("http://api2.online-convert.com/jobs")
                    .addHeader("x-oc-api-key", online_converter_ID)
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response allJOBs = client.newCall(getAllJOBs).execute();

            String curr =allJOBs.body().string().replace("\\", "").replaceAll("\"", "");

            job_ID = StringUtils.substringBetween(curr, "id:", ",token");

            System.out.println("JOB: " + job_ID);

            String server = StringUtils.substringBetween(curr, "server:", ",spent");
            System.out.println("Server: " + server);


            Request uploadFile = new Request.Builder()
                    .url(server + "/upload-file/" + job_ID)
                    .post(requestForUploadBody)
                    .addHeader("x-oc-api-key", online_converter_ID)
                    .build();

            client.newCall(uploadFile).execute();

            allJOBs = client.newCall(getAllJOBs).execute();
            curr = StringUtils.substringBetween(allJOBs.body().string().replace("\\", "").replaceAll("\"", ""), "code:", ",info");
            System.out.println(curr);

            while(curr.equals("processing")) {
                allJOBs = client.newCall(getAllJOBs).execute();
                curr = StringUtils.substringBetween(allJOBs.body().string().replace("\\", "").replaceAll("\"", ""), "code:", ",info");
            }

            if(curr.equals("completed")) {

                allJOBs = client.newCall(getAllJOBs).execute();

                URL website = new URL(StringUtils.substringBetween(allJOBs.body().string().replace("\\", "").replaceAll("\"", ""), "uri:", ",size"));
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(outputResource.toString());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                Request request3 = new Request.Builder()
                        .url("https://api2.online-convert.com/jobs/" + job_ID)
                        .delete()
                        .addHeader("x-oc-api-key", online_converter_ID)
                        .addHeader("cache-control", "private, must-revalidate")
                        .build();
                client.newCall(request3).execute();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
*/
