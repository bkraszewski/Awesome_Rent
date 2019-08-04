package pl.starter.android.service

data class UsersResponse(val users: List<User>)
data class UserRequest(val email:String, val role: String)
