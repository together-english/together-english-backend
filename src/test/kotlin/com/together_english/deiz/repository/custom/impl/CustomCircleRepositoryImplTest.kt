package com.together_english.deiz.repository.custom.impl

import com.together_english.deiz.model.circle.AttendMode
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.ContactWay
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.Gender
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.MemberRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestPropertySource(properties = [
    "jwt.secret=dummy-secret",
    "cloud.aws.credentials.access-key=dummy-access-key",
    "cloud.aws.credentials.secret-key=dummy-secret-key",
    "cloud.aws.region.static=dummy-region",
    "jwt.accessExpiration=241233",
    "jwt.refreshExpiration=342432",
    "cloud.aws.s3.bucket=dummy",
    "auth.mail-api.key=your-api-key-value",
    "auth.mail-api.secret-key=your-api-secret-key-value"
])
class CustomCircleRepositoryImplTest {

    @Autowired
    private lateinit var circleRepository: CircleRepository
    @Autowired
    private lateinit var memberRepository: MemberRepository
    @Test
    fun `test findCirclesWithSchedules using CircleRepository`() {
        // Given: Set up some test data
        val member = Member(
                name = "John Doe",
                nickname = "johnny",
                email = "john@example.com",
                hashedPassword = "hashedPassword123",
                profile = "http://example.com/profile.jpg",
                gender = Gender.M,
                age = 25,
                isTermsAgreed = true,
                isPrivacyAgreed = true,
                isMarketingAgreed = false
        )
        memberRepository.save(member)

        val circle = Circle(
                leader = member,
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
        circleRepository.save(circle)

        // When: We call the findCirclesWithSchedules method
        val pageable = PageRequest.of(0, 10)
        val result = circleRepository.findCirclesByPagination(pageable, null)

        // Then: Verify the results
        assertEquals(1, result.totalElements)
        val circleResponse = result.content[0]
        assertEquals(circle.id, circleResponse?.id)
        assertEquals(circle.thumbnailUrl, circleResponse?.thumbnailUrl)
        assertEquals(circle.title, circleResponse?.title)
        assertEquals(circle.introduction, circleResponse?.introduction)
        assertEquals(member.profile, circleResponse?.leaderProfile)
        assertEquals(member.nickname, circleResponse?.leaderName)
        assertEquals(circle.englishLevel, circleResponse?.englishLevel)
        assertEquals(circle.city, circleResponse?.city)
        assertEquals(circle.capacity, circleResponse?.capacity)
        assertEquals(circle.totalView, circleResponse?.totalView)
        assertEquals(circle.totalLike, circleResponse?.totalLike)
    }
}