import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ForkJoinHeaderTask extends RecursiveTask<HeaderStats> {
    private final List<String> blocks;
    private static final int THRESHOLD = 200;

    public ForkJoinHeaderTask(List<String> blocks) {
        this.blocks = blocks;
    }

    @Override
    protected HeaderStats compute() {
        if (blocks.size() <= THRESHOLD) {
            return processBlocks(blocks);
        } else {
            int mid = blocks.size() / 2;
            ForkJoinHeaderTask left = new ForkJoinHeaderTask(blocks.subList(0, mid));
            ForkJoinHeaderTask right = new ForkJoinHeaderTask(blocks.subList(mid, blocks.size()));
            left.fork();
            HeaderStats rightResult = right.compute();
            HeaderStats leftResult = left.join();
            leftResult.merge(rightResult);
            return leftResult;
        }
    }

    private HeaderStats processBlocks(List<String> blockList) {
        HeaderStats stats = new HeaderStats();
        for (String block : blockList) {
            String[] lines = block.split("\\r?\\n");
            if (lines.length == 0) continue;
            if (!lines[0].startsWith("HTTP/")) continue;

            int code = Integer.parseInt(lines[0].split(" ")[1]);
            stats.incrementStatus(code);

            for (String line : lines) {
                if (line.toLowerCase().startsWith("server:")) stats.incrementServer();
                if (line.toLowerCase().startsWith("x-powered-by:")) stats.incrementPoweredBy();
            }
        }
        return stats;
    }
}
