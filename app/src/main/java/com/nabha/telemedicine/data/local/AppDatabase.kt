package com.nabha.telemedicine.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AppointmentEntity::class,
        HealthRecordEntity::class,
        DoctorEntity::class,
        PharmacyEntity::class
    ],
    version  = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun doctorDao(): DoctorDao
    abstract fun pharmacyDao(): PharmacyDao
}
