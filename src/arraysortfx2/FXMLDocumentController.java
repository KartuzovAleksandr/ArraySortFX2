package arraysortfx2;

import java.util.Arrays;      // для конвертации массива в строку
import java.util.Random;      // для генерации случайных чисел
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;      // радиокнопки
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup; // группа радиокнопок
import javafx.scene.control.Label;       // текстовое поле (метка)
import javafx.scene.control.TextField;   // поле ввода 
import javafx.scene.control.RadioButton; // радио-кнопки 
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*; // графические примитивы
import javafx.scene.paint.Color; // для задания цвета
import javafx.scene.text.*; // для вывода текста
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {
    private static final double MOVEMENT_MS = 350; // продолжительность движения  
    @FXML                 
    private Label label1; // добавлял руками, как и методы, поскольку не знал, что  
    @FXML                 // одной директивы недостаточно - перед каждым элементом из FXML файла такая директива
    private Label label2; 
    @FXML               
    private Label label3; 
    @FXML
    private TextField edit1; // размер под двузначное число, больше - не уместитьтся вывод на экране
    @FXML
    private RadioButton RadioButton1; // по возрастанию
    @FXML
    private RadioButton RadioButton2; // по убиванию
    @FXML
    private ToggleGroup SortOrder; // группа радиокнопок
    @FXML
    private Button button1; // генерация случ чисел
    @FXML
    private Button button2; // сортировка
    @FXML
    private Button button3; // остановить анимацию
    @FXML
    private ComboBox<?> SortMethod; // выбор метода сортировки
    @FXML
    private String straight;
    @FXML
    private String bubble;
    @FXML
    private String selection;
    @FXML
    private String bucket;
    @FXML
    private String heap;
    @FXML
    private String merge;
    @FXML
    private AnchorPane Animated; // панель для вывода анимации сортировки
    private int MAXVALUE=50; // максимальное значение для случайных чисел
    
    Integer m[]; // объявляем пустой массив
    Integer mm[]; // для сохранения исходного (при повторной сортировки другим методом)
    Integer workArray[]; // промежуточный массив для сортировки слиянием
    Random r=new Random(); // создаем класс для генерации случайных чисел
    int n; // размерность массива
    int np; // номер смены элементов массива (прохода, шага сортировки)
    int ns; // счетчик числа шагов при сортировке
    boolean sorted; // признак успешной сортировки
    
    Circle c; // промежуточная переменная для обмена кружочками
    Text t; // промежуточная переменная для обмена надписями
        // массив кружочков
    public static Circle circ[];
       // массив надписей         
    public static Text text[];

    SequentialTransition st = new SequentialTransition(); // создаем последовательную анимацию
    
    // создаем кружочки и надписи
private void CreateCircle() {
     // создаем массивы
    circ=new Circle[n];
    text=new Text[n];
    // создаем кружочки
    for (int i=0; i<n; i++) {
        circ[i]=new Circle(50+i*50, 50, 20);
            // красим в радугу по значениям
        if (m[i] >= 0 && m[i] <= 7)   
            circ[i].setFill(Color.RED);
        if (m[i] > 7 && m[i] <= 14)   
            circ[i].setFill(Color.ORANGERED);
        if (m[i] > 14 && m[i] <= 21)   
            circ[i].setFill(Color.YELLOW);
        if (m[i] > 21 && m[i] <= 28)   
            circ[i].setFill(Color.GREEN);
        if (m[i] > 28 && m[i] <= 35)   
            circ[i].setFill(Color.AQUA);
        if (m[i] > 35 && m[i] <= 42)   
            circ[i].setFill(Color.BLUE);
        if (m[i] > 42 && m[i] <= 50)   
            circ[i].setFill(Color.MAGENTA);
           // надпись
        text[i]=new Text(43+i*50, 53, ""+m[i]);
        text[i].setStyle("-fx-font-weight: bold"); // жирный
        // text[i].setStroke(Color.BLACK); // белый
        // text[i].setFill(Color.WHITE);
    }        
        // добавляем в сцену
    for (int j=0; j<n; j++) 
        Animated.getChildren().addAll(circ[j], text[j]);
}

        // полный перебор
    private void StraightSort() {
        int p; // временная переменная
        ns=0;
        for (int i=0;i<n;i++) {
            for (int j=0;j<n;j++) {
                if (m[i]<m[j]) {
                    p=m[i];
                    m[i]=m[j];
                    m[j]=p;
                    ns++; // увеличиваем счетчик шагов
                    PlotAni(j,i); // анимируем смену элементов массива
                }
            } 
        }
    }

      // пузырьковая сортировка (ст-т гр. РЭА-11-17 Иванов В.)
    private void BubbleSort() {
        int p; // временная переменная
        ns=0;
        for(int i=0;i<n;i++) {
            for (int j=0;j<n-i-1;j++) {
	         if (m[j]>m[j+1]) {
		    p=m[j];
		    m[j]=m[j+1];
		    m[j+1]=p;
                    ns++; // увеличиваем счетчик шагов
                    PlotAni(j, j+1); // анимируем смену элементов массива
	        }
            }
        }
    }

        // инвертирование массива
    private void ReverseArray() {
        int p; // временная переменная
        if (RadioButton1.isSelected()) // в противном случае сортировка будет основным методом и ns не надо обнулять, 
            ns=0;                      // а прибавить шаги инвертирования к существующим    
        for (int i = 0; i < n / 2; i++) { // проходим половину массива
            p = m[i]; // первый меняем с последним, потом второй с n-1 и т.д.
            m[i] = m[n - 1 - i];
            m[n - i - 1] = p;
            ns++;
            PlotAni(i, n - 1 - i); // выводим массив на экран                    
        }        
    }

        // сортировка выбором (partially ст-т гр. РЭА-11-17 Шарафутдинов Н.)
    private void BucketSort() {
        ns=0;
            // находим максимальное число в массиве
        int BucketMax=m[0];
        for (int i=1; i<n; i++) {
            if (m[i]>BucketMax) {
                BucketMax=m[i];
            }
        }
            // заполняем корзину нулями
        int[] bucket = new int[BucketMax+1];
        Arrays.fill(bucket, 0);
            // вставляем каждый элемент массива в блок
        for (int i=0; i < n; i++) {
            bucket[m[i]]++;
            ns++;
        }
            //отсортируем числа в карманах используя сортировку подсчетом
        for (int i=0, j=0; i < bucket.length; i++) {
        for(; bucket[i]>0; (bucket[i])--) {
            m[j++] = i;
            ns++;
            }
        }
        CreateCircle(); // рисуем отсортированный массив, т.к. обмена элементами нет
    }
        // сортировка выбором (ст-т гр. РЭА-11-17 Романов С.)
    private void SelectionSort() {
        ns=0;        
            // находим минимальный элемент в оставшейся части массива
        for (int i = 0; i < n; i++) {
            int min = m[i]; 
            int imin = i;   
            for (int j = i + 1; j < n; j++) {
                if (m[j] < min) {
                    min = m[j];
                    imin = j;
                    ns++; // считаем за один шаг
                }
            }
                // если минимум не равен текущему, то ставим его на место
            if (i != imin) {
                int temp = m[i];
                m[i] = m[imin];
                m[imin] = temp;
                ns++;
                PlotAni(i, imin); // выводим массив на экран                    
            }
        }
    }
        // пирамидальная сортировка (ст-ка гр. РЭА-11-17 Федорова М.)
    private void HeapSort() {
        ns=0;
        int p;
        for (int i = n / 2 - 1; i >= 0; i--)  
            shiftDown(i, n);
        for (int i = n - 1; i > 0; i--) {
                // меняем местами элементы
            p=m[i];
            m[i]=m[0];
            m[0]=p;
            ns++;
            shiftDown(0, i);
        } 
        CreateCircle(); // рисуем отсортированный массив, т.к. обмен слишком сложен
    }    
        // ищет листья в дереве (для пирамидальной сортировки) Федорова М.
    private void shiftDown(int i, int n) {
        int child;
        int tmp;
        for (tmp = m[i]; 2*i+1 < n; i = child) {
            child = 2*i+1;
            if (child != n - 1 && (m[child] < m[child + 1]))
                child++;
            if (tmp < m[child]) {
                m[i] = m[child];
                ns++;
            }
            else
                break;
        }
        ns++;
        m[i] = tmp;
    }
        // сортировка слиянием (ст-т гр. РЭА-11-17 Матвеев С.)
    private void MergeSort(int lower, int upper) {
        if (lower != upper) {
            ns=0; // счетчик шагов
            int middle = (lower + upper) / 2;
            MergeSort(lower, middle); // рекурсия
            MergeSort(middle + 1, upper);
            merge(lower, middle + 1, upper);
            CreateCircle(); // рисуем отсортированный массив, т.к. обмена элементами нет
        }
    }
        // Слияние двух массивов (ст-т гр. РЭА-11-17 Матвеев С.)
    private void merge(int lower, int middle, int upper) {
        int i = 0;
        int lowerBound = lower;
        int mid = middle - 1;
        int nn = upper - lower + 1;
        while((lower <= mid) && (middle <= upper)) {
            if (m[lower] < m[middle]) {
                workArray[i++] = m[lower++];
                ns++;
            } else {
                workArray[i++] = m[middle++];
                ns++;
            }
        }
        while(lower <= mid) {
            workArray[i++] = m[lower++];
            ns++;
        }
        while(middle <= upper) {
            workArray[i++] = m[middle++];
            ns++;
        }
        for(i=0; i < nn; i++) {
            m[lowerBound+i] = workArray[i];
            ns++;
        }
    }
    
    // анимация смены элементов (partially ст-т гр. РЭА-11-17 Иванов В.)
    private void PlotAni(int i, int j) { // аргументы - номера для смены элементов
        // движение вправо кружка                       
                    //Instantiating TranslateTransition class   
                TranslateTransition tr1 = new TranslateTransition();  
                   //shifting the X coordinate of the centre of the circle by 400   
                tr1.setByX((j-i)*50); // движение на разницу между положениями элементов  
                    //setting the duration for the Translate transition   
                tr1.setDuration(Duration.millis(MOVEMENT_MS));  
                    //setting Circle as the node onto which the transition will be applied  
                tr1.setNode(circ[i]);  
                    // движение вправо надписи                       
                TranslateTransition tr2 = new TranslateTransition();  
                   //shifting the X coordinate of the centre of the circle by 400   
                tr2.setByX((j-i)*50);  
                    //setting the duration for the Translate transition   
                tr2.setDuration(Duration.millis(MOVEMENT_MS));  
                    //setting Circle as the node onto which the transition will be applied  
                tr2.setNode(text[i]);  
                    // движение влево кружка                       
                    //Instantiating TranslateTransition class   
                TranslateTransition tr3 = new TranslateTransition();  
                   //shifting the X coordinate of the centre of the circle by 400   
                tr3.setByX(-50*(j-i));  
                    //setting the duration for the Translate transition   
                tr3.setDuration(Duration.millis(MOVEMENT_MS));  
                    //setting Circle as the node onto which the transition will be applied  
                tr3.setNode(circ[j]);  
                    // движение влево надписи                       
                TranslateTransition tr4 = new TranslateTransition();  
                   //shifting the X coordinate of the centre of the circle by 400   
                tr4.setByX(-50*(j-i));  
                    //setting the duration for the Translate transition   
                tr4.setDuration(Duration.millis(MOVEMENT_MS));  
                    //setting Circle as the node onto which the transition will be applied  
                tr4.setNode(text[j]);  
                    // добавляем в анимацию
                st.getChildren().addAll(tr1, tr2, tr3, tr4);
                
                // сортируем кружочки - меняем их местами
                c=circ[i];
                circ[i]=circ[j];
                circ[j]=c;
                    // сортируем надписи - меняем их местами
                t=text[i];
                text[i]=text[j];
                text[j]=t;
    }
    
    @FXML
    private void handleButton1Action(ActionEvent event) { 
        String s=edit1.getText(); // введенный размер (текст)
        if (s.isEmpty()) return;  // если строка пуста - выход
        n=Integer.parseInt(s); // размерность массива, введенная в поле edit1
        if ((n<2) || (n>12)) return; // ограничение на размерность массива
        m=new Integer[n]; // создаем массив размерности n
        mm=new Integer[n]; // создаем резервный массив размерности n
        for (int i=0; i<n; i++)  // заполняем массив случайными числами
            m[i]=r.nextInt(MAXVALUE); // случайное целое от 0 до 100
        for (int i=0; i<n; i++) // запоминаем исходный массив
            mm[i]=m[i]; // или System.arraycopy(m, 0, mm, 0, n);
        label1.setText(Arrays.toString(m)); // вывод исходного массива в тестовую метку label1
        button2.setDisable(false); // активизируем кнопку сортировки - массив есть !!!
        button3.setDisable(false); // активизируем кнопку остановки анимации
        Animated.getChildren().clear(); // чистим всю панель (возможно там есть старые кружочки)
        CreateCircle();
        sorted=false;
    }
    
    @FXML
    private void handleButton2Action(ActionEvent event) { 
            // возвращаем исходный массив если он уже отсортирован
        if (sorted) {
            label2.setText(""); // чистим строку вывода
                // восстанавливаем исходный массив
            for (int i=0;i<n;i++)
                 m[i]=mm[i];
            CreateCircle(); // рисуем массив заново
        }
            // выбор метода сортировки
        switch ((String)SortMethod.getValue()) {
            case "StraightSort": StraightSort();
            break;
            case "BubbleSort": BubbleSort();
            break;
            case "SelectionSort": SelectionSort();
            break;
            case "BucketSort": BucketSort();
            break;
            case "HeapSort": HeapSort();
            break;
            case "MergeSort": workArray=new Integer[n];
                              MergeSort(0, n-1);
            break;
        } 
        if (! RadioButton1.isSelected()) // кнопка RadioButton2 выбрана (отмечена)
            ReverseArray(); // инвертируем массив
        label2.setText(Arrays.toString(m)); // вывод результируещего массива в тестовую метку label2
        label3.setText("Сортировка произведена за "+ns+ " шагов");
        st.play();
        st.getChildren().clear(); // чистим анимацию
        sorted=true; // массив отсортирован, но возможно пользователь выберет другой метод
    }

    @FXML
    private void handleButton3Action(ActionEvent event) { 
        st.stop(); // останавливаем анимацию (иногда идет слишком долго)
        st.getChildren().clear(); // чистим анимацию        
        label2.setText(""); // чистим строку вывода
        System.arraycopy(mm, 0, m, 0, n); // восстанавливаем исходный массив
        CreateCircle(); // рисуем массив заново
        sorted=false;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
