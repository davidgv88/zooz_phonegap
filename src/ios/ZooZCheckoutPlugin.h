//
//  ZooZPGPlugin.h
//  ZooZPGDemo
//
//  Created by Ronen Morecki on 4/16/12.
//  Copyright (c) 2012 ronen@zooz.co. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <ZooZSDK/ZooZ.h>

#import <Cordova/CDVPlugin.h>



@interface ZooZCheckoutPlugin : CDVPlugin<ZooZPaymentCallbackDelegate>{

}

- (void)pay:(NSMutableArray *)args withDict:(NSMutableDictionary *)options;
@end
