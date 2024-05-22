package com.example.a101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class UpgradeActivity extends AppCompatActivity {

    // PayPal configuration and request code
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("RemovedClientIdToAddToGithub");

    private Button backButton;
    private Button starterPurchaseButton;
    private Button intermediatePurchaseButton;
    private Button advancedPurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade);

        // Initialize buttons and set click listeners
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        starterPurchaseButton = findViewById(R.id.starterPurchaseButton);
        starterPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment("Starter Package", "10.00");
            }
        });

        intermediatePurchaseButton = findViewById(R.id.intermediatePurchaseButton);
        intermediatePurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment("Intermediate Package", "20.00");
            }
        });

        advancedPurchaseButton = findViewById(R.id.advancedPurchaseButton);
        advancedPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment("Advanced Package", "30.00");
            }
        });
    }

    // Method to initiate PayPal payment process
    private void processPayment(String itemName, String amount) {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), "AUD", itemName,
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    // Handling PayPal payment result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        // Payment was successful
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Payment was canceled by user
                Toast.makeText(this, "Transaction canceled.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Payment details were invalid
                Toast.makeText(this, "Transaction invalid.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Stop PayPal service onDestroy
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
