import network.SocketEngine;
import ui.MainFrame;
import util.PutMessageEvent;

/*Перш за все варто відмітити, що функціонал програми ділиться на
* 2 частини: графічну та мережеву (робота з сокетами).
* Відповідно до цього у проекті є пакет ui із класами для
* роботи з графічним інтерфейсом та пакет network для роботи
* з мережею. Пакет util містить не менш важливі для роботи
* програми класи різного призначення(детальніший опис в кожному окремо). */
public class Application {

    //головне вікно
    private MainFrame mainFrame;
    //об'єкт що працює з сокетами
    private SocketEngine socketEngine;

    //створюємо екземпляри класів для роботи проги
    public Application() {
        socketEngine = new SocketEngine();
        mainFrame = new MainFrame(socketEngine);
        //PutMessageEvent - спеціальний об'єкт, створений за шаблоном проектування
        //singleton - такий об'єкт може бути лише один і є доступним з будь-якої точки програми
        //через метод getInstance
        PutMessageEvent.getInstance().setMainFrame(mainFrame);
    }

    //створюємо і запускаємо нашу прогу
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    public void run() {
        //запускаємо роботу з сокетами
        socketEngine.start();
        //запускаємо графічне вікно
        mainFrame.setVisible(true);
    }
}