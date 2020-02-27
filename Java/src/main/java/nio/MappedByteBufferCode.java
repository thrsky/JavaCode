package nio;

import com.sun.tools.javac.util.Assert;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

/**
 * 这个类是MappedByteBuffer的测试类
 *
 * @author thrsky
 * @version 1.0.0
 * @date Created in 4:20 下午 2020/2/27
 */
public class MappedByteBufferCode {

    /**
     * 测试内容文本
     */
    private static final String DATA = "this is test data for MappedByteBuffer";
    /**
     * 最终这个文件会生成在 target/classes/test.txt
     **/
    private static final String TEST_FILE_PATH = "/test.txt";

    public static void main(String[] args) {
        MappedByteBufferCode byteBufferCode = new MappedByteBufferCode();
        byteBufferCode.write();
        byteBufferCode.read();
    }

    /**
     * 利用MappedByteBuffer进行文件写入
     */
    public void write() {
        Path path = Paths.get(MappedByteBufferCode.class.getResource(TEST_FILE_PATH).getPath());
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(READ_WRITE, 0, DATA.getBytes().length);
            if (mappedByteBuffer != null) {
                mappedByteBuffer.put(DATA.getBytes());
                mappedByteBuffer.force();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用MappedByteBuffer进行文件读取
     */
    public void read() {
        Path path = Paths.get(getClass().getResource(TEST_FILE_PATH).getPath());
        int length = DATA.getBytes(StandardCharsets.UTF_8).length;
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(READ_ONLY, 0, length);
            if (mappedByteBuffer != null) {
                byte[] bytes = new byte[length];
                mappedByteBuffer.get(bytes);
                String content = new String(bytes, StandardCharsets.UTF_8);
                Assert.check(content.equals(DATA));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
