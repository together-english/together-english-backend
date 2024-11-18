package com.together_english.deiz.repository.custom

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.dto.CirclePageResponse
import com.together_english.deiz.model.circle.dto.CircleSearchRequest
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.custom.CustomCircleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import jakarta.persistence.criteria.JoinType
import java.util.*

@Repository
class CustomCircleRepositoryImpl(
        private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor
) : CustomCircleRepository {

    override fun findCirclesByPagination(pageable: Pageable, request: CircleSearchRequest?)
    : Page<CirclePageResponse?> {
        return kotlinJdslJpqlExecutor.findPage(pageable) {
            selectNew<CirclePageResponse>(
                    path(Circle::id),
                    path(Circle::thumbnailUrl),
                    path(Circle::title),
                    path(Circle::introduction),
                    path(Member::profile).`as`(expression("leaderProfile")),
                    path(Member::nickname).`as`(expression("leaderName")),
                    path(Circle::englishLevel),
                    path(Circle::city),
                    path(Circle::capacity),
                    path(Circle::totalView),
                    path(Circle::totalLike),
            ).from(
                    entity(Circle::class),
                    join(Circle::leader)
            ).whereAnd(
                    request?.title?.let { path(Circle::title).like("%$it%") },
                    request?.level?.let { path(Circle::englishLevel).eq(it) },
                    request?.city?.let { path(Circle::city).eq(it) },
                    path(Circle::valid).eq(true)
            )

        }
    }
}
