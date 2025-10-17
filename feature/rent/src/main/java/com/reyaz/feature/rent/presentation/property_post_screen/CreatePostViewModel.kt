package com.reyaz.feature.rent.presentation.property_post_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.reyaz.core.auth.domain.repository.GoogleService
import com.reyaz.feature.rent.di.ApiKey
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.domain.repository.ImageRepository
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CreatePostViewModel(
    private val propertyRepository: PropertyRepository,
    private val googleSign: GoogleService,
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    private val _createPostUiState = MutableStateFlow(CreatePostUiState())
    val createPostUiState = _createPostUiState.asStateFlow()

    private val _postSuccess = MutableStateFlow<Boolean>(false)
    val postSuccess = _postSuccess.asStateFlow()

    //ui state for property
    private val _propertyState = MutableStateFlow(Property())
    val propertyState = _propertyState.asStateFlow()

    private val _isImageUploaded = MutableStateFlow<Boolean>(false)
    val isImageUploaded = _isImageUploaded.asStateFlow()

    //for showing circular progress indicator
    private val _isImageUploading = MutableStateFlow<Boolean>(false)
    val isImageUploading = _isImageUploading.asStateFlow()




    init {
        getUser()
    }

    //this viewmodel will be responsible for only posting the property

    fun getUser() {
        viewModelScope.launch {
            Firebase.auth.currentUser?.let {
                _user.value = it
            }
        }
    }

    fun checkSignInAndPost(
        context: Context,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
    ) {
        viewModelScope.launch {
            _createPostUiState.update {
                it.copy(isLoading = true)
            }
            if (_user.value == null) {
                val signInResult = doGoogleSignIn(
                    context = context,
                    launcher = launcher
                )
                if (signInResult.isSuccess) {
                    postProperty(propertyState.value)
                } else {
                    _createPostUiState.update {
                        it.copy(isLoading = false)
                    }
                    Log.d(
                        "PROPERTY_POST_VIEWMODEL",
                        "error " + signInResult.exceptionOrNull()?.message.toString()
                    )
                }
                return@launch
            }
            postProperty(propertyState.value)

        }
    }

    private fun postProperty(
        property: Property,
    ) {
        viewModelScope.launch {
            propertyRepository.postProperty(property)
                .onSuccess {
                    Log.d("PROPERTY_POST_VIEWMODEL", "postProperty: success")
                    _postSuccess.value = true
                }
                .onFailure {
                    _createPostUiState.update { it ->
                        it.copy(isLoading = false)
                    }
                    Log.d("PROPERTY_POST_VIEWMODEL", "error " + it.message.toString())
                }
        }
    }

    suspend fun doGoogleSignIn(
        context: Context,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
    ): Result<Unit> {
        val res = googleSign.googleSignIn(
            context = context,
            launcher = launcher,
        )
        if (res.isSuccess) {
            return Result.success(Unit)
        } else {
            Log.d("PROPERTY_POST_VIEWMODEL", "error " + res.exceptionOrNull()?.message.toString())
            return Result.failure(res.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }


    //all function related to ui changes
    fun onPropertyTitleChange(newTitle: String) {
        _propertyState.update {
            it.copy(propertyTitle = newTitle)
        }
    }

    fun onPropertyTypeChange(newType: String) {
        _propertyState.update {
            it.copy(propertyType = newType)
        }
    }

    fun onBhkChange(newBHK: String) {
        _propertyState.update {
            it.copy(propertyBHK = newBHK)
        }
    }

    fun onFloorNumberChange(newFloor: String) {
        _propertyState.update {
            it.copy(propertyFloorNumber = newFloor)
        }
    }

    fun onTotalFloorChange(newTotalFloor: String) {
        _propertyState.update {
            it.copy(totalFloor = newTotalFloor)
        }
    }

    fun onRentChange(newRent: String) {
        _propertyState.update {
            it.copy(propertyRent = newRent)
        }
    }

    fun onSecurityDepositChange(newSecurityDeposit: String) {
        _propertyState.update {
            it.copy(securityDeposit = newSecurityDeposit)
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _propertyState.update {
            it.copy(propertyDescription = newDescription)
        }
    }

    fun onAmenityChange(amenity: List<String>) {
        _propertyState.update {
            it.copy(amenities = amenity)
        }
    }

    fun onLocationChange(newLocation: String) {
        _propertyState.update {
            it.copy(propertyLocation = newLocation)
        }
    }

    fun hasEmptyField(): Boolean {
        val state = propertyState.value
        return state.propertyTitle.isBlank() ||
                state.propertyType.isBlank() ||
                state.propertyBHK.isBlank() ||
                state.totalFloor.isBlank() ||
                state.propertyFloorNumber.isBlank() ||
                state.propertyRent.isBlank() ||
                state.securityDeposit.isBlank() ||
                state.propertyLocation.isBlank() ||
                state.propertyDescription.isBlank()
    }
    fun convertAndUploadImage(uris: List<Uri>, context: Context) {
        _isImageUploading.value=true;
        val base64List = mutableListOf<String>()
        viewModelScope.launch{

            uris.forEach { uri ->
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var len: Int

                while (inputStream?.read(buffer).also { len = it ?: -1 } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                val imageBytes = outputStream.toByteArray()
                val base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                base64List.add(base64String)
            }
        }
        getUrl(ApiKey.KEY, base64List)
    }

    fun getUrl(key: String, image: List<String>) {
        val list = mutableListOf<String>()
        viewModelScope.launch {
            image.forEach { image ->
                imageRepository
                    .getUrls(key, image)
                    .onSuccess { uploadResponse ->
                        uploadResponse.data.url.let { url ->
                            list.add(url)

                            // Update state with the new list after each upload
                            _propertyState.update { property ->
                                property.copy(urlList = list)
                            }
                        }
                    }
                    .onFailure {
                        Log.d("PROPERTY_POST_VIEWMODEL", "error " + it.message.toString())
                    }
            }
                _isImageUploading.value=false
                _isImageUploaded.value=true//for telling whether post---or not
        }
    }
}
