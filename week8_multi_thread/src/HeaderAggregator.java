
public class HeaderAggregator implements Runnable {
    private final HeaderStats stats;

    public HeaderAggregator(HeaderStats stats) {
        this.stats = stats;
    }

    @Override
    public void run() {
        System.out.println("\n[Scheduled Aggregator]");
        System.out.println(stats);
    }
}
