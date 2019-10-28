package com.ofcoder.farpc.rpc.http;

import com.ofcoder.farpc.rpc.IConsumerServer;
import com.ofcoder.farpc.rpc.RequestDTO;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author far.liu
 */
public class HttpConsumerServer implements IConsumerServer {
    public Object execute(String address, RequestDTO requestDTO) {
        String[] addrs = address.split(":");
        String ip = addrs[0];
        Integer port = Integer.parseInt(addrs[1]);

        try {
            URL url = new URL("http", ip, port, "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(requestDTO);
            objectOutputStream.flush();
            objectOutputStream.close();


            InputStream inputStream = httpURLConnection.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object result =  objectInputStream.readObject();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
