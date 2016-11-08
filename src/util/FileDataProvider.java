package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/*А ось і клас, що реалізує раніше описаний інтерфейс*/
public class FileDataProvider implements DataProvider {

    /*Жорстко вказуємо адреси файлів де шукати необхідні дані
    * Як бачимо, шляхи вказані не абсолютні(типу C:\\folder\sample.txt)
    * а відносні. Відносно чого? Відносно кореню jar-архіву, у який збирається прога*/
    private final static String ADDRESS_PATH = "/address.txt";
    private final static String NAME_PATH = "/name.txt";


    @Override
    public InetAddress getAddress() {
        //одразу ініціалізуємо значення адреси null, щоб було що вертати у разі
        //якщо у файлі такої не буде
        InetAddress address = null;
        try {
            //отримуємо рідер з файлу з адресою
            BufferedReader input = new BufferedReader(new InputStreamReader(InetAddress.class.getResourceAsStream(ADDRESS_PATH)));
            //тут перетворюємо текстовий рядок(див нижче) з адресою в об'єкт, що інкапсулює
            //адресу в собі
            address = InetAddress.getByName((
                    //тут отримуємо текстовий рядок з адресою, який потім буде перетворено(див вище)
                    input.readLine()
                    ));
            //закриваємо рідер, щоб не трапилась "витічка пам'яті"
            input.close();
        }
        catch (Exception ex) {
            //якщо трапиться виключення, то просто виводимо його в консоль
            ex.printStackTrace();
        }
        return address;
    }

    @Override
    public String getName() {
        //те саме, тільки не адреса а ім'я
        //єдина різниця що ми повертаємо ім'я в такому ж вигляді як
        //отримали з файлу а не перетворюємо його
        String name = null;
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(InetAddress.class.getResourceAsStream(NAME_PATH)));
            name = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return name;
    }
}
