package codefod.com;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // TODO: Viết một chương trình Java cơ bản và thực chạy compile file chương trình ra bytecode
        // TODO: Thực chạy file bytecode bằng lệnh java
        // TODO: Thực hiện chạy file bytecode với cấu hình heap size, gc, ...

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            list.add(new Object());
        }
        System.out.println("Add " + list.size() + " into lists.");

        System.gc();

        for (int i = 0; i < 1000000; i++) {
            new Object();
        }

        System.gc();

        System.out.println("Program completed.");
    }
}