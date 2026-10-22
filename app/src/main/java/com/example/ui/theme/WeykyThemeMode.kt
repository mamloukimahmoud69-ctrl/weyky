package com.example.ui.theme

/**
 * The three distinct visual themes supported by WEYKY:
 * - TACTICAL: Classic HUD sci-fi aesthetic with octagonal chamfered cards, coordinate grids, and cyan accents.
 * - MODERN: Luxurious cinematic glassmorphism with dynamic ambient aura behind the core and neon accents.
 * - MINIMAL: Ultra-clean matte minimalist design with customizable backgrounds (colors, gradients, presets, custom image).
 */
enum class WeykyThemeMode(
  val id: String,
  val titleAr: String,
  val titleEn: String,
  val tag: String,
  val descriptionAr: String,
  val descriptionEn: String
) {
  TACTICAL(
    id = "tactical",
    titleAr = "تكتيكي (Tactical HUD)",
    titleEn = "Tactical HUD",
    tag = "HUD.01",
    descriptionAr = "خلفية سوداء ثابتة وهوية تقنية تكتيكية مستوحاة من واجهات الملاحة والمستشعرات العسكرية.",
    descriptionEn = "Fixed black OLED background with octagonal chamfered HUD cards, blueprint grids, and cybernetic telemetry."
  ),
  MODERN(
    id = "modern",
    titleAr = "عصري (Modern)",
    titleEn = "Modern Cinematic",
    tag = "GLASS.02",
    descriptionAr = "هوية سينمائية فاخرة بخلفية داكنة كوزمية، إضاءة هالية متغيرة خلف النواة، وبطاقات زجاجية مشعة.",
    descriptionEn = "Cinematic glassmorphism with ambient nebula backlighting behind the core and radiant cyan-purple glowing cards."
  ),
  MINIMAL(
    id = "minimal",
    titleAr = "بسيط (Minimal)",
    titleEn = "Minimal Clean",
    tag = "FLAT.03",
    descriptionAr = "تصميم هادئ وبسيط ببطاقات داكنة مسطحة وأنيقة، مع إمكانية التخصيص الكامل للخلفية والصور والعتامة.",
    descriptionEn = "Understated minimalist matte layout with subtle borders and complete background customization."
  );

  companion object {
    fun fromId(id: String?): WeykyThemeMode =
      entries.find { it.id.equals(id, ignoreCase = true) } ?: TACTICAL
  }
}
