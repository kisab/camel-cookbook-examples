package org.camelcookbook.examples.transactions.idempotentconsumer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Tests that demonstrate the behavior of idempotent consumption.
 */
public class IdempotentConsumerTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new IdempotentConsumerRouteBuilder();
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
