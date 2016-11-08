package network;

import util.DataProvider;
import util.FileDataProvider;

import java.io.IOException;
import java.net.ServerSocket;

//наш мережевий двіжок - тут вся робота з мережею, передача і прийом повідомлень
public class SocketEngine {

    //будемо приймати повідомлення на цей порт
    //та і відправляти теж
    private final static int SERVER_PORT = 4444;

    //сокет для сервера
    private ServerSocket serverSocket;
    //наш чарівний датапровайдер, в якого взнаємо куди конектитись і як на звати
    private DataProvider dataProvider = new FileDataProvider();
    //цей відповідає за передачу повідомлень
    private MessageSender messageSender;
    //цей відповідає за прийом повідомлень
    private MessageReceiver messageReceiver;

    public SocketEngine(){
        try {
            //ініціалізуємо сокет, який буде слухати вказаний порт
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        //створюємо отримувач повідомлень, для чого передаємо йому
        //серверсокет(приймає повідомлення ж сервер)
        messageReceiver = new MessageReceiver(serverSocket);
        //так само об'єкт, який відправляє повідомлення
        messageSender = new MessageSender(dataProvider, SERVER_PORT);
        //запускаємо їх, при цьому відправник стартує в окремому від сервера потоці
        messageReceiver.start();
        messageSender.start();
    }
    //при отриманні запиту на відправку повідомлення делегуємо цю задачу
    //об'єкту-відправнику
    public void sendMessage(String str) {
        messageSender.sendMessage(str);
    }
}
