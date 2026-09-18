package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anonymous_reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportCode: String,
    val crimeType: String,
    val entityName: String,
    val suspectOfficials: String,
    val narrative: String,
    val incidentDate: String,
    val location: String,
    val evidenceTypes: String,
    val hashSignature: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Generada"
)
