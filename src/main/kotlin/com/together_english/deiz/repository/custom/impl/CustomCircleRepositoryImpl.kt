package com.together_english.deiz.repository.custom.impl

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.repository.custom.CustomCircleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomCircleRepositoryImpl(
        private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor
) : CustomCircleRepository {

}
