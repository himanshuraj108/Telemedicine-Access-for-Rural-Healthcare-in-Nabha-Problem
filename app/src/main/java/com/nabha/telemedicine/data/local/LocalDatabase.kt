package com.nabha.telemedicine.data.local

import androidx.room.*
import com.nabha.telemedicine.domain.model.*
import kotlinx.coroutines.flow.Flow

// ── Entities ───────────────────────────────────────────────────────────────

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val doctorId: String,
    val doctorName: String,
    val doctorSpecialization: String,
    val doctorPicUrl: String,
    val scheduledAtMillis: Long,
    val type: String,
    val status: String,
    val symptoms: String,
    val notes: String,
    val prescription: String,
    val videoChannelId: String,
    val videoToken: String,
    val createdAtMillis: Long
)

@Entity(tableName = "health_records")
data class HealthRecordEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val title: String,
    val type: String,
    val description: String,
    val fileUrl: String,
    val thumbnailUrl: String,
    val doctorName: String,
    val hospital: String,
    val dateMillis: Long,
    val tagsJson: String,
    val isLocalOnly: Boolean,
    val localFilePath: String,
    val createdAtMillis: Long
)

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialization: String,
    val qualification: String,
    val experienceYears: Int,
    val hospital: String,
    val profilePicUrl: String,
    val languagesJson: String,
    val rating: Float,
    val totalReviews: Int,
    val consultationFee: Int,
    val isAvailable: Boolean,
    val specialtyColor: String,
    val cachedAtMillis: Long
)

@Entity(tableName = "pharmacies")
data class PharmacyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val ownerName: String,
    val address: String,
    val village: String,
    val phone: String,
    val latitude: Double,
    val longitude: Double,
    val isOpen: Boolean,
    val openTime: String,
    val closeTime: String,
    val lastUpdatedMillis: Long
)

// ── DAOs ───────────────────────────────────────────────────────────────────

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments WHERE patientId = :patientId ORDER BY scheduledAtMillis DESC")
    fun getAppointmentsForPatient(patientId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: String): AppointmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(appointments: List<AppointmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: AppointmentEntity)

    @Delete
    suspend fun delete(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE patientId = :patientId")
    suspend fun deleteAllForPatient(patientId: String)
}

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE patientId = :patientId ORDER BY createdAtMillis DESC")
    fun getRecordsForPatient(patientId: String): Flow<List<HealthRecordEntity>>

    @Query("SELECT * FROM health_records WHERE id = :id")
    suspend fun getRecordById(id: String): HealthRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<HealthRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HealthRecordEntity)

    @Delete
    suspend fun delete(record: HealthRecordEntity)
}

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctors ORDER BY rating DESC")
    fun getAllDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE specialization LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    fun searchDoctors(query: String): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE id = :id")
    suspend fun getDoctorById(id: String): DoctorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(doctors: List<DoctorEntity>)
}

@Dao
interface PharmacyDao {
    @Query("SELECT * FROM pharmacies ORDER BY name ASC")
    fun getAllPharmacies(): Flow<List<PharmacyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pharmacies: List<PharmacyEntity>)
}
