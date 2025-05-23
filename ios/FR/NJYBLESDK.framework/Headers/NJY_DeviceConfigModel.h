//
//  NJY_DeviceConfigModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/19.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_DeviceConfigModel : NSObject

@property (nonatomic, assign) NSInteger timeFormat; //时间格式（24小时/12小时）
@property (nonatomic, assign) NSInteger unitFormat; //单位格式（0：公制或 1：英制）
@property (nonatomic, assign) NSInteger tempFormat; //温度格式（0：摄氏度:1：华氏度）
@property (nonatomic, assign) NSInteger allDayHr;  //全天HR开关（0：关 1：开）


@end

NS_ASSUME_NONNULL_END
