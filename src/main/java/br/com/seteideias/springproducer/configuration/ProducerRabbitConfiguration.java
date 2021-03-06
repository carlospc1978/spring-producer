package br.com.seteideias.springproducer.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerRabbitConfiguration {

    @Value("${spring.rabbitmq.request.routing-key.producer}")
    private String queue;

    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchange;

    @Value("${spring.rabbitmq.request.deadletter.producer}")
    private String deadLetter;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Queue deadLeatter() {
        return new Queue(deadLetter);
    }

    @Bean
    Queue queue() {
        Map<String, Object> argumentos = new HashMap<>();
        argumentos.put("x-dead-letter-exchange", exchange);
        argumentos.put("x-dead-letter-routing-key", deadLetter);
        return new Queue(queue, true, false, false, argumentos);
    }

    @Bean
    public Binding bindingQueue() {
        return BindingBuilder.bind(queue())
                .to(exchange()).with(queue);
    }

    @Bean
    public Binding bindingDeadLetter() {
        return BindingBuilder.bind(deadLeatter())
                .to(exchange()).with(deadLetter);
    }

}
