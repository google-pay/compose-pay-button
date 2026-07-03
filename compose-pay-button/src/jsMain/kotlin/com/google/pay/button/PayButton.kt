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

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLScriptElement

private var isScriptLoading = false
private var isScriptLoaded = false
private val callbacks = mutableListOf<() -> Unit>()

private fun loadScript(onLoad: () -> Unit, onError: (Throwable) -> Unit) {
    if (isScriptLoaded) {
        onLoad()
        return
    }
    callbacks.add(onLoad)
    if (isScriptLoading) return
    isScriptLoading = true
    
    val script = document.createElement("script") as HTMLScriptElement
    script.src = "https://pay.google.com/gp/p/js/pay.js"
    script.async = true
    script.onload = {
        isScriptLoaded = true
        isScriptLoading = false
        callbacks.forEach { it() }
        callbacks.clear()
    }
    script.onerror = { _, _, _, _, _ ->
        isScriptLoading = false
        onError(Exception("Failed to load Google Pay SDK"))
    }
    document.head?.appendChild(script)
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
    var sdkState by remember { mutableStateOf<SdkState>(SdkState.Loading) }
    val buttonContainer = remember { document.createElement("div") as HTMLDivElement }

    DisposableEffect(Unit) {
        buttonContainer.style.position = "absolute"
        buttonContainer.style.zIndex = "1000"
        buttonContainer.style.display = "none"
        document.body?.appendChild(buttonContainer)

        loadScript(
            onLoad = {
                sdkState = SdkState.Ready
            },
            onError = {
                sdkState = SdkState.Failed(it)
                onError(it)
            }
        )

        onDispose {
            buttonContainer.remove()
        }
    }

    DisposableEffect(enabled) {
        buttonContainer.style.opacity = if (enabled) "1.0" else "0.5"
        buttonContainer.style.setProperty("pointer-events", if (enabled) "auto" else "none")
        onDispose {}
    }

    when (val state = sdkState) {
        SdkState.Loading -> {
            // Render nothing while loading
        }
        is SdkState.Failed -> {
            fallbackUi?.invoke()
        }
        SdkState.Ready -> {
            DisposableEffect(theme, type, radius, allowedPaymentMethods) {
                buttonContainer.innerHTML = ""
                
                val buttonColor = when (theme) {
                    ButtonTheme.Dark -> "black"
                    ButtonTheme.Light -> "white"
                }
                val buttonType = when (type) {
                    ButtonType.Book -> "book"
                    ButtonType.Buy -> "buy"
                    ButtonType.Checkout -> "checkout"
                    ButtonType.Donate -> "donate"
                    ButtonType.Order -> "order"
                    ButtonType.Pay -> "pay"
                    ButtonType.Plain -> "plain"
                    ButtonType.Subscribe -> "subscribe"
                    else -> "buy"
                }
                
                val radiusPx = radius.value.toString() + "px"
                
                val paymentsClient = js("new google.payments.api.PaymentsClient({environment: 'TEST'})")
                val allowedPaymentMethodsParsed = js("JSON.parse(allowedPaymentMethods)")
                val button = paymentsClient.createButton(
                    js("({ onClick: onClick, allowedPaymentMethods: allowedPaymentMethodsParsed, buttonColor: buttonColor, buttonType: buttonType, buttonSizeMode: 'fill' })")
                )
                
                val buttonElement = button.unsafeCast<org.w3c.dom.HTMLElement>()
                buttonElement.style.borderRadius = radiusPx
                buttonElement.style.width = "100%"
                buttonElement.style.height = "100%"
                
                buttonContainer.appendChild(buttonElement)
                onDispose {}
            }

            Box(
                modifier = modifier
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.positionInWindow()
                        val size = coordinates.size
                        val density = kotlinx.browser.window.devicePixelRatio
                        
                        buttonContainer.style.left = "${position.x / density}px"
                        buttonContainer.style.top = "${position.y / density}px"
                        buttonContainer.style.width = "${size.width / density}px"
                        buttonContainer.style.height = "${size.height / density}px"
                        buttonContainer.style.display = "block"
                    }
            )
        }
    }
}

private sealed interface SdkState {
    object Loading : SdkState
    class Failed(val error: Throwable) : SdkState
    object Ready : SdkState
}
