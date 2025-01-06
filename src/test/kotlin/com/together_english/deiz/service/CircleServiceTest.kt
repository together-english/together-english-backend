package com.together_english.deiz.service

import com.together_english.deiz.model.circle.*
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.DayOfWeek
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.Gender
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.FavoriteCircleRepository
import com.together_english.deiz.repository.MemberRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestPropertySource(
    properties = [
        "jwt.secret=dummy-secret",
        "cloud.aws.credentials.access-key=dummy-access-key",
        "cloud.aws.credentials.secret-key=dummy-secret-key",
        "cloud.aws.region.static=dummy-region",
        "jwt.accessExpiration=241233",
        "jwt.refreshExpiration=342432",
        "cloud.aws.s3.bucket=dummy",
        "auth.mail-api.key=your-api-key-value",
        "auth.mail-api.secret-key=your-api-secret-key-value",
        "domain.url=dummy-domain",
    ]
)

class CircleServiceTest() {
    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var circleService: CircleService

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var circleRepository: CircleRepository

    @Autowired
    private lateinit var favoriteCircleRepository: FavoriteCircleRepository

    private lateinit var savedMember: Member
    private lateinit var savedCircle: Circle

    @BeforeEach
    fun setUp() {
        entityManager.clear() // 초기화
        createSuccessTestData()
    }

    @AfterEach
    fun tearDown() {
        deleteTestData()
    }

    fun deleteTestData() {
        memberRepository.deleteAll()
        circleRepository.deleteAll()
    }

    fun createSuccessTestData() {
        val member = Member(
            name = "Jane Doe",
            nickname = "janedoe",
            email = "jane@example.com",
            hashedPassword = "password123",
            gender = Gender.F,
            age = 28,
            isTermsAgreed = true,
            isPrivacyAgreed = true
        )
        savedMember = memberRepository.save(member)

        val circle = Circle(
            leader = savedMember,
            title = "Sample Circle",
            englishLevel = EnglishLevel.BEGINNER,
            city = City.BUSAN,
            thumbnailUrl = "http://example.com/thumbnail.jpg",
            introduction = "This is a sample circle for beginners.",
            address = "123 Sample Street",
            capacity = 20,
            attendMode = AttendMode.OFFLINE,
            contactWay = ContactWay.EMAIL,
            onlineUrl = "http://example.com/meeting"
        )
        savedCircle = circleRepository.save(circle)
    }

    @Test
    fun add_FavoriteCircle_success() {
        // Given
        val favoriteCircle : FavoriteCircle = FavoriteCircle(
            circle = savedCircle,
            member = savedMember,
        )
        // When
        val savedFavoriteCircle = favoriteCircleRepository.save(favoriteCircle)
        // Then
        assertNotNull(savedFavoriteCircle)
        assertEquals(savedCircle.id, savedFavoriteCircle.circle.id)
        assertEquals(savedMember.id, savedFavoriteCircle.member.id)
    }

    @Test
    fun add_CircleFavorite_failed() {
        // Given
        var notSavedCircle = Circle(
            leader = savedMember,
            title = "Not saved Circle",
            englishLevel = EnglishLevel.BEGINNER,
            city = City.BUSAN,
            thumbnailUrl = "http://example.com/thumbnail.jpg",
            introduction = "This is a sample circle for beginners.",
            address = "123 Sample Street",
            capacity = 20,
            attendMode = AttendMode.OFFLINE,
            contactWay = ContactWay.EMAIL,
            onlineUrl = "http://example.com/meeting"
        )
        val favoriteCircle : FavoriteCircle = FavoriteCircle(
            circle = notSavedCircle,
            member = savedMember,
        )

        // When / Then
        assertThrows(JpaObjectRetrievalFailureException::class.java) {
            favoriteCircleRepository.save(favoriteCircle)
        }
    }


    @Test
    @Transactional
    fun `Lazy Loading 테스트 - circleSchedules 정상 로드 확인`() {
        // Given
        val savedCircle = createTestCircleWithSchedules()

        // When
        val circleDetailResponse = circleService.getCircleDetail(savedCircle.id)

        // Then
        assertNotNull(circleDetailResponse)
        assertEquals(savedCircle.id, circleDetailResponse.id)
        assertTrue(circleDetailResponse.circleSchedules.isNotEmpty())
        assertEquals(2, circleDetailResponse.circleSchedules.size)
        assertEquals(DayOfWeek.MONDAY, circleDetailResponse.circleSchedules[0].dayOfWeek)
        assertEquals("10:00", circleDetailResponse.circleSchedules[0].startTime)
        assertEquals("12:00", circleDetailResponse.circleSchedules[0].endTime)
    }

    private fun createTestCircleWithSchedules(): Circle {
        val circle = Circle(
            leader = createTestMember(),
            title = "Test Circle",
            englishLevel = EnglishLevel.BEGINNER,
            city = City.BUSAN,
            introduction = "This is a test circle",
            capacity = 20,
            attendMode = AttendMode.OFFLINE,
            contactWay = ContactWay.EMAIL
        )

        val schedule1 = CircleSchedule(
            circle = circle,
            dayOfWeek = DayOfWeek.MONDAY,
            startTime = "10:00",
            endTime = "12:00"
        )

        val schedule2 = CircleSchedule(
            circle = circle,
            dayOfWeek = DayOfWeek.WEDNESDAY,
            startTime = "14:00",
            endTime = "16:00"
        )

        circle.circleSchedules.addAll(listOf(schedule1, schedule2))

        entityManager.persist(circle)
        entityManager.flush() // 영속성 컨텍스트 반영
        entityManager.clear() // 영속성 컨텍스트 초기화 (Lazy Loading 테스트)

        return circle
    }

    private fun createTestMember(): Member {
        val member = Member(
            name = "John Doe",
            nickname = "johndoe",
            email = "johndoe@example.com",
            hashedPassword = "hashed_password",
            gender = Gender.M,
            age = 30,
            isTermsAgreed = true,
            isPrivacyAgreed = true
        )
        entityManager.persist(member)
        return member
    }

}