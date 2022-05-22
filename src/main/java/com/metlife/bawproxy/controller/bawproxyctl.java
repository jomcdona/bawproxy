package com.metlife.bawproxy.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class bawproxyctl {
    
    @GetMapping(path="/getContent/{id}", consumes="text/plain", produces="text/plain; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> getContent(@PathVariable int id)
    {
        String GET_URL = "https://s3.us-east.cloud-object-storage.appdomain.cloud/cloud-object-storage-xm-cos-standard-bawstore/Insurance_Industry_Primer.PDF";
        String USER_AGENT = "Mozilla/5.0";
        //return "You passed me id " + id;
        //StreamingResponseBody responseBody = null;
        //long totbytesread = 0L;
        ResponseEntity rspe = null;

        try
        {
            URL obj = new URL(GET_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                StreamingResponseBody responseBody = new StreamingResponseBody() 
                {

                    @Override
                    public void writeTo(OutputStream outputStream) throws IOException 
                    {
                        // TODO Auto-generated method stub
                        byte dataBuffer[] = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) 
                        {
                            outputStream.write(dataBuffer, 0, bytesRead);
                        }
                        
                    }
                };
                /* FileOutputStream fos = new FileOutputStream(new File("/Users/jonmcdonald/temp/testfile.pdf"));
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) {
                    fos.write(dataBuffer, 0, bytesRead);
                }
                fos.flush();
                fos.close(); */
                rspe = new ResponseEntity(responseBody, HttpStatus.OK);
            }
            //return "You passed me id " + id;
            //return new ResponseEntity(responseBody, HttpStatus.OK);
        }

        catch (ProtocolException poe)
        {

        }


        catch (MalformedURLException mue)
        {

        }

        catch (IOException ioe)
        {

        }
        
       
        return rspe;


    }

}
