package com.devvikram.talkzy.ui.screens.onboarding


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devvikram.talkzy.AppViewModel
import com.devvikram.talkzy.R
import com.devvikram.talkzy.ui.navigation.OnboardingDestination
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    onboardingFinished: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Skip Button - Only visible before last page -
                AnimatedVisibility(visible = pagerState.currentPage < onboardingPages.size - 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(OnboardingDestination.Login.route)
                                    onboardingFinished()
                                }
                                .padding(8.dp)
                        )
                    }
                }

                // Onboarding Pages with Parallax Effect
                HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                    val offset = pagerState.getOffsetForPage(page)
                    OnboardingPage(page = onboardingPages[page], offset)
                }

                // Indicators & Navigation
                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                    // Page Indicators
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(onboardingPages.size) { index ->
                            Indicator(isSelected = pagerState.currentPage == index)
                        }
                    }

                    // Navigation Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Previous Button
                        IconButton(
                            onClick = {
                                if (pagerState.currentPage > 0) {
                                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                                }
                            },
                            enabled = pagerState.currentPage > 0,
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        // Next or Login Button
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            IconButton(
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                                },
                                modifier = Modifier
                                    .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Next",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    navController.navigate(OnboardingDestination.Login.route)
                                    onboardingFinished()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary) // ✅ Themed Button Color
                            ) {
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
fun PagerState.getOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@Composable
fun OnboardingPage(page: OnboardingPageData, offset: Float) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(page.lottieRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .offset(y = (-30 * offset).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(300.dp)
                .offset(y = (-15 * offset).dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.offset(y = (-10 * offset).dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = (-5 * offset).dp)
        )
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val size by animateFloatAsState(targetValue = if (isSelected) 12f else 6f)
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(size.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                shape = CircleShape
            )
    )
}

//  Onboarding Data
data class OnboardingPageData(val lottieRes: Int, val title: String, val description: String)

val onboardingPages = listOf(
    OnboardingPageData(R.raw.connect_animation, "Connect Instantly", "Start chatting with your friends in real-time."),
    OnboardingPageData(R.raw.secure_animation, "Secure Messaging", "Your conversations are private with end-to-end encryption."),
    OnboardingPageData(R.raw.file_sharing_animation, "Share Anything", "Send images, videos, and documents easily."),
    OnboardingPageData(R.raw.stay_connected_animaton, "Stay Notified", "Never miss an important message.")
)



