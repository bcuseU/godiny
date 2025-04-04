import javafx.application.Application; // базовый класс с основными методами start, stop
import javafx.scene.Scene; // Работает со сценами и узлами такие как width, root и height
import javafx.scene.layout.Pane; // Класс Pane с методом getChildren, является контейнером, который позволяет размещать узлы
import javafx.scene.paint.Color; // Класс Color с разными методами rgb, в строчку, как в CSS и статические константы RED, WHITE. BLUE, GREEN ,YELLOW
import javafx.scene.shape.Arc; // Класс Arc позволяющий рисовать дуги, секторы круга и позволяет работать с их свойствами centerX, centerY, radiusX, radiusY
import javafx.scene.shape.ArcType; // Позволяет делать разные виды дуг
import javafx.scene.shape.Line; // Класс Line с методами startX, startY, endX, endY, strokeWidth, работает с линиями и вообще с разными геометрическими фигурами
import javafx.scene.text.Text; // Класс Text с разными свойствами, которые можно применять и добавлять текст или числа на сцену
import javafx.stage.Stage; // Класс Stage c методами setTitle, setScene Работате с заголовками, сценой, положением и размером
import java.time.LocalTime; // Класс LocalTime представляет собой методы, которые работают с временем (с часами, минутами, секундами и даже нано секундами)
import java.util.Timer; // Класс Timer планирует выполенние задач
import java.util.TimerTask; // Абстрактный класс, который явялется вспомогательным для класса Timer с базовыми методами

public class godiny extends Application {

    private static final double WIDTH = 600; // Определяет размер выводящегося окна по горизонтали
    private static final double  HEIGHT = 600; // Определяет размер выводящегося окна по вертикали
    private static final double CLOCK_RADIUS = 170; // Определяет радиус часов в выводящемся окне
    private static final double CLOCK_CENTER_X = WIDTH / 2; // Определяет положение часов выводящемся окне по горизонтали
    private static final double CLOCK_CENTER_Y = HEIGHT / 2.2; // Определяет положение часов выводящемся окне по вертикали

    private Pane root;
    private Line hourHand; // Часовая стрелка
    private Line minuteHand; // Минутная стрелка
    private Line secondHand; // Секундная стрелка


    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene); // Устанавливает сцену для окна
        primaryStage.setTitle("Часы"); // Заголовок
        primaryStage.show(); // Показывает окно с часами при каждом вызове

        drawClockFace(); // Рисует циферблат часов
        drawHands(); // Рисует основные стрелки часов

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateHands();
            }
        }, 0, 1000); // Обновляет положение стрелок каждую секунду и относится к Timer
    }

    private void drawClockFace() {
        Arc clockFace = new Arc(CLOCK_CENTER_X, CLOCK_CENTER_Y, CLOCK_RADIUS, CLOCK_RADIUS, 0, 360); // С помощью Arc рисуем циферблат
        clockFace.setType(ArcType.OPEN);
        clockFace.setStroke(Color.BLACK);
        clockFace.setFill(Color.WHITE);
        root.getChildren().add(clockFace);


        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 90);
            double x = CLOCK_CENTER_X + (CLOCK_RADIUS - 3) * Math.cos(angle); // Метки часов от радиуса x
            double y = CLOCK_CENTER_Y + (CLOCK_RADIUS - 3) * Math.sin(angle);  // Метки часов от радиуса y
            root.getChildren().add(new Line(x, y, x + 10 * Math.cos(angle), y + 10 * Math.sin(angle))); // С помощью line рисует метки часов x и y

            double textX = CLOCK_CENTER_X + (CLOCK_RADIUS + 20) * Math.cos(angle); // Цифры вне часов над метками x
            double textY = CLOCK_CENTER_Y + (CLOCK_RADIUS + 20) * Math.sin(angle); // Цифры вне часов над метками y
            Text text = new Text(textX, textY, String.valueOf(i));
            text.setFill(Color.BLACK); // Цвет
            root.getChildren().add(text);
        }
    }

    private void drawHands() { // Добавляет секундную, минутную и часовую стрелки
        hourHand = new Line(CLOCK_CENTER_X, CLOCK_CENTER_Y, CLOCK_CENTER_X, CLOCK_CENTER_Y - CLOCK_RADIUS / 2); // Определяет местоположение часовой стрелки по x и y
        minuteHand = new Line(CLOCK_CENTER_X, CLOCK_CENTER_Y, CLOCK_CENTER_X, CLOCK_CENTER_Y - CLOCK_RADIUS * 0.7); // Определяет местоположение минутной стрелки по x и y
        secondHand = new Line(CLOCK_CENTER_X, CLOCK_CENTER_Y, CLOCK_CENTER_X, CLOCK_CENTER_Y - CLOCK_RADIUS * 0.9); // Определяет местоположение секундной стрелки по x и y

        hourHand.setStroke(Color.BLACK); // Ставит цвет часовой стрелки
        minuteHand.setStroke(Color.BLACK); // Ставит цвет минутной стрели
        secondHand.setStroke(Color.RED); //ставит цвет секундной стрелки

        root.getChildren().addAll(hourHand, minuteHand, secondHand);
    }

    private void updateHands() { // Обновляет текущее время
        LocalTime currentTime = LocalTime.now(); // Получает настоящее время

        // Обновляем часовую стрелку
        double hourAngle = Math.toRadians((currentTime.getHour() % 12 + currentTime.getMinute() / 60.0) * 30 - 90);
        hourHand.setEndX(CLOCK_CENTER_X + (CLOCK_RADIUS / 2) * Math.cos(hourAngle));
        hourHand.setEndY(CLOCK_CENTER_Y + (CLOCK_RADIUS / 2) * Math.sin(hourAngle));

        // Обновляем минутную стрелку
        double minuteAngle = Math.toRadians(currentTime.getMinute() * 6 - 90);
        minuteHand.setEndX(CLOCK_CENTER_X + (CLOCK_RADIUS * 0.7) * Math.cos(minuteAngle));
        minuteHand.setEndY(CLOCK_CENTER_Y + (CLOCK_RADIUS * 0.7) * Math.sin(minuteAngle));

        // Обновляем секундную стрелку
        double secondAngle = Math.toRadians(currentTime.getSecond() * 6 - 90);
        secondHand.setEndX(CLOCK_CENTER_X + (CLOCK_RADIUS * 0.9) * Math.cos(secondAngle));
        secondHand.setEndY(CLOCK_CENTER_Y + (CLOCK_RADIUS * 0.9) * Math.sin(secondAngle));
    }

    public static void main(String[] args) {
        launch(args);
    }
}