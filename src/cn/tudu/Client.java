package cn.tudu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @path cn.tudu 王
 * @ClassName Client
 * @descriptiom 聊天室客户端
 * @author 【王永鑫】
 * @date 2017年9月14日 下午7:14:01
 */
public class Client
{
    /**
     * java.net.Socket 套接字 封装了TCP通讯协议,使用它可以基于TCP与远程客户端应用程序连接并通讯
     */
    private Socket  socket;
    private Scanner scanner;
    private String  host;

    /**
     * @构造方法
     * @TODO
     * @param
     */
    public Client()
    {
        /*
         * 实例化Socket就是与服务器端建立连接的过程,这里需要传递两个参数指定服务器端地址信息. 参数1:服务器端的连接ip;
         * 参数2:运行在服务器端的应用程序的端口
         * 
         * 通过ip可以找到服务器,通过端口可以找到运行在服务器端的应用程序,由于实例化就是建立连接,若服务器端没有响应,
         * 这里实例化Socket抛出异常
         */
        try
        {
            socket = new Socket("localhost", 8088);
            System.out.println("请求成功");

        } catch (UnknownHostException e)
        {
            // 将来针对异常在这里记录日志
            System.out.println("找不到ip");
            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println("端口找不到");
            e.printStackTrace();
        }

    }

    /**
     * @Method start()
     * @TODO 客户端开始工作的方法
     * @param
     * @return void
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public void start() throws Exception
    {

        ClientHandler clientHandler = new ClientHandler(socket);
        Thread thread = new Thread(clientHandler);
        thread.start();

        // 输出流 ---- 输出到服务器
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), "utf-8"), true);
        scanner = new Scanner(System.in);
        String line = null;
        while (true)
        {
            line = scanner.nextLine();
            pw.println(line);
        }

    }

    private class ClientHandler implements Runnable
    {

        private Socket service;

        /**
         * @构造方法
         * @TODO
         * @param
         */
        public ClientHandler(Socket service)
        {
            this.service = service;
            InetAddress address = this.service.getInetAddress();
            host = address.getHostAddress();
        }

        @Override
        public void run()
        {
            /**
             * socket 提供的方法,OutputStream .getOutputStream()
             * 通过socket获取输出流写出的字节,都会通过网络发送给远端的服务器
             */
            try
            {
                System.out.println(host + "上线了");

                // 输入流 ---- 读取服务器端的消息
                BufferedReader buff = new BufferedReader(new InputStreamReader(
                        service.getInputStream(), "utf-8"));
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
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @Method main()
     */
    public static void main(String[] args) throws Exception
    {
        Client c = new Client();
        c.start();
    }

}
