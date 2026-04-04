package com.nabha.telemedicine.core.di

import android.content.Context
import androidx.room.Room
import com.nabha.telemedicine.data.local.AppDatabase
import com.nabha.telemedicine.data.local.AppointmentDao
import com.nabha.telemedicine.data.local.DoctorDao
import com.nabha.telemedicine.data.local.HealthRecordDao
import com.nabha.telemedicine.data.local.PharmacyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nabha_telemedicine.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAppointmentDao(db: AppDatabase): AppointmentDao = db.appointmentDao()

    @Provides
    fun provideHealthRecordDao(db: AppDatabase): HealthRecordDao = db.healthRecordDao()

    @Provides
    fun provideDoctorDao(db: AppDatabase): DoctorDao = db.doctorDao()

    @Provides
    fun providePharmacyDao(db: AppDatabase): PharmacyDao = db.pharmacyDao()
}
