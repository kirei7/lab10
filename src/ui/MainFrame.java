package ui;

import network.SocketEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//єдиний клас для роботи з інтерфейсом
public class MainFrame extends JFrame {

    //загальний контейнер
    private Container container;
    //текстова область, у якій будуть відображатись повідомлення
    private JTextArea textArea;
    //область з прокруткою, у неї покладеться textArea
    //типу коли повідомлень стане багато, вона
    //повинна прокручуватись вниз
    private JScrollPane scrollPane;
    //поле для вводу повідомлення
    private JTextArea inputField;
    //кнопка щоб відіслати повідомлення
    private JButton sendButton;

    //задаємо формат у якому буде відображатись час відправки/прийому повідомлення
    private DateFormat dateFormat = new SimpleDateFormat("[hh:mm]");
    //екземпляр нашого класу для роботи з мережею
    private SocketEngine engine;

    //текстова константа, що містить символ переносу строки(тіпа абзацу)
    //її потрібно вставляти після кожного повідомлення, щоб кожне нове
    //повідомлення починалось з нової стрічки
    //можна було б просто записати її як '\n' для windows але я пишу код в лінуксі і
    //там для переносу строки використовується дещо інше, тому тут ми отримуємо
    //його через System.getProperty("line.separator"), таким чином, прога сама визначить
    //який символ використовувати, в залежності від того де вона запускається
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

    //конструктор, тут створюємо і збираємо докупи елементи інтерфейсу
    public MainFrame(SocketEngine engine) {
        super("Chat");
        this.engine = engine;
        int width = 350,
            height = 500;
        this.setSize(width, height);
        //наступні дві стрічки потрібні для того щоб вікно відкривалось посередині екрану
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        //завершуємо програму після закриття вікна
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //задаємо лейаут основного вікна - BorderLayout ділить його на верх-низ, ліво-право, і центр
        this.setLayout(new BorderLayout());

        //записуємо для зручності основний контейнер у змінну
        container = getContentPane();

        //створюємо текстову область неможливу для редагування та яка буде розтягуватись при заповненні
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        //область прокрутки
        scrollPane  = new JScrollPane(textArea);
        //її розмір
        scrollPane.setSize(width, height - 50);
        //забороняємо прокрутку по горизонталі
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //дозволяємо при необхідності прокрутку по вертикалі
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        //приблизно такі самі дії для області для введення повідомлень і її області прокрутки
        //область на 2 рядки та 20 символів в ширину
        inputField = new JTextArea(2, 20);
        //курсор на початок області
        inputField.setCaretPosition(0);
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputField);
        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        //кнопка для передачі повідомлення
        sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonActionListener());

        //панель яка буде містити панель прокрутки для вводу та кнопку передачі
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(inputScrollPane);
        southPanel.add(sendButton);

        //додаємо в основний контейнер текстову область для відображення повідомлень
        container.add(scrollPane, BorderLayout.CENTER);
        //і область з полем вводу і кнопкою
        container.add(southPanel, BorderLayout.SOUTH);

    }

    //метод, який викликається через PutMessageEvent
    //по суті вставляє в область для повідомлень... повідомлення
    public void putMessage(String msg) {
        //спеціальний об'єкт для зручності побудови строк
        StringBuilder builder = new StringBuilder();
        //в область вставляємо
        textArea.append(
                //строку
                builder
                        //яка починається з форматованої дати
                        .append(
                            dateFormat.format(new Date())
                        )
                        //повідомлення (переданого або отриманого)
                        .append(msg)
                        //і кінчається символом переносу строки який ми об'являли на початку
                        .append(LINE_SEPARATOR)
                .toString()
        );
    }

    //обробник події натискання на кнопку
    class SendButtonActionListener implements ActionListener {
        //при натисканні на кнопку
        @Override
        public void actionPerformed(ActionEvent e) {
            //отримуємо строку з поля вводу і обрізаємо пробіли на початку
            //і в кінці якщо такі є
            String str = inputField.getText().trim();
            //якщо ця строка не пуста
            if (!str.isEmpty()) {
                //просимо наш мережевий двіжок відіслати цю строку
                engine.sendMessage(str);
                //а поле для вводу очищаємо
                inputField.setText("");
            }

        }
    }
}
