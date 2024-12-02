package com.together_english.deiz.repository.custom

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.FavoriteCircle
import com.together_english.deiz.model.circle.dto.CirclePageResponse
import com.together_english.deiz.model.circle.dto.CircleSearchRequest
import com.together_english.deiz.model.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomCircleRepositoryImpl(
        private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor
) : CustomCircleRepository {

    override fun findCirclesByPagination(member: Member?, pageable: Pageable, request: CircleSearchRequest?)
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
                    caseWhen(
                            path(FavoriteCircle::member).path(Member::id).isNotNull()
                                    .and(path(FavoriteCircle::member).path(Member::id).eq(request?.member?.id))
                    )
                    .then(true)
                    .`else`(false)
                    .`as`(expression("likedByMe"))
            ).from(
                    entity(Circle::class),
                    join(Circle::leader),
                    leftJoin(Circle::favoriteCircle)
                            .on(
                                path(FavoriteCircle::circle).path(Circle::id).eq(path(Circle::id))
                                    .and(path(FavoriteCircle::member).path(Member::id).eq(path(Member::id)))
                            )
            )
            .whereAnd(
                    request?.title?.let { path(Circle::title).like("%$it%") },
                    request?.level?.let { path(Circle::englishLevel).eq(it) },
                    request?.city?.let { path(Circle::city).eq(it) },
                    path(Circle::valid).eq(true)
            )

        }
    }
}
