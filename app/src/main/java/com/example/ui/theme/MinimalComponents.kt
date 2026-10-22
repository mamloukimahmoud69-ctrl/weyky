package com.example.ui.theme

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

/**
 * Minimal Theme color palette constants.
 */
object MinimalThemeColors {
  val CardBackground = Color(0xEE141F2B)
  val CardBorder = Color(0x33546E7A)
  val TextPrimary = Color(0xFFF1F5F9)
  val TextSecondary = Color(0xFF94A3B8)
  val TextMuted = Color(0xFF64748B)

  val AccentCyan = Color(0xFF38D9F5)
  val AccentTeal = Color(0xFF00ADB5)
  val OutlineIconTint = Color(0xFF94A3B8)
}

/**
 * Custom background renderer for Minimal theme supporting:
 * - Default subtle geometric dark curves
 * - Solid color
 * - Linear gradient
 * - Offline wallpaper catalog preset
 * - Custom user image from file system + Dimming overlay
 */
@Composable
fun MinimalBackgroundContainer(
  config: MinimalBackgroundConfig,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  Box(modifier = modifier.fillMaxSize()) {
    // 1. Base Layer (Image / Preset / Gradient / Solid / Default)
    when (config.type) {
      MinimalBackgroundType.CUSTOM_IMAGE -> {
        val bitmap = remember(config.customImagePath) {
          config.customImagePath?.let { path ->
            try {
              val file = File(path)
              if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
            } catch (e: Exception) {
              null
            }
          }
        }

        if (bitmap != null) {
          Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          // Fallback to default minimal slate
          MinimalDefaultBackground(Modifier.fillMaxSize())
        }
      }

      MinimalBackgroundType.PRESET_WALLPAPER -> {
        val preset = OfflineWallpaperCatalog.findPreset(config.presetWallpaperId)
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(preset.toBrush())
        )
      }

      MinimalBackgroundType.GRADIENT -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  Color(config.gradientStartHex),
                  Color(config.gradientEndHex)
                )
              )
            )
        )
      }

      MinimalBackgroundType.SOLID_COLOR -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color(config.solidColorHex))
        )
      }

      MinimalBackgroundType.DEFAULT -> {
        MinimalDefaultBackground(Modifier.fillMaxSize())
      }
    }

    // 2. Dimming Overlay Layer (applied to background only)
    if (config.dimmingAlpha > 0f) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = config.dimmingAlpha.coerceIn(0f, 0.95f)))
      )
    }

    // 3. Main Content
    content()
  }
}

/**
 * Default Minimal background: subtle dark teal/slate curved geometric ambient atmosphere
 * matching the user's reference mockup.
 */
@Composable
fun MinimalDefaultBackground(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .background(Color(0xFF0C131B))
      .drawBehind {
        // Broad diagonal dark slate gradient
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFF0F1823),
              Color(0xFF0B1218),
              Color(0xFF070C10)
            )
          )
        )

        // Large subtle curved ambient wave in background
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0xFF005B60).copy(alpha = 0.22f),
              Color(0xFF0B242C).copy(alpha = 0.10f),
              Color.Transparent
            ),
            center = Offset(size.width * 0.15f, size.height * 0.55f),
            radius = size.width * 0.9f
          ),
          center = Offset(size.width * 0.15f, size.height * 0.55f),
          radius = size.width * 0.9f
        )
      }
  )
}

/**
 * Minimal Flat Card Container with matte dark fill, rounded corners, and subtle hairline border.
 */
@Composable
fun MinimalCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 14.dp,
  content: @Composable BoxScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)
  Box(
    modifier = modifier
      .clip(shape)
      .background(MinimalThemeColors.CardBackground)
      .border(
        width = 1.dp,
        color = MinimalThemeColors.CardBorder,
        shape = shape
      )
  ) {
    content()
  }
}

/**
 * Minimal Date Card (matching reference mockup with clean outline calendar icon and subtle slate styling).
 */
@Composable
fun MinimalDateCard(
  dateAr: String,
  dateEn: String,
  modifier: Modifier = Modifier
) {
  MinimalCard(
    modifier = modifier
      .height(76.dp)
      .testTag("minimal_date_card")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0x224A6572))
          .border(0.8.dp, Color(0x3390A4AE), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.DateRange,
          contentDescription = "Calendar",
          tint = MinimalThemeColors.TextSecondary,
          modifier = Modifier.size(20.dp)
        )
      }

      Column(
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = if (dateAr.isNotBlank()) dateAr else "الخميس، 22 أكتوبر",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MinimalThemeColors.TextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (dateEn.isNotBlank()) dateEn.uppercase() else "THURSDAY, 22 OCT",
          fontSize = 10.sp,
          fontWeight = FontWeight.Normal,
          fontFamily = FontFamily.Monospace,
          color = MinimalThemeColors.TextMuted,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

/**
 * Minimal Weather Card (matching reference mockup with clean outline cloud, temperature, condition, location).
 */
@Composable
fun MinimalWeatherCard(
  temperature: String,
  condition: String,
  location: String,
  modifier: Modifier = Modifier
) {
  MinimalCard(
    modifier = modifier
      .height(76.dp)
      .testTag("minimal_weather_card")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0x224A6572))
          .border(0.8.dp, Color(0x3390A4AE), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.WbCloudy,
          contentDescription = "Weather",
          tint = MinimalThemeColors.TextSecondary,
          modifier = Modifier.size(22.dp)
        )
      }

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = if (temperature.isNotBlank()) temperature else "18°",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MinimalThemeColors.TextPrimary
          )
          Text(
            text = if (condition.isNotBlank()) condition else "غائم جزئياً",
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = MinimalThemeColors.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MinimalThemeColors.TextSecondary,
            modifier = Modifier.size(11.dp)
          )
          Text(
            text = if (location.isNotBlank()) location else "الجزائر",
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = MinimalThemeColors.TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

/**
 * Minimal Music Card (matching reference mockup with flat matte container, clean pill tag, and outline playback controls).
 */
@Composable
fun MinimalMusicCard(
  title: String?,
  artist: String?,
  isPlaying: Boolean,
  onTogglePlayback: () -> Unit,
  onNext: () -> Unit,
  onPrevious: () -> Unit,
  modifier: Modifier = Modifier
) {
  MinimalCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("minimal_music_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      // Top row with pill tag: MUSIC ○
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x224A6572))
            .border(0.8.dp, Color(0x3390A4AE), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "MUSIC",
              fontSize = 9.sp,
              fontWeight = FontWeight.Medium,
              fontFamily = FontFamily.Monospace,
              color = MinimalThemeColors.TextSecondary
            )
            Box(
              modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(if (isPlaying) Color(0xFF22C55E) else MinimalThemeColors.TextSecondary)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Content row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Controls cluster
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          IconButton(
            onClick = onPrevious,
            modifier = Modifier.size(34.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SkipPrevious,
              contentDescription = "Previous",
              tint = MinimalThemeColors.TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          // Minimal play/pause ring
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color(0x2200B4D8))
              .border(1.2.dp, MinimalThemeColors.AccentCyan, CircleShape)
              .clickable { onTogglePlayback() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = MinimalThemeColors.AccentCyan,
              modifier = Modifier.size(24.dp)
            )
          }

          IconButton(
            onClick = onNext,
            modifier = Modifier.size(34.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SkipNext,
              contentDescription = "Next",
              tint = MinimalThemeColors.TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        // Track title, artist, and album thumbnail
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.padding(start = 8.dp)
        ) {
          Column(
            horizontalAlignment = Alignment.End
          ) {
            Text(
              text = title ?: "هل أنت راضٍ عن ..",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = MinimalThemeColors.TextPrimary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                tint = MinimalThemeColors.AccentCyan,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = artist ?: "عبد القادر - أغاني مختارة",
                fontSize = 11.sp,
                color = MinimalThemeColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          // Album artwork container
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                Brush.linearGradient(
                  colors = listOf(
                    Color(0xFF4A148C),
                    Color(0xFFC2185B),
                    Color(0xFFE65100)
                  )
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * Minimal Interaction Bar (matching reference mockup with matte app drawer button,
 * subtle rounded input pill with back arrow and mic, and matte settings button).
 */
@Composable
fun MinimalInteractionBar(
  inputQuery: String,
  onQueryChange: (String) -> Unit,
  onQuerySubmit: (String) -> Unit,
  onToggleVoice: () -> Unit,
  onOpenSettings: () -> Unit,
  onToggleAppDrawer: () -> Unit,
  isListening: Boolean,
  keyboardController: SoftwareKeyboardController?,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // App drawer button: matte rounded square
    MinimalCard(
      modifier = Modifier
        .size(48.dp)
        .clickable { onToggleAppDrawer() }
        .testTag("minimal_app_drawer_button"),
      cornerRadius = 12.dp
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Widgets,
          contentDescription = "App Drawer",
          tint = MinimalThemeColors.TextSecondary,
          modifier = Modifier.size(22.dp)
        )
      }
    }

    // Command input pill container
    MinimalCard(
      modifier = Modifier
        .weight(1f)
        .height(48.dp)
        .testTag("minimal_input_pill"),
      cornerRadius = 24.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Submit",
          tint = MinimalThemeColors.TextSecondary,
          modifier = Modifier
            .size(18.dp)
            .clickable {
              if (inputQuery.isNotBlank()) {
                onQuerySubmit(inputQuery)
                keyboardController?.hide()
              }
            }
        )

        Box(
          modifier = Modifier.weight(1f),
          contentAlignment = Alignment.CenterStart
        ) {
          if (inputQuery.isEmpty()) {
            Text(
              text = "... Ask WEYKY anything",
              fontSize = 13.sp,
              color = MinimalThemeColors.TextMuted,
              fontFamily = FontFamily.Default
            )
          }

          BasicTextField(
            value = inputQuery,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
              color = MinimalThemeColors.TextPrimary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Default
            ),
            cursorBrush = SolidColor(MinimalThemeColors.AccentCyan),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
              onSend = {
                if (inputQuery.isNotBlank()) {
                  onQuerySubmit(inputQuery)
                  keyboardController?.hide()
                }
              }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("minimal_command_input")
          )
        }

        IconButton(
          onClick = onToggleVoice,
          modifier = Modifier.size(34.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice Input",
            tint = if (isListening) Color(0xFF22C55E) else MinimalThemeColors.AccentCyan,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // Settings button: matte rounded square
    MinimalCard(
      modifier = Modifier
        .size(48.dp)
        .clickable { onOpenSettings() }
        .testTag("minimal_settings_button"),
      cornerRadius = 12.dp
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = "Settings",
          tint = MinimalThemeColors.TextSecondary,
          modifier = Modifier.size(22.dp)
        )
      }
    }
  }
}
