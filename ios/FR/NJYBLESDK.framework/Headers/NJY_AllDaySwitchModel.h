//
//  NJY_AllDaySwitchModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_AllDaySwitchModel : NSObject
/**
 开关类型
 switch_hr = 1;
 switch_bo2 = 2;
 switch_temp = 3;
 开关状态
 1：打开
 0：关闭
 */
@property (nonatomic, assign) NSInteger type;
@property (nonatomic, assign) NSInteger isOn;

@end

NS_ASSUME_NONNULL_END
