//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.concurrent.Semaphore;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        int storageSize = 7;
        Semaphore fullStorage = new Semaphore(storageSize);
        Semaphore emptyStorage = new Semaphore(0);
        Semaphore freeStorage = new Semaphore(1);
        LinkedList<String> storage = new LinkedList<>();

        int numConsumers = 3;
        int numProducers = 2;

        Thread[] consumers = new Thread[numConsumers];
        Thread[] producers = new Thread[numProducers];

        int[] itemsToAddConsumer = {4, 5, 6};
        int[] itemsToAddProducer = {7, 8};

        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(itemsToAddConsumer[i], fullStorage, emptyStorage, freeStorage, storage);
            consumers[i] = consumer;
            consumer.start();
        }

        for (int i = 0; i < numProducers; i++) {
            Producer producer = new Producer(itemsToAddProducer[i], fullStorage, emptyStorage, freeStorage, storage);
            producers[i] = producer;
            producer.start();
        }

        try {
            for (Thread consumer : consumers) {
                consumer.join();
            }
            for (Thread producer : producers) {
                producer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}