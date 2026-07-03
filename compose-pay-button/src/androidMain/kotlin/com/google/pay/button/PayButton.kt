/*
 * Copyright 2023 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.pay.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton as GmsPayButton

private const val FULL_ALPHA = 1f
private const val HALF_ALPHA = 0.5f

private fun ButtonTheme.toAndroidValue(): Int = when (this) {
    ButtonTheme.Dark -> ButtonConstants.ButtonTheme.DARK
    ButtonTheme.Light -> ButtonConstants.ButtonTheme.LIGHT
}

private fun ButtonType.toAndroidValue(): Int = when (this) {
    ButtonType.Book -> ButtonConstants.ButtonType.BOOK
    ButtonType.Buy -> ButtonConstants.ButtonType.BUY
    ButtonType.Checkout -> ButtonConstants.ButtonType.CHECKOUT
    ButtonType.Donate -> ButtonConstants.ButtonType.DONATE
    ButtonType.Order -> ButtonConstants.ButtonType.ORDER
    ButtonType.Pay -> ButtonConstants.ButtonType.PAY
    ButtonType.Plain -> ButtonConstants.ButtonType.PLAIN
    ButtonType.Subscribe -> ButtonConstants.ButtonType.SUBSCRIBE
    ButtonType.PIX -> ButtonConstants.ButtonType.PIX
    ButtonType.EWALLET -> ButtonConstants.ButtonType.EWALLET
}

@Composable
actual fun PayButton(
    onClick: () -> Unit,
    allowedPaymentMethods: String,
    modifier: Modifier,
    theme: ButtonTheme,
    type: ButtonType,
    radius: Dp,
    enabled: Boolean,
    onError: (Throwable) -> Unit,
    fallbackUi: @Composable (() -> Unit)?,
) {
    var showFallback by remember { mutableStateOf(false) }

    val radiusPixelValue = with(LocalDensity.current) { radius.toPx().toInt() }

    if (!showFallback) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                GmsPayButton(context).apply {
                    kotlin.runCatching {
                        this.initialize(
                            ButtonOptions.newBuilder()
                                .setButtonTheme(theme.toAndroidValue())
                                .setButtonType(type.toAndroidValue())
                                .setCornerRadius(radiusPixelValue)
                                .setAllowedPaymentMethods(allowedPaymentMethods)
                                .build()
                        )
                    }.onFailure {
                        onError(it)
                        showFallback = true
                    }
                }
            },
            update = { button ->
                if (!showFallback) {
                    button.apply {
                        alpha = if (enabled) FULL_ALPHA else HALF_ALPHA
                        isEnabled = enabled

                        if (enabled) {
                            setOnClickListener { onClick() }
                        } else {
                            setOnClickListener(null)
                        }
                    }
                }
            }
        )
    } else {
        fallbackUi?.invoke()
    }
}
