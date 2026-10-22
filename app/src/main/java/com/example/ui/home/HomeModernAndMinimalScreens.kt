package com.example.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.state.WeykyUiState
import com.example.ui.adaptive.DeviceProfile
import com.example.ui.theme.MinimalCard
import com.example.ui.theme.MinimalDateCard
import com.example.ui.theme.MinimalInteractionBar
import com.example.ui.theme.MinimalMusicCard
import com.example.ui.theme.MinimalThemeColors
import com.example.ui.theme.MinimalWeatherCard
import com.example.ui.theme.ModernDateCard
import com.example.ui.theme.ModernGlassCard
import com.example.ui.theme.ModernInteractionBar
import com.example.ui.theme.ModernMusicCard
import com.example.ui.theme.ModernThemeColors
import com.example.ui.theme.ModernWeatherCard

/**
 * Modern Phone Portrait Home Layout.
 * Features glassmorphic side-by-side Date & Weather cards, expandable glass music card,
 * dynamic radiant aura centered behind WEYKY core, and unified glass interaction bar.
 */
@Composable
fun ModernPortraitHomeScreen(
  uiState: WeykyUiState,
  deviceProfile: DeviceProfile,
  onQuerySubmit: (String) -> Unit,
  onQueryChange: (String) -> Unit,
  onOpenSettings: () -> Unit,
  onRequestPermission: (String) -> Unit,
  onToggleVoice: () -> Unit,
  onConfirmOption: (String) -> Unit,
  onToggleAppDrawer: () -> Unit,
  onToggleMediaPlayback: () -> Unit,
  onNextMediaTrack: () -> Unit,
  onPreviousMediaTrack: () -> Unit,
  keyboardController: SoftwareKeyboardController?,
  modifier: Modifier = Modifier
) {
  val dynamicCoreSize = deviceProfile.calculateAdaptiveCoreSize(uiState.coreScale)
  val isMusicVisible = uiState.isMusicPlaying || uiState.activeMediaMetadata != null
  val hasResponse = !uiState.lastResponseText.isNullOrBlank()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(
        horizontal = deviceProfile.contentPaddingHorizontalDp,
        vertical = deviceProfile.contentPaddingVerticalDp
      ),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. TOP SECTION: Date Card & Weather Card side-by-side
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = deviceProfile.maxCardWidthDp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        ModernDateCard(
          dateAr = uiState.currentDateString,
          dateEn = uiState.currentEnglishDateString,
          modifier = Modifier.weight(1f)
        )
        ModernWeatherCard(
          temperature = uiState.weatherSnapshot?.temperatureCelsius?.toInt()?.let { "$it°" } ?: "18°",
          condition = uiState.weatherSnapshot?.condition ?: "غائم جزئياً",
          location = uiState.weatherSnapshot?.locationName ?: "الجزائر",
          modifier = Modifier.weight(1f)
        )
      }

      // Music Card (shown when active)
      AnimatedVisibility(
        visible = isMusicVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        ModernMusicCard(
          title = uiState.activeMediaMetadata?.title,
          artist = uiState.activeMediaMetadata?.artist,
          isPlaying = uiState.isMusicPlaying,
          onTogglePlayback = onToggleMediaPlayback,
          onNext = onNextMediaTrack,
          onPrevious = onPreviousMediaTrack
        )
      }
    }

    // 2. CENTER SECTION: Living WEYKY Visual Core
    HomeCoreSection(
      uiState = uiState,
      coreSizeDp = dynamicCoreSize,
      onToggleVoice = onToggleVoice,
      modifier = Modifier
        .weight(1f, fill = true)
        .padding(vertical = 4.dp)
    )

    // 3. BOTTOM SECTION: Response Card + Modern Interaction Bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = deviceProfile.maxCardWidthDp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      AnimatedVisibility(
        visible = hasResponse,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        ModernResponseCard(
          text = uiState.lastResponseText.orEmpty(),
          suggestions = uiState.proactiveSuggestions,
          onQuerySubmit = onQuerySubmit,
          modifier = Modifier.fillMaxWidth()
        )
      }

      ModernInteractionBar(
        inputQuery = uiState.inputQuery,
        onQueryChange = onQueryChange,
        onQuerySubmit = onQuerySubmit,
        onToggleVoice = onToggleVoice,
        onOpenSettings = onOpenSettings,
        onToggleAppDrawer = onToggleAppDrawer,
        isListening = uiState.isVoiceListening,
        keyboardController = keyboardController
      )
    }
  }
}

/**
 * Modern Landscape / Tablet Layout.
 */
@Composable
fun ModernLandscapeHomeScreen(
  uiState: WeykyUiState,
  deviceProfile: DeviceProfile,
  onQuerySubmit: (String) -> Unit,
  onQueryChange: (String) -> Unit,
  onOpenSettings: () -> Unit,
  onRequestPermission: (String) -> Unit,
  onToggleVoice: () -> Unit,
  onConfirmOption: (String) -> Unit,
  onToggleAppDrawer: () -> Unit,
  onToggleMediaPlayback: () -> Unit,
  onNextMediaTrack: () -> Unit,
  onPreviousMediaTrack: () -> Unit,
  keyboardController: SoftwareKeyboardController?,
  modifier: Modifier = Modifier
) {
  val dynamicCoreSize = deviceProfile.calculateAdaptiveCoreSize(uiState.coreScale)
  val isMusicVisible = uiState.isMusicPlaying || uiState.activeMediaMetadata != null
  val hasResponse = !uiState.lastResponseText.isNullOrBlank()

  Row(
    modifier = modifier
      .fillMaxSize()
      .padding(
        horizontal = deviceProfile.contentPaddingHorizontalDp,
        vertical = deviceProfile.contentPaddingVerticalDp
      ),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // LEFT COLUMN: Weather & Music
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      ModernWeatherCard(
        temperature = uiState.weatherSnapshot?.temperatureCelsius?.toInt()?.let { "$it°" } ?: "18°",
        condition = uiState.weatherSnapshot?.condition ?: "غائم جزئياً",
        location = uiState.weatherSnapshot?.locationName ?: "الجزائر",
        modifier = Modifier.fillMaxWidth()
      )

      if (isMusicVisible) {
        ModernMusicCard(
          title = uiState.activeMediaMetadata?.title,
          artist = uiState.activeMediaMetadata?.artist,
          isPlaying = uiState.isMusicPlaying,
          onTogglePlayback = onToggleMediaPlayback,
          onNext = onNextMediaTrack,
          onPrevious = onPreviousMediaTrack,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    // CENTER COLUMN: Core & Interaction
    Column(
      modifier = Modifier
        .weight(1.4f)
        .fillMaxHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      HomeCoreSection(
        uiState = uiState,
        coreSizeDp = dynamicCoreSize,
        onToggleVoice = onToggleVoice,
        modifier = Modifier.weight(1f)
      )

      if (hasResponse) {
        ModernResponseCard(
          text = uiState.lastResponseText.orEmpty(),
          suggestions = uiState.proactiveSuggestions,
          onQuerySubmit = onQuerySubmit,
          modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )
      }

      ModernInteractionBar(
        inputQuery = uiState.inputQuery,
        onQueryChange = onQueryChange,
        onQuerySubmit = onQuerySubmit,
        onToggleVoice = onToggleVoice,
        onOpenSettings = onOpenSettings,
        onToggleAppDrawer = onToggleAppDrawer,
        isListening = uiState.isVoiceListening,
        keyboardController = keyboardController
      )
    }

    // RIGHT COLUMN: Date & App Drawer
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      ModernDateCard(
        dateAr = uiState.currentDateString,
        dateEn = uiState.currentEnglishDateString,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

/**
 * Modern Glassmorphic Response Card for Assistant replies.
 */
@Composable
fun ModernResponseCard(
  text: String,
  suggestions: List<com.example.core.intelligence.conversation.ActionableOption>,
  onQuerySubmit: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ModernGlassCard(
    modifier = modifier.testTag("modern_response_card"),
    cornerRadius = 16.dp,
    borderColor = ModernThemeColors.AccentCyan
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = ModernThemeColors.AccentCyan,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "WEYKY",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = ModernThemeColors.AccentCyan
        )
      }

      Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = ModernThemeColors.TextPrimary,
        lineHeight = 19.sp
      )

      if (suggestions.isNotEmpty()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          suggestions.forEach { option ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0x3300F0FF))
                .border(0.8.dp, Color(0x6600F0FF), RoundedCornerShape(8.dp))
                .clickable { onQuerySubmit(option.queryToExecute) }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = option.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ModernThemeColors.AccentCyan
              )
            }
          }
        }
      }
    }
  }
}

/**
 * Minimal Phone Portrait Home Layout.
 * Understated, clean, matte design matching reference mockup with custom background support.
 */
@Composable
fun MinimalPortraitHomeScreen(
  uiState: WeykyUiState,
  deviceProfile: DeviceProfile,
  onQuerySubmit: (String) -> Unit,
  onQueryChange: (String) -> Unit,
  onOpenSettings: () -> Unit,
  onRequestPermission: (String) -> Unit,
  onToggleVoice: () -> Unit,
  onConfirmOption: (String) -> Unit,
  onToggleAppDrawer: () -> Unit,
  onToggleMediaPlayback: () -> Unit,
  onNextMediaTrack: () -> Unit,
  onPreviousMediaTrack: () -> Unit,
  keyboardController: SoftwareKeyboardController?,
  modifier: Modifier = Modifier
) {
  val dynamicCoreSize = deviceProfile.calculateAdaptiveCoreSize(uiState.coreScale)
  val isMusicVisible = uiState.isMusicPlaying || uiState.activeMediaMetadata != null
  val hasResponse = !uiState.lastResponseText.isNullOrBlank()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(
        horizontal = deviceProfile.contentPaddingHorizontalDp,
        vertical = deviceProfile.contentPaddingVerticalDp
      ),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. TOP SECTION: Date Card & Weather Card side-by-side
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = deviceProfile.maxCardWidthDp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        MinimalDateCard(
          dateAr = uiState.currentDateString,
          dateEn = uiState.currentEnglishDateString,
          modifier = Modifier.weight(1f)
        )
        MinimalWeatherCard(
          temperature = uiState.weatherSnapshot?.temperatureCelsius?.toInt()?.let { "$it°" } ?: "18°",
          condition = uiState.weatherSnapshot?.condition ?: "غائم جزئياً",
          location = uiState.weatherSnapshot?.locationName ?: "الجزائر",
          modifier = Modifier.weight(1f)
        )
      }

      // Music Card
      AnimatedVisibility(
        visible = isMusicVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        MinimalMusicCard(
          title = uiState.activeMediaMetadata?.title,
          artist = uiState.activeMediaMetadata?.artist,
          isPlaying = uiState.isMusicPlaying,
          onTogglePlayback = onToggleMediaPlayback,
          onNext = onNextMediaTrack,
          onPrevious = onPreviousMediaTrack
        )
      }
    }

    // 2. CENTER SECTION: Living WEYKY Visual Core
    HomeCoreSection(
      uiState = uiState,
      coreSizeDp = dynamicCoreSize,
      onToggleVoice = onToggleVoice,
      modifier = Modifier
        .weight(1f, fill = true)
        .padding(vertical = 4.dp)
    )

    // 3. BOTTOM SECTION: Response Card + Minimal Interaction Bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = deviceProfile.maxCardWidthDp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      AnimatedVisibility(
        visible = hasResponse,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        MinimalResponseCard(
          text = uiState.lastResponseText.orEmpty(),
          suggestions = uiState.proactiveSuggestions,
          onQuerySubmit = onQuerySubmit,
          modifier = Modifier.fillMaxWidth()
        )
      }

      MinimalInteractionBar(
        inputQuery = uiState.inputQuery,
        onQueryChange = onQueryChange,
        onQuerySubmit = onQuerySubmit,
        onToggleVoice = onToggleVoice,
        onOpenSettings = onOpenSettings,
        onToggleAppDrawer = onToggleAppDrawer,
        isListening = uiState.isVoiceListening,
        keyboardController = keyboardController
      )
    }
  }
}

/**
 * Minimal Landscape / Tablet Layout.
 */
@Composable
fun MinimalLandscapeHomeScreen(
  uiState: WeykyUiState,
  deviceProfile: DeviceProfile,
  onQuerySubmit: (String) -> Unit,
  onQueryChange: (String) -> Unit,
  onOpenSettings: () -> Unit,
  onRequestPermission: (String) -> Unit,
  onToggleVoice: () -> Unit,
  onConfirmOption: (String) -> Unit,
  onToggleAppDrawer: () -> Unit,
  onToggleMediaPlayback: () -> Unit,
  onNextMediaTrack: () -> Unit,
  onPreviousMediaTrack: () -> Unit,
  keyboardController: SoftwareKeyboardController?,
  modifier: Modifier = Modifier
) {
  val dynamicCoreSize = deviceProfile.calculateAdaptiveCoreSize(uiState.coreScale)
  val isMusicVisible = uiState.isMusicPlaying || uiState.activeMediaMetadata != null
  val hasResponse = !uiState.lastResponseText.isNullOrBlank()

  Row(
    modifier = modifier
      .fillMaxSize()
      .padding(
        horizontal = deviceProfile.contentPaddingHorizontalDp,
        vertical = deviceProfile.contentPaddingVerticalDp
      ),
    horizontalArrangement = Arrangement.spacedBy(14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // LEFT COLUMN: Weather & Music
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      MinimalWeatherCard(
        temperature = uiState.weatherSnapshot?.temperatureCelsius?.toInt()?.let { "$it°" } ?: "18°",
        condition = uiState.weatherSnapshot?.condition ?: "غائم جزئياً",
        location = uiState.weatherSnapshot?.locationName ?: "الجزائر",
        modifier = Modifier.fillMaxWidth()
      )

      if (isMusicVisible) {
        MinimalMusicCard(
          title = uiState.activeMediaMetadata?.title,
          artist = uiState.activeMediaMetadata?.artist,
          isPlaying = uiState.isMusicPlaying,
          onTogglePlayback = onToggleMediaPlayback,
          onNext = onNextMediaTrack,
          onPrevious = onPreviousMediaTrack,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    // CENTER COLUMN: Core & Interaction
    Column(
      modifier = Modifier
        .weight(1.4f)
        .fillMaxHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      HomeCoreSection(
        uiState = uiState,
        coreSizeDp = dynamicCoreSize,
        onToggleVoice = onToggleVoice,
        modifier = Modifier.weight(1f)
      )

      if (hasResponse) {
        MinimalResponseCard(
          text = uiState.lastResponseText.orEmpty(),
          suggestions = uiState.proactiveSuggestions,
          onQuerySubmit = onQuerySubmit,
          modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )
      }

      MinimalInteractionBar(
        inputQuery = uiState.inputQuery,
        onQueryChange = onQueryChange,
        onQuerySubmit = onQuerySubmit,
        onToggleVoice = onToggleVoice,
        onOpenSettings = onOpenSettings,
        onToggleAppDrawer = onToggleAppDrawer,
        isListening = uiState.isVoiceListening,
        keyboardController = keyboardController
      )
    }

    // RIGHT COLUMN: Date & App Drawer
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      MinimalDateCard(
        dateAr = uiState.currentDateString,
        dateEn = uiState.currentEnglishDateString,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

/**
 * Minimal Response Card for Assistant replies.
 */
@Composable
fun MinimalResponseCard(
  text: String,
  suggestions: List<com.example.core.intelligence.conversation.ActionableOption>,
  onQuerySubmit: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  MinimalCard(
    modifier = modifier.testTag("minimal_response_card"),
    cornerRadius = 14.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = text,
        fontSize = 13.sp,
        color = MinimalThemeColors.TextPrimary,
        lineHeight = 18.sp
      )

      if (suggestions.isNotEmpty()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          suggestions.forEach { option ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0x224A6572))
                .border(0.8.dp, Color(0x44607D8B), RoundedCornerShape(6.dp))
                .clickable { onQuerySubmit(option.queryToExecute) }
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = option.title,
                fontSize = 11.sp,
                color = MinimalThemeColors.TextPrimary
              )
            }
          }
        }
      }
    }
  }
}
