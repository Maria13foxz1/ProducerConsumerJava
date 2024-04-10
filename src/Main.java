//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;

public class Main {
    static Semaphore emptyStorage = new Semaphore(0); // Порожній сховище
    static Semaphore fullStorage = new Semaphore(3);  // Повне сховище
    static Semaphore freeStorage = new Semaphore(1);  // Вільне місце в сховищі

    static LinkedList<String> storage = new LinkedList<>(); // Сховище
    static Lock lock = new ReentrantLock(); // Lock для доступу до сховища

    static class Producer extends Thread {
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    freeStorage.acquire(); // Захоплення семафора вільного місця
                    fullStorage.acquire(); // Захоплення семафора повного сховища

                    lock.lock(); // Захоплення локу для доступу до сховища
                    storage.add("item " + i); // Додавання елемента у сховище
                    System.out.println("Producer add item " + i);
                    lock.unlock(); // Звільнення локу для доступу до сховища

                    fullStorage.release(); // Звільнення семафора повного сховища
                    emptyStorage.release(); // Звільнення семафора порожнього сховища
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer extends Thread {
        public void run() {
            for (int i = 1; i <= 5; i++) {
                try {
                    emptyStorage.acquire(); // Захоплення семафора порожнього сховища
                    fullStorage.acquire(); // Захоплення семафора повного сховища

                    lock.lock(); // Захоплення локу для доступу до сховища
                    String item = storage.removeFirst(); // Вилучення першого елемента з сховища
                    System.out.println("Consumer get " + item);
                    lock.unlock(); // Звільнення локу для доступу до сховища

                    fullStorage.release(); // Звільнення семафора повного сховища
                    freeStorage.release(); // Звільнення семафора вільного місця в сховищі
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        Producer producer1 = new Producer();
        Consumer consumer1 = new Consumer();
        Consumer consumer2 = new Consumer();

        producer1.start();
        consumer1.start();
        consumer2.start();
    }
}