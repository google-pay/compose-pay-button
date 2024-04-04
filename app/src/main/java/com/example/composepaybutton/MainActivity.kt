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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                  "allowedAuthMethods": ["PAN_ONLY", "CRYPTOGRAM_3DS"],
                  "allowedCardNetworks": ["AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"]
                },
                "tokenizationSpecification": {
                  "type": "PAYMENT_GATEWAY",
                  "parameters": {
                    "gateway": "example",
                    "gatewayMerchantId": "exampleGatewayMerchantId"
                  }
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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Divider(thickness = 1.dp, color = Color.LightGray)
                    Text("default")

                    // Default
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods)

                    Divider(thickness = 1.dp, color = Color.LightGray)
                    Text("customized look")

                    // Customized look
                    PayButton(
                        onClick = onClick,
                        allowedPaymentMethods = allowedPaymentMethods,
                        radius = 1.dp,
                        modifier = Modifier.width(250.dp),
                        theme = ButtonTheme.Light
                    )

                    Divider(thickness = 1.dp, color = Color.LightGray)
                    Text("various button types")

                    // Customized labels
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Pay)
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Book)
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Subscribe)
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Donate)

                    Divider(thickness = 1.dp, color = Color.LightGray)
                    Text("disabled state")

                    // Disabled buttons
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Checkout, enabled = false)
                    PayButton(onClick = onClick, allowedPaymentMethods = allowedPaymentMethods, type = ButtonType.Checkout, theme = ButtonTheme.Light, enabled = false)
                }
            }
        }
    }
}
