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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ButtonTheme {
    Dark,
    Light,
}

enum class ButtonType {
    Book,
    Buy,
    Checkout,
    Donate,
    Order,
    Pay,
    Plain,
    Subscribe,
    PIX,
    EWALLET
}

@Composable
expect fun PayButton(
    onClick: () -> Unit,
    allowedPaymentMethods: String,
    modifier: Modifier = Modifier,
    theme: ButtonTheme = ButtonTheme.Dark,
    type: ButtonType = ButtonType.Buy,
    radius: Dp = 100.dp,
    enabled: Boolean = true,
    onError: (Throwable) -> Unit = {},
    fallbackUi: @Composable (() -> Unit)? = null,
)
