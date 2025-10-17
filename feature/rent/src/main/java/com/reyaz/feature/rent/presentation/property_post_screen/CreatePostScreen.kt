package com.reyaz.feature.rent.presentation.property_post_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.rent.presentation.property_post_screen.components.DescriptionField
import com.reyaz.feature.rent.presentation.property_post_screen.components.DropDownField
import com.reyaz.feature.rent.presentation.property_post_screen.components.MultiSelectField
import com.reyaz.feature.rent.presentation.property_post_screen.components.TextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onPostClick: (context: Context, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) -> Unit,
    viewModel: CreatePostViewModel,
    navigateToPostScreen: () -> Unit,
    createPostUiState: CreatePostUiState
) {

    val propertyState by viewModel.propertyState.collectAsStateWithLifecycle()
    val isImageUploaded by viewModel.isImageUploaded.collectAsStateWithLifecycle()
    val isImageUploading by viewModel.isImageUploading.collectAsStateWithLifecycle()
    val isPostDone = viewModel.postSuccess.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            Log.d("CreatePostScreen", "CreatePostScreen:picked ")
            viewModel.convertAndUploadImage(uris, context)
        }
    }

    LaunchedEffect(isPostDone) {
        if (isPostDone) {
            navigateToPostScreen()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
                .border(shape = RoundedCornerShape(10.dp), width = 1.dp, color = Color.LightGray)
        ) {
            Text(
                text = "Property Details",
                modifier = Modifier
                    .padding(start = 12.dp, top = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                "Property Title",
                "eg.spacious 2BHK flat in Jamia",
                propertyState.propertyTitle
            ) { it ->
                viewModel.onPropertyTitleChange(it)
            }

            DropDownField(
                title = "Property Type",
                options = listOf("Flat", "PG", "Room"),
                onSelect = { it ->
                    viewModel.onPropertyTypeChange(it)
                },
                selectedOption = propertyState.propertyType
            )

            DropDownField(
                title = "Property BHK",
                options = listOf("1BHK", "2BHK", "3BHK", "4BHK"),
                onSelect = { it ->
                    viewModel.onBhkChange(it)
                },
                selectedOption = propertyState.propertyBHK
            )

            TextField("Total Floor", "eg. 2", propertyState.totalFloor) { it ->
                viewModel.onTotalFloorChange(it)
            }

            TextField("Floor Number", "eg. 1", propertyState.propertyFloorNumber) {
                viewModel.onFloorNumberChange(it)
            }

            TextField("Monthly Rent", "2500", propertyState.propertyRent) {
                viewModel.onRentChange(it)
            }

            TextField("Security Deposit", "5000", propertyState.securityDeposit) {
                viewModel.onSecurityDepositChange(it)
            }
            TextField(
                title = "Location",
                example = "Batla house gali no 6",
                propertyValue = propertyState.propertyLocation
            ) { it ->
                viewModel.onLocationChange(it)
            }

            MultiSelectField(
                title = "Amenities",
                options = listOf(
                    "balcony",
                    "AC",
                    "Parking",
                    "lift",
                    "Wi-Fi",
                    "Kitchen",
                    "Power backup"
                ),
                selectedOptions = propertyState.amenities,
                onSelectionChanged = {
                    viewModel.onAmenityChange(it)
                },
                modifier = Modifier
            )

            DescriptionField(
                title = "Description",
                example = "Describe your property,rules,nearby facilities etc",
                description = propertyState.propertyDescription,
                onValueChange = {
                    viewModel.onDescriptionChange(it)
                }
            )

            Button(
                onClick = {
                    if (isImageUploaded) {
                        //if true means upload the image
                        if (viewModel.hasEmptyField()) {
                            Toast.makeText(
                                context,
                                "Please fill all the fields",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            onPostClick(context, launcher)
                        }
                    } else {
                        //pick image first
                        imageLauncher.launch("image/*")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp)
            ) {
                Text(
                    text = if (isImageUploaded) {
                        "post"
                    } else {
                        "upload images"
                    },
                )
            }
        }
//when i am uploading image and uploading entire data object then it will show circular progress indiacator
        if (createPostUiState.isLoading || isImageUploading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f))
                    .clickable(enabled = false) {}
            ) { CircularProgressIndicator() }
        }
    }
}
