package nk.sixstrings.util.extensions

import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Single

fun <T> ApolloMutationCall<T>.toSingle(): Single<Response<T>> {
    return Rx2Apollo.from(this).firstOrError()
}

fun <T> ApolloQueryCall<T>.toSingle(): Single<Response<T>> {
    return Rx2Apollo.from(this).lastOrError()
}

fun <T> Single<Response<T>>.unwrapResponseAsError(): Single<T> {
    return map { it.data() ?: throw GraphQlErrorThrowable(it.errors()) }
}

class GraphQlErrorThrowable(errors: List<Error>)
    : RuntimeException("GraphQL failed with errors: $errors")