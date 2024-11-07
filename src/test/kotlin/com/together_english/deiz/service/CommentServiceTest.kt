package com.together_english.deiz.service

import com.together_english.deiz.model.circle.*
import com.together_english.deiz.model.circle.dto.CommentUpdateRequest
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.Gender
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.CommentRepository
import com.together_english.deiz.repository.MemberRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.util.*


@SpringBootTest
@TestPropertySource(
    properties = [
        "jwt.secret=dummy-secret",
        "cloud.aws.credentials.access-key=dummy-access-key",
        "cloud.aws.credentials.secret-key=dummy-secret-key",
        "cloud.aws.region.static=dummy-region",
        "jwt.accessExpiration=241233",
        "jwt.refreshExpiration=342432",
        "cloud.aws.s3.bucket=dummy"
    ]
)
class CommentServiceTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var circleRepository: CircleRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    private lateinit var savedMember: Member
    private lateinit var savedCircle: Circle

    @BeforeEach
    fun setUp() {
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
        // 테스트 데이터 생성 (실제 데이터베이스에 저장)
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
    fun `create CircleComment_success`() {
        // Given
        val circleComment = CircleComment(
            content = "This is a test comment",
            circle = savedCircle,
            member = savedMember
        )
        require(circleComment.content.length > 2) { "댓글은 2글자 이상으로 작성해야합니다." }

        // When
        val savedComment = commentRepository.save(circleComment)

        // Then
        assertNotNull(savedComment.id)
        assertEquals("This is a test comment", savedComment.content)
        assertEquals(CommentStatus.REPORTED, savedComment.status)
        assertEquals(savedCircle.id, savedComment.circle.id)
        assertEquals(savedMember.id, savedComment.member.id)
    }

    @Test
    fun `update_CircleComment_success`() {
        // Given
        val circleComment = CircleComment(
            content = "This is test comment",
            circle = savedCircle,
            member = savedMember
        )
        val savedComment = commentRepository.save(circleComment)

        val commentUpdateRequest = CommentUpdateRequest(
            content = "test comment update",
            commentId = savedComment.id,
        )
        require(circleComment.content.length > 2) { "댓글은 2글자 이상으로 작성해야합니다." }

        // When
        val comment = commentRepository.findById(commentUpdateRequest.commentId)
            .orElseThrow { NoSuchElementException("comment id : ${commentUpdateRequest.commentId} not found") }

        require(comment.isWrittenBy(savedMember)) { "댓글 작성자만 수정 가능합니다." }
        comment.updateContent(commentUpdateRequest)

        // Then
        assertNotNull(comment.id)
        assertEquals("test comment update", comment.content)
        assertEquals(savedMember.id, comment.member.id)
        assertEquals(savedCircle.id, comment.circle.id)
    }

    @Test
    fun `update_CircleComment_failed`() {
        // Given
        val circleComment = CircleComment(
            content = "This is a test comment",
            circle = savedCircle,
            member = savedMember
        )
        val savedComment = commentRepository.save(circleComment)

        val commentUpdateRequest = CommentUpdateRequest(
            content = "test comment update",
            commentId = UUID.randomUUID(),
        )
        require(circleComment.content.length > 2) { "댓글은 2글자 이상으로 작성해야합니다." }

        // When
        val comment = commentRepository.findById(commentUpdateRequest.commentId)
            .orElseThrow { NoSuchElementException("comment id : ${commentUpdateRequest.commentId} not found") }

        require(comment.isWrittenBy(savedMember)) { "댓글 작성자만 수정 가능합니다." }
        comment.updateContent(commentUpdateRequest)

        // Then
        assertNotNull(comment.id)
        assertEquals("test comment update", comment.content)
        assertEquals(savedMember.id, comment.member.id)
        assertEquals(savedCircle.id, comment.circle.id)
    }
}
