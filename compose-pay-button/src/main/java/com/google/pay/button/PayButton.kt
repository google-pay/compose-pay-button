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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton as GmsPayButton
import com.google.android.gms.wallet.button.ButtonConstants

enum class ButtonTheme(val value: Int) {
    Dark(ButtonConstants.ButtonTheme.DARK),
    Light(ButtonConstants.ButtonTheme.LIGHT),
}

enum class ButtonType(val value: Int) {
    Book(ButtonConstants.ButtonType.BOOK),
    Buy(ButtonConstants.ButtonType.BUY),
    Checkout(ButtonConstants.ButtonType.CHECKOUT),
    Donate(ButtonConstants.ButtonType.DONATE),
    Order(ButtonConstants.ButtonType.ORDER),
    Pay(ButtonConstants.ButtonType.PAY),
    Plain(ButtonConstants.ButtonType.PLAIN),
    Subscribe(ButtonConstants.ButtonType.SUBSCRIBE),
}

private const val FULL_ALPHA = 1f
private const val HALF_ALPHA = 0.5f

@Composable
fun PayButton(
    onClick: () -> Unit,
    allowedPaymentMethods: String,
    modifier: Modifier = Modifier,
    theme: ButtonTheme = ButtonTheme.Dark,
    type: ButtonType = ButtonType.Buy,
    radius: Dp = 100.dp,
    enabled: Boolean = true,
) {

    val radiusPixelValue = with(LocalDensity.current) { radius.toPx().toInt() }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            GmsPayButton(context).apply {
                this.initialize(
                    ButtonOptions.newBuilder()
                        .setButtonTheme(theme.value)
                        .setButtonType(type.value)
                        .setCornerRadius(radiusPixelValue)
                        .setAllowedPaymentMethods(allowedPaymentMethods)
                        .build()
                )
            }
        },
        update = { button ->
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
    )
}