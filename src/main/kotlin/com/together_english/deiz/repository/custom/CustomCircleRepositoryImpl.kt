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
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
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
                count(entity(FavoriteCircle::class, "totalFavorite").path(FavoriteCircle::circle)).`as`(expression("totalLike")),
                caseWhen(
                    path(FavoriteCircle::member).path(Member::id).isNotNull()
                        .and(path(FavoriteCircle::member).path(Member::id).eq(request?.memberId))
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
                            .and(path(FavoriteCircle::member).path(Member::id).eq(request?.memberId))
                    ),
                leftJoin(Circle::favoriteCircle)
                    .on(
                        path(Circle::id).eq(path(Circle::id))
                    ).`as`(entity(FavoriteCircle::class, "totalFavorite"))
            )
                .whereAnd(
                    request?.title?.let { path(Circle::title).like("%$it%") },
                    request?.level?.let { path(Circle::englishLevel).eq(it) },
                    request?.city?.let { path(Circle::city).eq(it) },
                    request?.let {
                        if(it.likeByMeOnly == true && it.memberId != null) {
                            path(FavoriteCircle::member).path(Member::id).eq(it.memberId)
                        }else{
                            null
                        }
                    },
                    path (Circle::valid).eq(true)
                )
                .groupBy(
                    path(Circle::id),
                    path(Circle::thumbnailUrl),
                    path(Circle::title),
                    path(Circle::introduction),
                    path(Member::profile),
                    path(Member::nickname),
                    path(Circle::englishLevel),
                    path(Circle::city),
                    path(Circle::capacity),
                    path(Circle::totalView),
                    path(FavoriteCircle::member).path(Member::id),
                )
        }
    }
}
