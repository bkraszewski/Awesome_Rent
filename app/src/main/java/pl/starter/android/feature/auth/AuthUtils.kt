package pl.starter.android.feature.auth

import java.util.regex.Pattern

val EMAIL_REGEX = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
)
 fun isEmailInvalid(email: String?): Boolean {
    return !EMAIL_REGEX.matcher(email).matches()
}

 fun isStringBlank(email: String?): Boolean {
    return email?.isBlank() ?: true
}
