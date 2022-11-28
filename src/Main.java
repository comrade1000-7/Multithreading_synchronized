import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static int counter (String string) {
        int countR = 0;
        String[] split = string.split("");
        for (int i = 0; i < string.length(); i++) {
            if (split[i].equals("R")) countR++;
        }
        return countR;
    }

    public static void main(String[] args) {
        int allRoutes = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(allRoutes);

        for (int i = 0; i < allRoutes; i++) {
            executorService.submit(() -> {
                String route = generateRoute("RLRFR", 100);
                int count = counter(route);
                System.out.println(route + " " + count);
                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, 1);
                    } else {
                        sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                    }
                }
                return route;
            });
        }

        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        System.out.println("Самое частое количество повторений " + maxEntry.getKey()
                + " (встретилось " + maxEntry.getValue() + " раз)");
        System.out.println("Другие размеры: ");

        for (Map.Entry<Integer, Integer> entry: sizeToFreq.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
        }
    }
}
