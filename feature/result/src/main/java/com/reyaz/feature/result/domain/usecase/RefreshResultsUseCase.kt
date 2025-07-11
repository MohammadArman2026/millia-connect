package com.reyaz.feature.result.domain.usecase

import com.reyaz.feature.result.domain.repository.ResultRepository

class RefreshResultsUseCase(private val repo: ResultRepository) {
    suspend operator fun invoke() = repo.refreshLocalResults(shouldNotify = false)
}
