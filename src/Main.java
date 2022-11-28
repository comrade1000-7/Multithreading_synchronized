import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void findMax () {
        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        assert maxEntry != null;
        System.out.println("Самое частое количество повторений " + maxEntry.getKey()
                + " (встретилось " + maxEntry.getValue() + " раз)");
    }

    public static int counter (String string) {
        int countR = 0;
        String[] split = string.split("");
        for (int i = 0; i < string.length(); i++) {
            if (split[i].equals("R")) countR++;
        }
        return countR;
    }

    public static void main(String[] args) throws InterruptedException {
        int allRoutes = 1000;

        Thread findMaxThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                    findMax();
                }
            }
        });
        findMaxThread.start();

        for (int i = 0; i < allRoutes; i++) {
            Thread generateTread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int count = counter(route);
                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, 1);
                    } else {
                        sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                    }
                    System.out.println(route + " " + count);
                    sizeToFreq.notify();
                }
            });
            generateTread.start();
            generateTread.join();
        }
        findMaxThread.interrupt();
    }
}
