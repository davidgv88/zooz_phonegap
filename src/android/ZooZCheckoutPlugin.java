package com.zooz.phonegap.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.zooz.android.lib.CheckoutActivity;
import com.zooz.android.lib.model.ZooZInvoice;
import com.zooz.android.lib.model.ZooZSubscription;

/**
 * PhoneGap plugin which can be involved from javascript
 *
 * @author Nir Zohar
 * @since April 10, 2012
 */
public class ZooZCheckoutPlugin extends CordovaPlugin{

	private static final String TAG = ZooZCheckoutPlugin.class.getSimpleName();
	
	public static final String AMOUNT = "amount";
	public static final String CURRENCY_CODE = "currencyCode";
	public static final String APP_KEY = "appKey";
	public static final String IS_SANDBOX = "isSandbox";
	public static final String USER_ADDITIONAL_DETAILS = "userAdditionalDetails";
	public static final String USER_FIRST_NAME = "userFirstName";
	public static final String USER_LAST_NAME = "userLastName";
	public static final String USER_PHONE_COUNTRY_CODE = "userPhoneCountryCode";
	public static final String USER_PHONE_NUMBER = "userPhoneNumber";
	public static final String USER_EMAIL = "userEmail";
	
	public static final String ADDRESS_BILLING_STREET = "addressBillingStreet";
	public static final String ADDRESS_BILLING_CITY = "addressBillingCity";
	public static final String ADDRESS_BILLING_STATE = "addressBillingState";
	public static final String ADDRESS_BILLING_COUNTRY = "addressBillingCountry";
	public static final String ADDRESS_BILLING_ZIP_CODE = "addressBillingZipCode";
	public static final String ADDRESS_SHIPPING_STREET = "addressShippingStreet";
	public static final String ADDRESS_SHIPPING_CITY = "addressShippingCity";
	public static final String ADDRESS_SHIPPING_STATE = "addressShippingState";
	public static final String ADDRESS_SHIPPING_COUNTRY = "addressShippingCountry";
	public static final String ADDRESS_SHIPPING_ZIP_CODE = "addressShippingZipCode";
	
	public static final String INVOICE_NUMBER = "invoiceNumber";
	public static final String INVOICE_ADDITIONAL_DETAILS = "invoiceAdditionalDetails";
	
	public static final String CARRIER_BILLING_PRICE_LEVEL = "carrierBillingPriceLevel";
	public static final String SET_PRICE_BY_CARRIER_BILLING = "setPriceByCarrierBilling";
	
	public static final String ADDRESS_BILLING_FIRST_NAME_ATTRIBUTE = "addressBillingFirstName";
	public static final String ADDRESS_BILLING_LAST_NAME_ATTRIBUTE = "addressBillingLastName";
	public static final String ADDRESS_BILLING_PHONE_COUNTRY_CODE_ATTRIBUTE = "addressBillingPhoneCountryCode";
	public static final String ADDRESS_BILLING_PHONE_NUMBER_ATTRIBUTE = "addressBillingPhoneNumber";
	public static final String ADDRESS_SHIPPING_FIRST_NAME_ATTRIBUTE = "addressShippingFirstName";
	public static final String ADDRESS_SHIPPING_LAST_NAME_ATTRIBUTE = "addressShippingLastName";
	public static final String ADDRESS_SHIPPING_PHONE_COUNTRY_CODE_ATTRIBUTE = "addressShippingPhoneCountryCode";
	public static final String ADDRESS_SHIPPING_PHONE_NUMBER_ATTRIBUTE = "addressShippingPhoneNumber";
	
	public static final String IS_AUTHORIZE = "isAuthorize";
	public static final String JUMIO = "isJumio";
	public static final String JUMIO_API_TOKEN = "jumioApiToken"; //details of the merchant's Jumio account, in case it use Jumio 
	public static final String JUMIO_API_SECRET = "jumioApiSecret";
	public static final String JUMIO_APP_NAME = "jumioAppName";
	public static final String HEADER_BACKGROUND_COLOR = "headerBackgroundColor";
	public static final String HEADER_TITLE_COLOR = "headerTitleColor";
	public static final String IS_CARD_HOLDER_NAME_REQUIRED = "isCardHolderNameRequired";
	public static final String KIOSK_MODE = "kioskMode";
	public static final String ERROR_MESSAGE_SUFFIX = "errorMessageSuffix";
	public static final String SUBSCRIPTION = "subscription";
	public static final String SUBSCRIPTION_PERIOD_NUMBER = "periodNumber";
	public static final String SUBSCRIPTION_PERIOD_UNIT = "periodUnit";
	public static final String SUBSCRIPTION_RECURRENCE = "recurrence";
	public static final String SUBSCRIPTION_ACTION = "subscriptionAction";
	public static final String SUBSCRIPTION_ID = "id";
	
	

	public static final String ACTION_PAY = "pay";
	public static final String ACTION_SUBSCRIBE = "subscribe";
	public static final String ACTION_CHECK_SUBSCRIPTION = "checkSubscription";
	public static final String ACTION_CANCEL_SUBSCRIPTION = "cancelSubscription";

	private static final int ZooZ_Activity_ID = 24031202; // magic number
	private CallbackContext callbackContext;


	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		Log.d(TAG, "Plugin Called");

		if (!ACTION_PAY.equals(action) && !ACTION_SUBSCRIBE.equals(action) 
				&& !ACTION_CHECK_SUBSCRIPTION.equals(action) && !ACTION_CANCEL_SUBSCRIPTION.equals(action)) {
			Log.w(TAG, "Invalid action called.");
			return false;
		}
		
		this.callbackContext = callbackContext;
					
		try {
			Intent intent = new Intent(cordova.getActivity(), CheckoutActivity.class);

			JSONObject data = args.getJSONObject(0);
			
			double amount = data.getDouble(AMOUNT);
			String currencyCode = data.getString(CURRENCY_CODE);
			String appKey = data.getString(APP_KEY);
			
			boolean isSandbox = true;
			if (!data.isNull(IS_SANDBOX)) {
				isSandbox = data.getBoolean(IS_SANDBOX);
			}
			
			// send merchant credential, app_key as given in the registration
			intent.putExtra(CheckoutActivity.ZOOZ_APP_KEY, appKey);
			intent.putExtra(CheckoutActivity.ZOOZ_AMOUNT, amount);
			intent.putExtra(CheckoutActivity.ZOOZ_CURRENCY_CODE, currencyCode);
			intent.putExtra(CheckoutActivity.ZOOZ_IS_SANDBOX, isSandbox);
						
			// optional parameters
			if (!data.isNull(USER_FIRST_NAME)) {
				intent.putExtra(CheckoutActivity.ZOOZ_USER_FIRST_NAME, data.getString(USER_FIRST_NAME));
			}
			if (!data.isNull(USER_LAST_NAME)) {
				intent.putExtra(CheckoutActivity.ZOOZ_USER_LAST_NAME, data.getString(USER_LAST_NAME));
			}
			if (!data.isNull(USER_EMAIL)) {
				intent.putExtra(CheckoutActivity.ZOOZ_USER_EMAIL, data.getString(USER_EMAIL));
			}
			if (!data.isNull(USER_PHONE_COUNTRY_CODE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_USER_PHONE_COUNTRY_CODE, data.getString(USER_PHONE_COUNTRY_CODE));
			}
			if (!data.isNull(USER_PHONE_NUMBER)) {
				intent.putExtra(CheckoutActivity.ZOOZ_USER_PHONE_NUMBER, data.getString(USER_PHONE_NUMBER));
			}
			if (!data.isNull(USER_ADDITIONAL_DETAILS)) {
				intent.putExtra(CheckoutActivity.ZOOZ_USER_ADDITIONAL_DETAILS, data.getString(USER_ADDITIONAL_DETAILS));
			}
			if (!data.isNull(ADDRESS_BILLING_STREET)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_STREET, data.getString(ADDRESS_BILLING_STREET));
			}
			if (!data.isNull(ADDRESS_BILLING_CITY)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_CITY, data.getString(ADDRESS_BILLING_CITY));
			}
			if (!data.isNull(ADDRESS_BILLING_STATE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_STATE, data.getString(ADDRESS_BILLING_STATE));
			}
			if (!data.isNull(ADDRESS_BILLING_COUNTRY)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_COUNTRY, data.getString(ADDRESS_BILLING_COUNTRY));
			}
			if (!data.isNull(ADDRESS_BILLING_ZIP_CODE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_ZIP_CODE, data.getString(ADDRESS_BILLING_ZIP_CODE));
			}
			if (!data.isNull(ADDRESS_SHIPPING_STREET)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_STREET, data.getString(ADDRESS_SHIPPING_STREET));
			}
			if (!data.isNull(ADDRESS_SHIPPING_CITY)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_CITY, data.getString(ADDRESS_SHIPPING_CITY));
			}
			if (!data.isNull(ADDRESS_SHIPPING_STATE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_STATE, data.getString(ADDRESS_SHIPPING_STATE));
			}
			if (!data.isNull(ADDRESS_SHIPPING_COUNTRY)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_COUNTRY, data.getString(ADDRESS_SHIPPING_COUNTRY));
			}
			if (!data.isNull(ADDRESS_SHIPPING_ZIP_CODE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_ZIP_CODE, data.getString(ADDRESS_SHIPPING_ZIP_CODE));
			}
			if (!data.isNull(ADDRESS_BILLING_FIRST_NAME_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_FIRST_NAME_ATTRIBUTE, data.getString(ADDRESS_BILLING_FIRST_NAME_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_BILLING_LAST_NAME_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_LAST_NAME_ATTRIBUTE, data.getString(ADDRESS_BILLING_LAST_NAME_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_BILLING_PHONE_COUNTRY_CODE_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_PHONE_COUNTRY_CODE_ATTRIBUTE, data.getString(ADDRESS_BILLING_PHONE_COUNTRY_CODE_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_BILLING_PHONE_NUMBER_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_BILLING_PHONE_NUMBER_ATTRIBUTE, data.getString(ADDRESS_BILLING_PHONE_NUMBER_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_SHIPPING_FIRST_NAME_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_FIRST_NAME_ATTRIBUTE, data.getString(ADDRESS_SHIPPING_FIRST_NAME_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_SHIPPING_LAST_NAME_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_LAST_NAME_ATTRIBUTE, data.getString(ADDRESS_SHIPPING_LAST_NAME_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_SHIPPING_PHONE_COUNTRY_CODE_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_PHONE_COUNTRY_CODE_ATTRIBUTE, data.getString(ADDRESS_SHIPPING_PHONE_COUNTRY_CODE_ATTRIBUTE));
			}
			if (!data.isNull(ADDRESS_SHIPPING_PHONE_NUMBER_ATTRIBUTE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ADDRESS_SHIPPING_PHONE_NUMBER_ATTRIBUTE, data.getString(ADDRESS_SHIPPING_PHONE_NUMBER_ATTRIBUTE));
			}
			if (!data.isNull(IS_AUTHORIZE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_IS_AUTHORIZE, data.getBoolean(IS_AUTHORIZE));
			}
			if (!data.isNull(JUMIO)) {
				intent.putExtra(CheckoutActivity.ZOOZ_JUMIO, data.getBoolean(JUMIO));
			}
			if (!data.isNull(JUMIO_API_SECRET)) {
				intent.putExtra(CheckoutActivity.ZOOZ_JUMIO_API_SECRET, data.getString(JUMIO_API_SECRET));
			}
			if (!data.isNull(JUMIO_API_TOKEN)) {
				intent.putExtra(CheckoutActivity.ZOOZ_JUMIO_API_TOKEN, data.getString(JUMIO_API_TOKEN));
			}
			if (!data.isNull(JUMIO_APP_NAME)) {
				intent.putExtra(CheckoutActivity.ZOOZ_JUMIO_APP_NAME, data.getString(JUMIO_APP_NAME));
			}
			if (!data.isNull(CARRIER_BILLING_PRICE_LEVEL)) {
				intent.putExtra(CheckoutActivity.ZOOZ_CARRIER_BILLING_PRICE_LEVEL, data.getString(CARRIER_BILLING_PRICE_LEVEL));
			}
			if (!data.isNull(SET_PRICE_BY_CARRIER_BILLING)) {
				intent.putExtra(CheckoutActivity.ZOOZ_SET_PRICE_BY_CARRIER_BILLING, data.getBoolean(SET_PRICE_BY_CARRIER_BILLING));
			}
			if (!data.isNull(IS_CARD_HOLDER_NAME_REQUIRED)) {
				intent.putExtra(CheckoutActivity.ZOOZ_IS_CARD_HOLDER_NAME_REQUIRED, data.getBoolean(IS_CARD_HOLDER_NAME_REQUIRED));
			}
			if (!data.isNull(HEADER_BACKGROUND_COLOR)) {
				intent.putExtra(CheckoutActivity.ZOOZ_HEADER_BACKGROUND_COLOR, data.getString(HEADER_BACKGROUND_COLOR));
			}
			if (!data.isNull(HEADER_TITLE_COLOR)) {
				intent.putExtra(CheckoutActivity.ZOOZ_HEADER_TITLE_COLOR, data.getString(HEADER_TITLE_COLOR));
			}
			if (!data.isNull(KIOSK_MODE)) {
				intent.putExtra(CheckoutActivity.ZOOZ_KIOSK_MODE, data.getBoolean(KIOSK_MODE));
			}
			if (!data.isNull(ERROR_MESSAGE_SUFFIX)) {
				intent.putExtra(CheckoutActivity.ZOOZ_ERROR_MESSAGE_SUFFIX, data.getString(ERROR_MESSAGE_SUFFIX));
			}
			if (!data.isNull(SUBSCRIPTION_ACTION)) {
				intent.putExtra(CheckoutActivity.ZOOZ_SUBSCRIPTION_ACTION, data.getString(SUBSCRIPTION_ACTION));
			}
			

			ZooZInvoice zoozInvoice = new ZooZInvoice();
			
			if (!data.isNull(INVOICE_NUMBER)) {
				zoozInvoice.setInvoiceNumber(data.getString(INVOICE_NUMBER));
			}
			if (!data.isNull(INVOICE_ADDITIONAL_DETAILS)) {
				zoozInvoice.setInvoiceAdditionalDetails(data.getString(INVOICE_ADDITIONAL_DETAILS));
			}
			intent.putExtra(CheckoutActivity.ZOOZ_INVOICE, zoozInvoice);
			
			if (!data.isNull(SUBSCRIPTION)) {
				JSONObject jsonSubscription = data.getJSONObject(SUBSCRIPTION);
				int periodNumber = 0;
				int recurrence = 0;
				String periodUnit = null;
				String subscriptionId = null;
				
				if (!jsonSubscription.isNull(SUBSCRIPTION_PERIOD_NUMBER)) {
					periodNumber = jsonSubscription.getInt(SUBSCRIPTION_PERIOD_NUMBER);
				}
				if (!jsonSubscription.isNull(SUBSCRIPTION_RECURRENCE)) {
					recurrence = jsonSubscription.getInt(SUBSCRIPTION_RECURRENCE);
				}
				if (!jsonSubscription.isNull(SUBSCRIPTION_PERIOD_UNIT)) {
					periodUnit = jsonSubscription.getString(SUBSCRIPTION_PERIOD_UNIT);
				}
				if (!jsonSubscription.isNull(SUBSCRIPTION_ID)) {
					subscriptionId = jsonSubscription.getString(SUBSCRIPTION_ID);
				}
				
				ZooZSubscription zoozSubscription = new ZooZSubscription(periodUnit, periodNumber, recurrence,isSandbox);
				zoozSubscription.setSubscriptionId(subscriptionId);
				intent.putExtra(CheckoutActivity.ZOOZ_SUBSCRIPTION, zoozSubscription);
			}
			
			
			// start ZooZCheckoutActivity and wait to the activity result.
			cordova.startActivityForResult(this, intent, ZooZ_Activity_ID);

		} catch (JSONException ex) {
			Log.w(TAG, "JSON Exception.", ex);
			return false;
		}
		
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d(TAG, "Got activity result");
		
		if (requestCode != ZooZ_Activity_ID)
			return;

		JSONObject resultsData = new JSONObject();
		
		try {
			switch (resultCode) {
			case Activity.RESULT_OK:			
				
				if (intent.getSerializableExtra(CheckoutActivity.ZOOZ_SUBSCRIPTION) != null) {
					ZooZSubscription subscription = (ZooZSubscription) intent.getSerializableExtra(CheckoutActivity.ZOOZ_SUBSCRIPTION);
					
					resultsData.put("subscriptionId", subscription.getSubscriptionId());
					resultsData.put("subscriptionDisplayId", subscription.getSubscriptionDisplayId());
					resultsData.put("subscriptionStatus", subscription.getSubscriptionStatus().name());
					callbackContext.success(resultsData);
										
				} else {
					resultsData.put("transactionId",
							intent.getStringExtra(CheckoutActivity.ZOOZ_TRANSACTION_ID));
					
					resultsData.put("transactionDisplayId",
							intent.getStringExtra(CheckoutActivity.ZOOZ_TRANSACTION_DISPLAY_ID));
					
					resultsData.put("lastFourDigits", 
							intent.getStringExtra(CheckoutActivity.ZOOZ_LAST_FOUR_DIGITS));
					
					resultsData.put("fundSourceType", 
							intent.getStringExtra(CheckoutActivity.ZOOZ_FUND_SOURCE_TYPE));
					
					resultsData.put("paymentAtatus", 
							intent.getStringExtra(CheckoutActivity.ZOOZ_PAYMENT_STATUS));
					
					callbackContext.success(resultsData);
				
				}

				break;
			case Activity.RESULT_CANCELED:

				if (intent != null) {

					resultsData.put("errorCode", intent.getIntExtra(
							CheckoutActivity.ZOOZ_ERROR_CODE, 0));
					resultsData.put("errorMessage", intent
							.getStringExtra(CheckoutActivity.ZOOZ_ERROR_MSG));
					callbackContext.error(resultsData);

				} else {
					callbackContext.error("User canceled.");
				}
				break;

			default:
				break;
			}

		} catch (JSONException ex) {
			Log.w(TAG, "JSON Exception.", ex);
			callbackContext.error("JSON Exception. " + ex.getMessage());
		}
		

	}
}