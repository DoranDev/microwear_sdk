//
//  NJY_SleepModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
NS_ASSUME_NONNULL_BEGIN
 
@interface NJY_SleepModel : NSObject

@property (nonatomic, strong) NSString *date;
@property (nonatomic, strong) NSString *time;

@property (nonatomic, assign) NSTimeInterval sleepTime;         // 入睡时间
@property (nonatomic, assign) NSTimeInterval wakeTime;          // 醒来时间

@property (nonatomic, assign) CGFloat totalTime;                // 总计睡眠时间（秒）
@property (nonatomic, assign) CGFloat deepTime;                 // 累计深睡时间（秒）
@property (nonatomic, assign) CGFloat lightTime;                // 累计浅睡时间（秒）
@property (nonatomic, strong) NSMutableArray *detailList;

@end

@interface NJY_SleepDetailModel : NSObject
@property (nonatomic, assign) NSInteger quality;        // 睡眠质量：3 - 清醒；2 - 浅睡；1 - 深睡；
@property (nonatomic, assign) NSInteger seconds;        // 睡眠时长（秒）
@property (nonatomic, assign) NSTimeInterval moment;    // 采样时刻

@end
NS_ASSUME_NONNULL_END
