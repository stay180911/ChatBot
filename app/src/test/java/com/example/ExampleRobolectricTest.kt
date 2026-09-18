package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Dayanara", appName)
  }

  @Test
  fun `legal knowledge base contains corruption typologies`() {
    val typologies = com.example.data.legal.LegalKnowledgeBase.CRIME_TYPOLOGIES
    org.junit.Assert.assertTrue(typologies.any { it.id == "cohecho_soborno" })
    org.junit.Assert.assertTrue(typologies.any { it.id == "colusion_licitaciones" })
    org.junit.Assert.assertTrue(typologies.any { it.id == "peculado_malversacion" })
  }
}
