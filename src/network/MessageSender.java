package network;

import util.DataProvider;
import util.PutMessageEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

//об'єкт-відправник повідомлень
//наслідуємось від Thread, тому що нам необхідно буде запуститись в
//окремому від сервера потоці
public class MessageSender extends Thread {

    //потік, у який посилатимуться повідомлення
    private DataOutputStream clientOut;
    //сокет-відправник
    private Socket clientSocket;
    //адреса куди відправляємо
    private InetAddress address;
    //порт на який відправляємо
    private int port;
    //як ми себе називаємо
    private String myName;
    //в цього чувака будем просити додати до текстового поля відправлене поівдомлення
    private PutMessageEvent event;

    public MessageSender(DataProvider dataProvider, int port) {
        address = dataProvider.getAddress();
        myName = dataProvider.getName();
        this.port = port;
        event = PutMessageEvent.getInstance();
    }

    //собсно, метод, який буде запускатись в окремому потоці
    @Override
    public void run() {
        //поки не створиться клієнтський сокет (для відправки)
        while (clientSocket == null) {
            try {
                //намагаємося підконектитись до сервера з яким будем переписуватись
                clientSocket = new Socket(address, port);
            } catch (IOException ex) {
            }
        }
        //після того як ми нарешті законектились до іншого сервера(тобто після виходу з циклу)
        try {
            //отримуємо з сокету потік у який можна писати дані
            // (які потім сервер на іншому компі може зчитати)
            clientOut = new DataOutputStream(
                    clientSocket.getOutputStream()
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    //коли його просять відіслати повідомлення str
    public void sendMessage(String str) {
            try {
                //додає до повідомлення ім'я, щоб було зрозуміло хто відправив
                str = myName + ": " + str;
                //записуємо строку у потім для запису
                clientOut.writeUTF(str);
                //ця функція тіпа повідомляє шо запис закінчено або шось таке
                //якщо її не прописати то сервер не отримає повідомлення
                //точніше отримає, але буде думати що отримав неповне і буде чекати
                clientOut.flush();
                //одразу після відправки просимо розмістити відправлене повідомлення у текстовому
                //полі для повідомлень
                event.putMessage(str);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }
}
