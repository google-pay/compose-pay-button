# compose-pay-button

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

        setContent {
            // Default
            PayButton(onClick = onClick)

            // Customized look
            PayButton(
                onClick = onClick,
                radius = 1.dp,
                modifier = Modifier.width(200.dp),
                theme = ButtonTheme.Light
            )

            // Customized labels
            PayButton(onClick = onClick, type = ButtonType.Book)
            PayButton(onClick = onClick, type = ButtonType.Subscribe)
            PayButton(onClick = onClick, type = ButtonType.Donate)
        }
    }
}
```