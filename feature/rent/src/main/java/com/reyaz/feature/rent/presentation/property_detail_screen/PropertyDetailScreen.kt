package com.reyaz.feature.rent.presentation.property_detail_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.reyaz.feature.rent.domain.model.Property



@Composable
fun PropertyDetailScreen(property: Property) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (property.urlList.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(property.urlList.size) { index ->
                    Image(
                        painter = rememberAsyncImagePainter(property.urlList[index]),
                        contentDescription = null,
                        modifier = Modifier
                            .fillParentMaxWidth(0.9f)
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("No images available")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = property.propertyTitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = property.propertyLocation,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Rent: ₹${property.propertyRent} / month",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Security Deposit: ₹${property.securityDeposit}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))


        Text(
            text = "${property.propertyType} • ${property.propertyBHK}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Floor: ${property.propertyFloorNumber} / ${property.totalFloor}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))


        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = property.propertyDescription,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Amenities ---
        Text(
            text = "Amenities",
            style = MaterialTheme.typography.titleMedium
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            property.amenities.forEach { amenity ->
                Text("• $amenity", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
