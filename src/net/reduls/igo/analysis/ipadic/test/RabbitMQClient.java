package net.reduls.igo.analysis.ipadic.test;

/**
 * Development Environment: IntelliJ IDEA.
 * Developer: Wolfgang Kraske, PhD
 * Date: 4/26/11
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */

import com.rabbitmq.client.*;


/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 4/18/11
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class RabbitMQClient {
    public static void main(String[] args) {
        try {
            String request = (args.length > 0) ? args[0] : "Rabbit";
            String hostName = (args.length > 1) ? args[1] : "localhost";
            int portNumber = (args.length > 2) ? Integer.parseInt(args[2]) : AMQP.PROTOCOL.PORT;

/*
            ConnectionFactory cfconn = new ConnectionFactory();
            cfconn.setHost("192.168.0.139");
            cfconn.setPort(5672);
            cfconn.setUsername("rbt");
            cfconn.setPassword("NTTdocomo");
            cfconn.setVirtualHost("/");
*/

//            Connection conn;
//            Connection conn = cfconn.newConnection();

            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("192.168.0.139");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("rbt");
            connectionFactory.setPassword("NTTdocomo");
            connectionFactory.setVirtualHost("/");

            connectionFactory.getPort();

            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            QueueingConsumer consumer = new QueueingConsumer(channel);

            String rabbitQueue = "WG_TWT_QUEUE";
            String rabbitExchange  = "WG_TWT_QUEUE";
            String rabbitExchangeType = "direct";
            boolean rabbitExchangeDurable = true;
            boolean rabbitQueueDurable = true;
            boolean rabbitQueueAutoDelete = false;
            String rabbitRoutingKey = "WG_TWT_QUEUE";

            channel.exchangeDeclare(rabbitExchange/*exchange*/, rabbitExchangeType/*type*/, rabbitExchangeDurable);
            channel.queueDeclare(rabbitQueue/*queue*/, rabbitQueueDurable/*durable*/, false/*exclusive*/, rabbitQueueAutoDelete/*autoDelete*/, null);
            channel.queueBind(rabbitQueue/*queue*/, rabbitExchange/*exchange*/, rabbitRoutingKey/*routingKey*/);
            channel.basicConsume(rabbitQueue/*queue*/, false/*noAck*/, consumer);

            QueueingConsumer.Delivery task;

            task = consumer.nextDelivery();

            System.out.println(task.getBody());

      //      RpcClient service = new RpcClient(channel, "", "Hello");

      //      System.out.println(service.stringCall(request));
            connection.close();
        }   catch(Exception e) {
            System.err.println("Main thread caught exception: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
