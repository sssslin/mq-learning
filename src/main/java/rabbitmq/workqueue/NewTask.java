package rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;


/**
 * @author junfeng.ling
 * @date 2020/6/10 17:29
 * @Description:暂时只是敲了个demo，具体文档还没看 文档地址：https://www.rabbitmq.com/tutorials/tutorial-two-java.html
 */
public class NewTask {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            String message = String.join(" ", argv);

            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}