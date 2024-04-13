import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class Consumer extends Thread {
    private int itemNumbers;
    private Semaphore fullStorage;
    private Semaphore emptyStorage;
    private Semaphore freeStorage;
    private LinkedList<String> storage;

    public Consumer(int itemNumbers, Semaphore fullStorage, Semaphore emptyStorage, Semaphore freeStorage, LinkedList<String> storage) {
        this.itemNumbers = itemNumbers;
        this.fullStorage = fullStorage;
        this.emptyStorage = emptyStorage;
        this.freeStorage = freeStorage;
        this.storage = storage;
    }

    public void run() {
        for (int i = 1; i <= itemNumbers; i++) {
            try {
                emptyStorage.acquire();
                fullStorage.acquire();

                synchronized (storage) {
                    String item = storage.removeFirst();
                    System.out.println("Consumer get " + item);
                }

                fullStorage.release();
                freeStorage.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}