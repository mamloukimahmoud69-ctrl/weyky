package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
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
import com.example.core.state.WeykyUiState
import com.example.rendering.visual_state.CoreVisualState

/**
 * Modern Theme palette constants.
 */
object ModernThemeColors {
  val BackgroundDark = Color(0xFF030712)
  val GlassSurfaceTop = Color(0xD90C1A30)
  val GlassSurfaceBottom = Color(0xBF060F1E)

  val BorderGlowCyan = Color(0x9938D9F5)
  val BorderGlowBlue = Color(0x664D79FF)
  val BorderGlowPurple = Color(0x4D9D4EDD)

  val AccentCyan = Color(0xFF00F0FF)
  val AccentTeal = Color(0xFF00B4D8)
  val AccentPink = Color(0xFFF72585)
  val AccentPurple = Color(0xFF7209B7)

  val TextPrimary = Color(0xFFF8FAFC)
  val TextSecondary = Color(0xFF94A3B8)
  val TextCyan = Color(0xFF7DD3FC)

  // State-specific aura colors
  val AuraIdle = Color(0xFF00B4D8)
  val AuraListening = Color(0xFF10B981)
  val AuraResponding = Color(0xFF3B82F6)
  val AuraMusic = Color(0xFFD946EF)
  val AuraCharging = Color(0xFFF59E0B)
  val AuraProcessing = Color(0xFF38BDF8)
}

/**
 * Modern background modifier: deep celestial dark background with a dynamic radial aura
 * centered behind the WEYKY core, transitioning smoothly between state colors.
 */
fun Modifier.modernCinematicBackground(
  visualState: CoreVisualState,
  isPowerSaving: Boolean = false
): Modifier = this.drawBehind {
  // Base deep cosmic space gradient
  drawRect(
    brush = Brush.verticalGradient(
      colors = listOf(
        Color(0xFF020611),
        Color(0xFF040A18),
        Color(0xFF02050E)
      )
    )
  )

  if (!isPowerSaving) {
    val auraColor = when (visualState) {
      CoreVisualState.IDLE -> ModernThemeColors.AuraIdle
      CoreVisualState.LISTENING -> ModernThemeColors.AuraListening
      CoreVisualState.RESPONDING -> ModernThemeColors.AuraResponding
      CoreVisualState.MUSIC -> ModernThemeColors.AuraMusic
      CoreVisualState.CHARGING -> ModernThemeColors.AuraCharging
      CoreVisualState.PROCESSING -> ModernThemeColors.AuraProcessing
      CoreVisualState.SUCCESS -> Color(0xFF10B981)
      CoreVisualState.ERROR -> Color(0xFFEF4444)
      CoreVisualState.CALL -> Color(0xFF06B6D4)
      CoreVisualState.NOTIFICATION -> Color(0xFFF59E0B)
      else -> ModernThemeColors.AuraIdle
    }

    // Wide atmospheric nebula glow centered behind the core
    val centerOffset = Offset(size.width * 0.5f, size.height * 0.52f)
    val glowRadius = size.width * 0.85f

    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          auraColor.copy(alpha = 0.28f),
          auraColor.copy(alpha = 0.14f),
          Color(0xFF3B82F6).copy(alpha = 0.08f),
          Color.Transparent
        ),
        center = centerOffset,
        radius = glowRadius
      ),
      center = centerOffset,
      radius = glowRadius
    )

    // Secondary subtle upper-left cosmic dust reflection
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          Color(0xFF7209B7).copy(alpha = 0.12f),
          Color.Transparent
        ),
        center = Offset(size.width * 0.25f, size.height * 0.20f),
        radius = size.width * 0.6f
      ),
      center = Offset(size.width * 0.25f, size.height * 0.20f),
      radius = size.width * 0.6f
    )
  }
}

/**
 * Modern Glassmorphic Container Card with rounded corners, translucent glass fill,
 * soft glowing border, and top specular sheen.
 */
@Composable
fun ModernGlassCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 20.dp,
  borderColor: Color = ModernThemeColors.BorderGlowCyan,
  content: @Composable BoxScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)

  Box(
    modifier = modifier
      .shadow(
        elevation = 8.dp,
        shape = shape,
        ambientColor = Color(0x3300F0FF),
        spotColor = borderColor.copy(alpha = 0.5f)
      )
      .clip(shape)
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            ModernThemeColors.GlassSurfaceTop,
            ModernThemeColors.GlassSurfaceBottom
          )
        )
      )
      .border(
        width = 1.2.dp,
        brush = Brush.verticalGradient(
          colors = listOf(
            borderColor.copy(alpha = 0.85f),
            ModernThemeColors.BorderGlowBlue.copy(alpha = 0.45f),
            Color(0x2238D9F5)
          )
        ),
        shape = shape
      )
      .drawBehind {
        // Specular highlight line along top edge
        drawLine(
          color = Color.White.copy(alpha = 0.22f),
          start = Offset(cornerRadius.toPx(), 1f),
          end = Offset(size.width - cornerRadius.toPx(), 1f),
          strokeWidth = 1.5f,
          cap = StrokeCap.Round
        )
      }
  ) {
    content()
  }
}

/**
 * Modern Date Card (matching reference mockup: rounded calendar badge, Arabic date, English subtitle).
 */
@Composable
fun ModernDateCard(
  dateAr: String,
  dateEn: String,
  modifier: Modifier = Modifier
) {
  ModernGlassCard(
    modifier = modifier
      .height(76.dp)
      .testTag("modern_date_card")
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
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0x3338D9F5))
          .border(1.dp, Color(0x6638D9F5), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.DateRange,
          contentDescription = "Calendar",
          tint = ModernThemeColors.AccentCyan,
          modifier = Modifier.size(22.dp)
        )
      }

      Column(
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = if (dateAr.isNotBlank()) dateAr else "الخميس، 22 أكتوبر",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = ModernThemeColors.TextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (dateEn.isNotBlank()) dateEn.uppercase() else "THURSDAY, 22 OCT",
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          fontFamily = FontFamily.Monospace,
          color = ModernThemeColors.TextCyan,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

/**
 * Modern Weather Card (matching reference mockup: glowing weather icon, temperature, condition, location pin).
 */
@Composable
fun ModernWeatherCard(
  temperature: String,
  condition: String,
  location: String,
  modifier: Modifier = Modifier
) {
  ModernGlassCard(
    modifier = modifier
      .height(76.dp)
      .testTag("modern_weather_card")
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
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0x3338D9F5))
          .border(1.dp, Color(0x6638D9F5), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.WbCloudy,
          contentDescription = "Weather",
          tint = Color(0xFFFFD166),
          modifier = Modifier.size(24.dp)
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ModernThemeColors.TextPrimary
          )
          Text(
            text = if (condition.isNotBlank()) condition else "غائم جزئياً",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = ModernThemeColors.TextSecondary,
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
            tint = ModernThemeColors.AccentCyan,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = if (location.isNotBlank()) location else "الجزائر",
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = ModernThemeColors.TextCyan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

/**
 * Modern Music Player Card (matching reference mockup with pill badge, glowing cyan play ring,
 * track title, artist, soundwave, and gradient artwork thumbnail).
 */
@Composable
fun ModernMusicCard(
  title: String?,
  artist: String?,
  isPlaying: Boolean,
  onTogglePlayback: () -> Unit,
  onNext: () -> Unit,
  onPrevious: () -> Unit,
  modifier: Modifier = Modifier
) {
  ModernGlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("modern_music_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      // Top row with pill tag: MUSIC •
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x3300B4D8))
            .border(0.8.dp, Color(0x6638D9F5), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "MUSIC",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = ModernThemeColors.AccentCyan
            )
            Box(
              modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(if (isPlaying) Color(0xFF22C55E) else ModernThemeColors.AccentCyan)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Main content row: Controls on left, Track info & Artwork on right (RTL compliant)
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
              tint = ModernThemeColors.TextPrimary,
              modifier = Modifier.size(20.dp)
            )
          }

          // Center glowing circular play/pause ring
          Box(
            modifier = Modifier
              .size(44.dp)
              .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                spotColor = ModernThemeColors.AccentCyan
              )
              .clip(CircleShape)
              .background(Color(0x3300F0FF))
              .border(1.8.dp, ModernThemeColors.AccentCyan, CircleShape)
              .clickable { onTogglePlayback() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = ModernThemeColors.AccentCyan,
              modifier = Modifier.size(26.dp)
            )
          }

          IconButton(
            onClick = onNext,
            modifier = Modifier.size(34.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SkipNext,
              contentDescription = "Next",
              tint = ModernThemeColors.TextPrimary,
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
              fontWeight = FontWeight.Bold,
              color = ModernThemeColors.TextPrimary,
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
                tint = ModernThemeColors.AccentCyan,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = artist ?: "عبد القادر - أغاني مختارة",
                fontSize = 11.sp,
                color = ModernThemeColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          // Album artwork thumbnail with radiant gradient
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(
                  colors = listOf(
                    Color(0xFF7209B7),
                    Color(0xFFF72585),
                    Color(0xFFFFB703)
                  )
                )
              )
              .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * Modern Interaction Bar (matching reference mockup with glassmorphic app drawer button,
 * pill search input with microphone icon, and glassmorphic settings button).
 */
@Composable
fun ModernInteractionBar(
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
    // App drawer button: glassmorphic rounded square
    ModernGlassCard(
      modifier = Modifier
        .size(50.dp)
        .clickable { onToggleAppDrawer() }
        .testTag("modern_app_drawer_button"),
      cornerRadius = 14.dp
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Widgets,
          contentDescription = "App Drawer",
          tint = ModernThemeColors.AccentCyan,
          modifier = Modifier.size(24.dp)
        )
      }
    }

    // Command input pill container
    ModernGlassCard(
      modifier = Modifier
        .weight(1f)
        .height(50.dp)
        .testTag("modern_input_pill"),
      cornerRadius = 25.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Submit",
          tint = ModernThemeColors.AccentCyan,
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
              color = ModernThemeColors.TextSecondary,
              fontFamily = FontFamily.Default
            )
          }

          BasicTextField(
            value = inputQuery,
            onValueChange = onQueryChange,
            textStyle = TextStyle(
              color = ModernThemeColors.TextPrimary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Default
            ),
            cursorBrush = SolidColor(ModernThemeColors.AccentCyan),
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
              .testTag("modern_command_input")
          )
        }

        // Microphone action with active pulse
        IconButton(
          onClick = onToggleVoice,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice Input",
            tint = if (isListening) Color(0xFF22C55E) else ModernThemeColors.AccentCyan,
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }

    // Settings button: glassmorphic rounded square
    ModernGlassCard(
      modifier = Modifier
        .size(50.dp)
        .clickable { onOpenSettings() }
        .testTag("modern_settings_button"),
      cornerRadius = 14.dp
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = "Settings",
          tint = ModernThemeColors.AccentCyan,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}
