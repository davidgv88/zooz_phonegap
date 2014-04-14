//
//  ZooZPGPlugin.m
//  ZooZPGDemo
//
//  Created by Ronen Morecki on 4/16/12.
//  Copyright (c) 2012 ronen@zooz.co. All rights reserved.
//

#import "ZooZCheckoutPlugin.h"

@interface ZooZCheckoutPlugin ()

@property(nonatomic, copy, readwrite) NSString *activeCallbackId;

@end

@implementation ZooZCheckoutPlugin
@synthesize activeCallbackId;

NSDictionary * activeResponse;

-(NSString *)stringForKey:(NSString *)key withDict:(NSDictionary *)dict defaultVal:(NSString *)def{
    NSString * val = [dict valueForKey:key];
    if(!val)
        return def;
    return val;
}


+(void)prepareBaseRequest:(NSMutableArray *)args withDict:(NSMutableDictionary *)options forRequest:(ZooZPaymentRequest *)req{
    //    req.requireAddress = NO; ///optional not ask for zip code
    /* Optional - recommended */
    req.payerDetails.firstName = [options valueForKey:@"userFirstName"];
    req.payerDetails.lastName = [options valueForKey:@"userLastName"];
    req.payerDetails.email = [options valueForKey:@"userEmail"];
    req.payerDetails.additionalDetails = [options valueForKey:@"userAdditionalDetails"];
    
    req.payerDetails.phoneNumber = [options valueForKey:@"userPhoneNumber"];
    req.payerDetails.phoneCountryCode = [options valueForKey:@"userPhoneCountryCode"];
    req.payerDetails.billingAddress.country = [options valueForKey:@"addressBillingCountry"];
    req.payerDetails.billingAddress.city = [options valueForKey:@"addressBillingCity"];
    req.payerDetails.billingAddress.streetAddress = [options valueForKey:@"addressBillingStreet"];
    req.payerDetails.billingAddress.state = [options valueForKey:@"addressBillingState"];
    req.payerDetails.billingAddress.zipCode=[options valueForKey:@"addressBillingZipCode"];
    
    
    req.payerDetails.shippingAddress.country = [options valueForKey:@"addressShippingCountry"];
    req.payerDetails.shippingAddress.city = [options valueForKey:@"addressShippingCity"];
    req.payerDetails.shippingAddress.streetAddress = [options valueForKey:@"addressShippingStreet"];
    req.payerDetails.shippingAddress.state = [options valueForKey:@"addressShippingState"];
    req.payerDetails.shippingAddress.zipCode=[options valueForKey:@"addressShippingZipCode"];
}

- (void)manageCards:(NSMutableArray *)args withDict:(NSMutableDictionary *)options{
    NSLog(@"%@, %@", args, options);
    self.activeCallbackId = [args objectAtIndex:0];
    
    
    ZooZ * zooz = [ZooZ sharedInstance];
    zooz.sandbox = [[options valueForKey:@"isSandbox"] boolValue];
    
    
    //you can edit this to customize the colors of the nav bar to match your app colors.
    //    zooz.tintColor = [UIColor blackColor];
    //    zooz.barButtonTintColor = [UIColor darkGrayColor];
    

    ZooZPaymentRequest * req = [zooz createManageFundSourcesRequestWithDelegate:self];
    [ZooZCheckoutPlugin prepareBaseRequest:args withDict:options forRequest:req];
    NSString * currency = [self stringForKey:@"currencyCode" withDict:options defaultVal:@"USD"];
    req.currencyCode =currency;

    
    [zooz openPayment:req forAppKey:[options valueForKey:@"appKey"]];
}


- (void)pay:(CDVInvokedUrlCommand*)command{

    NSArray *args_pre = command.arguments;
    NSMutableArray *args = [NSMutableArray arrayWithArray:args_pre];
    
    NSMutableDictionary *options = [args objectAtIndex:0];
    
    
    if ([[options valueForKey:@"isAuthorize"] boolValue]) {
        [self manageCards:args withDict:options];
    } else {
        
        NSLog(@"%@, %@", args, options);
        self.activeCallbackId = [args objectAtIndex:0];
        
        ZooZ * zooz = [ZooZ sharedInstance];
        zooz.sandbox = [[options valueForKey:@"isSandbox"] boolValue];
        
        
        //you can edit this to customize the colors of the nav bar to match your app colors.
        //    zooz.tintColor = [UIColor blackColor];
        //    zooz.barButtonTintColor = [UIColor darkGrayColor];
        
        NSString * invNumber = [self stringForKey:@"invoiceNumber" withDict:options defaultVal:@"n/a"];
        float amount = [[options valueForKey:@"amount"] floatValue];
        NSString * currency = [self stringForKey:@"currencyCode" withDict:options defaultVal:@"USD"];
        
        ZooZPaymentRequest * req = [zooz createPaymentRequestWithTotal:amount invoiceRefNumber:invNumber delegate:self];
        req.currencyCode =currency;
        //    req.requireAddress = NO;
        /* Optional - recommended */
        [ZooZCheckoutPlugin prepareBaseRequest:args withDict:options forRequest:req];
        
        
        req.invoice.additionalDetails = [options valueForKey:@"invoiceAdditionalDetails"];
        /*************************************************************************
         Invoice optional properties can be added here,
         Please review ZooZ SDK documentation for all properties
         **********************************************************
         
         ZooZInvoiceItem * item = [ZooZInvoiceItem invoiceItem:12.1 quantity:2 name:@"some item name title"];
         item.additionalDetails = @"200 chars custom description in this name";
         item.itemId = @"optional sku item number";
         [req.invoice addItem:item];
         ******************************************************** */
        
        
        /* End of optional */
        
        
        [zooz openPayment:req forAppKey:[options valueForKey:@"appKey"]];
        
        /*  CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_NO_RESULT];
         result.keepCallback = [NSNumber numberWithInt:1];
         
         [self success:result callbackId:self.activeCallbackId];
         */
    }
}
- (void)openPaymentRequestFailed:(ZooZPaymentRequest *)request withErrorCode:(int)errorCode andErrorMessage:(NSString *)errorMessage{
	NSLog(@"failed: %@", errorMessage);
    //this is a network / integration failure, not a payment processing failure.
	NSDictionary * responseDict = [NSDictionary dictionaryWithObjectsAndKeys:errorMessage, @"errorMessage", [NSNumber numberWithInt:errorCode], @"errorCode", nil];
    
    CDVPluginResult * result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:responseDict];
    
    //[self writeJavascript:[result toErrorCallbackString:callback]];
    [self error:result callbackId:self.activeCallbackId];
    
}

- (void)paymentSuccessWithResponse:(ZooZPaymentResponse *)response{
	NSLog(@"payment success with payment Id: %@, %@, %@, %@, %f", response.transactionDisplayID, response.transactionID, response.fundSourceType, response.lastFourDigits, response.paidAmount);
    
    // Convert CardIOCreditCardInfo into dictionary for passing back to javascript
    NSMutableDictionary * responseDict = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                          response.transactionDisplayID, @"transactionId",
                                          response.transactionID, @"paymentToken",
                                          response.fundSourceType, @"cardType",
                                          nil];
    
    if(activeResponse){
        //[activeResponse release];
    }
    
    activeResponse = responseDict;
    //[activeResponse retain];
}


-(void)paymentSuccessDialogClosed{
    NSLog(@"Payment dialog closed after success");
    //see paymentSuccessWithResponse: for the response transaction ID. 
        
    [self success:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:activeResponse] callbackId:self.activeCallbackId];
    //    [self sendSuccessTo:self.activeCallbackId withObject:responseDict];
}

- (void)paymentCanceled{
	NSLog(@"payment cancelled");
    //dialog closed without payment completed
}


-(void)dealloc{
    //[activeCallbackId release], activeCallbackId = nil;
    //[activeResponse release], activeResponse = nil;
    //[super dealloc];
}



@end
