package com.miranda.gymlibrary.core.util

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(): String = when (this) {
    is UnknownHostException,
    is ConnectException ->
        "Sem conexão com a internet. Verifique sua rede e tente novamente."

    is SocketTimeoutException ->
        "A requisição demorou muito. Verifique sua conexão e tente novamente."

    is HttpException -> when (code()) {
        401, 403 -> "Erro de autenticação. Verifique sua API key."
        404 -> "Conteúdo não encontrado."
        in 500..599 -> "Serviço indisponível no momento. Tente novamente mais tarde."
        else -> "Não foi possível carregar os dados. Tente novamente."
    }

    else -> "Ocorreu um erro inesperado. Tente novamente."
}
