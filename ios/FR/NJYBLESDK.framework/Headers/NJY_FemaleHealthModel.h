//
//  NJY_FemaleHealthModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_FemaleHealthModel : NSObject

@property (nonatomic, assign) NSInteger isOpen;                              // 开关  0 ：关闭 1：打开
@property (nonatomic, assign) NSInteger mode;                               // 健康模式：生理期、备孕和怀孕期
@property (nonatomic, assign) NSInteger daysAdvancedToRemind;               // 提前几天提醒
@property (nonatomic, copy) NSString *remindTime;                           // 提醒时间
@property (nonatomic, assign) NSInteger mensesPeriod;                       // 经期长度 - 天
@property (nonatomic, assign) NSInteger mensesCycle;                        // 生理循环周期长度 - 天
@property (nonatomic, copy) NSString *lastMensesBeginDate;                  // 上一次经期开始日期
@property (nonatomic, assign) NSInteger daysOfMensesOverToNext;             // 经期结束到下次经期开始的天数
@property (nonatomic, assign) NSInteger tipOn;                              // 经提醒开关  0 ：关闭 1：打开

@end

NS_ASSUME_NONNULL_END
