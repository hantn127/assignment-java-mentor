import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static final String[] FOLDERS = {"headers/100", "headers/200", "headers/300", "headers/400", "headers/500"};
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        HeaderStats stats = new HeaderStats();

        // Start scheduled aggregation
        scheduler.scheduleAtFixedRate(new HeaderAggregator(stats), 0, 5, TimeUnit.SECONDS);

        List<Future<Void>> futures = new ArrayList<>();
        for (String folderPath : FOLDERS) {
            File folder = new File("week8_multi_thread/" + folderPath);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    Callable<Void> task = new HeaderProcessorTask(file, stats);
                    futures.add(executor.submit(task));
                }
            }
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        scheduler.shutdown();
    }
}
