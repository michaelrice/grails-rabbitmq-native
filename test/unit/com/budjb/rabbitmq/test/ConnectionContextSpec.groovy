package com.budjb.rabbitmq.test

import com.budjb.rabbitmq.connection.ConnectionConfiguration
import com.budjb.rabbitmq.connection.ConnectionContext
import com.budjb.rabbitmq.consumer.RabbitConsumerAdapter
import com.rabbitmq.client.ConnectionFactory

import spock.lang.Specification

class ConnectionContextSpec extends Specification {
    def 'Verify RabbitMQ connection factory parameters'() {
        setup:
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration([
            'host': 'localhost',
            'username': 'guest',
            'password': 'guest'
        ])
        ConnectionFactory connectionFactory = Mock(ConnectionFactory)
        ConnectionContext connectionContext = new ConnectionContext(connectionConfiguration)
        connectionContext.setConnectionFactory(connectionFactory)

        when:
        connectionContext.openConnection()

        then:
        1 * connectionFactory.newConnection(_)
        1 * connectionFactory.setHost('localhost')
        1 * connectionFactory.setUsername('guest')
        1 * connectionFactory.setPassword('guest')
    }

    def 'Validate start/stop/reset functionality'() {
        setup:
        RabbitConsumerAdapter consumer1 = Mock(RabbitConsumerAdapter)
        RabbitConsumerAdapter consumer2 = Mock(RabbitConsumerAdapter)
        RabbitConsumerAdapter consumer3 = Mock(RabbitConsumerAdapter)

        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration([
            'host': 'localhost',
            'username': 'guest',
            'password': 'guest'
        ])
        ConnectionFactory connectionFactory = Mock(ConnectionFactory)
        ConnectionContext connectionContext = new ConnectionContext(connectionConfiguration)
        connectionContext.setConnectionFactory(connectionFactory)
        connectionContext.registerConsumer(consumer1)
        connectionContext.registerConsumer(consumer2)
        connectionContext.registerConsumer(consumer3)

        when:
        connectionContext.startConsumers()

        then:
        1 * consumer1.start()
        1 * consumer2.start()
        1 * consumer3.start()

        when:
        connectionContext.stopConsumers()

        then:
        1 * consumer1.stop()
        1 * consumer2.stop()
        1 * consumer3.stop()
    }
}
