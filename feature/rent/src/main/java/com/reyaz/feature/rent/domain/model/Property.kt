package com.reyaz.feature.rent.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//currently i am creating this for testing purpose once it is done i will add other valures
//related to property like flat,location,bhk...\
@Parcelize
data class Property(
  val id: String = "1",
  val urlList: List<String> = emptyList(),
  val propertyTitle: String = "Spacious 2BHK Apartment",
  val propertyType: String = "Apartment",
  val propertyBHK: String = "2 BHK",
  val propertyLocation: String = "Koramangala, Bangalore",
  val propertyRent: String = "25000",
  val securityDeposit: String = "50000",
  val propertyFloorNumber: String = "3",
  val totalFloor: String = "5",
  val propertyDescription: String = "A beautiful and spacious apartment located in the heart of the city. Comes with all modern amenities.",
  val amenities: List<String> = listOf("Lift", "Security", "Parking", "Power Backup")
) : Parcelable