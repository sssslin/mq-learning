package rabbitmq.helllworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author junfeng.ling
 * @date 2020/6/9 10:17
 * @Description:这是官方的demo,demo下面的命令行只是为了运行代码，在这里，直接用IDE代替了
 * javac -cp amqp-client-5.7.1.jar Send.java Recv.java
 * cp 是classpath 的缩写，意思是，把当前目录当做类路径，编译源码
 */
public class Recv {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, deliver) ->{
            String message = new String(deliver.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag-> {});
    }
}
