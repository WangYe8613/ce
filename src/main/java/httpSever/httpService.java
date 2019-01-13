package main.java.httpSever;

import main.java.request.requestService;
import main.java.response.responseService;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class httpService {
    /**
     * WEB_ROOT is the directory where our html and other files reside.
     * For this package,WEB_ROOT is the "webroot" directory under the
     * working directory.
     * the working directory is the location in the file system
     * from where the java command was invoke.
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        httpService server = new httpService();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 80;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("192.144.188.26"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        while (!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                //create Request object and parse
                requestService request = new requestService(input);
                request.parse();

                //create Response object
                responseService response = new responseService(output);
                response.setRequest(request);
                response.sendStaticResource();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}

