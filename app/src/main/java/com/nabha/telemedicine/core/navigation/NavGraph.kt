package com.nabha.telemedicine.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import com.nabha.telemedicine.core.design.components.BottomNavItem
import com.nabha.telemedicine.core.design.components.NabhaBottomNavigationBar
import com.nabha.telemedicine.core.design.theme.SurfaceDark
import com.nabha.telemedicine.feature.appointments.AppointmentsScreen
import com.nabha.telemedicine.feature.auth.*
import com.nabha.telemedicine.feature.consultation.*
import com.nabha.telemedicine.feature.emergency.EmergencyScreen
import com.nabha.telemedicine.feature.home.HomeScreen
import com.nabha.telemedicine.feature.onboarding.*
import com.nabha.telemedicine.feature.pharmacy.*
import com.nabha.telemedicine.feature.records.*
import com.nabha.telemedicine.feature.settings.SettingsScreen
import com.nabha.telemedicine.feature.splash.SplashScreen
import com.nabha.telemedicine.feature.symptom.*

/** Routes where the global bottom navigation bar should be visible. */
private val bottomNavRoutes = setOf(
    Screen.Home.route,
    Screen.Appointments.route,
    Screen.Records.route,
    Screen.Pharmacy.route,
    Screen.Settings.route
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route,         Icons.Rounded.Home,          Icons.Rounded.Home,          "Home"),
    BottomNavItem(Screen.Appointments.route, Icons.Rounded.CalendarMonth, Icons.Rounded.CalendarMonth, "Appointments"),
    BottomNavItem(Screen.Records.route,      Icons.Rounded.FolderOpen,    Icons.Rounded.FolderOpen,    "Records"),
    BottomNavItem(Screen.Pharmacy.route,     Icons.Rounded.LocalPharmacy, Icons.Rounded.LocalPharmacy, "Pharmacy"),
    BottomNavItem(Screen.Settings.route,     Icons.Rounded.Settings,      Icons.Rounded.Settings,      "Settings")
)

@Composable
fun NabhaNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show bottom bar only on the 5 main-tab screens
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        containerColor = SurfaceDark,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter   = slideInVertically { it } + fadeIn(tween(200)),
                exit    = slideOutVertically { it } + fadeOut(tween(150))
            ) {
                NabhaBottomNavigationBar(
                    items          = bottomNavItems,
                    selectedRoute  = currentRoute ?: Screen.Home.route,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Splash.route,
            modifier         = Modifier.padding(
                // Only apply bottom padding on tab screens so sub-screens aren't pushed up
                bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else PaddingValues().calculateBottomPadding()
            ),
            enterTransition  = { slideInHorizontally(tween(300)) { it / 2 } + fadeIn(tween(300)) },
            exitTransition   = { slideOutHorizontally(tween(300)) { -it / 2 } + fadeOut(tween(200)) },
            popEnterTransition  = { slideInHorizontally(tween(300)) { -it / 2 } + fadeIn(tween(300)) },
            popExitTransition   = { slideOutHorizontally(tween(300)) { it / 2 } + fadeOut(tween(200)) }
        ) {
            // ── Splash ────────────────────────────────────────────────────────
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToLanguage = { navController.navigate(Screen.LanguageSelect.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }},
                    onNavigateToHome = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }}
                )
            }

            // ── Onboarding ─────────────────────────────────────────────────────
            composable(Screen.LanguageSelect.route) {
                LanguageSelectionScreen(
                    onLanguageSelected = { navController.navigate(Screen.Onboarding.route) }
                )
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onGetStarted = { navController.navigate(Screen.Login.route) }
                )
            }

            // ── Auth (FREE: Firebase Email/Password) ──────────────────────────
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.ProfileSetup.route) {
                ProfileSetupScreen(
                    onProfileSaved = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }}
                )
            }

            // ── Main App ───────────────────────────────────────────────────────
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            // ── Consultation ───────────────────────────────────────────────────
            composable(Screen.FindDoctor.route) {
                FindDoctorScreen(navController = navController)
            }
            composable(
                route     = Screen.DoctorProfile.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("doctorId") ?: ""
                DoctorProfileScreen(doctorId = id, navController = navController)
            }
            composable(
                route     = Screen.BookAppointment.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("doctorId") ?: ""
                BookAppointmentScreen(doctorId = id, navController = navController)
            }

            // Video call — FREE via Jitsi Meet (no Agora key required)
            composable(
                route     = Screen.VideoCall.route,
                arguments = listOf(
                    navArgument("channelId") { type = NavType.StringType },
                    navArgument("token")     { type = NavType.StringType }
                )
            ) { backStack ->
                val channelId = backStack.arguments?.getString("channelId") ?: ""
                val token     = backStack.arguments?.getString("token") ?: ""
                VideoCallScreen(channelId = channelId, token = token, navController = navController)
            }

            // ── Records & Health ───────────────────────────────────────────────
            composable(Screen.Records.route) {
                HealthRecordsScreen(navController = navController)
            }
            composable(
                route     = Screen.RecordDetail.route,
                arguments = listOf(navArgument("recordId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("recordId") ?: ""
                RecordDetailScreen(recordId = id, navController = navController)
            }

            // ── Pharmacy — FREE Maps (OpenStreetMap) ───────────────────────────
            composable(Screen.Pharmacy.route) {
                PharmacyScreen(navController = navController)
            }
            composable(Screen.PharmacyMap.route) {
                PharmacyMapScreen(navController = navController)
            }
            composable(Screen.MedicineSearch.route) {
                MedicineSearchScreen(navController = navController)
            }

            // ── AI Symptom Checker (100% offline, no cost) ────────────────────
            composable(Screen.SymptomChecker.route) {
                SymptomCheckerScreen(navController = navController)
            }
            composable(Screen.DiagnosisResult.route) {
                DiagnosisResultScreen(navController = navController)
            }

            // ── Appointments ───────────────────────────────────────────────────
            composable(Screen.Appointments.route) {
                AppointmentsScreen(navController = navController)
            }

            // ── Emergency SOS ──────────────────────────────────────────────────
            composable(Screen.Emergency.route) {
                EmergencyScreen(navController = navController)
            }

            // ── Settings ───────────────────────────────────────────────────────
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }

            composable(Screen.Notifications.route) {
                SettingsScreen(navController = navController)
            }
        }
    }
}
