package org.camelcookbook.examples.transactions.idempotentconsumer;

import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.camel.test.spring.CamelSpringTestSupport;

/**
 * Tests that demonstrate the behavior of idempotent consumption.
 */
public class IdempotentConsumerSpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/idempotentConsumer-context.xml");
    }

    @Test
    public void testReplayOfSameMessageWillNotTriggerCall() throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

}
