import java.util.concurrent.atomic.AtomicInteger;

public class HeaderStats {
    private final AtomicInteger[] statusGroups = new AtomicInteger[6];
    private final AtomicInteger total = new AtomicInteger(0);
    private final AtomicInteger serverCount = new AtomicInteger(0);
    private final AtomicInteger poweredByCount = new AtomicInteger(0);

    public HeaderStats() {
        for (int i = 0; i < 6; i++) statusGroups[i] = new AtomicInteger();
    }

    public void incrementStatus(int code) {
        statusGroups[code / 100].incrementAndGet();
        total.incrementAndGet();
    }

    public void incrementServer() {
        serverCount.incrementAndGet();
    }

    public void incrementPoweredBy() {
        poweredByCount.incrementAndGet();
    }

    public void merge(HeaderStats other) {
        for (int i = 0; i < 6; i++) {
            statusGroups[i].addAndGet(other.statusGroups[i].get());
        }
        serverCount.addAndGet(other.serverCount.get());
        poweredByCount.addAndGet(other.poweredByCount.get());
        total.addAndGet(other.total.get());
    }

    @Override
    public String toString() {
        int totalVal = total.get();
        return "  2xx: " + statusGroups[2].get() +
                "\n  3xx: " + statusGroups[3].get() +
                "\n  4xx: " + statusGroups[4].get() +
                "\n  5xx: " + statusGroups[5].get() +
                String.format("\n  Server Header Present: %.1f%%", totalVal == 0 ? 0 : (100.0 * serverCount.get() / totalVal)) +
                String.format("\n  X-Powered-By Present: %.1f%%", totalVal == 0 ? 0 : (100.0 * poweredByCount.get() / totalVal));
    }
}

