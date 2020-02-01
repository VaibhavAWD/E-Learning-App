package com.vaibhavdhunde.app.elearning.api

import java.io.IOException

class NetworkException(message: String): IOException(message)
class ApiException(message: String): IOException(message)