package net.javaguides.springboottesting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootTestingApplicationIT extends AbstractPostgreSQLTestContainers {

    @Test
    void contextLoads() {
    }

}
