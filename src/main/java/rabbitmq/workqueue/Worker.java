package rabbitmq.workqueue;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author junfeng.ling
 * @date 2020/6/10 17:55
 * @Description:
 * 参考文档：https://www.cnblogs.com/wuhenzhidu/p/10787702.html
 * 在这个demo中，我学习到的一点是，如何重复启动同样的程序：通过idea run/debug对话框，赋值配置文件即可（ctrl + D）
 * 在这个版本的代码代码中，生产者发送的信息都是按照 循环调度 的方式平均分配给消费者的，所以在官方demo中，
 * 一个worker接收到135，另外一个接收到的是24
 * 在这个官方demo中，还能学习到的概念就是 公平调度、消息回执、消息的持久化等等
 * 以上就是工作队列中可以学习到的内容
 */
public class Worker {
    private final static String TASK_QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] Done");
            }
        };

        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
