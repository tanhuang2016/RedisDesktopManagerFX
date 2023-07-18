package xyz.hashdog.rdm.redis.imp.console;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用RedisConsole实现,不管什么类型的RedisClient,都只要实现不同的SocketAcquirer,
 * 就可以获取对应的socket进行交互通信
 *
 * @Author th
 * @Date 2023/7/18 21:31
 */
public class RedisConsole implements xyz.hashdog.rdm.redis.RedisConsole {

    /**
     * socket获取器
     */
    private SocketAcquirer socketAcquirer;

    public RedisConsole(SocketAcquirer socketAcquirer) {
        this.socketAcquirer = socketAcquirer;
    }

    @Override
    public List<String> sendCommand(String cmd) {
        try {
            Socket socket = socketAcquirer.getSocket();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            // 获取输入输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(cmd + "\r\n");
            writer.flush();
            List<String> result = parseResult(reader);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析到结果集
     * 需要根据RESP解析需要的结果
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private List<String> parseResult(BufferedReader reader) throws IOException {
        String line;
        if ((line = reader.readLine()) != null) {
            return ReaderParseEnum.getByLine(line).readerParser.parse(line,reader);
        }
        return new ArrayList<>();
    }
}
