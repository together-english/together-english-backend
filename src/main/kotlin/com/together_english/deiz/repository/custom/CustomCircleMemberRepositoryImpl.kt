package com.together_english.deiz.repository.custom

import com.linecorp.kotlinjdsl.querymodel.jpql.path.Paths.path
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleMember
import com.together_english.deiz.model.circle.FavoriteCircle
import com.together_english.deiz.model.circle.dto.CircleMemberDetailResponse
import com.together_english.deiz.model.circle.dto.CircleMemberPageResponse
import com.together_english.deiz.model.member.dto.MyJoinedCirclePageResponse
import com.together_english.deiz.model.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

class CustomCircleMemberRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : CustomCircleMemberRepository {

    override fun findMemberByCircle(circleId: UUID, pageable: Pageable): Page<CircleMemberPageResponse?> {
        val result =
            kotlinJdslJpqlExecutor.findPage(pageable) {
                selectNew<CircleMemberPageResponse>(
                    path(CircleMember::id).`as`(expression("circleMemberId")),
                    path(Member::nickname),
                    path(Member::profile),
                    path(CircleMember::role),
                ).from(
                    entity(CircleMember::class),
                    join(CircleMember::circle),
                    join(CircleMember::member),
                ).whereAnd(
                    path(CircleMember::circle).path(Circle::id).eq(circleId),
                    path(Circle::valid).eq(true),
                    path(Member::valid).eq(true)
                )
            }
        return result
    }

    override fun findMemberDetailsByCircle(circleMemberId: UUID): CircleMemberDetailResponse {
        val result =
            kotlinJdslJpqlExecutor.findAll {
                selectNew<CircleMemberDetailResponse>(
                    path(CircleMember::circle).path(Circle::id).`as`(expression("circleId")),
                    path(Member::nickname),
                    path(Member::email),
                    path(Member::profile),
                    path(Member::gender),
                    path(Member::age),
                    path(CircleMember::role),
                ).from(
                    entity(CircleMember::class),
                    join(CircleMember::circle),
                    join(CircleMember::member),
                ).whereAnd(
                    path(CircleMember::id).eq(circleMemberId),
                    path(Circle::valid).eq(true),
                    path(Member::valid).eq(true)
                )
            }.single()
        return result!!
    }

    override fun findCircleByMember(member: Member, pageable: Pageable): Page<MyJoinedCirclePageResponse?> {
        val leaderPath = path(Circle::leader)
        val favoriteMemberPath = path(FavoriteCircle::member)
        val favoriteCirclePath = path(FavoriteCircle::circle)

        val result =
            kotlinJdslJpqlExecutor.findPage(pageable) {
                selectNew<MyJoinedCirclePageResponse>(
                    path(Circle::id),
                    path(Circle::thumbnailUrl),
                    path(Circle::title),
                    path(Circle::introduction),
                    leaderPath.path(Member::profile).`as`(expression("leaderProfile")),
                    leaderPath.path(Member::nickname).`as`(expression("leaderName")),
                    path(Circle::englishLevel),
                    path(Circle::city),
                    path(Circle::capacity),
                    path(Circle::totalView),
                    count(entity(FavoriteCircle::class, "totalFavorite")).`as`(expression("totalLike")),
                    caseWhen(
                        favoriteMemberPath.path(Member::id).isNotNull()
                            .and(favoriteMemberPath.path(Member::id).eq(member.id))
                    )
                        .then(true)
                        .`else`(false)
                        .`as`(expression("likedByMe"))
                ).from(
                    entity(CircleMember::class),
                    join(CircleMember::circle),
                    join(CircleMember::member),
                    leftJoin(Circle::favoriteCircle)
                        .on(
                            favoriteCirclePath.path(Circle::id).eq(path(Circle::id))
                                .and(favoriteMemberPath.path(Member::id).eq(path(Member::id)))
                        ),
                    leftJoin(Circle::favoriteCircle)
                        .on(
                            path(Circle::id).eq(path(Circle::id))
                        ).`as`(entity(FavoriteCircle::class, "totalFavorite"))
                ).whereAnd(
                    path(Member::id).eq(member.id),
                    path(Circle::valid).eq(true),
                    path(Member::valid).eq(true)
                ).groupBy(
                        path(Circle::id),
                        path(Circle::thumbnailUrl),
                        path(Circle::title),
                        path(Circle::introduction),
                        leaderPath.path(Member::profile),
                        leaderPath.path(Member::nickname),
                        path(Circle::englishLevel),
                        path(Circle::city),
                        path(Circle::capacity),
                        path(Circle::totalView),
                        path(FavoriteCircle::member).path(Member::id),
                    ).orderBy(
                    path(Circle::updatedAt).desc()
                )
            }
        return result
    }
    }