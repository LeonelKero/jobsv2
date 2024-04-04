package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class JobRepositoryTest(@Autowired private val underTest: JobRepository) : AbstractTestContainerTest() {

    @Test
    fun isEnvironmentSetup() {
        assertThat(postgreSQLContainer.isRunning).isTrue()
        assertThat(postgreSQLContainer.isCreated).isTrue()
    }

    @BeforeEach
    fun setUp() {
        underTest.deleteAll() // clean database
        val job1 = Job(
            title = "Product Manager",
            description = "Evaluate and provide product visibility",
            minSalary = 3003.0,
            maxSalary = 8000.0,
            location = "Boston",
            companyId = 1L
        )
        val job2 = Job(
            title = "Kotlin Developer",
            description = "Intermediate Kotlin developer with solid experience in Spring Boot",
            minSalary = 1500.0,
            maxSalary = 7500.0,
            location = "Douala",
            companyId = 1L
        )
        val job3 = Job(
            title = "AI Engineer",
            description = "Senior artificial intelligence engineer",
            minSalary = 20_000.0,
            maxSalary = 210_000.0,
            location = "Delhi",
            companyId = 1L
        )
        val job4 = Job(
            title = "Digital Artist",
            description = "Internship at the art/design department",
            minSalary = 200.0,
            maxSalary = 600.0,
            location = "Kampala",
            companyId = 1L
        )
        underTest.saveAll(listOf(job1, job2, job3, job4)) // insert records
    }

    @Test
    fun findByMinSalaryRange() {
        // Given
        val startingAt = 100.0
        val endingAt = 270.0
        // When
        val jobs = underTest.findByMinSalaryBetween(startingAt, endingAt)
        // Then
        assertThat(jobs).isNotEmpty
        assertThat(jobs.size).isEqualTo(1)
    }

    @Test
    fun findByMaxSalaryRange() {
        // Given
        val startingAt = 7_500.0
        val endingAt = 300_000.0
        // When
        val jobs = underTest.findByMaxSalaryBetween(startingAt, endingAt)
        // Then
        assertThat(jobs).isNotEmpty
        assertThat(jobs.size).isEqualTo(3)
    }
}