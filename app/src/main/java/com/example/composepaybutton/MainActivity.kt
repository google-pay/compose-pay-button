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

package com.example.composepaybutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.google.pay.button.ButtonTheme
import com.google.pay.button.ButtonType
import com.google.pay.button.PayButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onClick = { println("Button clicked") }

        // as per https://developers.google.com/pay/api/android/reference/request-objects#PaymentMethod
        val allowedPaymentMethods = """
            [
              {
                "type": "CARD",
                "parameters": {
                  "allowedAuthMethods": ["PAN_ONLY","CRYPTOGRAM_3DS"],
                  "allowedCardNetworks": ["AMEX", "DISCOVER", "INTERAC", "JCB", "MASTERCARD", "VISA"]
                }
              }
            ]
        """.trimIndent()

        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    // Default
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods)

                    // Customized look
                    PayButton(
                        onClick = onClick,
                        allowedPaymentMethods = allowedPaymentMethods,
                        radius = 1.dp,
                        modifier = Modifier.width(200.dp),
                        theme = ButtonTheme.Light
                    )

                    // Customized labels
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Book)
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Subscribe)
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Donate)
                }
            }
        }
    }
}
