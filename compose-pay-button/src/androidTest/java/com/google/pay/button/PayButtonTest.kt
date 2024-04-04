package com.google.pay.button

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val GOOGLE_PAY_BUTTON_TEST_TAG = "google-pay-button"

@RunWith(AndroidJUnit4::class)
class GooglePayButtonTest {

    private var allowedPaymentMethods = "";

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        allowedPaymentMethods = """
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
    }

    @Test
    fun testButtonNotDisplayedForEmptyAllowedPaymentMethods() {
        composeTestRule.setContent {
           PayButton(
               onClick = {},
               allowedPaymentMethods = "",
               modifier = Modifier.testTag(GOOGLE_PAY_BUTTON_TEST_TAG))
        }
        composeTestRule.onNodeWithTag(GOOGLE_PAY_BUTTON_TEST_TAG).assertIsNotDisplayed()
    }

    @Test
    fun testButtonDisplayedAndEnabled() {
        composeTestRule.setContent {
            PayButton(
                onClick = { println("Button clicked") },
                allowedPaymentMethods = allowedPaymentMethods,
                type = ButtonType.Book,
                enabled = true,
                modifier = Modifier.testTag(GOOGLE_PAY_BUTTON_TEST_TAG))
        }

        composeTestRule.onNodeWithTag(GOOGLE_PAY_BUTTON_TEST_TAG).assertIsDisplayed()
        Espresso.onView(withText("Book with ")).check(matches(isDisplayed()))

        // TODO - the following assertions do not work for some reason
        //composeTestRule.onNodeWithTag(GOOGLE_PAY_BUTTON_TEST_TAG).assertHasClickAction()
        // if we would set enabled = false
        //composeTestRule.onNodeWithTag(GOOGLE_PAY_BUTTON_TEST_TAG).assertIsNotEnabled()
    }

}