package org.appenders.log4j2.elasticsearch.jest;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2018 Rafal Foltynski
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.appenders.log4j2.elasticsearch.BatchEmitterFactory;
import org.appenders.log4j2.elasticsearch.NoopFailoverPolicy;
import org.junit.Test;
import org.mockito.Mockito;

import static org.appenders.log4j2.elasticsearch.jest.JestHttpObjectFactoryTest.createTestObjectFactoryBuilder;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;

public class BulkEmitterFactoryTest {


    @Test
    public void acceptsBulkProcessorObjectFactory() {

        // given
        BatchEmitterFactory emitterFactory = new BulkEmitterFactory();

        // when
        boolean result = emitterFactory.accepts(JestHttpObjectFactory.class);

        // then
        assertTrue(result);
    }

    @Test
    public void acceptsExtendingClientObjectFactories() {

        // given
        BatchEmitterFactory emitterFactory = new BulkEmitterFactory();

        // when
        boolean result = emitterFactory.accepts(TestBulkProcessorObjectFactory.class);

        // then
        assertTrue(result);
    }

    @Test
    public void createsBatchListener() {

        // given
        BatchEmitterFactory factory = new BulkEmitterFactory();
        JestHttpObjectFactory clientObjectFactory = spy(createTestObjectFactoryBuilder().build());
        NoopFailoverPolicy failoverPolicy = new NoopFailoverPolicy();

        // when
        factory.createInstance(1, 1, clientObjectFactory, failoverPolicy);

        // then
        Mockito.verify(clientObjectFactory).createBatchListener(eq(failoverPolicy));

    }

    @Test
    public void createsBulkOperations() {

        // given
        BatchEmitterFactory factory = new BulkEmitterFactory();
        JestHttpObjectFactory clientObjectFactory = spy(createTestObjectFactoryBuilder().build());

        // when
        factory.createInstance(1, 1, clientObjectFactory, new NoopFailoverPolicy());

        // then
        Mockito.verify(clientObjectFactory).createBatchOperations();

    }

    public static class TestBulkProcessorObjectFactory extends JestHttpObjectFactory {
        protected TestBulkProcessorObjectFactory() {
            super(null, 0, 0, 0, 0, false, null);
        }
    }
}
