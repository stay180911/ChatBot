package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM anonymous_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM anonymous_reports WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: Long): ReportEntity?

    @Query("SELECT * FROM anonymous_reports WHERE reportCode = :code LIMIT 1")
    suspend fun getReportByCode(code: String): ReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Query("DELETE FROM anonymous_reports WHERE id = :id")
    suspend fun deleteReport(id: Long)

    @Query("DELETE FROM anonymous_reports")
    suspend fun deleteAllReports()
}
