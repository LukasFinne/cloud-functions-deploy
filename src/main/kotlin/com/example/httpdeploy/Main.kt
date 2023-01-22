package com.example.httpdeploy

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import java.util.logging.Logger

@Suppress("unused") // Entrypoint

class App : HttpFunction {
    private val logger: Logger = Logger.getLogger(App::class.java.name)

    override fun service(request: HttpRequest, response: HttpResponse) {
        response.writer.write("Hello World!")
    }
}