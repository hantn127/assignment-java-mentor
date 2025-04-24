import java.io.File;

public class HeaderCompressorProcess {
    private final File file;

    public HeaderCompressorProcess(File file) {
        this.file = file;
    }

    public void start() {
        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "echo", "[Compressor] Compressing:", file.getPath());
                pb.inheritIO();
                pb.start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
