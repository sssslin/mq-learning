package rabbitmq.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * https://www.cnblogs.com/julyluo/p/6265775.html
  exchange有4个类型：direct，topic，fanout，header
  绑定方式：
  fanout：直接将消息发送到所有队列中，无需匹配
  direct：队列和exchange两者的key完全匹配的时候才能发送消息
  topic：也需要进行key的匹配，但是topic模式中，可以用通配符进行匹配，比如
 '*','#'.其中'*'表示匹配一个单词， '#'则表示匹配没有或者多个单词
  header:通过header匹配
 Sending messages to many consumers at once
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");


        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = args.length < 1? "info: Hello World!": String.join(" ", args);
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
