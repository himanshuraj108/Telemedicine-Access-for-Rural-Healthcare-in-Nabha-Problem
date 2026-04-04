package com.nabha.telemedicine.core.navigation

sealed class Screen(val route: String) {
    object Splash         : Screen("splash")
    object LanguageSelect : Screen("language_select")
    object Onboarding     : Screen("onboarding")

    // Free Auth: email/password via Firebase (no SMS/no billing)
    object Login          : Screen("login")
    object ProfileSetup   : Screen("profile_setup")

    // Main
    object Home           : Screen("home")
    object Notifications  : Screen("notifications")

    // Consultation
    object FindDoctor     : Screen("find_doctor")
    object DoctorProfile  : Screen("doctor_profile/{doctorId}") {
        fun withId(id: String) = "doctor_profile/$id"
    }
    object BookAppointment : Screen("book_appointment/{doctorId}") {
        fun withId(id: String) = "book_appointment/$id"
    }
    // Video call via Jitsi Meet (FREE — no Agora key needed)
    object VideoCall      : Screen("video_call/{channelId}/{token}") {
        fun withArgs(channelId: String, token: String = "jitsi_free") = "video_call/$channelId/$token"
    }

    // Records
    object Records        : Screen("health_records")
    object RecordDetail   : Screen("record_detail/{recordId}") {
        fun withId(id: String) = "record_detail/$id"
    }

    // Pharmacy — map powered by OpenStreetMap (FREE, no Google Maps API key)
    object Pharmacy       : Screen("pharmacy")
    object PharmacyMap    : Screen("pharmacy_map")
    object MedicineSearch : Screen("medicine_search")

    // AI Symptom (100% on-device, no cost)
    object SymptomChecker : Screen("symptom_checker")
    object DiagnosisResult: Screen("diagnosis_result")

    // Appointments
    object Appointments   : Screen("appointments")

    // Emergency
    object Emergency      : Screen("emergency")

    // Settings
    object Settings       : Screen("settings")
}
