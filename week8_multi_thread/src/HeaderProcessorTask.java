import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class HeaderProcessorTask implements Callable<Void> {
    private final File file;
    private final HeaderStats stats;

    public HeaderProcessorTask(File file, HeaderStats stats) {
        this.file = file;
        this.stats = stats;
    }

    @Override
    public Void call() throws Exception {
        System.out.println("Processing " + file.getPath() + " with " + Thread.currentThread().getName());
        String content = new String(Files.readAllBytes(file.toPath()));
        String[] blocks = content.split("\\r?\\n\\r?\\n");

        List<String> blockList = Arrays.asList(blocks);
        HeaderStats fileStats;
        if (blockList.size() > 1000) {
            ForkJoinPool pool = new ForkJoinPool();
            fileStats = pool.invoke(new ForkJoinHeaderTask(blockList));
        } else {
            fileStats = new ForkJoinHeaderTask(blockList).compute();
        }

        stats.merge(fileStats);
        File outFile = new File("week8_multi_thread/output/processed/" + file.getName().replace(".header", ".stats"));
        outFile.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            writer.write(fileStats.toString());
        }

        new HeaderCompressorProcess(outFile).start();
        return null;
    }
}