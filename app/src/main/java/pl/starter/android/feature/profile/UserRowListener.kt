package pl.starter.android.feature.profile

import pl.starter.android.service.User

interface UserRowListener {
    fun onItemRequested(user: User)
}
