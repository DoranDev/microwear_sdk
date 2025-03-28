//
//  NJY_TimeSetModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_TimeSetModel : NSObject
@property (nonatomic, assign) BOOL isOn; //开关状态  1：打开  0：关闭
@property (nonatomic, strong) NSDate *startTime; //开始时间
@property (nonatomic, strong) NSDate *endTime;   //结束时间
@property (nonatomic, assign) NSInteger interval; //提醒间隔

- (NSInteger)getStartHour;
- (NSInteger)getStartMin;
- (NSInteger)getEndHour;
- (NSInteger)getEndMin;
@end

NS_ASSUME_NONNULL_END
