package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
 
public class ServerClient {
    Socket socket; // ��ǻ�Ϳ� ��Ʈ��ũ ����� ����
 
    public ServerClient(Socket socket) {// ������
        this.socket = socket;
        receive();
    }// client()
 
    /* Ŭ���̾�Ʈ�κ��� �޼����� ���޹޴� �޼ҵ� */
    public void receive() {
        Runnable thread = new Runnable() {// ������ ����
 
            @Override
            public void run() {
                try {
                    while (true) {
                        /* �޼��� �ޱ� ���� intPutStream */
                        InputStream in = socket.getInputStream();
                        byte[] buffer = new byte[512]; // �ѹ��� 512byte��ŭ ���� ���� �� �ְ� ����
                        int length = in.read(buffer);
                        while (length == -1)
                            throw new IOException();// ���� �߻� ó��
                        /* getRemoteSocketAddress(): ���� ������ Ŭ���̾�Ʈ�� ������ �ּҿ� ���� �ּҸ� GET */
                        
                        //ServerMain.textArea.appendText(message);
                        System.out.println("[�޼��� ���� ����]" + socket.getRemoteSocketAddress() + ":"
                                + Thread.currentThread().getName());
                        /* ���� ���� ���� �ѱ۵� �����ϵ��� ���� */
                        String message = new String(buffer, 0, length, "UTF-8");
                        /* ���� ���� �޼����� �ٸ� Ŭ���̾�Ʈ�� ���� �� �ֵ��� ���� */
                        for (ServerClient client : ServerMain.clients) {
                            client.send(message);
                        }
                    }
                } catch (Exception e) {
                    try {
                        System.out.println("[Ŭ���̾�Ʈ ���� ����]" + socket.getRemoteSocketAddress() + ":"
                                + Thread.currentThread().getName());
                        ServerMain.clients.remove(ServerClient.this);
                        socket.close();//    
                    } catch (Exception e2) {// ������ �߻� ������ ���� �� ���� ó��
                        e2.printStackTrace();
                    }
                }
            }
        };
        ServerMain.threadPool.submit(thread);// ������� thread�� threadPool�� ������
    }// receive()
 
    /* Ŭ���̾�Ʈ�� �޼����� ������ �޼ҵ� */
    public void send(String Message) {
        Runnable thread = new Runnable() {
 
            @Override
            public void run() {
                try {
                    /* �޼����� ������ ���� OutPutStream */
                    OutputStream out = socket.getOutputStream();
                    byte[] buffer = Message.getBytes("UTF-8");
                    /* �޼��� �۽ſ� �����Ͽ��ٸ� buffer ��� ������ ������ ������, flush�� ���� ������ �κб��� �˷��� */
                    out.write(buffer);
                    out.flush();
                } catch (Exception e) {
                    try {
                        System.out.println("[�޼��� �۽� ����]" + socket.getRemoteSocketAddress() + ":"
                                + Thread.currentThread().getName());
                        /* ������ �߻������� �ش� Ŭ���̾�Ʈ�� ���� */
                        //ServerMain.clients.remove(ServerClient.this);
                        //socket.close();// �ݾƹ���
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
 
            }
        };
        ServerMain.threadPool.submit(thread);
    }// send()
}// client{}
 