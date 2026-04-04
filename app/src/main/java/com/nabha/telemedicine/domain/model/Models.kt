package com.nabha.telemedicine.domain.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val village: String = "",
    val district: String = "Nabha",
    val state: String = "Punjab",
    val age: Int = 0,
    val gender: Gender = Gender.PREFER_NOT_TO_SAY,
    val bloodGroup: String = "",
    val aadhaarLast4: String = "",
    val profilePicUrl: String = "",
    val preferredLanguage: Language = Language.PUNJABI,
    val isProfileComplete: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

enum class Gender { MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY }
enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    PUNJABI("pa", "Punjabi", "ਪੰਜਾਬੀ")
}

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val qualification: String = "",
    val experienceYears: Int = 0,
    val hospital: String = "Nabha Civil Hospital",
    val profilePicUrl: String = "",
    val languages: List<String> = listOf("Punjabi", "Hindi"),
    val rating: Float = 0f,
    val totalReviews: Int = 0,
    val consultationFee: Int = 0,
    val isAvailable: Boolean = false,
    val availableSlots: List<TimeSlot> = emptyList(),
    val nextAvailable: Timestamp? = null,
    val specialtyColor: String = "#0284C7"
)

data class TimeSlot(
    val id: String = "",
    val date: Timestamp? = null,
    val startTime: String = "",
    val endTime: String = "",
    val isBooked: Boolean = false
)

data class Appointment(
    val id: String = "",
    val patientId: String = "",
    val patientName: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val doctorSpecialization: String = "",
    val doctorPicUrl: String = "",
    val scheduledAt: Timestamp? = null,
    val type: ConsultationType = ConsultationType.VIDEO,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val symptoms: String = "",
    val notes: String = "",
    val prescription: String = "",
    val videoChannelId: String = "",
    val videoToken: String = "",
    val createdAt: Timestamp? = null
)

enum class ConsultationType { VIDEO, PHONE, IN_PERSON }
enum class AppointmentStatus { PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED }

data class HealthRecord(
    val id: String = "",
    val patientId: String = "",
    val title: String = "",
    val type: RecordType = RecordType.PRESCRIPTION,
    val description: String = "",
    val fileUrl: String = "",
    val thumbnailUrl: String = "",
    val doctorName: String = "",
    val hospital: String = "",
    val date: Timestamp? = null,
    val tags: List<String> = emptyList(),
    val isLocalOnly: Boolean = false,
    val localFilePath: String = "",
    val createdAt: Timestamp? = null
)

enum class RecordType { PRESCRIPTION, LAB_RESULT, SCAN_REPORT, VACCINATION, DISCHARGE_SUMMARY, OTHER }

data class Pharmacy(
    val id: String = "",
    val name: String = "",
    val ownerName: String = "",
    val address: String = "",
    val village: String = "",
    val phone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isOpen: Boolean = true,
    val openTime: String = "09:00",
    val closeTime: String = "21:00",
    val medicines: List<Medicine> = emptyList(),
    val lastUpdated: Timestamp? = null,
    val distanceKm: Double = 0.0
)

data class Medicine(
    val id: String = "",
    val name: String = "",
    val genericName: String = "",
    val manufacturer: String = "",
    val type: MedicineType = MedicineType.TABLET,
    val dosage: String = "",
    val price: Double = 0.0,
    val isAvailable: Boolean = false,
    val stockCount: Int = 0,
    val pharmacyId: String = "",
    val pharmacyName: String = "",
    val requiresPrescription: Boolean = false,
    val lastUpdated: Timestamp? = null
)

enum class MedicineType { TABLET, CAPSULE, SYRUP, INJECTION, CREAM, DROPS, INHALER, OTHER }

data class SymptomCheckResult(
    val possibleConditions: List<DiagnosisCondition> = emptyList(),
    val urgencyLevel: UrgencyLevel = UrgencyLevel.LOW,
    val recommendedAction: String = "",
    val recommendedFacility: String = "",
    val disclaimer: String = ""
)

data class DiagnosisCondition(
    val name: String = "",
    val confidence: Float = 0f,
    val description: String = "",
    val suggestedSpecialty: String = ""
)

enum class UrgencyLevel { LOW, MEDIUM, HIGH, EMERGENCY }

data class Notification(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val isRead: Boolean = false,
    val relatedId: String = "",
    val createdAt: Timestamp? = null
)

enum class NotificationType { APPOINTMENT, CONSULTATION, PHARMACY, GENERAL }
