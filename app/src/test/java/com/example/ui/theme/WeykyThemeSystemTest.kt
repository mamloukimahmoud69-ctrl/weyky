package com.example.ui.theme

import com.example.core.state.WeykyUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeykyThemeSystemTest {

  @Test
  fun testThemeModeResolution_validAndFallback() {
    assertEquals(WeykyThemeMode.TACTICAL, WeykyThemeMode.fromId("tactical"))
    assertEquals(WeykyThemeMode.MODERN, WeykyThemeMode.fromId("modern"))
    assertEquals(WeykyThemeMode.MINIMAL, WeykyThemeMode.fromId("minimal"))
    assertEquals(WeykyThemeMode.MODERN, WeykyThemeMode.fromId("MODERN"))
    // Safe fallback to TACTICAL on unknown or null id
    assertEquals(WeykyThemeMode.TACTICAL, WeykyThemeMode.fromId("unknown_id"))
    assertEquals(WeykyThemeMode.TACTICAL, WeykyThemeMode.fromId(null))
  }

  @Test
  fun testThreeThemesHaveDistinctMetadata() {
    val modes = WeykyThemeMode.entries
    assertEquals(3, modes.size)

    val ids = modes.map { it.id }.toSet()
    assertEquals(3, ids.size)

    // Verify all 3 modes have unique Arabic and English titles
    modes.forEach { mode ->
      assertTrue(mode.titleAr.isNotBlank())
      assertTrue(mode.titleEn.isNotBlank())
      assertTrue(mode.descriptionAr.isNotBlank())
      assertTrue(mode.tag.isNotBlank())
    }
  }

  @Test
  fun testOfflineWallpaperCatalog_hasRequiredCategoriesAndPresets() {
    val categories = OfflineWallpaperCatalog.CATEGORIES
    assertTrue(categories.contains("فضائي"))
    assertTrue(categories.contains("تجريدي"))
    assertTrue(categories.contains("طبيعي"))
    assertTrue(categories.contains("معماري"))
    assertTrue(categories.contains("داكن"))

    val presets = OfflineWallpaperCatalog.PRESETS
    assertTrue(presets.isNotEmpty())

    presets.forEach { preset ->
      assertNotNull(preset.id)
      assertTrue(preset.colors.size >= 2)
      assertNotNull(preset.toBrush())
    }

    // Lookup preset by id
    val found = OfflineWallpaperCatalog.findPreset("cosmic_nebula")
    assertEquals("cosmic_nebula", found.id)
    assertEquals("فضائي", found.category)
  }

  @Test
  fun testMinimalBackgroundConfig_defaultsAndMutations() {
    val config = MinimalBackgroundConfig()
    assertEquals(MinimalBackgroundType.DEFAULT, config.type)
    assertEquals(0.40f, config.dimmingAlpha, 0.001f)
    assertTrue(config.favoriteWallpaperIds.contains("abstract_slate"))

    // Toggle favorite
    val updated = config.copy(
      favoriteWallpaperIds = config.favoriteWallpaperIds + "emerald_wave"
    )
    assertTrue(updated.favoriteWallpaperIds.contains("emerald_wave"))
  }

  @Test
  fun testSolidColorPresets_includeOledBlackAndNavy() {
    val presets = MinimalSolidColorPresets
    assertTrue(presets.any { it.second == 0xFF000000L })
    assertTrue(presets.any { it.first.contains("كحلي") })
  }

  @Test
  fun testWeykyUiState_preservesDefaultCoreScaleAndHoldsTheme() {
    val state = WeykyUiState()
    assertEquals(0.44f, state.coreScale, 0.001f)
    assertEquals(WeykyThemeMode.TACTICAL, state.themeMode)
    assertEquals(MinimalBackgroundType.DEFAULT, state.minimalBackgroundConfig.type)
  }
}
