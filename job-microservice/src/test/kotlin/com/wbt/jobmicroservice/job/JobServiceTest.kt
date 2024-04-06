package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import com.wbt.jobmicroservice.job.external.CompanyResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class JobServiceTest : AbstractTestContainerTest() {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @InjectMocks
    private lateinit var underTest: JobService

    @Mock
    private lateinit var jobRepository: JobRepository

    @BeforeEach
    fun setUp() {
        underTest = JobService(jobRepository, restTemplate)
    }

    @Test
    fun isContainerSetup() {
        assertThat(postgreSQLContainer.isCreated).isTrue()
        assertThat(postgreSQLContainer.isRunning).isTrue()
    }

    @Test
    fun findAll() {
        // GIVEN
        Mockito.`when`(jobRepository.findAll()).thenReturn(
            listOf(
                Job(
                    1L,
                    "Java developer",
                    "Intermediate java developer",
                    1400.0,
                    3_000.0,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    "Douala",
                    1L
                )
            )
        )
        Mockito.`when`(
            restTemplate.getForObject(
                "http://localhost:8082/api/v1/companies/{id}",
                CompanyResponse::class.java,
                1L
            )
        ).thenReturn(
            CompanyResponse(1L, "wbt", "work beats talent")
        )
        // WHEN
        underTest.findAll()
        // THEN
        Mockito.verify(jobRepository).findAll()
    }

    @Test
    fun whenFindJoBbyIdAlsoGetRelatedExistingCompanyDetail() {
        // GIVEN
        val companyId = 1L
        val jobId = 1L
        val job = Job(
            jobId,
            "Java developer",
            "Intermediate java developer",
            1400.0,
            3_000.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Douala",
            companyId
        )

        Mockito.`when`(jobRepository.findById(jobId)).thenReturn(Optional.of(job))

        Mockito.`when`(
            restTemplate.getForObject(
                "http://localhost:8082/api/v1/companies/{id}",
                CompanyResponse::class.java,
                companyId
            )
        ).thenReturn(CompanyResponse(companyId, "wbt", "work beats talent"))
        // WHEN
        val response = underTest.findById(jobId)
        // THEN
        Mockito.verify(jobRepository).findById(jobId)
        assertThat(response.company).isNotNull
    }

    @Test
    fun save() {
        // GIVEN
        val companyId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        val argumentCaptor = ArgumentCaptor.forClass(Job::class.java)
        Mockito
            .`when`(jobRepository.save(Mockito.any(Job::class.java)))
            .thenReturn(
                Job(
                    1L,
                    jobRequest.title,
                    jobRequest.description,
                    jobRequest.minSalary,
                    jobRequest.maxSalary,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    jobRequest.location,
                    jobRequest.company
                )
            )
        // WHEN
        val isSaved = underTest.save(jobRequest, companyId)
        Mockito.verify(jobRepository).save(argumentCaptor.capture())
        val capturedJob = argumentCaptor.value
        // THEN
        assertThat(isSaved).isTrue()
        assertThat(capturedJob.id).isNull()
        assertThat(capturedJob.title).isEqualTo(jobRequest.title)
        assertThat(capturedJob.description).isEqualTo(jobRequest.description)
        assertThat(capturedJob.minSalary).isEqualTo(jobRequest.minSalary)
        assertThat(capturedJob.maxSalary).isEqualTo(jobRequest.maxSalary)
        assertThat(capturedJob.location).isEqualTo(jobRequest.location)
        assertThat(capturedJob.companyId).isEqualTo(jobRequest.company)
    }

    @Test
    fun jobsWithMaxSalaryRange() {
        // GIVEN
        val companyId = 1L
        val maxDown = 1400.0
        val maxUp = 3_000.0
        Mockito
            .`when`(jobRepository.findByMaxSalaryBetween(maxDown, maxUp))
            .thenReturn(
                listOf(
                    Job(
                        1L,
                        "Java developer",
                        "Intermediate java developer",
                        1400.0,
                        3_000.0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "Douala",
                        companyId
                    )
                )
            )
        Mockito.`when`(
            restTemplate.getForObject(
                "http://localhost:8082/api/v1/companies/{id}",
                CompanyResponse::class.java,
                companyId
            )
        ).thenReturn(CompanyResponse(companyId, "wbt", "work beats talent"))
        // WHEN
        val actualResult = underTest.jobsWithMaxSalaryRange(maxDown, maxUp)
        // THEN
        Mockito.verify(jobRepository).findByMaxSalaryBetween(maxDown, maxUp)
        assertThat(actualResult.size).isGreaterThan(0)
    }

    @Test
    fun jobsWithMinSalaryRange() {
        // GIVEN
        val companyId = 1L
        val minDown = 1400.0
        val minUp = 3_000.0
        Mockito
            .`when`(jobRepository.findByMinSalaryBetween(minDown, minUp))
            .thenReturn(
                listOf(
                    Job(
                        1L,
                        "Java developer",
                        "Intermediate java developer",
                        1400.0,
                        3_000.0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "Douala",
                        companyId
                    )
                )
            )
        Mockito.`when`(
            restTemplate.getForObject(
                "http://localhost:8082/api/v1/companies/{id}",
                CompanyResponse::class.java,
                companyId
            )
        ).thenReturn(CompanyResponse(companyId, "wbt", "work beats talent"))
        // WHEN
        val actualResult = underTest.jobsWithMinSalaryRange(minDown, minUp)
        // THEN
        Mockito.verify(jobRepository).findByMinSalaryBetween(minDown, minUp)
        assertThat(actualResult.size).isGreaterThan(0)
    }

    @Test
    fun delete() {
        // GIVEN
        val companyId = 1L
        val jobId = 1L
        val job = Job(
            jobId,
            "Java developer",
            "Intermediate java developer",
            1400.0,
            3_000.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Douala",
            companyId
        )
        Mockito
            .`when`(jobRepository.findById(jobId))
            .thenReturn(Optional.of(job))
        val argumentCaptor = ArgumentCaptor.forClass(Job::class.java)
        // WHEN
        val isDeleted = underTest.delete(jobId)
        Mockito.verify(jobRepository).delete(argumentCaptor.capture())
        val capturedValue = argumentCaptor.value
        // THEN
        Mockito.verify(jobRepository).findById(jobId)
        assertThat(isDeleted).isTrue()
        assertThat(capturedValue).isNotNull
    }

    @Test
    fun whenJobDoNotExistTryToDeleteThrowException() {
        // GIVEN
        val impossibleJobId = -1L
        Mockito
            .`when`(jobRepository.findById(impossibleJobId))
            .thenReturn(Optional.empty())
        // WHEN
        assertThatThrownBy {
            underTest.delete(impossibleJobId)
        }.hasMessage("Job resource with Id $impossibleJobId not found")
        // THEN
        Mockito.verify(jobRepository, Mockito.never()).delete(Mockito.any())
    }

    @Test
    fun update() {
        // GIVEN
        val companyId = 1L
        val jobId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        val job = Job(
            jobId,
            "Java developer",
            "Intermediate java developer",
            1400.0,
            3_000.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Douala",
            companyId
        )
        Mockito
            .`when`(jobRepository.findById(jobId))
            .thenReturn(Optional.of(job))
        val captor = ArgumentCaptor.forClass(Job::class.java)
        // WHEN
        val actualResult = underTest.update(jobId, jobRequest)
        Mockito.verify(jobRepository).save(captor.capture())
        val capturedValue = captor.value
        // THEN
        assertThat(actualResult).isTrue()
        assertThat(capturedValue.title).isEqualTo(jobRequest.title)
        assertThat(capturedValue.description).isEqualTo(jobRequest.description)
        assertThat(capturedValue.minSalary).isEqualTo(jobRequest.minSalary)
        assertThat(capturedValue.maxSalary).isEqualTo(jobRequest.maxSalary)
        assertThat(capturedValue.location).isEqualTo(jobRequest.location)
        assertThat(capturedValue.companyId).isEqualTo(jobRequest.company)
    }

    @Test
    fun whenJobDoNotExistTryToUpdateThrowException() {
        // GIVEN
        val companyId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        val impossibleJobId = -1L
        Mockito
            .`when`(jobRepository.findById(impossibleJobId))
            .thenReturn(Optional.empty())
        // WHEN
        assertThatThrownBy {
            underTest.update(impossibleJobId, jobRequest)
        }.hasMessage("Job resource with Id $impossibleJobId not found")
        // THEN
        Mockito.verify(jobRepository, Mockito.never()).save(Mockito.any(Job::class.java))
    }
}