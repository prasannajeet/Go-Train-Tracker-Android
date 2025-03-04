package com.p2.apps.mygotraintracker.domain.usecase

import com.p2.apps.mygotraintracker.contract.IUseCase
import com.p2.apps.mygotraintracker.data.api.ApiPath
import com.p2.apps.mygotraintracker.data.api.HttpWebServiceHandler
import com.p2.apps.mygotraintracker.data.api.dto.AllLinesResponse
import com.p2.apps.mygotraintracker.domain.model.GOTrainLine
import io.ktor.http.HttpMethod

class GetAllGOTrainLinesUseCase(override val apiClient: HttpWebServiceHandler) : IUseCase<String, List<GOTrainLine>> {
    override suspend fun execute(input: String): Result<List<GOTrainLine>> =
        apiClient.request<AllLinesResponse>(HttpMethod.Get, ApiPath.AllLines(input).path)
            .fold(
                ifLeft = { response ->
                    Result.success(
                        response.allLines.lines
                            .filter { it.isTrain && !it.isBus }
                            .flatMap { line -> 
                                line.variants.map { variant -> 
                                    GOTrainLine(variant.code, line.name)
                                }
                            }
                    )
                },
                ifRight = { error -> Result.failure(Exception(error.message)) }
            )
}
