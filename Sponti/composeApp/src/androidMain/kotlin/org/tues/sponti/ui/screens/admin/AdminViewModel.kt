package org.tues.sponti.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tues.sponti.core.RepositoryProvider
import org.tues.sponti.data.admin.AdminRepository
import org.tues.sponti.data.chal.ChalRepository
import org.tues.sponti.data.network.ErrorResponse
import org.tues.sponti.data.network.RetrofitClient
import org.tues.sponti.data.user.Role
import org.tues.sponti.ui.screens.common.FieldError

class AdminViewModel(
    private val adminRepository: AdminRepository = AdminRepository(),
    private val chalRepository: ChalRepository = RepositoryProvider.chalRepository
) :
    ViewModel() {
    private val _state = MutableStateFlow(AdminState())
    val state = _state.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = adminRepository.getAllUsers(page = _state.value.pageUsers)

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.items != null && body.totalPages != null) {
                        _state.update {
                            it.copy(
                                users = body.items,
                                totalPagesUsers = maxOf(body.totalPages, 1)
                            )
                        }
                    }
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun changeUserRole(id: String, role: Role) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = adminRepository.updateUserRole(userId = id, role = role)

                if (resp.isSuccessful) {
                    loadUsers()
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = adminRepository.deleteUser(userId = id)

                if (resp.isSuccessful) {
                    loadUsers()
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadChallenges(approved: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = adminRepository.getChallenges(
                    approved = approved,
                    page = if (approved) _state.value.pageApprovedChallenges else _state.value.pagePendingChallenges
                )

                if (resp.isSuccessful) {
                    val body = resp.body()

                    if (body?.items != null && body.totalPages != null) {
                        _state.update {
                            it.copy(
                                challenges = body.items,
                                totalPagesApprovedChallenges = if (approved) maxOf(
                                    body.totalPages,
                                    1
                                ) else it.totalPagesApprovedChallenges,
                                totalPagesPendingChallenges = if (approved) it.totalPagesPendingChallenges else maxOf(
                                    body.totalPages,
                                    1
                                )
                            )
                        }
                    }
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun approveChallenge(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = adminRepository.approveChallenge(challengeId = id)

                if (resp.isSuccessful) {
                    chalRepository.invalidateChallenges()
                    loadChallenges(false)
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteChallenge(id: String, approved: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val resp = adminRepository.deleteChallenge(challengeId = id)

                if (resp.isSuccessful) {
                    chalRepository.invalidateChallenges()
                    loadChallenges(approved = approved)
                } else {
                    val errorJson = resp.errorBody()?.string()
                    val adapter = RetrofitClient.getMoshi().adapter(ErrorResponse::class.java)
                    val parsed = errorJson?.let { adapter.fromJson(it) }
                    _state.update { it.copy(globalError = parsed?.toFieldError()) }
                }
            } catch (_: Exception) {
                _state.update { it.copy(globalError = FieldError.Network) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSelectedTabChange(value: AdminTab) {
        _state.update { it.copy(selectedTab = value) }
    }

    fun onPageUsersChange(value: Int) {
        _state.update { it.copy(pageUsers = value.coerceIn(1, it.totalPagesUsers)) }
    }

    fun onPageApprovedChallengesChange(value: Int) {
        _state.update {
            it.copy(
                pageApprovedChallenges = value.coerceIn(
                    1,
                    it.totalPagesApprovedChallenges
                )
            )
        }
    }

    fun onPagePendingChallengesChange(value: Int) {
        _state.update {
            it.copy(
                pagePendingChallenges = value.coerceIn(
                    1,
                    it.totalPagesPendingChallenges
                )
            )
        }
    }

    fun clearGlobalError() {
        _state.update { it.copy(globalError = null) }
    }
}