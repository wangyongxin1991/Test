package cn.tudu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @path cn.tudu
 * @ClassName Service
 * @descriptiom TODO
 * @author 【王永鑫】
 * @date 2017-9-17 下午1:32:26
 */
public class Service
{
    private ServerSocket server;
    private Scanner      scanner;
    private Socket       socket;
    private String       host;

    /**
     * @构造方法
     * @TODO
     */
    public Service()
    {
        try
        {
            server = new ServerSocket(8088);// 要被请求的端口号
        } catch (IOException e)
        {
            // 服务器端记录异常日志
            System.out.println("服务器端启动异常!!");
            e.printStackTrace();
        }
    }

    public void start() throws IOException
    {

        while (true)
        {
            System.out.println("服务端等待连接!!");
            socket = server.accept();// 放在whilex循环里，可以不断的获得新的客户端的请求
            System.out.println("服务端被连接!!");

            // Socket service = new Socket("localhost", 8088);
            ServerHendler hendler = new ServerHendler(socket);
            Thread thread = new Thread(hendler);
            thread.start();

            // System.out.println(thread.getName());
            // 服务端输入
            // ServerHandlerPut serHanPut = new ServerHandlerPut(service);
            // Thread shp1 = new Thread(serHanPut);
            // shp1.start();
        }
    }

    public class ServerHendler implements Runnable
    {
        private Socket socket;

        /**
         * @构造方法
         * @TODO
         * @param
         */
        public ServerHendler(Socket socket)
        {
            this.socket = socket;
            InetAddress address = this.socket.getInetAddress();
            host = address.getHostAddress();
        }

        @Override
        public void run()
        {
            try
            {
                // 读取客户端信息
                BufferedReader buff = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(), "utf-8"));
                // // 输出流
                // PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                // socket.getOutputStream(), "utf-8"), true);

                System.out.println(host + "上线了");

                String message = null;
                while ((message = buff.readLine()) != null)
                {
                    System.out.println(host + "说:" + message);
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Method main()
     * @TODO
     * @param
     * @return void
     * @throws Exception
     */
    public static void main(String[] args)
    {
        Service s = new Service();
        try
        {
            s.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
