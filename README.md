# Google Pay button for Jetpack Compose

[![Maven Central](https://img.shields.io/maven-central/v/com.google.pay.button/compose-pay-button)](https://search.maven.org/search?q=g:com.google.pay.button)

An Android library that provides a [Jetpack Compose](https://developer.android.com/jetpack/compose) wrapper on top of the
[Google Pay Button API](https://developers.google.com/pay/api/android/guides/tutorial).

## Installation

The library is hosted on Maven central and can be used by ensuring the following lines exist in each gradle file:

**build.gradle:**

```groovy
repositories {
    mavenCentral()
}
```

**app/build.gradle:**

```
dependencies {
    implementation "com.google.pay.button:compose-pay-button:<version>"
}
```

## Usage

```kotlin
// other imports omitted for brevity
// see full example in the "app" directory

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
```
