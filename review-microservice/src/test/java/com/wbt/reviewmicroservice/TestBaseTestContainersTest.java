package com.wbt.reviewmicroservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBaseTestContainersTest extends BaseTestContainersUnitTest {

    @Test
    void testContainerIsWellConfigured() {
        assertThat(postgresSqlContainer.isRunning()).isTrue();
        assertThat(postgresSqlContainer.isCreated()).isTrue();
    }
}
