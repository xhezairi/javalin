/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.core.security

import io.javalin.http.Context
import io.javalin.http.Handler

object SecurityUtil {

    @JvmStatic
    fun roles(vararg roles: Role) = setOf(*roles)

    @JvmStatic
    fun noopAccessManager(handler: Handler, ctx: Context, permittedRoles: Set<Role>) {
        if (permittedRoles.isNotEmpty()) {
            throw IllegalStateException("No access manager configured. Add an access manager using 'Javalin.create(c -> c.accessManager(...))'.")
        }
        handler.handle(ctx)
    }

    @JvmStatic
    fun sslRedirect(ctx: Context) {
        if (ctx.scheme() == "http") {
            ctx.redirect(ctx.fullUrl().replace("http", "https"), 301)
        }
    }

}

internal enum class CoreRoles : Role { NO_WRAP } // used to avoid wrapping CORS options

/**
 * Auth credentials for basic HTTP authorization.
 * Contains the Base64 decoded [username] and [password] from the Authorization header.
 * @see Context.basicAuthCredentials
 */
data class BasicAuthCredentials(val username: String, val password: String)
