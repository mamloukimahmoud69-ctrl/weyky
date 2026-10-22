package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Types of backgrounds supported by the Minimal theme.
 */
enum class MinimalBackgroundType {
  DEFAULT,
  SOLID_COLOR,
  GRADIENT,
  PRESET_WALLPAPER,
  CUSTOM_IMAGE
}

/**
 * Offline wallpaper presets curated by category, rendered dynamically with high-quality shaders.
 */
data class OfflineWallpaperPreset(
  val id: String,
  val category: String,
  val nameAr: String,
  val nameEn: String,
  val colors: List<Long>,
  val angle: Float = 45f
) {
  fun toBrush(): Brush {
    val composeColors = colors.map { Color(it) }
    return Brush.linearGradient(composeColors)
  }
}

/**
 * Catalog of offline wallpapers grouped by category.
 */
object OfflineWallpaperCatalog {
  val PRESETS: List<OfflineWallpaperPreset> = listOf(
    // 1. فضائي (Cosmic)
    OfflineWallpaperPreset(
      id = "cosmic_nebula",
      category = "فضائي",
      nameAr = "سديم كوزمي",
      nameEn = "Cosmic Nebula",
      colors = listOf(0xFF030712L, 0xFF0B1B33L, 0xFF1E1035L, 0xFF050D1AL)
    ),
    OfflineWallpaperPreset(
      id = "deep_space",
      category = "فضائي",
      nameAr = "أعماق الفضاء",
      nameEn = "Deep Space",
      colors = listOf(0xFF000000L, 0xFF060D17L, 0xFF0F1E2EL, 0xFF02070EL)
    ),
    OfflineWallpaperPreset(
      id = "aurora_borealis",
      category = "فضائي",
      nameAr = "شفق قطبي",
      nameEn = "Polar Aurora",
      colors = listOf(0xFF020E14L, 0xFF05232AL, 0xFF0B3B38L, 0xFF010E12L)
    ),

    // 2. تجريدي (Abstract)
    OfflineWallpaperPreset(
      id = "abstract_slate",
      category = "تجريدي",
      nameAr = "منحنيات الأردواز",
      nameEn = "Slate Curves",
      colors = listOf(0xFF0D141CL, 0xFF131F2BL, 0xFF192A3AL, 0xFF0B1219L)
    ),
    OfflineWallpaperPreset(
      id = "emerald_wave",
      category = "تجريدي",
      nameAr = "تموج زمردي",
      nameEn = "Emerald Wave",
      colors = listOf(0xFF031411L, 0xFF082622L, 0xFF0F3B35L, 0xFF02120FL)
    ),
    OfflineWallpaperPreset(
      id = "midnight_obsidian",
      category = "تجريدي",
      nameAr = "حجر السج الليلي",
      nameEn = "Midnight Obsidian",
      colors = listOf(0xFF0A0C10L, 0xFF121620L, 0xFF1A2030L, 0xFF08090CL)
    ),

    // 3. طبيعي (Nature)
    OfflineWallpaperPreset(
      id = "nordic_fjord",
      category = "طبيعي",
      nameAr = "فيورد نرويجي",
      nameEn = "Nordic Fjord",
      colors = listOf(0xFF08121AL, 0xFF0E222EL, 0xFF183849L, 0xFF071017L)
    ),
    OfflineWallpaperPreset(
      id = "desert_dusk",
      category = "طبيعي",
      nameAr = "شفق صحراوي",
      nameEn = "Desert Dusk",
      colors = listOf(0xFF140B0BL, 0xFF2A1513L, 0xFF3D1F17L, 0xFF100808L)
    ),
    OfflineWallpaperPreset(
      id = "pine_forest",
      category = "طبيعي",
      nameAr = "غابة صنوبر ليلية",
      nameEn = "Pine Forest",
      colors = listOf(0xFF040F0CL, 0xFF0B1F19L, 0xFF133328L, 0xFF030D0AL)
    ),

    // 4. معماري (Architectural)
    OfflineWallpaperPreset(
      id = "monolith_dark",
      category = "معماري",
      nameAr = "مونوليث خرساني",
      nameEn = "Monolith Dark",
      colors = listOf(0xFF0D0F12L, 0xFF161A20L, 0xFF202630L, 0xFF0A0B0EL)
    ),
    OfflineWallpaperPreset(
      id = "urban_shadows",
      category = "معماري",
      nameAr = "ظلال هندسية",
      nameEn = "Urban Shadows",
      colors = listOf(0xFF0B0E14L, 0xFF141924L, 0xFF1C2434L, 0xFF090B10L)
    ),

    // 5. داكن (OLED Dark)
    OfflineWallpaperPreset(
      id = "pure_carbon",
      category = "داكن",
      nameAr = "كربون داكن",
      nameEn = "Pure Carbon",
      colors = listOf(0xFF000000L, 0xFF050608L, 0xFF0A0C10L, 0xFF000000L)
    ),
    OfflineWallpaperPreset(
      id = "deep_graphite",
      category = "داكن",
      nameAr = "غرافيت عميق",
      nameEn = "Deep Graphite",
      colors = listOf(0xFF060709L, 0xFF0F1116L, 0xFF151820L, 0xFF050608L)
    ),
    OfflineWallpaperPreset(
      id = "eclipse_black",
      category = "داكن",
      nameAr = "كسوف كلي",
      nameEn = "Eclipse Black",
      colors = listOf(0xFF010204L, 0xFF080B10L, 0xFF0D121CL, 0xFF010204L)
    )
  )

  val CATEGORIES: List<String> = listOf("الكل", "فضائي", "تجريدي", "طبيعي", "معماري", "داكن")

  fun findPreset(id: String): OfflineWallpaperPreset =
    PRESETS.find { it.id == id } ?: PRESETS[0]
}

/**
 * Solid color presets for Minimal theme.
 */
val MinimalSolidColorPresets: List<Pair<String, Long>> = listOf(
  "كحلي مسائي (الافتراضي)" to 0xFF0D141CL,
  "أسود فاحم (OLED)" to 0xFF000000L,
  "رمادي داكن" to 0xFF101317L,
  "أردواز غامق" to 0xFF121B24L,
  "زيتي ليلي" to 0xFF0A1412L,
  "عنابي غامق" to 0xFF160A0DL,
  "تيتانيوم بارد" to 0xFF15181FL
)

/**
 * State configuration for Minimal theme background customization.
 */
data class MinimalBackgroundConfig(
  val type: MinimalBackgroundType = MinimalBackgroundType.DEFAULT,
  val solidColorHex: Long = 0xFF0D141CL,
  val gradientStartHex: Long = 0xFF0B141EL,
  val gradientEndHex: Long = 0xFF17283AL,
  val presetWallpaperId: String = "abstract_slate",
  val customImagePath: String? = null,
  val dimmingAlpha: Float = 0.40f,
  val favoriteWallpaperIds: Set<String> = setOf("abstract_slate", "cosmic_nebula")
)
