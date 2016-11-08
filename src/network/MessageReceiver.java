package network;

import util.PutMessageEvent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//отримувач теж запустимо в окремому потоці
public class MessageReceiver extends Thread {

    private ServerSocket serverSocket;
    private PutMessageEvent event;

    public MessageReceiver(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        event = PutMessageEvent.getInstance();
    }

    @Override
    public void run() {try {
        //при отриманні запиту на з'єднання формуємо сокет клієнта(з яким спілкуємось)
        Socket fromSocket = serverSocket.accept();
        //в консоль виводимо, що з'єднання встановлене
        System.out.println("Connected");
        //отримуємо потік данних для зчитування(звідки приходять повідомлення)
        DataInputStream serverIn = new DataInputStream(
                fromSocket.getInputStream()
        );
        //строка в яку будемо записувати повідомлення що приходять
        String received = "";
        //доки приходять повідомлення
        while (received != null) {
            //зчитуємо строку що прийшла
            received = serverIn.readUTF();
            //просимо розмістити відправлене повідомлення у текстовому
            //полі для повідомлень
            event.putMessage(received);
        }
    }
    catch (IOException ex) {
        ex.printStackTrace();
    }
    }
}
