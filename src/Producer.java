import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class Producer extends Thread {
    private int itemNumbers;
    private Semaphore fullStorage;
    private Semaphore emptyStorage;
    private Semaphore freeStorage;
    private LinkedList<String> storage;

    public Producer(int itemNumbers, Semaphore fullStorage, Semaphore emptyStorage, Semaphore freeStorage, LinkedList<String> storage) {
        this.itemNumbers = itemNumbers;
        this.fullStorage = fullStorage;
        this.emptyStorage = emptyStorage;
        this.freeStorage = freeStorage;
        this.storage = storage;
    }

    public void run() {
        for (int i = 1; i <= itemNumbers; i++) {
            try {
                freeStorage.acquire();
                fullStorage.acquire();

                synchronized (storage) {
                    storage.add("item " + i);
                    System.out.println("Producer add item " + i);
                }

                fullStorage.release();
                emptyStorage.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}