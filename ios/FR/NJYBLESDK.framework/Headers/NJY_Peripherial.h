//
//  NJY_Peripherial.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_Peripherial : NSObject

@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *mac;
@property (nonatomic, strong) NSNumber *rssi;
@property (nonatomic, strong) CBPeripheral *peripheral;
@property (nonatomic, strong) NSString *devInfo;

@end

NS_ASSUME_NONNULL_END
