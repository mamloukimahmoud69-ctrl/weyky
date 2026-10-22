package com.example.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.io.FileOutputStream

@Composable
fun ThemeSelectorSection(
  currentMode: WeykyThemeMode,
  minimalConfig: MinimalBackgroundConfig,
  borderColor: Color,
  textPrimary: Color,
  textSecondary: Color,
  panelBg: Color,
  onSelectTheme: (WeykyThemeMode) -> Unit,
  onUpdateMinimalConfig: (MinimalBackgroundConfig) -> Unit,
  onResetAllDefaults: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showResetConfirmDialog by remember { mutableStateOf(false) }

  // File launcher for user image in Minimal mode
  val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      try {
        // Copy picked image to local persistent app files
        val targetFile = File(context.filesDir, "custom_minimal_wallpaper.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
          FileOutputStream(targetFile).use { output ->
            input.copyTo(output)
          }
        }
        onUpdateMinimalConfig(
          minimalConfig.copy(
            type = MinimalBackgroundType.CUSTOM_IMAGE,
            customImagePath = targetFile.absolutePath
          )
        )
      } catch (e: Exception) {
        // Ignore failure, safe fallback
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("theme_selector_section"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "اختر نمط المظهر العام (Visual Themes):",
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = textPrimary
    )

    // 1. Three Visual Theme Cards
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      WeykyThemeMode.entries.forEach { mode ->
        val isSelected = currentMode == mode
        ThemePreviewCard(
          mode = mode,
          isSelected = isSelected,
          accentColor = borderColor,
          textPrimary = textPrimary,
          textSecondary = textSecondary,
          onClick = { onSelectTheme(mode) }
        )
      }
    }

    // 2. Conditional Background Customization for Minimal Only
    if (currentMode == WeykyThemeMode.MINIMAL) {
      MinimalCustomizationPanel(
        config = minimalConfig,
        accentColor = borderColor,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        panelBg = panelBg,
        onPickImage = { imagePickerLauncher.launch("image/*") },
        onUpdateConfig = onUpdateMinimalConfig
      )
    } else {
      // Informative banner explaining why background customization is locked for Tactical & Modern
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0x2238D9F5))
          .border(1.dp, Color(0x4438D9F5), RoundedCornerShape(10.dp))
          .padding(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = borderColor,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "تخصيص الخلفية متاح حصرياً في النمط البسيط (Minimal). النمطان التكتيكي (Tactical) والعصري (Modern) يحتفظان بتصاميمهما السينمائية المتخصصة.",
            fontSize = 12.sp,
            color = textPrimary
          )
        }
      }
    }

    // 3. Reset to Defaults Button
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) {
      OutlinedButton(
        onClick = { showResetConfirmDialog = true },
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252))
      ) {
        Icon(
          imageVector = Icons.Default.RestartAlt,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("استعادة المظهر الافتراضي (Tactical)")
      }
    }

    // Confirmation Dialog for Factory Reset
    if (showResetConfirmDialog) {
      AlertDialog(
        onDismissRequest = { showResetConfirmDialog = false },
        title = { Text("تأكيد استعادة المظهر") },
        text = {
          Text("هل تريد استعادة المظهر التكتيكي الافتراضي وخلفيته السوداء؟ لن يتم حذف الخلفيات المحفوظة في المفضلة.")
        },
        confirmButton = {
          Button(
            onClick = {
              showResetConfirmDialog = false
              onResetAllDefaults()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
          ) {
            Text("نعم، استعادة", color = Color.White)
          }
        },
        dismissButton = {
          OutlinedButton(onClick = { showResetConfirmDialog = false }) {
            Text("إلغاء")
          }
        }
      )
    }
  }
}

/**
 * Individual Visual Theme Card with live preview representation.
 */
@Composable
fun ThemePreviewCard(
  mode: WeykyThemeMode,
  isSelected: Boolean,
  accentColor: Color,
  textPrimary: Color,
  textSecondary: Color,
  onClick: () -> Unit
) {
  val cardWidth = 190.dp
  val cardHeight = 220.dp

  Box(
    modifier = Modifier
      .size(cardWidth, cardHeight)
      .clip(RoundedCornerShape(16.dp))
      .background(if (isSelected) Color(0x3300F0FF) else Color(0xEE0A121C))
      .border(
        width = if (isSelected) 2.2.dp else 1.dp,
        color = if (isSelected) accentColor else Color(0x444A6572),
        shape = RoundedCornerShape(16.dp)
      )
      .clickable { onClick() }
      .testTag("theme_card_${mode.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      // Graphical Preview Miniature
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(100.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(Color.Black)
      ) {
        when (mode) {
          WeykyThemeMode.TACTICAL -> {
            // Tactical Miniature: Pure black with cyan chamfered wireframes
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Black)
                .padding(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(30.dp)
                  .clip(CutCornerShape(6.dp))
                  .background(Color(0xE6040D17))
                  .border(1.dp, Color(0xFF38D9F5), CutCornerShape(6.dp))
              )
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .align(Alignment.Center)
                  .clip(CircleShape)
                  .background(Color(0xFF00ADB5))
              )
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(24.dp)
                  .align(Alignment.BottomCenter)
                  .clip(CutCornerShape(4.dp))
                  .background(Color(0xE6040D17))
                  .border(1.dp, Color(0xFF38D9F5), CutCornerShape(4.dp))
              )
            }
          }

          WeykyThemeMode.MODERN -> {
            // Modern Miniature: Nebula gradient aura with glassmorphic cards
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                  Brush.verticalGradient(
                    colors = listOf(Color(0xFF030712), Color(0xFF071224), Color(0xFF030712))
                  )
                )
                .padding(6.dp)
            ) {
              // Glowing aura behind orb
              Box(
                modifier = Modifier
                  .size(50.dp)
                  .align(Alignment.Center)
                  .clip(CircleShape)
                  .background(
                    Brush.radialGradient(
                      colors = listOf(Color(0xFF00F0FF).copy(alpha = 0.5f), Color.Transparent)
                    )
                  )
              )
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .align(Alignment.Center)
                  .clip(CircleShape)
                  .background(Color(0xFF00F0FF))
              )
              // Glass card
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(28.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xD90C1A30))
                  .border(1.dp, Color(0xFF38D9F5), RoundedCornerShape(8.dp))
              )
            }
          }

          WeykyThemeMode.MINIMAL -> {
            // Minimal Miniature: Flat matte slate cards, understated look
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF0C131B))
                .padding(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(28.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xEE141F2B))
                  .border(0.8.dp, Color(0x44607D8B), RoundedCornerShape(6.dp))
              )
              Box(
                modifier = Modifier
                  .size(20.dp)
                  .align(Alignment.Center)
                  .clip(CircleShape)
                  .background(Color(0xFF26A69A))
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Theme titles & selection badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = mode.titleAr,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = textPrimary
        )
        if (isSelected) {
          Box(
            modifier = Modifier
              .size(18.dp)
              .clip(CircleShape)
              .background(accentColor),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(12.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = mode.descriptionAr,
        fontSize = 10.sp,
        color = textSecondary,
        maxLines = 3
      )
    }
  }
}

/**
 * Minimal Background Customization Controls Panel:
 * - Device image picker with dimming slider
 * - Curated offline wallpaper categories and grid
 * - Solid color presets
 * - Reset minimal background
 */
@Composable
fun MinimalCustomizationPanel(
  config: MinimalBackgroundConfig,
  accentColor: Color,
  textPrimary: Color,
  textSecondary: Color,
  panelBg: Color,
  onPickImage: () -> Unit,
  onUpdateConfig: (MinimalBackgroundConfig) -> Unit
) {
  var selectedCategory by remember { mutableStateOf("الكل") }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xEE0C141E))
      .border(1.dp, Color(0x33546E7A), RoundedCornerShape(12.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Text(
      text = "تخصيص خلفية النمط البسيط (Minimal Wallpaper):",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = textPrimary
    )

    // Option 1: Choose Custom Image from Device
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = onPickImage,
        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
        modifier = Modifier.testTag("pick_custom_image_button")
      ) {
        Icon(
          imageVector = Icons.Default.AddPhotoAlternate,
          contentDescription = null,
          tint = Color.Black,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("اختيار صورة من الهاتف", color = Color.Black, fontSize = 12.sp)
      }

      if (config.type == MinimalBackgroundType.CUSTOM_IMAGE) {
        OutlinedButton(
          onClick = {
            onUpdateConfig(config.copy(type = MinimalBackgroundType.DEFAULT, customImagePath = null))
          }
        ) {
          Text("إلغاء الصورة", fontSize = 11.sp)
        }
      }
    }

    // Dimming Overlay Controls (For images / presets)
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "تعتيم الخلفية لحماية وضوح النصوص:",
          fontSize = 12.sp,
          color = textPrimary
        )
        Text(
          text = "${(config.dimmingAlpha * 100).toInt()}%",
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          color = accentColor
        )
      }

      // Quick Dimming Presets
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("خفيف" to 0.20f, "متوسط" to 0.45f, "داكن" to 0.70f).forEach { (label, alpha) ->
          val isCurrent = Math.abs(config.dimmingAlpha - alpha) < 0.05f
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (isCurrent) accentColor.copy(alpha = 0.25f) else Color(0x22FFFFFF))
              .border(0.8.dp, if (isCurrent) accentColor else Color(0x33FFFFFF), RoundedCornerShape(6.dp))
              .clickable { onUpdateConfig(config.copy(dimmingAlpha = alpha)) }
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(label, fontSize = 11.sp, color = textPrimary)
          }
        }
      }

      Slider(
        value = config.dimmingAlpha,
        onValueChange = { onUpdateConfig(config.copy(dimmingAlpha = it)) },
        valueRange = 0.0f..0.90f,
        colors = SliderDefaults.colors(
          thumbColor = accentColor,
          activeTrackColor = accentColor
        )
      )
    }

    // Option 2: Curated Offline Wallpapers
    Text(
      text = "مكتبة الخلفيات المدمجة (أوفلاين):",
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = textPrimary
    )

    // Category filter chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      OfflineWallpaperCatalog.CATEGORIES.forEach { category ->
        val selected = selectedCategory == category
        FilterChip(
          selected = selected,
          onClick = { selectedCategory = category },
          label = { Text(category, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = accentColor.copy(alpha = 0.25f),
            selectedLabelColor = accentColor
          )
        )
      }
    }

    // Grid of wallpapers
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val presets = OfflineWallpaperCatalog.PRESETS.filter {
        selectedCategory == "الكل" || it.category == selectedCategory
      }

      presets.forEach { preset ->
        val isPresetActive = config.type == MinimalBackgroundType.PRESET_WALLPAPER &&
          config.presetWallpaperId == preset.id
        val isFavorite = config.favoriteWallpaperIds.contains(preset.id)

        Box(
          modifier = Modifier
            .size(110.dp, 130.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(preset.toBrush())
            .border(
              width = if (isPresetActive) 2.dp else 0.8.dp,
              color = if (isPresetActive) accentColor else Color(0x44FFFFFF),
              shape = RoundedCornerShape(10.dp)
            )
            .clickable {
              onUpdateConfig(
                config.copy(
                  type = MinimalBackgroundType.PRESET_WALLPAPER,
                  presetWallpaperId = preset.id
                )
              )
            }
            .padding(8.dp)
        ) {
          // Favorite toggle button on top-right
          IconButton(
            onClick = {
              val updated = config.favoriteWallpaperIds.toMutableSet()
              if (isFavorite) updated.remove(preset.id) else updated.add(preset.id)
              onUpdateConfig(config.copy(favoriteWallpaperIds = updated))
            },
            modifier = Modifier
              .size(24.dp)
              .align(Alignment.TopEnd)
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Favorite",
              tint = if (isFavorite) Color(0xFFFF5252) else Color.White.copy(alpha = 0.7f),
              modifier = Modifier.size(16.dp)
            )
          }

          Text(
            text = preset.nameAr,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart)
          )
        }
      }
    }

    // Option 3: Solid Color Presets
    Text(
      text = "ألوان خلفية ثابتة (Solid Color):",
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = textPrimary
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      MinimalSolidColorPresets.forEach { (label, hex) ->
        val isCurrent = config.type == MinimalBackgroundType.SOLID_COLOR && config.solidColorHex == hex
        val swatchColor = Color(hex)

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(swatchColor)
            .border(
              width = if (isCurrent) 2.dp else 1.dp,
              color = if (isCurrent) accentColor else Color(0x55FFFFFF),
              shape = CircleShape
            )
            .clickable {
              onUpdateConfig(
                config.copy(
                  type = MinimalBackgroundType.SOLID_COLOR,
                  solidColorHex = hex
                )
              )
            },
          contentAlignment = Alignment.Center
        ) {
          if (isCurrent) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // Reset Minimal background alone
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) {
      Button(
        onClick = {
          onUpdateConfig(
            config.copy(
              type = MinimalBackgroundType.DEFAULT,
              customImagePath = null,
              dimmingAlpha = 0.40f
            )
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33546E7A))
      ) {
        Text("استعادة خلفية Minimal الافتراضية", fontSize = 11.sp, color = textPrimary)
      }
    }
  }
}
